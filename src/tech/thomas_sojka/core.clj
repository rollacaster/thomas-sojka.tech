(ns tech.thomas-sojka.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :as hiccup]
            [tech.thomas-sojka.org-parser-hiccup :as org-parser-hiccup]))

(doseq [file (->> (file-seq (io/file "content"))
                  (remove #(.isDirectory %)) )]
  (->> (org-parser-hiccup/parse file)
       (map (fn [h] (hiccup/html h)))
       str/join
       (spit (str "public/" (str/replace (.getName file) #".org$" ".html")))))
