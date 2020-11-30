/*********************                                                        */
/*! \file solver_state.cpp
 ** \verbatim
 ** Top contributors (to current version):
 **   Andrew Reynolds, Tianyi Liang, Mathias Preiner
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief Implementation of the solver state of the theory of strings.
 **/

#include "theory/strings/solver_state.h"

#include "theory/strings/theory_strings_utils.h"
#include "theory/strings/word.h"

using namespace std;
using namespace CVC4::context;
using namespace CVC4::kind;

namespace CVC4 {
namespace theory {
namespace strings {

SolverState::SolverState(context::Context* c,
                         context::UserContext* u,
                         Valuation& v)
    : TheoryState(c, u, v), d_eeDisequalities(c), d_pendingConflictSet(c, false)
{
  d_zero = NodeManager::currentNM()->mkConst(Rational(0));
  d_false = NodeManager::currentNM()->mkConst(false);
}

SolverState::~SolverState()
{
  for (std::pair<const Node, EqcInfo*>& it : d_eqcInfo)
  {
    delete it.second;
  }
}

const context::CDList<Node>& SolverState::getDisequalityList() const
{
  return d_eeDisequalities;
}

void SolverState::eqNotifyNewClass(TNode t)
{
  Kind k = t.getKind();
  if (k == STRING_LENGTH || k == STRING_TO_CODE)
  {
    Node r = d_ee->getRepresentative(t[0]);
    EqcInfo* ei = getOrMakeEqcInfo(r);
    if (k == STRING_LENGTH)
    {
      ei->d_lengthTerm = t[0];
    }
    else
    {
      ei->d_codeTerm = t[0];
    }
  }
  else if (t.isConst())
  {
    if (t.getType().isStringLike())
    {
      EqcInfo* ei = getOrMakeEqcInfo(t);
      ei->d_prefixC = t;
      ei->d_suffixC = t;
    }
  }
  else if (k == STRING_CONCAT)
  {
    addEndpointsToEqcInfo(t, t, t);
  }
}

void SolverState::eqNotifyMerge(TNode t1, TNode t2)
{
  EqcInfo* e2 = getOrMakeEqcInfo(t2, false);
  if (e2)
  {
    Assert(t1.getType().isStringLike());
    EqcInfo* e1 = getOrMakeEqcInfo(t1);
    // add information from e2 to e1
    if (!e2->d_lengthTerm.get().isNull())
    {
      e1->d_lengthTerm.set(e2->d_lengthTerm);
    }
    if (!e2->d_codeTerm.get().isNull())
    {
      e1->d_codeTerm.set(e2->d_codeTerm);
    }
    if (!e2->d_prefixC.get().isNull())
    {
      setPendingPrefixConflictWhen(
          e1->addEndpointConst(e2->d_prefixC, Node::null(), false));
    }
    if (!e2->d_suffixC.get().isNull())
    {
      setPendingPrefixConflictWhen(
          e1->addEndpointConst(e2->d_suffixC, Node::null(), true));
    }
    if (e2->d_cardinalityLemK.get() > e1->d_cardinalityLemK.get())
    {
      e1->d_cardinalityLemK.set(e2->d_cardinalityLemK);
    }
    if (!e2->d_normalizedLength.get().isNull())
    {
      e1->d_normalizedLength.set(e2->d_normalizedLength);
    }
  }
}

void SolverState::eqNotifyDisequal(TNode t1, TNode t2, TNode reason)
{
  if (t1.getType().isStringLike())
  {
    // store disequalities between strings, may need to check if their lengths
    // are equal/disequal
    d_eeDisequalities.push_back(t1.eqNode(t2));
  }
}

EqcInfo* SolverState::getOrMakeEqcInfo(Node eqc, bool doMake)
{
  std::map<Node, EqcInfo*>::iterator eqc_i = d_eqcInfo.find(eqc);
  if (eqc_i != d_eqcInfo.end())
  {
    return eqc_i->second;
  }
  if (doMake)
  {
    EqcInfo* ei = new EqcInfo(d_context);
    d_eqcInfo[eqc] = ei;
    return ei;
  }
  return nullptr;
}

TheoryModel* SolverState::getModel() { return d_valuation.getModel(); }

void SolverState::addEndpointsToEqcInfo(Node t, Node concat, Node eqc)
{
  Assert(concat.getKind() == STRING_CONCAT
         || concat.getKind() == REGEXP_CONCAT);
  EqcInfo* ei = nullptr;
  // check each side
  for (unsigned r = 0; r < 2; r++)
  {
    unsigned index = r == 0 ? 0 : concat.getNumChildren() - 1;
    Node c = utils::getConstantComponent(concat[index]);
    if (!c.isNull())
    {
      if (ei == nullptr)
      {
        ei = getOrMakeEqcInfo(eqc);
      }
      Trace("strings-eager-pconf-debug")
          << "New term: " << concat << " for " << t << " with prefix " << c
          << " (" << (r == 1) << ")" << std::endl;
      setPendingPrefixConflictWhen(ei->addEndpointConst(t, c, r == 1));
    }
  }
}

Node SolverState::getLengthExp(Node t, std::vector<Node>& exp, Node te)
{
  Assert(areEqual(t, te));
  Node lt = utils::mkNLength(te);
  if (hasTerm(lt))
  {
    // use own length if it exists, leads to shorter explanation
    return lt;
  }
  EqcInfo* ei = getOrMakeEqcInfo(t, false);
  Node lengthTerm = ei ? ei->d_lengthTerm : Node::null();
  if (lengthTerm.isNull())
  {
    // typically shouldnt be necessary
    lengthTerm = t;
  }
  Debug("strings") << "SolverState::getLengthTerm " << t << " is " << lengthTerm
                   << std::endl;
  if (te != lengthTerm)
  {
    exp.push_back(te.eqNode(lengthTerm));
  }
  return Rewriter::rewrite(
      NodeManager::currentNM()->mkNode(STRING_LENGTH, lengthTerm));
}

Node SolverState::getLength(Node t, std::vector<Node>& exp)
{
  return getLengthExp(t, exp, t);
}

Node SolverState::explainNonEmpty(Node s)
{
  Assert(s.getType().isStringLike());
  Node emp = Word::mkEmptyWord(s.getType());
  if (areDisequal(s, emp))
  {
    return s.eqNode(emp).negate();
  }
  Node sLen = utils::mkNLength(s);
  if (areDisequal(sLen, d_zero))
  {
    return sLen.eqNode(d_zero).negate();
  }
  return Node::null();
}

bool SolverState::isEqualEmptyWord(Node s, Node& emps)
{
  Node sr = getRepresentative(s);
  if (sr.isConst())
  {
    if (Word::getLength(sr) == 0)
    {
      emps = sr;
      return true;
    }
  }
  return false;
}

void SolverState::setPendingPrefixConflictWhen(Node conf)
{
  if (conf.isNull() || d_pendingConflictSet.get())
  {
    return;
  }
  InferInfo iiPrefixConf;
  iiPrefixConf.d_id = Inference::PREFIX_CONFLICT;
  iiPrefixConf.d_conc = d_false;
  utils::flattenOp(AND, conf, iiPrefixConf.d_ant);
  setPendingConflict(iiPrefixConf);
}

void SolverState::setPendingConflict(InferInfo& ii)
{
  if (!d_pendingConflictSet.get())
  {
    d_pendingConflict = ii;
    d_pendingConflictSet.set(true);
  }
}

bool SolverState::hasPendingConflict() const { return d_pendingConflictSet; }

bool SolverState::getPendingConflict(InferInfo& ii) const
{
  if (d_pendingConflictSet)
  {
    ii = d_pendingConflict;
    return true;
  }
  return false;
}

std::pair<bool, Node> SolverState::entailmentCheck(options::TheoryOfMode mode,
                                                   TNode lit)
{
  return d_valuation.entailmentCheck(mode, lit);
}

void SolverState::separateByLength(
    const std::vector<Node>& n,
    std::map<TypeNode, std::vector<std::vector<Node>>>& cols,
    std::map<TypeNode, std::vector<Node>>& lts)
{
  unsigned leqc_counter = 0;
  // map (length, type) to an equivalence class identifier
  std::map<std::pair<Node, TypeNode>, unsigned> eqc_to_leqc;
  // backwards map
  std::map<unsigned, std::pair<Node, TypeNode>> leqc_to_eqc;
  // Collection of eqc for each identifier. Notice that some identifiers may
  // not have an associated length in the mappings above, if the length of
  // an equivalence class is unknown.
  std::map<unsigned, std::vector<Node> > eqc_to_strings;
  NodeManager* nm = NodeManager::currentNM();
  for (const Node& eqc : n)
  {
    Assert(d_ee->getRepresentative(eqc) == eqc);
    TypeNode tnEqc = eqc.getType();
    EqcInfo* ei = getOrMakeEqcInfo(eqc, false);
    Node lt = ei ? ei->d_lengthTerm : Node::null();
    if (!lt.isNull())
    {
      lt = nm->mkNode(STRING_LENGTH, lt);
      Node r = d_ee->getRepresentative(lt);
      std::pair<Node, TypeNode> lkey(r, tnEqc);
      if (eqc_to_leqc.find(lkey) == eqc_to_leqc.end())
      {
        eqc_to_leqc[lkey] = leqc_counter;
        leqc_to_eqc[leqc_counter] = lkey;
        leqc_counter++;
      }
      eqc_to_strings[eqc_to_leqc[lkey]].push_back(eqc);
    }
    else
    {
      eqc_to_strings[leqc_counter].push_back(eqc);
      leqc_counter++;
    }
  }
  for (const std::pair<const unsigned, std::vector<Node> >& p : eqc_to_strings)
  {
    Assert(!p.second.empty());
    // get the type of the collection
    TypeNode stn = p.second[0].getType();
    cols[stn].emplace_back(p.second.begin(), p.second.end());
    lts[stn].push_back(leqc_to_eqc[p.first].first);
  }
}

}  // namespace strings
}  // namespace theory
}  // namespace CVC4
