(ns tech.thomas-sojka.cache-test
  (:require [babashka.fs :as fs]
            [clojure.test :refer [deftest is]]
            [tech.thomas-sojka.cache :as sut]))

(deftest caching-build
  (let [expensive-fn-call-count (atom 0)
        expensive-fn #(swap! expensive-fn-call-count inc)
        file (str (fs/path (System/getProperty "user.dir") "test/base/tech/thomas_sojka"))]
    (sut/clear-cache)

    (sut/cached-process-file expensive-fn file)
    (is (= @expensive-fn-call-count 1))

    (sut/cached-process-file expensive-fn file)
    (is (= @expensive-fn-call-count 1))))
