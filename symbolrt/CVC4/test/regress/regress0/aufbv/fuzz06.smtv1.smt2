(set-option :incremental false)
(set-info :status sat)
(set-logic QF_AUFBV)
(declare-fun v0 () (_ BitVec 3))
(declare-fun v1 () (_ BitVec 10))
(declare-fun v2 () (_ BitVec 1))
(declare-fun v3 () (_ BitVec 10))
(declare-fun v4 () (_ BitVec 10))
(declare-fun a5 () (Array (_ BitVec 6) (_ BitVec 11)))
(check-sat-assuming ( (let ((_let_0 (bvsub ((_ zero_extend 5) (_ bv5 3)) (_ bv71 8)))) (let ((_let_1 (ite (bvugt v1 ((_ zero_extend 9) v2)) (_ bv1 1) (_ bv0 1)))) (let ((_let_2 (bvxnor v3 ((_ sign_extend 9) (bvnot v2))))) (let ((_let_3 ((_ rotate_left 2) (_ bv71 8)))) (let ((_let_4 (bvudiv v4 _let_2))) (let ((_let_5 (bvor v0 ((_ zero_extend 2) _let_1)))) (let ((_let_6 ((_ sign_extend 10) v2))) (let ((_let_7 (select (store (store a5 ((_ extract 6 1) (_ bv71 8)) _let_6) ((_ extract 6 1) v1) ((_ sign_extend 3) _let_0)) ((_ extract 7 2) _let_2)))) (let ((_let_8 (bvudiv ((_ zero_extend 7) _let_5) _let_2))) (let ((_let_9 ((_ extract 7 3) _let_3))) (let ((_let_10 (bvxor ((_ zero_extend 9) _let_1) v3))) (let ((_let_11 (bvneg (select (store a5 ((_ extract 6 1) (_ bv71 8)) _let_6) ((_ zero_extend 3) (_ bv5 3)))))) (let ((_let_12 ((_ zero_extend 2) (ite (bvuge ((_ sign_extend 9) (bvnot v2)) _let_10) (_ bv1 1) (_ bv0 1))))) (let ((_let_13 ((_ sign_extend 3) _let_9))) (let ((_let_14 (bvnand v1 _let_4))) (let ((_let_15 ((_ sign_extend 2) (bvmul (_ bv71 8) _let_13)))) (let ((_let_16 (xor (bvule v3 ((_ sign_extend 7) v0)) (ite (and (bvsge (ite (= (_ bv1 1) ((_ extract 7 7) _let_7)) _let_5 _let_12) ((_ sign_extend 2) (ite (bvsgt v0 (ite (= (_ bv1 1) ((_ extract 7 7) _let_7)) _let_5 _let_12)) (_ bv1 1) (_ bv0 1)))) (bvult ((_ sign_extend 2) _let_0) v1)) (bvsge _let_2 ((_ sign_extend 9) (bvcomp _let_0 ((_ zero_extend 7) _let_1)))) (not (bvule ((_ zero_extend 5) (bvudiv ((_ zero_extend 2) (bvnot v2)) _let_5)) _let_0)))))) (let ((_let_17 (or (and (bvugt (bvcomp _let_0 ((_ zero_extend 7) _let_1)) _let_1) (= ((_ sign_extend 2) (_ bv71 8)) v3)) (or (=> (not (or (= _let_4 ((_ zero_extend 7) v0)) (bvsge _let_15 (bvxor v4 _let_8)))) (bvult ((_ zero_extend 3) _let_0) _let_7)) (and (xor (or (distinct _let_4 ((_ zero_extend 7) _let_5)) (ite (=> (bvsle ((_ sign_extend 2) _let_3) _let_2) (= ((_ sign_extend 7) v0) _let_8)) (bvslt v4 ((_ sign_extend 7) v0)) (= v2 (ite (bvuge ((_ sign_extend 9) (bvnot v2)) _let_10) (_ bv1 1) (_ bv0 1))))) (and (ite (bvugt ((_ zero_extend 3) _let_0) (bvand _let_11 _let_6)) (bvult v1 _let_14) (or (bvslt _let_5 ((_ sign_extend 2) (bvcomp _let_0 ((_ zero_extend 7) _let_1)))) (bvult _let_2 _let_8))) (not (bvult _let_2 _let_14)))) (not (bvsle ((_ zero_extend 9) (ite (bvsgt v0 (ite (= (_ bv1 1) ((_ extract 7 7) _let_7)) _let_5 _let_12)) (_ bv1 1) (_ bv0 1))) _let_10))))))) (let ((_let_18 (= (bvule ((_ sign_extend 9) (bvcomp _let_0 ((_ zero_extend 7) _let_1))) v4) (= ((_ zero_extend 9) v2) v3)))) (let ((_let_19 (=> (= (xor (and (ite (bvugt _let_2 ((_ zero_extend 2) (bvmul (_ bv71 8) _let_13))) (bvsge _let_13 _let_3) (bvult (bvudiv ((_ zero_extend 7) v0) _let_4) ((_ zero_extend 9) (ite (bvsgt v0 (ite (= (_ bv1 1) ((_ extract 7 7) _let_7)) _let_5 _let_12)) (_ bv1 1) (_ bv0 1))))) (and (bvuge (bvxor v4 _let_8) _let_8) (xor (bvuge (select (store a5 ((_ extract 6 1) (_ bv71 8)) _let_6) ((_ zero_extend 3) (_ bv5 3))) ((_ sign_extend 8) (_ bv5 3))) (bvugt ((_ zero_extend 3) _let_0) _let_7)))) (bvugt v3 v3)) (xor (bvsgt ((_ sign_extend 4) (bvnot v2)) _let_9) (= (bvult (bvand _let_11 _let_6) ((_ zero_extend 1) _let_2)) (bvuge _let_12 (_ bv5 3))))) (bvule _let_11 ((_ zero_extend 3) _let_3))))) (and (and (and (and (not (ite (= (xor (= (bvult (ite (= (_ bv1 1) ((_ extract 7 7) _let_7)) _let_5 _let_12) ((_ sign_extend 2) (ite (bvuge ((_ sign_extend 9) (bvnot v2)) _let_10) (_ bv1 1) (_ bv0 1)))) (bvugt ((_ zero_extend 1) _let_14) _let_7)) (=> (bvsle _let_4 ((_ zero_extend 9) (bvcomp _let_0 ((_ zero_extend 7) _let_1)))) (=> (bvuge (ite (bvuge ((_ sign_extend 9) (bvnot v2)) _let_10) (_ bv1 1) (_ bv0 1)) (bvcomp _let_0 ((_ zero_extend 7) _let_1))) (bvsle v4 (bvxor v4 _let_8))))) (= (bvuge ((_ sign_extend 1) _let_4) (select (store a5 ((_ extract 6 1) (_ bv71 8)) _let_6) ((_ zero_extend 3) (_ bv5 3)))) (= (bvule ((_ sign_extend 7) (bvcomp _let_0 ((_ zero_extend 7) _let_1))) _let_3) (bvult ((_ sign_extend 9) (ite (distinct (_ bv5 3) ((_ sign_extend 2) (bvnot v2))) (_ bv1 1) (_ bv0 1))) (bvxor v4 _let_8))))) _let_18 _let_18)) (=> (=> (or _let_17 _let_17) (= _let_7 ((_ sign_extend 8) (ite (= (_ bv1 1) ((_ extract 7 7) _let_7)) _let_5 _let_12)))) (=> (not (not (=> (bvsgt v4 ((_ zero_extend 2) _let_0)) (bvsge _let_7 _let_7)))) (not (= (ite (and (ite _let_16 _let_16 (xor (bvugt (bvmul (_ bv71 8) _let_13) ((_ zero_extend 5) (ite (= (_ bv1 1) ((_ extract 7 7) _let_7)) _let_5 _let_12))) (bvugt _let_0 (bvmul (_ bv71 8) _let_13)))) (ite (bvslt ((_ zero_extend 9) v2) _let_14) (= (distinct ((_ zero_extend 7) (_ bv5 3)) (bvudiv ((_ zero_extend 7) v0) _let_4)) (bvslt v1 ((_ zero_extend 9) v2))) (not (bvule _let_15 _let_8)))) _let_19 _let_19) (and (bvult (bvnot v2) _let_1) (bvsle ((_ zero_extend 9) (bvnot v2)) _let_10))))))) (not (= _let_5 (_ bv0 3)))) (not (= _let_2 (_ bv0 10)))) (not (= _let_4 (_ bv0 10)))))))))))))))))))))))) ))