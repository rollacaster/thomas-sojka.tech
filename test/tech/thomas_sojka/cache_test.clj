(ns tech.thomas-sojka.cache-test
  (:require [babashka.fs :as fs]
            [clojure.test :refer [deftest is]]
            [tech.thomas-sojka.cache :as sut]))

(deftest cached-process-file
  (let [expensive-fn-call-count (atom 0)
        expensive-fn (fn [_] (swap! expensive-fn-call-count inc))
        file (str (fs/path (System/getProperty "user.dir") "test/base/tech/thomas_sojka"))]
    (sut/clear-cache)

    (is (= (sut/cached-process-file :test expensive-fn file) 1))

    (is (= (sut/cached-process-file :test expensive-fn file) 1))))
