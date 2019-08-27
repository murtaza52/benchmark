(ns sample.core
  (:require [clojure.core.reducers :as r]
            [criterium.core :as cc :refer [bench quick-bench]]))

;; core/map without transducers

(quick-bench (doall (->> [1 2 3 4]
                         (map inc)
                         (map inc)
                         (map inc))))

;; Evaluation count : 168090 in 6 samples of 28015 calls.
;; Execution time mean : 3.651319 µs
;; Execution time std-deviation : 88.055389 ns
;; Execution time lower quantile : 3.584198 µs ( 2.5%)
;; Execution time upper quantile : 3.799202 µs (97.5%)
;; Overhead used : 7.546189 ns
;; Found 1 outliers in 6 samples (16.6667 %)
;; low-severe	 1 (16.6667 %)
;; Variance from outliers : 13.8889 % Variance is moderately inflated by outliers



;; transducers with a non lazy seq

(quick-bench (doall (->> [1 2 3 4]
                         (sequence (comp (map inc)
                                      (map inc)
                                      (map inc))))))

;; Evaluation count : 214902 in 6 samples of 35817 calls.
;; Execution time mean : 2.776696 µs
;; Execution time std-deviation : 24.377634 ns
;; Execution time lower quantile : 2.750123 µs ( 2.5%)
;; Execution time upper quantile : 2.809933 µs (97.5%)
;; Overhead used : 7.546189 ns


;;;;
;; tranducers with a lazy seq
;;;;

(bench (doall (->> (range 1 5)
                   (sequence (comp (map inc)
                                (map inc)
                                (map inc))))))

;; Evaluation count : 214230 in 6 samples of 35705 calls.
;; Execution time mean : 3.361220 µs
;; Execution time std-deviation : 622.084860 ns
;; Execution time lower quantile : 2.874093 µs ( 2.5%)
;; Execution time upper quantile : 4.328653 µs (97.5%)
;; Overhead used : 7.546189 ns


;;;;
;; core.reducers
;;;;

;; the fn evaluation is lazy, the last operation forces it.

(bench (->> [1 2 3 4]
            (r/map inc)
            (r/map inc)
            (r/map inc)
            (into [])))

;; Evaluation count : 331302 in 6 samples of 55217 calls.
;; Execution time mean : 2.035153 µs
;; Execution time std-deviation : 314.070348 ns
;; Execution time lower quantile : 1.720615 µs ( 2.5%)
;; Execution time upper quantile : 2.381706 µs (97.5%)
;; Overhead used : 7.546189 ns


;;;; Evaluating a larger range so that the chunking comes into play ;;;;


;; core/map without transducers

(quick-bench (doall (->> (range 500)
                         (map inc)
                         (map inc)
                         (map inc))))



;; transducers with a non lazy seq

(quick-bench (doall (->> (doall (range 500))
                         (sequence (comp (map inc)
                                      (map inc)
                                      (map inc))))))

;; Evaluation count : 2598 in 6 samples of 433 calls.
;; Execution time mean : 237.164523 µs
;; Execution time std-deviation : 5.336417 µs
;; Execution time lower quantile : 231.751575 µs ( 2.5%)
;; Execution time upper quantile : 244.836021 µs (97.5%)
;; Overhead used : 7.546189 ns


;; tranducers with a lazy seq

(bench (doall (->> (range 500)
                   (sequence (comp (map inc)
                                (map inc)
                                (map inc))))))

;; Evaluation count : 325140 in 60 samples of 5419 calls.
;; Execution time mean : 202.666330 µs
;; Execution time std-deviation : 20.788644 µs
;; Execution time lower quantile : 185.770700 µs ( 2.5%)
;; Execution time upper quantile : 263.915488 µs (97.5%)
;; Overhead used : 6.413159 ns



;; core.reducers


(bench (->> (range 500)
            (r/map inc)
            (r/map inc)
            (r/map inc)
            (into [])))

;; Evaluation count : 360180 in 60 samples of 6003 calls.
;; Execution time mean : 168.195850 µs
;; Execution time std-deviation : 1.886885 µs
;; Execution time lower quantile : 166.246432 µs ( 2.5%)
;; Execution time upper quantile : 173.177642 µs (97.5%)
;; Overhead used : 6.413159 ns



