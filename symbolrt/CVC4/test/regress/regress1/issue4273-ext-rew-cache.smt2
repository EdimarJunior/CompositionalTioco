; COMMAND-LINE: --incremental
; EXPECT: sat
; EXPECT: sat
; EXPECT: sat
; EXPECT: sat
; EXPECT: sat
(set-logic LIA)
(declare-fun _substvar_3331_ () Bool) 
(declare-fun _substvar_3308_ () Bool)
(declare-fun _substvar_3278_ () Bool)
(declare-fun _substvar_3430_ () Bool)
(declare-fun _substvar_3578_ () Bool)
(declare-fun _substvar_3541_ () Bool)
(declare-fun _substvar_3558_ () Bool)
(declare-fun _substvar_3545_ () Bool)
(declare-fun _substvar_3627_ () Bool) 
(set-option :ext-rewrite-quant true)
(set-option :ext-rew-prep true)
(declare-const v0 Bool)
(declare-const i2 Int)
(declare-const i3 Int) 
(declare-const i6 Int)
(declare-const i7 Int)
(declare-const v1 Bool)
(declare-const v4 Bool)
(declare-const v7 Bool) 
(declare-const v8 Bool)
(push 1)
(declare-const v9 Bool) 
(declare-const v10 Bool)
(assert (or (forall ((q30 Bool) (q31 Bool) (q32 Int)) (=> (xor q30 true q31 _substvar_3545_ q31 q30 q31 q30 (xor v1 (xor true v1 v1 true v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) v0 (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)))) _substvar_3558_)) _substvar_3541_))
(check-sat)
(declare-const v14 Bool)
(check-sat)
(pop 1)
(declare-const i25 Int)
(assert (not (exists ((q150 Bool) (q151 Bool)) (= true _substvar_3578_ (or v0 (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) v4 (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 true v1 v1 v0 true v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 true v1 v1 v0 true v1) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v4 (not v4) (not v4) (not v4) (not v4) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (and v0 (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) v4 (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v4 (not v4) (not v4) (not v4) (not v4) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (>= 0 0) v0 (>= 0 0) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) true (not v4) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) true) (distinct 156 156) (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 true v1 v1 v0 true v1)) v0 v0 (xor v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) v0 (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1))) v1 v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) true _substvar_3430_))))
(check-sat)
(assert (not (forall ((q159 Int) (q160 Bool) (q161 Bool) (q162 Bool)) (not (= q162 q162 q162 q161 _substvar_3278_ q161 (= true _substvar_3308_ (or v0 (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) v4 (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v1 (xor true v1 v1 true v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v4 (not v4) (not v4) (not v4) (not v4) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (and v0 (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) v4 (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v4 (not v4) (not v4) (not v4) (not v4) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (>= 0 0) v0 (>= 0 0) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) true (not v4) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) (distinct 156 156) (xor (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 true v1 v1 v0 true v1)) v0 v0 (xor v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1) v0 (= v0 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 true v1 v1 v0 true v1))) v1 v1 (xor (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1 v1 v0 (distinct v1 v1 v1 v1 v1 v0 v0 v0 v1 v0) v1)) _substvar_3627_ _substvar_3331_) q160 q162)))))
(declare-const v66 Bool)
(declare-const v68 Bool)
(check-sat)
(check-sat)  
