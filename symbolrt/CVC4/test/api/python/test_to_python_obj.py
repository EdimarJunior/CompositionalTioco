from fractions import Fraction
import pytest

import pycvc4
from pycvc4 import kinds


def testGetBool():
    solver = pycvc4.Solver()
    t = solver.mkTrue()
    f = solver.mkFalse()
    assert t.toPythonObj() == True
    assert f.toPythonObj() == False


def testGetInt():
    solver = pycvc4.Solver()
    two = solver.mkInteger(2)
    assert two.toPythonObj() == 2


def testGetReal():
    solver = pycvc4.Solver()
    half = solver.mkReal("1/2")
    assert half.toPythonObj() == Fraction(1, 2)

    neg34 = solver.mkReal("-3/4")
    assert neg34.toPythonObj() == Fraction(-3, 4)

    neg1 = solver.mkInteger("-1")
    assert neg1.toPythonObj() == -1


def testGetBV():
    solver = pycvc4.Solver()
    three = solver.mkBitVector(8, 3)
    assert three.toPythonObj() == 3


def testGetArray():
    solver = pycvc4.Solver()
    arrsort = solver.mkArraySort(solver.getRealSort(), solver.getRealSort())
    zero_array = solver.mkConstArray(arrsort, solver.mkInteger(0))
    stores = solver.mkTerm(kinds.Store, zero_array, solver.mkInteger(1), solver.mkInteger(2))
    stores = solver.mkTerm(kinds.Store, stores, solver.mkInteger(2), solver.mkInteger(3))
    stores = solver.mkTerm(kinds.Store, stores, solver.mkInteger(4), solver.mkInteger(5))

    array_dict = stores.toPythonObj()

    assert array_dict[1] == 2
    assert array_dict[2] == 3
    assert array_dict[4] == 5
    # an index that wasn't stored at should give zero
    assert array_dict[8] == 0


def testGetSymbol():
    solver = pycvc4.Solver()
    solver.mkConst(solver.getBooleanSort(), "x")


def testGetString():
    solver = pycvc4.Solver()

    s1 = '"test\n"😃\\u{a}'
    t1 = solver.mkString(s1)
    assert s1 == t1.toPythonObj()

    s2 = '❤️CVC4❤️'
    t2 = solver.mkString(s2)
    assert s2 == t2.toPythonObj()
