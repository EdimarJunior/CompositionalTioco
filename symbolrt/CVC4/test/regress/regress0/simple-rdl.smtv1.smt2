(set-option :incremental false)
(set-info :status unsat)
(set-logic QF_RDL)
(declare-fun x () Real)
(declare-fun y () Real)
(check-sat-assuming ( (not (=> (< (- x y) 0.0) (< x y))) ))
