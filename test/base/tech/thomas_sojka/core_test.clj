(ns tech.thomas-sojka.core-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :as t]
   [tech.thomas-sojka.core :as sut]))

(t/deftest dest-path
  (t/is (= (sut/dest-path "public" :en
                          (io/file"resources/content/100-days-of-spaced-repetition.org"))
           "public/100-days-of-spaced-repetition.html"))

  (t/is (= (sut/dest-path "public" :en
                          (io/file "resources/content/external-blogs/cross-stack-javascript-with-react.org"))
           nil))

  (t/is (= (sut/dest-path "public" :de
                          (io/file"resources/content/100-days-of-spaced-repetition.org"))
           "public/de/100-days-of-spaced-repetition.html"))

  (t/is (= (sut/dest-path "public" :en (io/file"resources/images/christmas-card-2019.png"))
           "public/images/christmas-card-2019.png"))

  (t/is (= (sut/dest-path "public" :en "index.html") "public/index.html"))

  (t/is (= (sut/dest-path "public" :de "index.html") "public/de/index.html"))

  (t/is (= (sut/dest-path "public" :de (io/file "public/js/cljs-runtime/goog.html.trustedresourceurl.js"))
           "public/de/js/cljs-runtime/goog.html.trustedresourceurl.js"))

  )
