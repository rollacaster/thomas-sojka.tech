(ns tech.thomas-sojka.org-parser-meta
  (:require [org-parser.parser :as parser]
            [clojure.string :as str]
            [tech.thomas-sojka.cache :as cache]))

(defn parse-keyword [[[_ key] [_ value]]]
  (case (keyword (str/lower-case key))
    :nav [:nav (Integer/parseInt value)]
    [(keyword (str/lower-case key)) value]))

(defn parse-it [org-path]
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

(defn parse [org-path] (cache/cached-process-file :parse parse-it org-path))

(comment
  (def org-path "content/100-days-of-spaced-repetition.org")
  (parse "content/100-days-of-spaced-repetition.org"))
