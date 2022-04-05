(ns tech.thomas-sojka.data
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [tech.thomas-sojka.org-parser-meta :as org-parser-meta]))

(defn- link [file]
  (str (when (.getParent file) (str/replace (.getParent file) "content" ""))
       "/"
       (str/replace (.getName file) #".org$" ".html")))

(defn- date [date-str]
  (.parse
   (java.text.SimpleDateFormat. "<yyyy-MM-dd>")
   date-str))

(defn- html-path [file]
  (str/replace (.getName file) #".org$" ".html"))

(defn nav-links [content-files]
  (conj (->> content-files
             (map (fn [f] (assoc (org-parser-meta/parse f) :link (link f))))
             (filter (fn [{:keys [content-type]}] (= content-type "page")))
             (sort-by :nav))
        {:title "Home"
         :link "/"
         :nav 0}))

(defn- sort-content [content]
  (->> content
       (map #(update % :date date))
       (sort-by :date #(compare %2 %1))))

(defn- content-projects [content-files]
  (->> content-files
       (map org-parser-meta/parse)
       (filter #(= (:content-type %) "project"))
       sort-content))

(defn- content-external-blogs [content-files]
  (->> content-files
       (map org-parser-meta/parse)
       (filter #(= (:content-type %) "external-blog"))
       sort-content))

(defn- content-blogs [content-files]
  (->> content-files
       (map #(-> %
                 org-parser-meta/parse
                 (assoc :link (html-path %))))
       (filter #(= (:content-type %) "blog"))
       sort-content))

(defn- content-talks [content-files]
  (->> content-files
       (map #(-> %
                 org-parser-meta/parse
                 (assoc :link (html-path %))))
       (filter #(= (:content-type %) "talk"))
       sort-content))

(defn content [content-files]
  {:blogs (content-blogs content-files)
   :projects (->> content-files
                  content-projects
                  (map #(set/rename-keys % {:ext-link :link})))
   :talks (->> content-files
               content-talks
               (map #(set/rename-keys % {:ext-link :link})))
   :external-blogs (->> content-files
                        content-external-blogs
                        (map #(set/rename-keys % {:ext-link :link})))})
