(ns tech.thomas-sojka.org-parser-meta
  (:require [clojure.string :as str]
            [org-parser.parser :as parser]))

(defn parse-keyword [[[_ key] [_ value]]]
  [(keyword (str/lower-case key)) value])

(defn parse [org-path]
  (->> (parser/parse (slurp org-path))
       (drop 1)
       (reduce
        (fn [hiccup [type & remaining]]
          (case type
            :keyword-line (into hiccup (parse-keyword remaining))
            :affiliated-keyword-line (into hiccup (parse-keyword remaining))
            hiccup))
        [])
       (apply hash-map)))

(comment
  (def org-path "content/100-days-of-spaced-repetition.org")
  (parse "content/100-days-of-spaced-repetition.org"))


