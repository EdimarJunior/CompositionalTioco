(set-logic QF_LIA)
(set-info :status sat)
(declare-fun _substvar_67_ () Int)
(declare-fun _substvar_66_ () Int)
(declare-fun _substvar_59_ () Int)
(declare-fun _substvar_54_ () Int)
(declare-fun _substvar_53_ () Int)
(declare-fun _substvar_65_ () Int)
(declare-fun _substvar_37_ () Int)
(declare-fun _substvar_40_ () Int)
(declare-fun _substvar_38_ () Int)
(declare-fun _substvar_39_ () Int)
(declare-fun _substvar_60_ () Int)
(declare-fun _substvar_70_ () Int)
(declare-fun x4 () Int)
(declare-fun x13 () Int)
(declare-fun x3 () Int)
(declare-fun x18 () Int)
(declare-fun x7 () Int)
(declare-fun x15 () Int)
(declare-fun x9 () Int)
(declare-fun x1 () Int)
(declare-fun x17 () Int)
(declare-fun x16 () Int)
(declare-fun x8 () Int)
(declare-fun x19 () Int)
(declare-fun x2 () Int)
(declare-fun x12 () Int)
(declare-fun x10 () Int)
(declare-fun x6 () Int)
(declare-fun x14 () Int)
(declare-fun x11 () Int)
(declare-fun x5 () Int)
(assert (let ( (x23 (* _substvar_37_ 2)) (x24 false)) (let ((x26 false) (x25 (not false))) (let ((x29 (= 0 0)) (x30 false) (x27 (not false)) (x28 false)) (let ((x34 false) (x37 (>= _substvar_37_ 8)) (x33 false) (x36 (= 0 (+ x23 0))) (x32 (= 16 _substvar_60_)) (x38 (+ x23 (- _substvar_67_))) (x35 (= 0 _substvar_67_))) (let ((x39 (= x38 16)) (x40 false)) (let ((x43 (= _substvar_53_ 16))) (let ((x45 false) (x46 false)) (let ((x49 (= 0 0)) (x48 false) (x47 (not false))) (and (and (and (and (and (and (or false x27) (and (or false x32) (and (and (or (not false) false) (and (or x49 false) (and (or x43 false) (and (and (and (or x36 x35) (and (and (and (and (or false x25) (or false x39)) (or x39 false)) (or x36 x37)) (or x35 (not x37)))) (or x43 false)) (or false x47))))) (or false x49)))) (or false x32)) (or x29 false)) (or (not false) false)) (or x29 false)) (and (and (and (and (<= 0 0) (not false)) (and (and (= false false) (and (and (and (<= 0 0) (not false)) (and (and (and (not false) (>= 0 0)) (and (and (<= 0 1) (>= 0 0)) (and (and (and (>= 0 0) x47) (and (and (>= 1 0) (<= 0 0)) (and (and (and (and (= false false) (and (and (>= 0 0) (not false)) (and (and (= false false) (and (and (>= 0 0) (not false)) (and (and (and (and (not false) (>= 0 0)) (and (and (= false false) (and (and (and (>= 1 0) (>= 0 0)) (and (and (and (not false) (>= 0 0)) (and (and (>= 0 0) (not false)) (and (<= 0 0) x27))) (and (<= 0 _substvar_37_) x25))) (and (<= 0 0) (not false)))) (and (>= 0 0) (not false)))) (= false false)) (and (not false) (>= 0 0))))) (and (<= 0 0) (>= 1 0))))) (and (>= 0 0) (>= 1 0))) (and (>= 0 0) (not (<= 16 0)))) (= false false)))) (= (= 1 0) (not (<= 0 16)))))) (= (not (>= 16 0)) (= 0 1)))) (and (not (>= 0 8)) (>= 0 0)))) (and (<= 0 0) (<= 0 1)))) (= (= 0 1) (not (>= 16 0)))) (not (= _substvar_54_ 0)))))))))))))
(check-sat)
(exit)