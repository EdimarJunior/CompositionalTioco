(set-option :incremental false)
(set-info :status unsat)
(set-logic QF_BV)
(declare-fun x () (_ BitVec 32))
(declare-fun y () (_ BitVec 32))
(check-sat-assuming ( (not (= ((_ extract 31 0) (concat x y)) ((_ extract 31 0) y))) ))
