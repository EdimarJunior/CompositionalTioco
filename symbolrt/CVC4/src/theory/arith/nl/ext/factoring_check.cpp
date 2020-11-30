/*********************                                                        */
/*! \file factoring_check.cpp
 ** \verbatim
 ** Top contributors (to current version):
 **   Gereon Kremer
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief Implementation of factoring check
 **/

#include "theory/arith/nl/ext/factoring_check.h"

#include "expr/node.h"
#include "theory/arith/arith_msum.h"
#include "theory/arith/inference_manager.h"
#include "theory/arith/nl/nl_model.h"

namespace CVC4 {
namespace theory {
namespace arith {
namespace nl {

FactoringCheck::FactoringCheck(InferenceManager& im, NlModel& model)
    : d_im(im), d_model(model)
{
  d_zero = NodeManager::currentNM()->mkConst(Rational(0));
  d_one = NodeManager::currentNM()->mkConst(Rational(1));
}

void FactoringCheck::check(const std::vector<Node>& asserts,
                           const std::vector<Node>& false_asserts)
{
  NodeManager* nm = NodeManager::currentNM();
  Trace("nl-ext") << "Get factoring lemmas..." << std::endl;
  for (const Node& lit : asserts)
  {
    bool polarity = lit.getKind() != Kind::NOT;
    Node atom = lit.getKind() == Kind::NOT ? lit[0] : lit;
    Node litv = d_model.computeConcreteModelValue(lit);
    bool considerLit = false;
    // Only consider literals that are in false_asserts.
    considerLit = std::find(false_asserts.begin(), false_asserts.end(), lit)
                  != false_asserts.end();

    if (considerLit)
    {
      std::map<Node, Node> msum;
      if (ArithMSum::getMonomialSumLit(atom, msum))
      {
        Trace("nl-ext-factor") << "Factoring for literal " << lit
                               << ", monomial sum is : " << std::endl;
        if (Trace.isOn("nl-ext-factor"))
        {
          ArithMSum::debugPrintMonomialSum(msum, "nl-ext-factor");
        }
        std::map<Node, std::vector<Node> > factor_to_mono;
        std::map<Node, std::vector<Node> > factor_to_mono_orig;
        for (std::map<Node, Node>::iterator itm = msum.begin();
             itm != msum.end();
             ++itm)
        {
          if (!itm->first.isNull())
          {
            if (itm->first.getKind() == Kind::NONLINEAR_MULT)
            {
              std::vector<Node> children;
              for (unsigned i = 0; i < itm->first.getNumChildren(); i++)
              {
                children.push_back(itm->first[i]);
              }
              std::map<Node, bool> processed;
              for (unsigned i = 0; i < itm->first.getNumChildren(); i++)
              {
                if (processed.find(itm->first[i]) == processed.end())
                {
                  processed[itm->first[i]] = true;
                  children[i] = d_one;
                  if (!itm->second.isNull())
                  {
                    children.push_back(itm->second);
                  }
                  Node val = nm->mkNode(Kind::MULT, children);
                  if (!itm->second.isNull())
                  {
                    children.pop_back();
                  }
                  children[i] = itm->first[i];
                  val = Rewriter::rewrite(val);
                  factor_to_mono[itm->first[i]].push_back(val);
                  factor_to_mono_orig[itm->first[i]].push_back(itm->first);
                }
              }
            }
          }
        }
        for (std::map<Node, std::vector<Node> >::iterator itf =
                 factor_to_mono.begin();
             itf != factor_to_mono.end();
             ++itf)
        {
          Node x = itf->first;
          if (itf->second.size() == 1)
          {
            std::map<Node, Node>::iterator itm = msum.find(x);
            if (itm != msum.end())
            {
              itf->second.push_back(itm->second.isNull() ? d_one : itm->second);
              factor_to_mono_orig[x].push_back(x);
            }
          }
          if (itf->second.size() <= 1)
          {
            continue;
          }
          Node sum = nm->mkNode(Kind::PLUS, itf->second);
          sum = Rewriter::rewrite(sum);
          Trace("nl-ext-factor")
              << "* Factored sum for " << x << " : " << sum << std::endl;
          Node kf = getFactorSkolem(sum);
          std::vector<Node> poly;
          poly.push_back(nm->mkNode(Kind::MULT, x, kf));
          std::map<Node, std::vector<Node> >::iterator itfo =
              factor_to_mono_orig.find(x);
          Assert(itfo != factor_to_mono_orig.end());
          for (std::map<Node, Node>::iterator itm = msum.begin();
               itm != msum.end();
               ++itm)
          {
            if (std::find(itfo->second.begin(), itfo->second.end(), itm->first)
                == itfo->second.end())
            {
              poly.push_back(ArithMSum::mkCoeffTerm(
                  itm->second, itm->first.isNull() ? d_one : itm->first));
            }
          }
          Node polyn =
              poly.size() == 1 ? poly[0] : nm->mkNode(Kind::PLUS, poly);
          Trace("nl-ext-factor")
              << "...factored polynomial : " << polyn << std::endl;
          Node conc_lit = nm->mkNode(atom.getKind(), polyn, d_zero);
          conc_lit = Rewriter::rewrite(conc_lit);
          if (!polarity)
          {
            conc_lit = conc_lit.negate();
          }

          std::vector<Node> lemma_disj;
          lemma_disj.push_back(lit.negate());
          lemma_disj.push_back(conc_lit);
          Node flem = nm->mkNode(Kind::OR, lemma_disj);
          Trace("nl-ext-factor") << "...lemma is " << flem << std::endl;
          d_im.addPendingArithLemma(flem, InferenceId::NL_FACTOR);
        }
      }
    }
  }
}

Node FactoringCheck::getFactorSkolem(Node n)
{
  std::map<Node, Node>::iterator itf = d_factor_skolem.find(n);
  if (itf == d_factor_skolem.end())
  {
    NodeManager* nm = NodeManager::currentNM();
    Node k = nm->mkSkolem("kf", n.getType());
    Node k_eq = Rewriter::rewrite(k.eqNode(n));
    d_im.addPendingArithLemma(k_eq, InferenceId::NL_FACTOR);
    d_factor_skolem[n] = k;
    return k;
  }
  return itf->second;
}

}  // namespace nl
}  // namespace arith
}  // namespace theory
}  // namespace CVC4