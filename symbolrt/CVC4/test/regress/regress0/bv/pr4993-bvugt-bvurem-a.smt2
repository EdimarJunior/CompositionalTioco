; COMMAND-LINE: --bv-div-zero-const --no-check-unsat-cores
(set-logic QF_BV)
(set-info :status unsat)
(declare-const x (_ BitVec 4))
(declare-const y (_ BitVec 4))
(assert (not (= (bvugt (bvurem y x) x) (ite (= x #x0) (bvugt y #x0) false))))
(check-sat)
