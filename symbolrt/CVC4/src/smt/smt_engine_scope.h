/*********************                                                        */
/*! \file smt_engine_scope.h
 ** \verbatim
 ** Top contributors (to current version):
 **   Andrew Reynolds, Andres Noetzli, Tim King
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief [[ Add one-line brief description here ]]
 **
 ** [[ Add lengthier description here ]]
 ** \todo document this file
 **/

#include "cvc4_private.h"

#ifndef CVC4__SMT__SMT_ENGINE_SCOPE_H
#define CVC4__SMT__SMT_ENGINE_SCOPE_H

#include "expr/node_manager.h"

#include "options/options.h"

namespace CVC4 {

class ProofManager;
class SmtEngine;
class StatisticsRegistry;

namespace smt {

SmtEngine* currentSmtEngine();
bool smtEngineInScope();

// FIXME: Maybe move into SmtScope?
ProofManager* currentProofManager();

/** get the current resource manager */
ResourceManager* currentResourceManager();

class SmtScope : public NodeManagerScope
{
 public:
  SmtScope(const SmtEngine* smt);
  ~SmtScope();
  /**
   * This returns the StatisticsRegistry attached to the currently in scope
   * SmtEngine.
   */
  static StatisticsRegistry* currentStatisticsRegistry();

 private:
  /** The old SmtEngine, to be restored on destruction. */
  SmtEngine* d_oldSmtEngine;
  /** Options scope */
  Options::OptionsScope d_optionsScope;
};/* class SmtScope */


}/* CVC4::smt namespace */
}/* CVC4 namespace */

#endif /* CVC4__SMT__SMT_ENGINE_SCOPE_H */
