/*********************                                                        */
/*! \file symbol_manager.cpp
 ** \verbatim
 ** Top contributors (to current version):
 **   Andrew Reynolds
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief The symbol manager
 **/

#include "expr/symbol_manager.h"

#include "context/cdhashmap.h"
#include "context/cdhashset.h"
#include "context/cdlist.h"
#include "context/cdo.h"

using namespace CVC4::context;

namespace CVC4 {

// ---------------------------------------------- SymbolManager::Implementation

class SymbolManager::Implementation
{
  using TermStringMap = CDHashMap<api::Term, std::string, api::TermHashFunction>;
  using TermSet = CDHashSet<api::Term, api::TermHashFunction>;
  using SortList = CDList<api::Sort>;
  using TermList = CDList<api::Term>;

 public:
  Implementation()
      : d_context(),
        d_names(&d_context),
        d_namedAsserts(&d_context),
        d_declareSorts(&d_context),
        d_declareTerms(&d_context),
        d_hasPushedScope(&d_context, false)
  {
  }

  ~Implementation() {}
  /** set expression name */
  bool setExpressionName(api::Term t,
                         const std::string& name,
                         bool isAssertion = false);
  /** get expression name */
  bool getExpressionName(api::Term t,
                         std::string& name,
                         bool isAssertion = false) const;
  /** get expression names */
  void getExpressionNames(const std::vector<api::Term>& ts,
                          std::vector<std::string>& names,
                          bool areAssertions = false) const;
  /** get expression names */
  std::map<api::Term, std::string> getExpressionNames(bool areAssertions) const;
  /** get model declare sorts */
  std::vector<api::Sort> getModelDeclareSorts() const;
  /** get model declare terms */
  std::vector<api::Term> getModelDeclareTerms() const;
  /** Add declared sort to the list of model declarations. */
  void addModelDeclarationSort(api::Sort s);
  /** Add declared term to the list of model declarations. */
  void addModelDeclarationTerm(api::Term t);
  /** reset */
  void reset();
  /** Push a scope in the expression names. */
  void pushScope(bool isUserContext);
  /** Pop a scope in the expression names. */
  void popScope();

 private:
  /** The context manager for the scope maps. */
  Context d_context;
  /** Map terms to names */
  TermStringMap d_names;
  /** The set of terms with assertion names */
  TermSet d_namedAsserts;
  /** Declared sorts (for model printing) */
  SortList d_declareSorts;
  /** Declared terms (for model printing) */
  TermList d_declareTerms;
  /**
   * Have we pushed a scope (e.g. a let or quantifier) in the current context?
   */
  CDO<bool> d_hasPushedScope;
};

bool SymbolManager::Implementation::setExpressionName(api::Term t,
                                                      const std::string& name,
                                                      bool isAssertion)
{
  Trace("sym-manager") << "set expression name: " << t << " -> " << name
                       << ", isAssertion=" << isAssertion << std::endl;
  // cannot name subexpressions under quantifiers
  PrettyCheckArgument(
      !d_hasPushedScope.get(), name, "cannot name function in a scope");
  if (isAssertion)
  {
    d_namedAsserts.insert(t);
  }
  if (d_names.find(t) != d_names.end())
  {
    // already named assertion
    return false;
  }
  d_names[t] = name;
  return true;
}

bool SymbolManager::Implementation::getExpressionName(api::Term t,
                                                      std::string& name,
                                                      bool isAssertion) const
{
  TermStringMap::const_iterator it = d_names.find(t);
  if (it == d_names.end())
  {
    return false;
  }
  if (isAssertion)
  {
    // must be an assertion name
    if (d_namedAsserts.find(t) == d_namedAsserts.end())
    {
      return false;
    }
  }
  name = (*it).second;
  return true;
}

void SymbolManager::Implementation::getExpressionNames(
    const std::vector<api::Term>& ts,
    std::vector<std::string>& names,
    bool areAssertions) const
{
  for (const api::Term& t : ts)
  {
    std::string name;
    if (getExpressionName(t, name, areAssertions))
    {
      names.push_back(name);
    }
  }
}

std::map<api::Term, std::string>
SymbolManager::Implementation::getExpressionNames(bool areAssertions) const
{
  std::map<api::Term, std::string> emap;
  for (TermStringMap::const_iterator it = d_names.begin(),
                                     itend = d_names.end();
       it != itend;
       ++it)
  {
    api::Term t = (*it).first;
    if (areAssertions && d_namedAsserts.find(t) == d_namedAsserts.end())
    {
      continue;
    }
    emap[t] = (*it).second;
  }
  return emap;
}

std::vector<api::Sort> SymbolManager::Implementation::getModelDeclareSorts()
    const
{
  std::vector<api::Sort> declareSorts(d_declareSorts.begin(),
                                      d_declareSorts.end());
  return declareSorts;
}

std::vector<api::Term> SymbolManager::Implementation::getModelDeclareTerms()
    const
{
  std::vector<api::Term> declareTerms(d_declareTerms.begin(),
                                      d_declareTerms.end());
  return declareTerms;
}

void SymbolManager::Implementation::addModelDeclarationSort(api::Sort s)
{
  Trace("sym-manager") << "addModelDeclarationSort " << s << std::endl;
  d_declareSorts.push_back(s);
}

void SymbolManager::Implementation::addModelDeclarationTerm(api::Term t)
{
  Trace("sym-manager") << "addModelDeclarationTerm " << t << std::endl;
  d_declareTerms.push_back(t);
}

void SymbolManager::Implementation::pushScope(bool isUserContext)
{
  Trace("sym-manager") << "pushScope, isUserContext = " << isUserContext
                       << std::endl;
  PrettyCheckArgument(!d_hasPushedScope.get() || !isUserContext,
                      "cannot push a user context within a scope context");
  d_context.push();
  if (!isUserContext)
  {
    d_hasPushedScope = true;
  }
}

void SymbolManager::Implementation::popScope()
{
  Trace("sym-manager") << "popScope" << std::endl;
  if (d_context.getLevel() == 0)
  {
    throw ScopeException();
  }
  d_context.pop();
  Trace("sym-manager-debug")
      << "d_hasPushedScope is now " << d_hasPushedScope.get() << std::endl;
}

void SymbolManager::Implementation::reset()
{
  // clear names?
}

// ---------------------------------------------- SymbolManager

SymbolManager::SymbolManager(api::Solver* s)
    : d_solver(s),
      d_implementation(new SymbolManager::Implementation()),
      d_globalDeclarations(false)
{
}

SymbolManager::~SymbolManager() {}

SymbolTable* SymbolManager::getSymbolTable() { return &d_symtabAllocated; }

bool SymbolManager::setExpressionName(api::Term t,
                                      const std::string& name,
                                      bool isAssertion)
{
  return d_implementation->setExpressionName(t, name, isAssertion);
}

bool SymbolManager::getExpressionName(api::Term t,
                                      std::string& name,
                                      bool isAssertion) const
{
  return d_implementation->getExpressionName(t, name, isAssertion);
}

void SymbolManager::getExpressionNames(const std::vector<api::Term>& ts,
                                       std::vector<std::string>& names,
                                       bool areAssertions) const
{
  return d_implementation->getExpressionNames(ts, names, areAssertions);
}

std::map<api::Term, std::string> SymbolManager::getExpressionNames(
    bool areAssertions) const
{
  return d_implementation->getExpressionNames(areAssertions);
}
std::vector<api::Sort> SymbolManager::getModelDeclareSorts() const
{
  return d_implementation->getModelDeclareSorts();
}
std::vector<api::Term> SymbolManager::getModelDeclareTerms() const
{
  return d_implementation->getModelDeclareTerms();
}

void SymbolManager::addModelDeclarationSort(api::Sort s)
{
  d_implementation->addModelDeclarationSort(s);
}

void SymbolManager::addModelDeclarationTerm(api::Term t)
{
  d_implementation->addModelDeclarationTerm(t);
}

size_t SymbolManager::scopeLevel() const
{
  return d_symtabAllocated.getLevel();
}

void SymbolManager::pushScope(bool isUserContext)
{
  d_implementation->pushScope(isUserContext);
  d_symtabAllocated.pushScope();
}

void SymbolManager::popScope()
{
  d_symtabAllocated.popScope();
  d_implementation->popScope();
}

void SymbolManager::setGlobalDeclarations(bool flag)
{
  d_globalDeclarations = flag;
}

bool SymbolManager::getGlobalDeclarations() const
{
  return d_globalDeclarations;
}

void SymbolManager::reset()
{
  d_symtabAllocated.reset();
  d_implementation->reset();
}

}  // namespace CVC4
