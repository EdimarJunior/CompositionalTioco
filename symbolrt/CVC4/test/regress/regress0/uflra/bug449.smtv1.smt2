(set-option :incremental false)
(set-info :status sat)
(set-logic QF_UFLRA)
(declare-fun p0 (Real) Bool)
(declare-fun v0 () Real)
(check-sat-assuming ( (and (p0 v0) (< v0 0.0) (not (p0 (/ (- 1) 1)))) ))
