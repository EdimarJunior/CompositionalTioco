/*********************                                                        */
/*! \file anti_skolem.h
 ** \verbatim
 ** Top contributors (to current version):
 **   Andrew Reynolds, Mathias Preiner, Tim King
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief anti-skolemization
 **/

#include "cvc4_private.h"

#ifndef CVC4__THEORY__QUANT_ANTI_SKOLEM_H
#define CVC4__THEORY__QUANT_ANTI_SKOLEM_H

#include <map>
#include <vector>

#include "context/cdhashset.h"
#include "context/cdo.h"
#include "expr/node.h"
#include "expr/type_node.h"
#include "theory/quantifiers/quant_util.h"
#include "theory/quantifiers/single_inv_partition.h"

namespace CVC4 {
namespace theory {
namespace quantifiers {

class QuantAntiSkolem : public QuantifiersModule {
public:
  QuantAntiSkolem( QuantifiersEngine * qe );
  virtual ~QuantAntiSkolem();

  bool sendAntiSkolemizeLemma( std::vector< Node >& quants,
                               bool pconnected = true );

  /* Call during quantifier engine's check */
  void check(Theory::Effort e, QEffort quant_e) override;
  /* Called for new quantifiers */
  void registerQuantifier(Node q) override {}
  void assertNode(Node n) override {}
  /** Identify this module (for debugging, dynamic configuration, etc..) */
  std::string identify() const override { return "QuantAntiSkolem"; }

 private:
  typedef context::CDHashSet<Node, NodeHashFunction> NodeSet;

  std::map< Node, bool > d_quant_processed;
  std::map< Node, SingleInvocationPartition > d_quant_sip;
  std::map< Node, std::vector< TypeNode > > d_ask_types;
  std::map< Node, std::vector< unsigned > > d_ask_types_index;
  
  class SkQuantTypeCache {
  public:
    std::map< TypeNode, SkQuantTypeCache > d_children;
    std::vector< Node > d_quants;
    void add( std::vector< TypeNode >& typs, Node q, unsigned index = 0 );
    void clear() { 
      d_children.clear();
      d_quants.clear(); 
    }
    void sendLemmas( QuantAntiSkolem * ask );
  }; 
  SkQuantTypeCache d_sqtc;
  
  class CDSkQuantCache {
  public:
    CDSkQuantCache( context::Context* c ) : d_valid( c, false ){}
    ~CDSkQuantCache();
    std::map< Node, CDSkQuantCache* > d_data;
    context::CDO< bool > d_valid;
    bool add( context::Context* c, std::vector< Node >& quants, unsigned index = 0 );
  };
  CDSkQuantCache * d_sqc;
}; /* class QuantAntiSkolem */

}/* namespace CVC4::theory::quantifiers */
}/* namespace CVC4::theory */
}/* namespace CVC4 */

#endif
