(set-option :incremental false)
(set-info :source "MathSat group")
(set-info :status sat)
(set-info :category "random")
(set-info :difficulty "3")
(set-logic QF_UFLRA)
(declare-fun f0_1 (Real) Real)
(declare-fun f0_2 (Real Real) Real)
(declare-fun f0_3 (Real Real Real) Real)
(declare-fun f0_4 (Real Real Real Real) Real)
(declare-fun f1_1 (Real) Real)
(declare-fun f1_2 (Real Real) Real)
(declare-fun f1_3 (Real Real Real) Real)
(declare-fun f1_4 (Real Real Real Real) Real)
(declare-fun x0 () Real)
(declare-fun x1 () Real)
(declare-fun x2 () Real)
(declare-fun x3 () Real)
(declare-fun x4 () Real)
(declare-fun x5 () Real)
(declare-fun x6 () Real)
(declare-fun x7 () Real)
(declare-fun x8 () Real)
(declare-fun x9 () Real)
(declare-fun P0 () Bool)
(declare-fun P1 () Bool)
(declare-fun P2 () Bool)
(declare-fun P3 () Bool)
(declare-fun P4 () Bool)
(declare-fun P5 () Bool)
(declare-fun P6 () Bool)
(declare-fun P7 () Bool)
(declare-fun P8 () Bool)
(declare-fun P9 () Bool)
(check-sat-assuming ( (let ((_let_0 (- 0 10))) (let ((_let_1 (- (+ (* (/ _let_0 1) x3) (* 13.0 x7)) (* 5.0 x1)))) (let ((_let_2 (f0_1 x9))) (let ((_let_3 (+ (+ (* 17.0 _let_2) (* 24.0 x5)) (* 23.0 (f0_1 x1))))) (let ((_let_4 (f0_2 x4 x7))) (let ((_let_5 (+ (+ (* (/ (- 0 23) 1) x0) (* 17.0 x2)) (* 7.0 x4)))) (let ((_let_6 (- (+ (* (/ (- 0 21) 1) _let_4) (* 17.0 x8)) (* 6.0 (f0_2 x9 x3))))) (let ((_let_7 (+ (+ (* (/ (- 0 14) 1) (- (+ (* (/ (- 0 19) 1) x5) (* 26.0 x2)) (* 21.0 (- (+ (* 4.0 x1) (* 29.0 x9)) (* 21.0 x7))))) (* 16.0 (- (- (* (/ (- 0 12) 1) x8) (* 1.0 x5)) (* 16.0 x7)))) (* 8.0 (f0_2 x6 x5))))) (let ((_let_8 (f0_1 x5))) (let ((_let_9 (- 0 8))) (let ((_let_10 (= (f1_2 x1 (- (+ (* 4.0 x1) (* 29.0 x9)) (* 21.0 x7))) (f0_2 x6 x5)))) (let ((_let_11 (< (- (+ (* 4.0 x1) (* 29.0 x9)) (* 21.0 x7)) (/ (- 0 20) 1)))) (let ((_let_12 (< (f1_1 x6) 8.0))) (let ((_let_13 (< (f0_1 x3) (/ (- 0 24) 1)))) (let ((_let_14 (= (f0_2 x6 x5) x8))) (let ((_let_15 (= (f1_2 x9 x8) _let_6))) (let ((_let_16 (= (f1_1 x1) _let_5))) (let ((_let_17 (= (f0_2 x6 x5) _let_2))) (let ((_let_18 (< _let_7 (/ (- 0 22) 1)))) (let ((_let_19 (= (+ (- (* 1.0 _let_7) (* 3.0 _let_1)) (* 12.0 (f0_2 x6 x5))) (- (+ (* (/ (- 0 17) 1) x5) (* 27.0 x1)) (* 12.0 x3))))) (let ((_let_20 (< x1 (/ (- 0 27) 1)))) (let ((_let_21 (< _let_8 (/ (- 0 27) 1)))) (let ((_let_22 (< _let_8 (/ _let_0 1)))) (let ((_let_23 (= (- (+ (* (/ (- 0 17) 1) x5) (* 27.0 x1)) (* 12.0 x3)) _let_7))) (let ((_let_24 (- 0 1))) (let ((_let_25 (< _let_3 (/ _let_24 1)))) (let ((_let_26 (- 0 28))) (let ((_let_27 (< _let_7 (/ _let_24 1)))) (let ((_let_28 (< _let_2 (/ _let_9 1)))) (let ((_let_29 (< _let_5 19.0))) (let ((_let_30 (= (+ (- (* 1.0 _let_7) (* 3.0 _let_1)) (* 12.0 (f0_2 x6 x5))) x6))) (let ((_let_31 (< (+ (- (* (/ _let_9 1) x2) (* 19.0 x6)) (* 8.0 x1)) 26.0))) (let ((_let_32 (< x8 (/ (- 0 13) 1)))) (let ((_let_33 (- 0 26))) (let ((_let_34 (< (f1_1 x6) 17.0))) (let ((_let_35 (< (- (+ (* 4.0 x1) (* 29.0 x9)) (* 21.0 x7)) 15.0))) (let ((_let_36 (< (f1_2 x9 x0) 9.0))) (let ((_let_37 (< _let_4 22.0))) (let ((_let_38 (< x6 18.0))) (let ((_let_39 (- 0 15))) (let ((_let_40 (< x0 (/ _let_39 1)))) (let ((_let_41 (< (f1_2 x0 x0) (/ _let_39 1)))) (let ((_let_42 (< x9 6.0))) (let ((_let_43 (< _let_7 22.0))) (let ((_let_44 (< x5 (/ _let_33 1)))) (let ((_let_45 (< _let_6 5.0))) (let ((_let_46 (= (f1_2 x9 x8) x1))) (let ((_let_47 (not _let_12))) (let ((_let_48 (not _let_38))) (let ((_let_49 (not _let_11))) (let ((_let_50 (not _let_16))) (let ((_let_51 (not _let_20))) (let ((_let_52 (not (< _let_4 (/ _let_26 1))))) (let ((_let_53 (not _let_44))) (let ((_let_54 (not _let_13))) (let ((_let_55 (not _let_32))) (let ((_let_56 (not _let_25))) (let ((_let_57 (not _let_30))) (let ((_let_58 (not (< (- (- (* (/ (- 0 24) 1) x4) (* 21.0 x2)) (* 9.0 x5)) (/ _let_26 1))))) (let ((_let_59 (not _let_40))) (let ((_let_60 (not (< (f1_1 x6) 15.0)))) (let ((_let_61 (not (< _let_1 (/ (- 0 24) 1))))) (let ((_let_62 (not _let_10))) (let ((_let_63 (not _let_19))) (let ((_let_64 (not _let_18))) (let ((_let_65 (not _let_21))) (let ((_let_66 (or _let_41 _let_65))) (let ((_let_67 (not (< x2 (/ (- 0 18) 1))))) (let ((_let_68 (not (< x7 23.0)))) (let ((_let_69 (not (< (f0_1 x2) 0.0)))) (let ((_let_70 (not (< _let_8 (/ _let_26 1))))) (let ((_let_71 (not (= x4 x5)))) (let ((_let_72 (not _let_27))) (let ((_let_73 (not _let_23))) (let ((_let_74 (not (< (f1_2 x0 x0) 1.0)))) (let ((_let_75 (not (< (- (+ (* (/ (- 0 11) 1) x9) (* 16.0 x7)) (* 4.0 x1)) 9.0)))) (let ((_let_76 (not (< x0 (/ (- 0 14) 1))))) (let ((_let_77 (not (< _let_3 (/ _let_33 1))))) (let ((_let_78 (not _let_37))) (let ((_let_79 (not _let_46))) (let ((_let_80 (not (= (f1_2 _let_2 _let_1) x6)))) (let ((_let_81 (not (< x8 3.0)))) (let ((_let_82 (not _let_17))) (let ((_let_83 (not (< (f0_1 x3) (/ (- 0 17) 1))))) (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (and (or (or P6 (not _let_14)) (< (- (- (* (/ (- 0 24) 1) x4) (* 21.0 x2)) (* 9.0 x5)) (/ _let_26 1))) (or (or (< _let_3 (/ _let_33 1)) P6) _let_47)) (or (or _let_17 _let_48) P3)) (or (or _let_49 _let_34) (< (- (+ (* (/ (- 0 11) 1) x9) (* 16.0 x7)) (* 4.0 x1)) 9.0))) (or (or P5 (< _let_1 (/ (- 0 24) 1))) _let_50)) (or (or P5 (not P2)) P4)) (or (or _let_27 _let_41) (< (- (- (* (/ (- 0 24) 1) x4) (* 21.0 x2)) (* 9.0 x5)) (/ _let_26 1)))) (or (or (< x8 3.0) _let_51) (< _let_3 (/ _let_33 1)))) (or (or (not _let_15) _let_22) _let_52)) (or (or _let_53 (< x0 (/ (- 0 14) 1))) _let_54)) (or (or _let_42 _let_41) _let_55)) (or (or _let_56 (= (f1_2 _let_2 _let_1) x6)) _let_57)) (or (or _let_58 _let_19) _let_42)) (or (or _let_20 P8) (not P7))) (or (or _let_53 _let_59) (= x4 x5))) (or (or P6 _let_17) _let_51)) (or (or _let_60 P4) (not P1))) (or (or _let_61 _let_62) (< x0 (/ (- 0 14) 1)))) (or (or (= (f1_2 _let_2 _let_1) x6) _let_20) _let_52)) (or (or _let_63 _let_37) _let_43)) (or (or (< x2 (/ (- 0 18) 1)) _let_64) P5)) (or (or _let_32 (not _let_34)) _let_58)) (or (or _let_25 (< _let_3 (/ _let_33 1))) (< (f1_2 x0 x0) 1.0))) (or _let_66 (not _let_45))) (or (or (not P3) _let_29) (< _let_1 (/ (- 0 24) 1)))) (or (or _let_54 (not P9)) _let_31)) (or (or _let_67 _let_67) _let_42)) (or (or (< (f0_1 x3) (/ (- 0 17) 1)) (< _let_4 (/ _let_26 1))) _let_68)) (or (or _let_50 _let_69) _let_19)) (or (or _let_70 (not P8)) (< x0 (/ (- 0 14) 1)))) (or (or P5 _let_31) (not P3))) (or (or _let_10 _let_13) (not P4))) (or (or P4 _let_71) _let_30)) (or (or P5 (not P1)) _let_18)) (or (or P6 _let_19) _let_72)) (or (or (not _let_22) _let_35) (not P5))) (or (or (< (f0_1 x2) 0.0) _let_38) (not P1))) (or (or (not P0) _let_13) _let_17)) (or (or _let_62 _let_20) (< (f0_1 x3) (/ (- 0 17) 1)))) (or (or _let_73 _let_29) _let_74)) (or (or (not P2) _let_19) _let_40)) (or (or (= (f1_2 _let_2 _let_1) x6) _let_75) _let_10)) (or (or _let_13 _let_64) (< _let_8 (/ _let_26 1)))) (or (or _let_17 _let_67) _let_74)) (or (or _let_69 _let_59) (< (- (+ (* (/ (- 0 17) 1) x5) (* 27.0 x1)) (* 12.0 x3)) 17.0))) (or (or _let_76 _let_23) P7)) (or (or _let_57 _let_70) (= (f1_2 _let_2 _let_1) x6))) (or (or _let_74 (< x8 3.0)) _let_10)) (or (or _let_10 (not P1)) _let_61)) (or (or _let_42 _let_16) _let_65)) (or (or _let_68 P3) _let_77)) (or (or (not _let_41) _let_36) _let_53)) (or (or _let_21 (not P5)) _let_60)) (or (or _let_36 _let_78) _let_30)) (or (or _let_27 _let_70) _let_18)) (or (or _let_58 P1) (not P0))) (or (or _let_78 _let_28) _let_49)) (or (or (not P3) _let_27) _let_73)) (or (or _let_70 (not _let_36)) _let_47)) (or (or _let_75 _let_31) _let_53)) (or (or _let_79 _let_76) _let_55)) (or (or (< (f0_1 x3) (/ (- 0 17) 1)) _let_27) _let_49)) (or (or _let_28 _let_12) _let_55)) (or (or P1 _let_61) _let_70)) (or (or _let_80 _let_14) (< x8 3.0))) (or (or (not P0) _let_46) _let_16)) (or (or _let_46 _let_32) _let_27)) (or (or _let_81 P2) P0)) (or (or _let_82 _let_20) _let_79)) (or (or _let_16 _let_69) _let_60)) (or (or _let_83 _let_49) (< (- (+ (* (/ (- 0 11) 1) x9) (* 16.0 x7)) (* 4.0 x1)) 9.0))) (or (or (not P6) P1) _let_14)) (or (or _let_83 P9) _let_68)) (or (or _let_51 (< (- (+ (* (/ (- 0 11) 1) x9) (* 16.0 x7)) (* 4.0 x1)) 9.0)) _let_27)) (or (or _let_42 _let_29) _let_11)) (or (or _let_36 P9) _let_42)) (or (or _let_75 _let_72) _let_79)) (or (or _let_65 _let_75) _let_31)) (or (or (not P2) _let_76) _let_37)) (or (or _let_82 (not _let_35)) (not P5))) (or (or _let_18 _let_28) (not _let_31))) (or (or _let_15 _let_11) _let_71)) (or (or _let_34 _let_63) _let_22)) (or (or _let_45 P7) _let_37)) (or (or _let_42 _let_11) (not P2))) (or (or _let_52 _let_21) P3)) (or (or _let_48 _let_44) (< (- (- (* (/ (- 0 24) 1) x4) (* 21.0 x2)) (* 9.0 x5)) (/ _let_26 1)))) (or (or _let_62 _let_44) _let_82)) (or (or _let_19 _let_75) _let_58)) (or (or _let_58 _let_21) P8)) (or (or _let_79 _let_52) _let_42)) (or (or _let_77 _let_64) P1)) (or (or P3 _let_34) (not P2))) (or (or (not P1) _let_17) _let_50)) (or _let_66 _let_80)) (or (or (not _let_43) (not P8)) _let_69)) (or (or (not P2) _let_37) _let_46)) (or (or P2 _let_18) _let_81)) (or (or _let_40 (not P5)) _let_22)) (or (or P2 _let_70) _let_56)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))) ))
