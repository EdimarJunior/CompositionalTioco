/*********************                                                        */
/*! \file node_manager.cpp
 ** \verbatim
 ** Top contributors (to current version):
 **   Morgan Deters, Andrew Reynolds, Tim King
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief Expression manager implementation.
 **
 ** Expression manager implementation.
 **
 ** Reviewed by Chris Conway, Apr 5 2010 (bug #65).
 **/
#include "expr/node_manager.h"

#include <algorithm>
#include <stack>
#include <utility>

#include "base/check.h"
#include "base/listener.h"
#include "expr/attribute.h"
#include "expr/dtype.h"
#include "expr/node_manager_attributes.h"
#include "expr/skolem_manager.h"
#include "expr/type_checker.h"
#include "options/options.h"
#include "options/smt_options.h"
#include "util/resource_manager.h"
#include "util/statistics_registry.h"

using namespace std;
using namespace CVC4::expr;

namespace CVC4 {

thread_local NodeManager* NodeManager::s_current = NULL;

namespace {

/**
 * This class sets it reference argument to true and ensures that it gets set
 * to false on destruction. This can be used to make sure a flag gets toggled
 * in a function even on exceptional exit (e.g., see reclaimZombies()).
 */
struct ScopedBool {
  bool& d_value;

  ScopedBool(bool& value) :
    d_value(value) {

    Debug("gc") << ">> setting ScopedBool\n";
    d_value = true;
  }

  ~ScopedBool() {
    Debug("gc") << "<< clearing ScopedBool\n";
    d_value = false;
  }
};

/**
 * Similarly, ensure d_nodeUnderDeletion gets set to NULL even on
 * exceptional exit from NodeManager::reclaimZombies().
 */
struct NVReclaim {
  NodeValue*& d_deletionField;

  NVReclaim(NodeValue*& deletionField) :
    d_deletionField(deletionField) {

    Debug("gc") << ">> setting NVRECLAIM field\n";
  }

  ~NVReclaim() {
    Debug("gc") << "<< clearing NVRECLAIM field\n";
    d_deletionField = NULL;
  }
};

} // namespace

namespace attr {
  struct LambdaBoundVarListTag { };
}/* CVC4::attr namespace */

// attribute that stores the canonical bound variable list for function types
typedef expr::Attribute<attr::LambdaBoundVarListTag, Node> LambdaBoundVarListAttr;

NodeManager::NodeManager(ExprManager* exprManager)
    : d_statisticsRegistry(new StatisticsRegistry()),
      d_skManager(new SkolemManager),
      next_id(0),
      d_attrManager(new expr::attr::AttributeManager()),
      d_exprManager(exprManager),
      d_nodeUnderDeletion(NULL),
      d_inReclaimZombies(false),
      d_abstractValueCount(0),
      d_skolemCounter(0)
{
  init();
}

TypeNode NodeManager::booleanType()
{
  return mkTypeConst<TypeConstant>(BOOLEAN_TYPE);
}

TypeNode NodeManager::integerType()
{
  return mkTypeConst<TypeConstant>(INTEGER_TYPE);
}

TypeNode NodeManager::realType()
{
  return mkTypeConst<TypeConstant>(REAL_TYPE);
}

TypeNode NodeManager::stringType()
{
  return mkTypeConst<TypeConstant>(STRING_TYPE);
}

TypeNode NodeManager::regExpType()
{
  return mkTypeConst<TypeConstant>(REGEXP_TYPE);
}

TypeNode NodeManager::roundingModeType()
{
  return mkTypeConst<TypeConstant>(ROUNDINGMODE_TYPE);
}

TypeNode NodeManager::boundVarListType()
{
  return mkTypeConst<TypeConstant>(BOUND_VAR_LIST_TYPE);
}

TypeNode NodeManager::instPatternType()
{
  return mkTypeConst<TypeConstant>(INST_PATTERN_TYPE);
}

TypeNode NodeManager::instPatternListType()
{
  return mkTypeConst<TypeConstant>(INST_PATTERN_LIST_TYPE);
}

TypeNode NodeManager::builtinOperatorType()
{
  return mkTypeConst<TypeConstant>(BUILTIN_OPERATOR_TYPE);
}

TypeNode NodeManager::mkBitVectorType(unsigned size)
{
  return mkTypeConst<BitVectorSize>(BitVectorSize(size));
}

TypeNode NodeManager::mkFloatingPointType(unsigned exp, unsigned sig)
{
  return mkTypeConst<FloatingPointSize>(FloatingPointSize(exp, sig));
}

TypeNode NodeManager::mkFloatingPointType(FloatingPointSize fs)
{
  return mkTypeConst<FloatingPointSize>(fs);
}

void NodeManager::init() {
  // `mkConst()` indirectly needs the correct NodeManager in scope because we
  // call `NodeValue::inc()` which uses `NodeManager::curentNM()`
  NodeManagerScope nms(this);

  poolInsert( &expr::NodeValue::null() );

  for(unsigned i = 0; i < unsigned(kind::LAST_KIND); ++i) {
    Kind k = Kind(i);

    if(hasOperator(k)) {
      d_operators[i] = mkConst(Kind(k));
    }
  }
}

NodeManager::~NodeManager() {
  // have to ensure "this" is the current NodeManager during
  // destruction of operators, because they get GCed.

  NodeManagerScope nms(this);

  // Destroy skolem manager before cleaning up attributes and zombies
  d_skManager = nullptr;

  {
    ScopedBool dontGC(d_inReclaimZombies);
    // hopefully by this point all SmtEngines have been deleted
    // already, along with all their attributes
    d_attrManager->deleteAllAttributes();
  }

  for(unsigned i = 0; i < unsigned(kind::LAST_KIND); ++i) {
    d_operators[i] = Node::null();
  }

  d_unique_vars.clear();

  TypeNode dummy;
  d_tt_cache.d_children.clear();
  d_tt_cache.d_data = dummy;
  d_rt_cache.d_children.clear();
  d_rt_cache.d_data = dummy;

  d_registeredDTypes.clear();
  // clear the datatypes
  d_ownedDTypes.clear();

  Assert(!d_attrManager->inGarbageCollection());

  std::vector<NodeValue*> order = TopologicalSort(d_maxedOut);
  d_maxedOut.clear();

  while (!d_zombies.empty() || !order.empty()) {
    if (d_zombies.empty()) {
      // Delete the maxed out nodes in toplogical order once we know
      // there are no additional zombies, or other nodes to worry about.
      Assert(!order.empty());
      // We process these in reverse to reverse the topological order.
      NodeValue* greatest_maxed_out = order.back();
      order.pop_back();
      Assert(greatest_maxed_out->HasMaximizedReferenceCount());
      Debug("gc") << "Force zombify " << greatest_maxed_out << std::endl;
      greatest_maxed_out->d_rc = 0;
      markForDeletion(greatest_maxed_out);
    } else {
      reclaimZombies();
    }
  }

  poolRemove( &expr::NodeValue::null() );

  if(Debug.isOn("gc:leaks")) {
    Debug("gc:leaks") << "still in pool:" << endl;
    for(NodeValuePool::const_iterator i = d_nodeValuePool.begin(),
          iend = d_nodeValuePool.end();
        i != iend;
        ++i) {
      Debug("gc:leaks") << "  " << *i
                        << " id=" << (*i)->d_id
                        << " rc=" << (*i)->d_rc
                        << " " << **i << endl;
    }
    Debug("gc:leaks") << ":end:" << endl;
  }

  // defensive coding, in case destruction-order issues pop up (they often do)
  delete d_statisticsRegistry;
  d_statisticsRegistry = NULL;
  delete d_attrManager;
  d_attrManager = NULL;
}

size_t NodeManager::registerDatatype(std::shared_ptr<DType> dt)
{
  size_t sz = d_registeredDTypes.size();
  d_registeredDTypes.push_back(dt);
  return sz;
}

const DType& NodeManager::getDTypeForIndex(size_t index) const
{
  // if this assertion fails, it is likely due to not managing datatypes
  // properly w.r.t. multiple NodeManagers.
  Assert(index < d_registeredDTypes.size());
  return *d_registeredDTypes[index];
}

void NodeManager::reclaimZombies() {
  // FIXME multithreading
  Assert(!d_attrManager->inGarbageCollection());

  Debug("gc") << "reclaiming " << d_zombies.size() << " zombie(s)!\n";

  // during reclamation, reclaimZombies() is never supposed to be called
  Assert(!d_inReclaimZombies)
      << "NodeManager::reclaimZombies() not re-entrant!";

  // whether exit is normal or exceptional, the Reclaim dtor is called
  // and ensures that d_inReclaimZombies is set back to false.
  ScopedBool r(d_inReclaimZombies);

  // We copy the set away and clear the NodeManager's set of zombies.
  // This is because reclaimZombie() decrements the RC of the
  // NodeValue's children, which may (recursively) reclaim them.
  //
  // Let's say we're reclaiming zombie NodeValue "A" and its child "B"
  // then becomes a zombie (NodeManager::markForDeletion(B) is called).
  //
  // One way to handle B's zombification would be simply to put it
  // into d_zombies.  This is what we do.  However, if we were to
  // concurrently process d_zombies in the loop below, such addition
  // may be invisible to us (B is leaked) or even invalidate our
  // iterator, causing a crash.  So we need to copy the set away.

  vector<NodeValue*> zombies;
  zombies.reserve(d_zombies.size());
  remove_copy_if(d_zombies.begin(),
                 d_zombies.end(),
                 back_inserter(zombies),
                 NodeValueReferenceCountNonZero());
  d_zombies.clear();

#ifdef _LIBCPP_VERSION
  NodeValue* last = NULL;
#endif
  for(vector<NodeValue*>::iterator i = zombies.begin();
      i != zombies.end();
      ++i) {
    NodeValue* nv = *i;
#ifdef _LIBCPP_VERSION
    // Work around an apparent bug in libc++'s hash_set<> which can
    // (very occasionally) have an element repeated.
    if(nv == last) {
      continue;
    }
    last = nv;
#endif

    // collect ONLY IF still zero
    if(nv->d_rc == 0) {
      if(Debug.isOn("gc")) {
        Debug("gc") << "deleting node value " << nv
                    << " [" << nv->d_id << "]: ";
        nv->printAst(Debug("gc"));
        Debug("gc") << endl;
      }

      // remove from the pool
      kind::MetaKind mk = nv->getMetaKind();
      if(mk != kind::metakind::VARIABLE && mk != kind::metakind::NULLARY_OPERATOR) {
        poolRemove(nv);
      }

      // whether exit is normal or exceptional, the NVReclaim dtor is
      // called and ensures that d_nodeUnderDeletion is set back to
      // NULL.
      NVReclaim rc(d_nodeUnderDeletion);
      d_nodeUnderDeletion = nv;

      // remove attributes
      { // notify listeners of deleted node
        TNode n;
        n.d_nv = nv;
        nv->d_rc = 1; // so that TNode doesn't assert-fail
        for (NodeManagerListener* listener : d_listeners)
        {
          listener->nmNotifyDeleteNode(n);
        }
        // this would mean that one of the listeners stowed away
        // a reference to this node!
        Assert(nv->d_rc == 1);
      }
      nv->d_rc = 0;
      d_attrManager->deleteAllAttributes(nv);

      // decr ref counts of children
      nv->decrRefCounts();
      if(mk == kind::metakind::CONSTANT) {
        // Destroy (call the destructor for) the C++ type representing
        // the constant in this NodeValue.  This is needed for
        // e.g. CVC4::Rational, since it has a gmp internal
        // representation that mallocs memory and should be cleaned
        // up.  (This won't delete a pointer value if used as a
        // constant, but then, you should probably use a smart-pointer
        // type for a constant payload.)
        kind::metakind::deleteNodeValueConstant(nv);
      }
      free(nv);
    }
  }
}/* NodeManager::reclaimZombies() */

std::vector<NodeValue*> NodeManager::TopologicalSort(
    const std::vector<NodeValue*>& roots) {
  std::vector<NodeValue*> order;
  // The stack of nodes to visit. The Boolean value is false when visiting the
  // node in preorder and true when visiting it in postorder.
  std::vector<std::pair<bool, NodeValue*> > stack;
  // Nodes that have been visited in both pre- and postorder
  NodeValueIDSet visited;
  const NodeValueIDSet root_set(roots.begin(), roots.end());

  for (size_t index = 0; index < roots.size(); index++) {
    NodeValue* root = roots[index];
    if (visited.find(root) == visited.end()) {
      stack.push_back(std::make_pair(false, root));
    }
    while (!stack.empty()) {
      NodeValue* current = stack.back().second;
      const bool visited_children = stack.back().first;
      Debug("gc") << "Topological sort " << current << " " << visited_children
                  << std::endl;
      if (visited_children) {
        if (root_set.find(current) != root_set.end()) {
          order.push_back(current);
        }
        stack.pop_back();
      }
      else if (visited.find(current) == visited.end())
      {
        stack.back().first = true;
        visited.insert(current);
        for (unsigned i = 0; i < current->getNumChildren(); ++i) {
          expr::NodeValue* child = current->getChild(i);
          stack.push_back(std::make_pair(false, child));
        }
      }
      else
      {
        stack.pop_back();
      }
    }
  }
  Assert(order.size() == roots.size());
  return order;
} /* NodeManager::TopologicalSort() */

TypeNode NodeManager::getType(TNode n, bool check)
{
  // Many theories' type checkers call Node::getType() directly.  This
  // is incorrect, since "this" might not be the caller's current node
  // manager.  Rather than force the individual typecheckers not to do
  // this (by policy, which would be imperfect and lead to
  // hard-to-find bugs, which it has in the past), we just set this
  // node manager to be current for the duration of this check.
  //
  NodeManagerScope nms(this);

  TypeNode typeNode;
  bool hasType = getAttribute(n, TypeAttr(), typeNode);
  bool needsCheck = check && !getAttribute(n, TypeCheckedAttr());


  Debug("getType") << this << " getting type for " << &n << " " << n << ", check=" << check << ", needsCheck = " << needsCheck << ", hasType = " << hasType << endl;

#ifdef CVC4_DEBUG
  // already did type check eagerly upon creation in node builder
  bool doTypeCheck = false;
#else
  bool doTypeCheck = true;
#endif
  if (needsCheck && doTypeCheck)
  {
    /* Iterate and compute the children bottom up. This avoids stack
       overflows in computeType() when the Node graph is really deep,
       which should only affect us when we're type checking lazily. */
    stack<TNode> worklist;
    worklist.push(n);

    while( !worklist.empty() ) {
      TNode m = worklist.top();

      bool readyToCompute = true;

      for( TNode::iterator it = m.begin(), end = m.end();
           it != end;
           ++it ) {
        if( !hasAttribute(*it, TypeAttr())
            || (check && !getAttribute(*it, TypeCheckedAttr())) ) {
          readyToCompute = false;
          worklist.push(*it);
        }
      }

      if( readyToCompute ) {
        Assert(check || m.getMetaKind() != kind::metakind::NULLARY_OPERATOR);
        /* All the children have types, time to compute */
        typeNode = TypeChecker::computeType(this, m, check);
        worklist.pop();
      }
    } // end while

    /* Last type computed in loop should be the type of n */
    Assert(typeNode == getAttribute(n, TypeAttr()));
  } else if( !hasType || needsCheck ) {
    /* We can compute the type top-down, without worrying about
       deep recursion. */
    Assert(check || n.getMetaKind() != kind::metakind::NULLARY_OPERATOR);
    typeNode = TypeChecker::computeType(this, n, check);
  }

  /* The type should be have been computed and stored. */
  Assert(hasAttribute(n, TypeAttr()));
  /* The check should have happened, if we asked for it. */
  Assert(!check || getAttribute(n, TypeCheckedAttr()));

  Debug("getType") << "type of " << &n << " " <<  n << " is " << typeNode << endl;
  return typeNode;
}

Node NodeManager::mkSkolem(const std::string& prefix, const TypeNode& type, const std::string& comment, int flags) {
  Node n = NodeBuilder<0>(this, kind::SKOLEM);
  setAttribute(n, TypeAttr(), type);
  setAttribute(n, TypeCheckedAttr(), true);
  if((flags & SKOLEM_EXACT_NAME) == 0) {
    stringstream name;
    name << prefix << '_' << ++d_skolemCounter;
    setAttribute(n, expr::VarNameAttr(), name.str());
  } else {
    setAttribute(n, expr::VarNameAttr(), prefix);
  }
  if((flags & SKOLEM_NO_NOTIFY) == 0) {
    for(vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
      (*i)->nmNotifyNewSkolem(n, comment, (flags & SKOLEM_IS_GLOBAL) == SKOLEM_IS_GLOBAL);
    }
  }
  return n;
}

TypeNode NodeManager::mkBagType(TypeNode elementType)
{
  CheckArgument(
      !elementType.isNull(), elementType, "unexpected NULL element type");
  CheckArgument(elementType.isFirstClass(),
                elementType,
                "cannot store types that are not first-class in bags. Try "
                "option --uf-ho.");
  Debug("bags") << "making bags type " << elementType << std::endl;
  return mkTypeNode(kind::BAG_TYPE, elementType);
}

TypeNode NodeManager::mkSequenceType(TypeNode elementType)
{
  CheckArgument(
      !elementType.isNull(), elementType, "unexpected NULL element type");
  CheckArgument(elementType.isFirstClass(),
                elementType,
                "cannot store types that are not first-class in sequences. Try "
                "option --uf-ho.");
  return mkTypeNode(kind::SEQUENCE_TYPE, elementType);
}

TypeNode NodeManager::mkDatatypeType(DType& datatype, uint32_t flags)
{
  // Not worth a special implementation; this doesn't need to be fast
  // code anyway.
  std::vector<DType> datatypes;
  datatypes.push_back(datatype);
  std::vector<TypeNode> result = mkMutualDatatypeTypes(datatypes, flags);
  Assert(result.size() == 1);
  return result.front();
}

std::vector<TypeNode> NodeManager::mkMutualDatatypeTypes(
    const std::vector<DType>& datatypes, uint32_t flags)
{
  std::set<TypeNode> unresolvedTypes;
  return mkMutualDatatypeTypes(datatypes, unresolvedTypes, flags);
}

std::vector<TypeNode> NodeManager::mkMutualDatatypeTypes(
    const std::vector<DType>& datatypes,
    const std::set<TypeNode>& unresolvedTypes,
    uint32_t flags)
{
  NodeManagerScope nms(this);
  std::map<std::string, TypeNode> nameResolutions;
  std::vector<TypeNode> dtts;

  // have to build deep copy so that datatypes will live in this class
  std::vector<std::shared_ptr<DType> > dt_copies;
  for (const DType& dt : datatypes)
  {
    d_ownedDTypes.push_back(std::unique_ptr<DType>(new DType(dt)));
    dt_copies.push_back(std::move(d_ownedDTypes.back()));
  }

  // First do some sanity checks, set up the final Type to be used for
  // each datatype, and set up the "named resolutions" used to handle
  // simple self- and mutual-recursion, for example in the definition
  // "nat = succ(pred:nat) | zero", a named resolution can handle the
  // pred selector.
  for (const std::shared_ptr<DType>& dtc : dt_copies)
  {
    TypeNode typeNode;
    // register datatype with the node manager
    size_t index = registerDatatype(dtc);
    if (dtc->getNumParameters() == 0)
    {
      typeNode = mkTypeConst(DatatypeIndexConstant(index));
    }
    else
    {
      TypeNode cons = mkTypeConst(DatatypeIndexConstant(index));
      std::vector<TypeNode> params;
      params.push_back(cons);
      for (unsigned int ip = 0; ip < dtc->getNumParameters(); ++ip)
      {
        params.push_back(dtc->getParameter(ip));
      }

      typeNode = mkTypeNode(kind::PARAMETRIC_DATATYPE, params);
    }
    AlwaysAssert(nameResolutions.find(dtc->getName()) == nameResolutions.end())
        << "cannot construct two datatypes at the same time "
           "with the same name";
    nameResolutions.insert(std::make_pair(dtc->getName(), typeNode));
    dtts.push_back(typeNode);
  }

  // Second, set up the type substitution map for complex type
  // resolution (e.g. if "list" is the type we're defining, and it has
  // a selector of type "ARRAY INT OF list", this can't be taken care
  // of using the named resolutions that we set up above.  A
  // preliminary array type was set up, and now needs to have "list"
  // substituted in it for the correct type.
  //
  // @TODO get rid of named resolutions altogether and handle
  // everything with these resolutions?
  std::vector<TypeNode> paramTypes;
  std::vector<TypeNode> paramReplacements;
  std::vector<TypeNode> placeholders;  // to hold the "unresolved placeholders"
  std::vector<TypeNode> replacements;  // to hold our final, resolved types
  for (const TypeNode& ut : unresolvedTypes)
  {
    std::string name = ut.getAttribute(expr::VarNameAttr());
    std::map<std::string, TypeNode>::const_iterator resolver =
        nameResolutions.find(name);
    AlwaysAssert(resolver != nameResolutions.end())
        << "cannot resolve type " + name
               + "; it's not among the datatypes being defined";
    // We will instruct the Datatype to substitute "ut" (the
    // unresolved SortType used as a placeholder in complex types)
    // with "(*resolver).second" (the TypeNode we created in the
    // first step, above).
    if (ut.isSort())
    {
      placeholders.push_back(ut);
      replacements.push_back((*resolver).second);
    }
    else
    {
      Assert(ut.isSortConstructor());
      paramTypes.push_back(ut);
      paramReplacements.push_back((*resolver).second);
    }
  }

  // Lastly, perform the final resolutions and checks.
  for (const TypeNode& ut : dtts)
  {
    const DType& dt = ut.getDType();
    if (!dt.isResolved())
    {
      const_cast<DType&>(dt).resolve(nameResolutions,
                                     placeholders,
                                     replacements,
                                     paramTypes,
                                     paramReplacements);
    }
    // Check the datatype has been resolved properly.
    for (size_t i = 0, ncons = dt.getNumConstructors(); i < ncons; i++)
    {
      const DTypeConstructor& c = dt[i];
      TypeNode testerType CVC4_UNUSED = c.getTester().getType();
      Assert(c.isResolved() && testerType.isTester() && testerType[0] == ut)
          << "malformed tester in datatype post-resolution";
      TypeNode ctorType CVC4_UNUSED = c.getConstructor().getType();
      Assert(ctorType.isConstructor()
            && ctorType.getNumChildren() == c.getNumArgs() + 1
            && ctorType.getRangeType() == ut)
          << "malformed constructor in datatype post-resolution";
      // for all selectors...
      for (size_t j = 0, nargs = c.getNumArgs(); j < nargs; j++)
      {
        const DTypeSelector& a = c[j];
        TypeNode selectorType = a.getType();
        Assert(a.isResolved() && selectorType.isSelector()
              && selectorType[0] == ut)
            << "malformed selector in datatype post-resolution";
        // This next one's a "hard" check, performed in non-debug builds
        // as well; the other ones should all be guaranteed by the
        // CVC4::DType class, but this actually needs to be checked.
        AlwaysAssert(!selectorType.getRangeType().isFunctionLike())
            << "cannot put function-like things in datatypes";
      }
    }
  }

  for (NodeManagerListener* nml : d_listeners)
  {
    nml->nmNotifyNewDatatypes(dtts, flags);
  }

  return dtts;
}

TypeNode NodeManager::mkConstructorType(const std::vector<TypeNode>& args,
                                        TypeNode range)
{
  std::vector<TypeNode> sorts = args;
  sorts.push_back(range);
  return mkTypeNode(kind::CONSTRUCTOR_TYPE, sorts);
}

TypeNode NodeManager::TupleTypeCache::getTupleType( NodeManager * nm, std::vector< TypeNode >& types, unsigned index ) {
  if( index==types.size() ){
    if( d_data.isNull() ){
      std::stringstream sst;
      sst << "__cvc4_tuple";
      for (unsigned i = 0; i < types.size(); ++ i) {
        sst << "_" << types[i];
      }
      DType dt(sst.str());
      dt.setTuple();
      std::stringstream ssc;
      ssc << sst.str() << "_ctor";
      std::shared_ptr<DTypeConstructor> c =
          std::make_shared<DTypeConstructor>(ssc.str());
      for (unsigned i = 0; i < types.size(); ++ i) {
        std::stringstream ss;
        ss << sst.str() << "_stor_" << i;
        c->addArg(ss.str().c_str(), types[i]);
      }
      dt.addConstructor(c);
      d_data = nm->mkDatatypeType(dt);
      Debug("tuprec-debug") << "Return type : " << d_data << std::endl;
    }
    return d_data;
  }else{
    return d_children[types[index]].getTupleType( nm, types, index+1 );
  }
}

TypeNode NodeManager::RecTypeCache::getRecordType( NodeManager * nm, const Record& rec, unsigned index ) {
  if( index==rec.getNumFields() ){
    if( d_data.isNull() ){
      const Record::FieldVector& fields = rec.getFields();
      std::stringstream sst;
      sst << "__cvc4_record";
      for(Record::FieldVector::const_iterator i = fields.begin(); i != fields.end(); ++i) {
        sst << "_" << (*i).first << "_" << (*i).second;
      }
      DType dt(sst.str());
      dt.setRecord();
      std::stringstream ssc;
      ssc << sst.str() << "_ctor";
      std::shared_ptr<DTypeConstructor> c =
          std::make_shared<DTypeConstructor>(ssc.str());
      for(Record::FieldVector::const_iterator i = fields.begin(); i != fields.end(); ++i) {
        c->addArg((*i).first, TypeNode::fromType((*i).second));
      }
      dt.addConstructor(c);
      d_data = nm->mkDatatypeType(dt);
      Debug("tuprec-debug") << "Return type : " << d_data << std::endl;
    }
    return d_data;
  }else{
    return d_children[TypeNode::fromType( rec[index].second )][rec[index].first].getRecordType( nm, rec, index+1 );
  }
}

TypeNode NodeManager::mkFunctionType(const std::vector<TypeNode>& sorts)
{
  Assert(sorts.size() >= 2);
  CheckArgument(!sorts[sorts.size() - 1].isFunction(),
                sorts[sorts.size() - 1],
                "must flatten function types");
  return mkTypeNode(kind::FUNCTION_TYPE, sorts);
}

TypeNode NodeManager::mkPredicateType(const std::vector<TypeNode>& sorts)
{
  Assert(sorts.size() >= 1);
  std::vector<TypeNode> sortNodes;
  sortNodes.insert(sortNodes.end(), sorts.begin(), sorts.end());
  sortNodes.push_back(booleanType());
  return mkFunctionType(sortNodes);
}

TypeNode NodeManager::mkFunctionType(const TypeNode& domain,
                                     const TypeNode& range)
{
  std::vector<TypeNode> sorts;
  sorts.push_back(domain);
  sorts.push_back(range);
  return mkFunctionType(sorts);
}

TypeNode NodeManager::mkFunctionType(const std::vector<TypeNode>& argTypes,
                                     const TypeNode& range)
{
  Assert(argTypes.size() >= 1);
  std::vector<TypeNode> sorts(argTypes);
  sorts.push_back(range);
  return mkFunctionType(sorts);
}

TypeNode NodeManager::mkTupleType(const std::vector<TypeNode>& types) {
  std::vector< TypeNode > ts;
  Debug("tuprec-debug") << "Make tuple type : ";
  for (unsigned i = 0; i < types.size(); ++ i) {
    CheckArgument(!types[i].isFunctionLike(), types, "cannot put function-like types in tuples");
    ts.push_back( types[i] );
    Debug("tuprec-debug") << types[i] << " ";
  }
  Debug("tuprec-debug") << std::endl;
  return d_tt_cache.getTupleType( this, ts );
}

TypeNode NodeManager::mkRecordType(const Record& rec) {
  return d_rt_cache.getRecordType( this, rec );
}

void NodeManager::reclaimAllZombies(){
  reclaimZombiesUntil(0u);
}

/** Reclaim zombies while there are more than k nodes in the pool (if possible).*/
void NodeManager::reclaimZombiesUntil(uint32_t k){
  if(safeToReclaimZombies()){
    while(poolSize() >= k && !d_zombies.empty()){
      reclaimZombies();
    }
  }
}

size_t NodeManager::poolSize() const{
  return d_nodeValuePool.size();
}

TypeNode NodeManager::mkSort(uint32_t flags) {
  NodeBuilder<1> nb(this, kind::SORT_TYPE);
  Node sortTag = NodeBuilder<0>(this, kind::SORT_TAG);
  nb << sortTag;
  TypeNode tn = nb.constructTypeNode();
  for(std::vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
    (*i)->nmNotifyNewSort(tn, flags);
  }
  return tn;
}

TypeNode NodeManager::mkSort(const std::string& name, uint32_t flags) {
  NodeBuilder<1> nb(this, kind::SORT_TYPE);
  Node sortTag = NodeBuilder<0>(this, kind::SORT_TAG);
  nb << sortTag;
  TypeNode tn = nb.constructTypeNode();
  setAttribute(tn, expr::VarNameAttr(), name);
  for(std::vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
    (*i)->nmNotifyNewSort(tn, flags);
  }
  return tn;
}

TypeNode NodeManager::mkSort(TypeNode constructor,
                                    const std::vector<TypeNode>& children,
                                    uint32_t flags) {
  Assert(constructor.getKind() == kind::SORT_TYPE
         && constructor.getNumChildren() == 0)
      << "expected a sort constructor";
  Assert(children.size() > 0) << "expected non-zero # of children";
  Assert(hasAttribute(constructor.d_nv, expr::SortArityAttr())
         && hasAttribute(constructor.d_nv, expr::VarNameAttr()))
      << "expected a sort constructor";
  std::string name = getAttribute(constructor.d_nv, expr::VarNameAttr());
  Assert(getAttribute(constructor.d_nv, expr::SortArityAttr())
         == children.size())
      << "arity mismatch in application of sort constructor";
  NodeBuilder<> nb(this, kind::SORT_TYPE);
  Node sortTag = Node(constructor.d_nv->d_children[0]);
  nb << sortTag;
  nb.append(children);
  TypeNode type = nb.constructTypeNode();
  setAttribute(type, expr::VarNameAttr(), name);
  for(std::vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
    (*i)->nmNotifyInstantiateSortConstructor(constructor, type, flags);
  }
  return type;
}

TypeNode NodeManager::mkSortConstructor(const std::string& name,
                                        size_t arity,
                                        uint32_t flags)
{
  Assert(arity > 0);
  NodeBuilder<> nb(this, kind::SORT_TYPE);
  Node sortTag = NodeBuilder<0>(this, kind::SORT_TAG);
  nb << sortTag;
  TypeNode type = nb.constructTypeNode();
  setAttribute(type, expr::VarNameAttr(), name);
  setAttribute(type, expr::SortArityAttr(), arity);
  for(std::vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
    (*i)->nmNotifyNewSortConstructor(type, flags);
  }
  return type;
}

Node NodeManager::mkVar(const std::string& name, const TypeNode& type, uint32_t flags) {
  Node n = NodeBuilder<0>(this, kind::VARIABLE);
  setAttribute(n, TypeAttr(), type);
  setAttribute(n, TypeCheckedAttr(), true);
  setAttribute(n, expr::VarNameAttr(), name);
  setAttribute(n, expr::GlobalVarAttr(), flags & ExprManager::VAR_FLAG_GLOBAL);
  for(std::vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
    (*i)->nmNotifyNewVar(n, flags);
  }
  return n;
}

Node* NodeManager::mkVarPtr(const std::string& name,
                            const TypeNode& type, uint32_t flags) {
  Node* n = NodeBuilder<0>(this, kind::VARIABLE).constructNodePtr();
  setAttribute(*n, TypeAttr(), type);
  setAttribute(*n, TypeCheckedAttr(), true);
  setAttribute(*n, expr::VarNameAttr(), name);
  setAttribute(*n, expr::GlobalVarAttr(), flags & ExprManager::VAR_FLAG_GLOBAL);
  for(std::vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
    (*i)->nmNotifyNewVar(*n, flags);
  }
  return n;
}

Node NodeManager::mkBoundVar(const std::string& name, const TypeNode& type) {
  Node n = mkBoundVar(type);
  setAttribute(n, expr::VarNameAttr(), name);
  return n;
}

Node* NodeManager::mkBoundVarPtr(const std::string& name,
                                 const TypeNode& type) {
  Node* n = mkBoundVarPtr(type);
  setAttribute(*n, expr::VarNameAttr(), name);
  return n;
}

Node NodeManager::getBoundVarListForFunctionType( TypeNode tn ) {
  Assert(tn.isFunction());
  Node bvl = tn.getAttribute(LambdaBoundVarListAttr());
  if( bvl.isNull() ){
    std::vector< Node > vars;
    for( unsigned i=0; i<tn.getNumChildren()-1; i++ ){
      vars.push_back(mkBoundVar(tn[i]));
    }
    bvl = mkNode(kind::BOUND_VAR_LIST, vars);
    Trace("functions") << "Make standard bound var list " << bvl << " for " << tn << std::endl;
    tn.setAttribute(LambdaBoundVarListAttr(),bvl);
  }
  return bvl;
}

Node NodeManager::mkVar(const TypeNode& type, uint32_t flags) {
  Node n = NodeBuilder<0>(this, kind::VARIABLE);
  setAttribute(n, TypeAttr(), type);
  setAttribute(n, TypeCheckedAttr(), true);
  setAttribute(n, expr::GlobalVarAttr(), flags & ExprManager::VAR_FLAG_GLOBAL);
  for(std::vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
    (*i)->nmNotifyNewVar(n, flags);
  }
  return n;
}

Node* NodeManager::mkVarPtr(const TypeNode& type, uint32_t flags) {
  Node* n = NodeBuilder<0>(this, kind::VARIABLE).constructNodePtr();
  setAttribute(*n, TypeAttr(), type);
  setAttribute(*n, TypeCheckedAttr(), true);
  setAttribute(*n, expr::GlobalVarAttr(), flags & ExprManager::VAR_FLAG_GLOBAL);
  for(std::vector<NodeManagerListener*>::iterator i = d_listeners.begin(); i != d_listeners.end(); ++i) {
    (*i)->nmNotifyNewVar(*n, flags);
  }
  return n;
}

Node NodeManager::mkBoundVar(const TypeNode& type) {
  Node n = NodeBuilder<0>(this, kind::BOUND_VARIABLE);
  setAttribute(n, TypeAttr(), type);
  setAttribute(n, TypeCheckedAttr(), true);
  return n;
}

Node* NodeManager::mkBoundVarPtr(const TypeNode& type) {
  Node* n = NodeBuilder<0>(this, kind::BOUND_VARIABLE).constructNodePtr();
  setAttribute(*n, TypeAttr(), type);
  setAttribute(*n, TypeCheckedAttr(), true);
  return n;
}

Node NodeManager::mkInstConstant(const TypeNode& type) {
  Node n = NodeBuilder<0>(this, kind::INST_CONSTANT);
  n.setAttribute(TypeAttr(), type);
  n.setAttribute(TypeCheckedAttr(), true);
  return n;
}

Node NodeManager::mkBooleanTermVariable() {
  Node n = NodeBuilder<0>(this, kind::BOOLEAN_TERM_VARIABLE);
  n.setAttribute(TypeAttr(), booleanType());
  n.setAttribute(TypeCheckedAttr(), true);
  return n;
}

Node NodeManager::mkNullaryOperator(const TypeNode& type, Kind k) {
  std::map< TypeNode, Node >::iterator it = d_unique_vars[k].find( type );
  if( it==d_unique_vars[k].end() ){
    Node n = NodeBuilder<0>(this, k).constructNode();
    setAttribute(n, TypeAttr(), type);
    //setAttribute(n, TypeCheckedAttr(), true);
    d_unique_vars[k][type] = n;
    Assert(n.getMetaKind() == kind::metakind::NULLARY_OPERATOR);
    return n;
  }else{
    return it->second;
  }
}

Node NodeManager::mkSingleton(const TypeNode& t, const TNode n)
{
  Assert(n.getType().isSubtypeOf(t))
      << "Invalid operands for mkSingleton. The type '" << n.getType()
      << "' of node '" << n << "' is not a subtype of '" << t << "'."
      << std::endl;
  Node op = mkConst(SingletonOp(t));
  Node singleton = mkNode(kind::SINGLETON, op, n);
  return singleton;
}

Node NodeManager::mkBag(const TypeNode& t, const TNode n, const TNode m)
{
  Assert(n.getType().isSubtypeOf(t))
      << "Invalid operands for mkBag. The type '" << n.getType()
      << "' of node '" << n << "' is not a subtype of '" << t << "'."
      << std::endl;
  Node op = mkConst(MakeBagOp(t));
  Node bag = mkNode(kind::MK_BAG, op, n, m);
  return bag;
}

Node NodeManager::mkAbstractValue(const TypeNode& type) {
  Node n = mkConst(AbstractValue(++d_abstractValueCount));
  n.setAttribute(TypeAttr(), type);
  n.setAttribute(TypeCheckedAttr(), true);
  return n;
}

bool NodeManager::safeToReclaimZombies() const{
  // FIXME multithreading
  return !d_inReclaimZombies && !d_attrManager->inGarbageCollection();
}

void NodeManager::deleteAttributes(const std::vector<const expr::attr::AttributeUniqueId*>& ids){
  d_attrManager->deleteAttributes(ids);
}

void NodeManager::debugHook(int debugFlag){
  // For debugging purposes only, DO NOT CHECK IN ANY CODE!
}

Kind NodeManager::getKindForFunction(TNode fun)
{
  TypeNode tn = fun.getType();
  if (tn.isFunction())
  {
    return kind::APPLY_UF;
  }
  else if (tn.isConstructor())
  {
    return kind::APPLY_CONSTRUCTOR;
  }
  else if (tn.isSelector())
  {
    return kind::APPLY_SELECTOR;
  }
  else if (tn.isTester())
  {
    return kind::APPLY_TESTER;
  }
  return kind::UNDEFINED_KIND;
}

}/* CVC4 namespace */
