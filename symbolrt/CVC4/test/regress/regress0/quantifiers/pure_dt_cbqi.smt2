; COMMAND-LINE: --cegqi --no-check-models
; EXPECT: sat
(set-logic ALL_SUPPORTED)
(set-info :status sat)
(declare-datatypes ((nat 0)) (( (Suc (pred nat)) (zero))))
(declare-fun y () nat)
(assert (forall ((x nat)) (not (= y (Suc x)))))
(check-sat)
