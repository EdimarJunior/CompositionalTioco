(set-option :incremental false)
(set-info :status sat)
(set-logic QF_AUFBV)
(declare-fun v0 () (_ BitVec 9))
(declare-fun v1 () (_ BitVec 14))
(declare-fun a2 () (Array (_ BitVec 2) (_ BitVec 5)))
(declare-fun a3 () (Array (_ BitVec 8) (_ BitVec 5)))
(check-sat-assuming ( (let ((_let_0 (ite (bvslt (_ bv712 10) (_ bv712 10)) (_ bv1 1) (_ bv0 1)))) (let ((_let_1 (ite (= (_ bv1 1) ((_ extract 2 2) v1)) ((_ sign_extend 5) v0) v1))) (let ((_let_2 (select a2 ((_ extract 2 1) (_ bv712 10))))) (let ((_let_3 (ite (bvsge ((_ zero_extend 4) _let_2) v0) (_ bv1 1) (_ bv0 1)))) (let ((_let_4 (ite (bvule (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2)) (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2))) (_ bv1 1) (_ bv0 1)))) (let ((_let_5 ((_ zero_extend 0) _let_1))) (let ((_let_6 (bvsdiv ((_ zero_extend 9) (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2))) v1))) (let ((_let_7 ((_ sign_extend 4) _let_3))) (let ((_let_8 ((_ sign_extend 4) (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2))))) (let ((_let_9 (= ((_ sign_extend 4) (bvnand (_ bv712 10) ((_ zero_extend 5) (select a2 ((_ extract 3 2) (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1))))))) v1))) (let ((_let_10 ((_ zero_extend 4) _let_3))) (let ((_let_11 (= (bvugt v1 ((_ zero_extend 9) (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2)))) (bvsle (bvsdiv ((_ zero_extend 9) (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1))) _let_1) ((_ zero_extend 4) (_ bv712 10)))))) (let ((_let_12 (or (and (bvugt _let_6 v1) (bvugt _let_3 ((_ extract 0 0) _let_0))) (= (distinct _let_1 _let_5) (bvsle _let_7 (select a2 ((_ extract 3 2) (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1))))))))) (let ((_let_13 (=> (or (or (bvsge _let_1 ((_ zero_extend 5) v0)) (distinct v0 ((_ sign_extend 4) (select a2 ((_ extract 3 2) (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1))))))) (bvsge ((_ zero_extend 9) (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1))) _let_1)) (and (= _let_6 ((_ sign_extend 9) (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2)))) (= _let_6 ((_ sign_extend 9) (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2)))))))) (let ((_let_14 (bvnot (_ bv0 14)))) (and (and (and (and (= (xor (=> (ite _let_11 _let_11 (xor (xor (or (bvugt _let_4 _let_0) (bvuge (select a2 ((_ extract 3 2) (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1)))) ((_ zero_extend 4) _let_0))) (distinct ((_ zero_extend 9) (select a2 ((_ extract 3 2) (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1))))) _let_6)) (ite (xor (bvsle _let_6 ((_ zero_extend 13) _let_0)) (bvsge _let_8 v0)) (= (bvslt ((_ zero_extend 4) (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2))) v0) (or (or (=> (bvslt _let_2 (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2))) (bvsle _let_10 (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2)))) (bvsle _let_2 _let_10)) (bvugt _let_10 (select (store (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ zero_extend 1) _let_0) _let_2) ((_ extract 1 0) _let_2))))) (bvult _let_8 v0)))) (or (and _let_12 _let_12) (and (bvuge (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1)) _let_7) (distinct _let_10 (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1)))))) (and _let_13 _let_13)) (xor (xor (= (bvsge _let_6 _let_1) (bvslt _let_1 ((_ sign_extend 9) _let_2))) (bvslt (select (store (store a2 ((_ sign_extend 1) _let_0) ((_ extract 12 8) _let_1)) ((_ zero_extend 1) _let_0) ((_ extract 6 2) v1)) ((_ extract 12 11) v1)) ((_ sign_extend 4) _let_4))) (=> (or _let_9 _let_9) (= _let_5 ((_ sign_extend 5) v0))))) (not (= v1 (_ bv0 14)))) (not (= v1 _let_14))) (not (= _let_1 (_ bv0 14)))) (not (= _let_1 _let_14)))))))))))))))))) ))
