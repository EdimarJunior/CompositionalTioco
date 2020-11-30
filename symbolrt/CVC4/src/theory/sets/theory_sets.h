/*********************                                                        */
/*! \file theory_sets.h
 ** \verbatim
 ** Top contributors (to current version):
 **   Andrew Reynolds, Tim King, Kshitij Bansal
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief Sets theory.
 **
 ** Sets theory.
 **/

#include "cvc4_private.h"

#ifndef CVC4__THEORY__SETS__THEORY_SETS_H
#define CVC4__THEORY__SETS__THEORY_SETS_H

#include <memory>

#include "theory/sets/inference_manager.h"
#include "theory/sets/skolem_cache.h"
#include "theory/sets/solver_state.h"
#include "theory/theory.h"
#include "theory/theory_eq_notify.h"
#include "theory/uf/equality_engine.h"

namespace CVC4 {
namespace theory {
namespace sets {

class TheorySetsPrivate;
class TheorySetsScrutinize;

class TheorySets : public Theory
{
  friend class TheorySetsPrivate;
  friend class TheorySetsRels;
 public:
  /** Constructs a new instance of TheorySets w.r.t. the provided contexts. */
  TheorySets(context::Context* c,
             context::UserContext* u,
             OutputChannel& out,
             Valuation valuation,
             const LogicInfo& logicInfo,
             ProofNodeManager* pnm);
  ~TheorySets() override;

  //--------------------------------- initialization
  /** get the official theory rewriter of this theory */
  TheoryRewriter* getTheoryRewriter() override;
  /**
   * Returns true if we need an equality engine. If so, we initialize the
   * information regarding how it should be setup. For details, see the
   * documentation in Theory::needsEqualityEngine.
   */
  bool needsEqualityEngine(EeSetupInfo& esi) override;
  /** finish initialization */
  void finishInit() override;
  //--------------------------------- end initialization

  //--------------------------------- standard check
  /** Post-check, called after the fact queue of the theory is processed. */
  void postCheck(Effort level) override;
  /** Notify fact */
  void notifyFact(TNode atom, bool pol, TNode fact, bool isInternal) override;
  //--------------------------------- end standard check
  /** Collect model values in m based on the relevant terms given by termSet */
  bool collectModelValues(TheoryModel* m,
                          const std::set<Node>& termSet) override;
  void computeCareGraph() override;
  TrustNode explain(TNode) override;
  Node getModelValue(TNode) override;
  std::string identify() const override { return "THEORY_SETS"; }
  void preRegisterTerm(TNode node) override;
  TrustNode expandDefinition(Node n) override;
  PPAssertStatus ppAssert(TrustNode tin,
                          TrustSubstitutionMap& outSubstitutions) override;
  void presolve() override;
  bool isEntailed(Node n, bool pol);

 private:
  /** Functions to handle callbacks from equality engine */
  class NotifyClass : public TheoryEqNotifyClass
  {
   public:
    NotifyClass(TheorySetsPrivate& theory, TheoryInferenceManager& im)
        : TheoryEqNotifyClass(im), d_theory(theory)
    {
    }
    void eqNotifyNewClass(TNode t) override;
    void eqNotifyMerge(TNode t1, TNode t2) override;
    void eqNotifyDisequal(TNode t1, TNode t2, TNode reason) override;
    
   private:
    TheorySetsPrivate& d_theory;
  };
  /** The skolem cache */
  SkolemCache d_skCache;
  /** The state of the sets solver at full effort */
  SolverState d_state;
  /** The inference manager */
  InferenceManager d_im;
  /** The internal theory */
  std::unique_ptr<TheorySetsPrivate> d_internal;
  /** Instance of the above class */
  NotifyClass d_notify;
}; /* class TheorySets */

}/* CVC4::theory::sets namespace */
}/* CVC4::theory namespace */
}/* CVC4 namespace */

#endif /* CVC4__THEORY__SETS__THEORY_SETS_H */
