(ns tech.thomas-sojka.data
  (:require [babashka.fs :as fs]
            [clojure.set :as set]
            [clojure.string :as str]
            [tech.thomas-sojka.i18n :as i18n]
            [tech.thomas-sojka.org-parser-meta :as org-parser-meta]))

(defn- link [file]
  (-> file
      fs/path
      (str/replace "resources/content" "")
      fs/strip-ext
      (str ".html")))

(defn- date [date-str]
  (.parse
   (java.text.SimpleDateFormat. "<yyyy-MM-dd>")
   date-str))

(defn- html-path [file]
  (str/replace (.getName file) #".org$" ".html"))

(defn- translate-title [{:keys [i18n-key] :as nav-link}]
  (update nav-link :title (fn [title] (i18n/translate i18n-key title))))

(defn nav-links [content-files]
  (conj (->> content-files
             (map (fn [f] (-> (org-parser-meta/parse f)
                             (assoc :link (link f))
                             (update :i18n-key keyword)
                             (translate-title))))
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
