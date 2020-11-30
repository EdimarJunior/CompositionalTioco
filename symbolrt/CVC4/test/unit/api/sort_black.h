/*********************                                                        */
/*! \file sort_black.h
 ** \verbatim
 ** Top contributors (to current version):
 **   Aina Niemetz, Andrew Reynolds
 ** This file is part of the CVC4 project.
 ** Copyright (c) 2009-2020 by the authors listed in the file AUTHORS
 ** in the top-level source directory and their institutional affiliations.
 ** All rights reserved.  See the file COPYING in the top-level source
 ** directory for licensing information.\endverbatim
 **
 ** \brief Black box testing of the guards of the C++ API functions.
 **
 ** Black box testing of the guards of the C++ API functions.
 **/

#include <cxxtest/TestSuite.h>

#include "api/cvc4cpp.h"
#include "base/configuration.h"

using namespace CVC4::api;

class SortBlack : public CxxTest::TestSuite
{
 public:
  void setUp() override;
  void tearDown() override;

  void testGetDatatype();
  void testDatatypeSorts();
  void testInstantiate();
  void testGetFunctionArity();
  void testGetFunctionDomainSorts();
  void testGetFunctionCodomainSort();
  void testGetArrayIndexSort();
  void testGetArrayElementSort();
  void testGetSetElementSort();
  void testGetBagElementSort();
  void testGetSequenceElementSort();
  void testGetUninterpretedSortName();
  void testIsUninterpretedSortParameterized();
  void testGetUninterpretedSortParamSorts();
  void testGetUninterpretedSortConstructorName();
  void testGetUninterpretedSortConstructorArity();
  void testGetBVSize();
  void testGetFPExponentSize();
  void testGetFPSignificandSize();
  void testGetDatatypeParamSorts();
  void testGetDatatypeArity();
  void testGetTupleLength();
  void testGetTupleSorts();

  void testSortCompare();
  void testSortSubtyping();

  void testSortScopedToString();

 private:
  Solver d_solver;
};

void SortBlack::setUp() {}

void SortBlack::tearDown() {}

void SortBlack::testGetDatatype()
{
  // create datatype sort, check should not fail
  DatatypeDecl dtypeSpec = d_solver.mkDatatypeDecl("list");
  DatatypeConstructorDecl cons = d_solver.mkDatatypeConstructorDecl("cons");
  cons.addSelector("head", d_solver.getIntegerSort());
  dtypeSpec.addConstructor(cons);
  DatatypeConstructorDecl nil = d_solver.mkDatatypeConstructorDecl("nil");
  dtypeSpec.addConstructor(nil);
  Sort dtypeSort = d_solver.mkDatatypeSort(dtypeSpec);
  TS_ASSERT_THROWS_NOTHING(dtypeSort.getDatatype());
  // create bv sort, check should fail
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getDatatype(), CVC4ApiException&);
}

void SortBlack::testDatatypeSorts()
{
  Sort intSort = d_solver.getIntegerSort();
  // create datatype sort to test
  DatatypeDecl dtypeSpec = d_solver.mkDatatypeDecl("list");
  DatatypeConstructorDecl cons = d_solver.mkDatatypeConstructorDecl("cons");
  cons.addSelector("head", intSort);
  cons.addSelectorSelf("tail");
  dtypeSpec.addConstructor(cons);
  DatatypeConstructorDecl nil = d_solver.mkDatatypeConstructorDecl("nil");
  dtypeSpec.addConstructor(nil);
  Sort dtypeSort = d_solver.mkDatatypeSort(dtypeSpec);
  Datatype dt = dtypeSort.getDatatype();
  TS_ASSERT(!dtypeSort.isConstructor());
  TS_ASSERT_THROWS(dtypeSort.getConstructorCodomainSort(), CVC4ApiException&);
  TS_ASSERT_THROWS(dtypeSort.getConstructorDomainSorts(), CVC4ApiException&);
  TS_ASSERT_THROWS(dtypeSort.getConstructorArity(), CVC4ApiException&);

  // get constructor
  DatatypeConstructor dcons = dt[0];
  Term consTerm = dcons.getConstructorTerm();
  Sort consSort = consTerm.getSort();
  TS_ASSERT(consSort.isConstructor());
  TS_ASSERT(!consSort.isTester());
  TS_ASSERT(!consSort.isSelector());
  TS_ASSERT(consSort.getConstructorArity() == 2);
  std::vector<Sort> consDomSorts = consSort.getConstructorDomainSorts();
  TS_ASSERT(consDomSorts[0] == intSort);
  TS_ASSERT(consDomSorts[1] == dtypeSort);
  TS_ASSERT(consSort.getConstructorCodomainSort() == dtypeSort);

  // get tester
  Term isConsTerm = dcons.getTesterTerm();
  TS_ASSERT(isConsTerm.getSort().isTester());
  TS_ASSERT(isConsTerm.getSort().getTesterDomainSort() == dtypeSort);
  Sort booleanSort = d_solver.getBooleanSort();
  TS_ASSERT(isConsTerm.getSort().getTesterCodomainSort() == booleanSort);
  TS_ASSERT_THROWS(booleanSort.getTesterDomainSort(), CVC4ApiException&);
  TS_ASSERT_THROWS(booleanSort.getTesterCodomainSort(), CVC4ApiException&);

  // get selector
  DatatypeSelector dselTail = dcons[1];
  Term tailTerm = dselTail.getSelectorTerm();
  TS_ASSERT(tailTerm.getSort().isSelector());
  TS_ASSERT(tailTerm.getSort().getSelectorDomainSort() == dtypeSort);
  TS_ASSERT(tailTerm.getSort().getSelectorCodomainSort() == dtypeSort);
  TS_ASSERT_THROWS(booleanSort.getSelectorDomainSort(), CVC4ApiException&);
  TS_ASSERT_THROWS(booleanSort.getSelectorCodomainSort(), CVC4ApiException&);
}

void SortBlack::testInstantiate()
{
  // instantiate parametric datatype, check should not fail
  Sort sort = d_solver.mkParamSort("T");
  DatatypeDecl paramDtypeSpec = d_solver.mkDatatypeDecl("paramlist", sort);
  DatatypeConstructorDecl paramCons =
      d_solver.mkDatatypeConstructorDecl("cons");
  DatatypeConstructorDecl paramNil = d_solver.mkDatatypeConstructorDecl("nil");
  paramCons.addSelector("head", sort);
  paramDtypeSpec.addConstructor(paramCons);
  paramDtypeSpec.addConstructor(paramNil);
  Sort paramDtypeSort = d_solver.mkDatatypeSort(paramDtypeSpec);
  TS_ASSERT_THROWS_NOTHING(
      paramDtypeSort.instantiate(std::vector<Sort>{d_solver.getIntegerSort()}));
  // instantiate non-parametric datatype sort, check should fail
  DatatypeDecl dtypeSpec = d_solver.mkDatatypeDecl("list");
  DatatypeConstructorDecl cons = d_solver.mkDatatypeConstructorDecl("cons");
  cons.addSelector("head", d_solver.getIntegerSort());
  dtypeSpec.addConstructor(cons);
  DatatypeConstructorDecl nil = d_solver.mkDatatypeConstructorDecl("nil");
  dtypeSpec.addConstructor(nil);
  Sort dtypeSort = d_solver.mkDatatypeSort(dtypeSpec);
  TS_ASSERT_THROWS(
      dtypeSort.instantiate(std::vector<Sort>{d_solver.getIntegerSort()}),
      CVC4ApiException&);
}

void SortBlack::testGetFunctionArity()
{
  Sort funSort = d_solver.mkFunctionSort(d_solver.mkUninterpretedSort("u"),
                                         d_solver.getIntegerSort());
  TS_ASSERT_THROWS_NOTHING(funSort.getFunctionArity());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getFunctionArity(), CVC4ApiException&);
}

void SortBlack::testGetFunctionDomainSorts()
{
  Sort funSort = d_solver.mkFunctionSort(d_solver.mkUninterpretedSort("u"),
                                         d_solver.getIntegerSort());
  TS_ASSERT_THROWS_NOTHING(funSort.getFunctionDomainSorts());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getFunctionDomainSorts(), CVC4ApiException&);
}

void SortBlack::testGetFunctionCodomainSort()
{
  Sort funSort = d_solver.mkFunctionSort(d_solver.mkUninterpretedSort("u"),
                                         d_solver.getIntegerSort());
  TS_ASSERT_THROWS_NOTHING(funSort.getFunctionCodomainSort());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getFunctionCodomainSort(), CVC4ApiException&);
}

void SortBlack::testGetArrayIndexSort()
{
  Sort elementSort = d_solver.mkBitVectorSort(32);
  Sort indexSort = d_solver.mkBitVectorSort(32);
  Sort arraySort = d_solver.mkArraySort(indexSort, elementSort);
  TS_ASSERT_THROWS_NOTHING(arraySort.getArrayIndexSort());
  TS_ASSERT_THROWS(indexSort.getArrayIndexSort(), CVC4ApiException&);
}

void SortBlack::testGetArrayElementSort()
{
  Sort elementSort = d_solver.mkBitVectorSort(32);
  Sort indexSort = d_solver.mkBitVectorSort(32);
  Sort arraySort = d_solver.mkArraySort(indexSort, elementSort);
  TS_ASSERT_THROWS_NOTHING(arraySort.getArrayElementSort());
  TS_ASSERT_THROWS(indexSort.getArrayElementSort(), CVC4ApiException&);
}

void SortBlack::testGetSetElementSort()
{
  Sort setSort = d_solver.mkSetSort(d_solver.getIntegerSort());
  TS_ASSERT_THROWS_NOTHING(setSort.getSetElementSort());
  Sort elementSort = setSort.getSetElementSort();
  TS_ASSERT(elementSort == d_solver.getIntegerSort());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getSetElementSort(), CVC4ApiException&);
}

void SortBlack::testGetBagElementSort()
{
  Sort bagSort = d_solver.mkBagSort(d_solver.getIntegerSort());
  TS_ASSERT_THROWS_NOTHING(bagSort.getBagElementSort());
  Sort elementSort = bagSort.getBagElementSort();
  TS_ASSERT(elementSort == d_solver.getIntegerSort());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getBagElementSort(), CVC4ApiException&);
}

void SortBlack::testGetSequenceElementSort()
{
  Sort seqSort = d_solver.mkSequenceSort(d_solver.getIntegerSort());
  TS_ASSERT(seqSort.isSequence());
  TS_ASSERT_THROWS_NOTHING(seqSort.getSequenceElementSort());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT(!bvSort.isSequence());
  TS_ASSERT_THROWS(bvSort.getSequenceElementSort(), CVC4ApiException&);
}

void SortBlack::testGetUninterpretedSortName()
{
  Sort uSort = d_solver.mkUninterpretedSort("u");
  TS_ASSERT_THROWS_NOTHING(uSort.getUninterpretedSortName());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getUninterpretedSortName(), CVC4ApiException&);
}

void SortBlack::testIsUninterpretedSortParameterized()
{
  Sort uSort = d_solver.mkUninterpretedSort("u");
  TS_ASSERT(!uSort.isUninterpretedSortParameterized());
  Sort sSort = d_solver.mkSortConstructorSort("s", 1);
  Sort siSort = sSort.instantiate({uSort});
  TS_ASSERT(siSort.isUninterpretedSortParameterized());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.isUninterpretedSortParameterized(),
                   CVC4ApiException&);
}

void SortBlack::testGetUninterpretedSortParamSorts()
{
  Sort uSort = d_solver.mkUninterpretedSort("u");
  TS_ASSERT_THROWS_NOTHING(uSort.getUninterpretedSortParamSorts());
  Sort sSort = d_solver.mkSortConstructorSort("s", 2);
  Sort siSort = sSort.instantiate({uSort, uSort});
  TS_ASSERT(siSort.getUninterpretedSortParamSorts().size() == 2);
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getUninterpretedSortParamSorts(), CVC4ApiException&);
}

void SortBlack::testGetUninterpretedSortConstructorName()
{
  Sort sSort = d_solver.mkSortConstructorSort("s", 2);
  TS_ASSERT_THROWS_NOTHING(sSort.getSortConstructorName());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getSortConstructorName(), CVC4ApiException&);
}

void SortBlack::testGetUninterpretedSortConstructorArity()
{
  Sort sSort = d_solver.mkSortConstructorSort("s", 2);
  TS_ASSERT_THROWS_NOTHING(sSort.getSortConstructorArity());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getSortConstructorArity(), CVC4ApiException&);
}

void SortBlack::testGetBVSize()
{
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS_NOTHING(bvSort.getBVSize());
  Sort setSort = d_solver.mkSetSort(d_solver.getIntegerSort());
  TS_ASSERT_THROWS(setSort.getBVSize(), CVC4ApiException&);
}

void SortBlack::testGetFPExponentSize()
{
  if (CVC4::Configuration::isBuiltWithSymFPU())
  {
    Sort fpSort = d_solver.mkFloatingPointSort(4, 8);
    TS_ASSERT_THROWS_NOTHING(fpSort.getFPExponentSize());
    Sort setSort = d_solver.mkSetSort(d_solver.getIntegerSort());
    TS_ASSERT_THROWS(setSort.getFPExponentSize(), CVC4ApiException&);
  }
}

void SortBlack::testGetFPSignificandSize()
{
  if (CVC4::Configuration::isBuiltWithSymFPU())
  {
    Sort fpSort = d_solver.mkFloatingPointSort(4, 8);
    TS_ASSERT_THROWS_NOTHING(fpSort.getFPSignificandSize());
    Sort setSort = d_solver.mkSetSort(d_solver.getIntegerSort());
    TS_ASSERT_THROWS(setSort.getFPSignificandSize(), CVC4ApiException&);
  }
}

void SortBlack::testGetDatatypeParamSorts()
{
  // create parametric datatype, check should not fail
  Sort sort = d_solver.mkParamSort("T");
  DatatypeDecl paramDtypeSpec = d_solver.mkDatatypeDecl("paramlist", sort);
  DatatypeConstructorDecl paramCons =
      d_solver.mkDatatypeConstructorDecl("cons");
  DatatypeConstructorDecl paramNil = d_solver.mkDatatypeConstructorDecl("nil");
  paramCons.addSelector("head", sort);
  paramDtypeSpec.addConstructor(paramCons);
  paramDtypeSpec.addConstructor(paramNil);
  Sort paramDtypeSort = d_solver.mkDatatypeSort(paramDtypeSpec);
  TS_ASSERT_THROWS_NOTHING(paramDtypeSort.getDatatypeParamSorts());
  // create non-parametric datatype sort, check should fail
  DatatypeDecl dtypeSpec = d_solver.mkDatatypeDecl("list");
  DatatypeConstructorDecl cons = d_solver.mkDatatypeConstructorDecl("cons");
  cons.addSelector("head", d_solver.getIntegerSort());
  dtypeSpec.addConstructor(cons);
  DatatypeConstructorDecl nil = d_solver.mkDatatypeConstructorDecl("nil");
  dtypeSpec.addConstructor(nil);
  Sort dtypeSort = d_solver.mkDatatypeSort(dtypeSpec);
  TS_ASSERT_THROWS(dtypeSort.getDatatypeParamSorts(), CVC4ApiException&);
}

void SortBlack::testGetDatatypeArity()
{
  // create datatype sort, check should not fail
  DatatypeDecl dtypeSpec = d_solver.mkDatatypeDecl("list");
  DatatypeConstructorDecl cons = d_solver.mkDatatypeConstructorDecl("cons");
  cons.addSelector("head", d_solver.getIntegerSort());
  dtypeSpec.addConstructor(cons);
  DatatypeConstructorDecl nil = d_solver.mkDatatypeConstructorDecl("nil");
  dtypeSpec.addConstructor(nil);
  Sort dtypeSort = d_solver.mkDatatypeSort(dtypeSpec);
  TS_ASSERT_THROWS_NOTHING(dtypeSort.getDatatypeArity());
  // create bv sort, check should fail
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getDatatypeArity(), CVC4ApiException&);
}

void SortBlack::testGetTupleLength()
{
  Sort tupleSort = d_solver.mkTupleSort(
      {d_solver.getIntegerSort(), d_solver.getIntegerSort()});
  TS_ASSERT_THROWS_NOTHING(tupleSort.getTupleLength());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getTupleLength(), CVC4ApiException&);
}

void SortBlack::testGetTupleSorts()
{
  Sort tupleSort = d_solver.mkTupleSort(
      {d_solver.getIntegerSort(), d_solver.getIntegerSort()});
  TS_ASSERT_THROWS_NOTHING(tupleSort.getTupleSorts());
  Sort bvSort = d_solver.mkBitVectorSort(32);
  TS_ASSERT_THROWS(bvSort.getTupleSorts(), CVC4ApiException&);
}

void SortBlack::testSortCompare()
{
  Sort boolSort = d_solver.getBooleanSort();
  Sort intSort = d_solver.getIntegerSort();
  Sort bvSort = d_solver.mkBitVectorSort(32);
  Sort bvSort2 = d_solver.mkBitVectorSort(32);
  TS_ASSERT(bvSort >= bvSort2);
  TS_ASSERT(bvSort <= bvSort2);
  TS_ASSERT((intSort > boolSort) != (intSort < boolSort));
  TS_ASSERT((intSort > bvSort || intSort == bvSort) == (intSort >= bvSort));
}

void SortBlack::testSortSubtyping()
{
  Sort intSort = d_solver.getIntegerSort();
  Sort realSort = d_solver.getRealSort();
  TS_ASSERT(intSort.isSubsortOf(realSort));
  TS_ASSERT(!realSort.isSubsortOf(intSort));
  TS_ASSERT(intSort.isComparableTo(realSort));
  TS_ASSERT(realSort.isComparableTo(intSort));

  Sort arraySortII = d_solver.mkArraySort(intSort, intSort);
  Sort arraySortIR = d_solver.mkArraySort(intSort, realSort);
  TS_ASSERT(!arraySortII.isComparableTo(intSort));
  // we do not support subtyping for arrays
  TS_ASSERT(!arraySortII.isComparableTo(arraySortIR));

  Sort setSortI = d_solver.mkSetSort(intSort);
  Sort setSortR = d_solver.mkSetSort(realSort);
  // we don't support subtyping for sets
  TS_ASSERT(!setSortI.isComparableTo(setSortR));
  TS_ASSERT(!setSortI.isSubsortOf(setSortR));
  TS_ASSERT(!setSortR.isComparableTo(setSortI));
  TS_ASSERT(!setSortR.isSubsortOf(setSortI));
}

void SortBlack::testSortScopedToString()
{
  std::string name = "uninterp-sort";
  Sort bvsort8 = d_solver.mkBitVectorSort(8);
  Sort uninterp_sort = d_solver.mkUninterpretedSort(name);
  TS_ASSERT_EQUALS(bvsort8.toString(), "(_ BitVec 8)");
  TS_ASSERT_EQUALS(uninterp_sort.toString(), name);
  Solver solver2;
  TS_ASSERT_EQUALS(bvsort8.toString(), "(_ BitVec 8)");
  TS_ASSERT_EQUALS(uninterp_sort.toString(), name);
}
