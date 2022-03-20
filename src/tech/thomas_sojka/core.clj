(ns tech.thomas-sojka.core
  (:require
   [clj-rss.core :as rss]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [glow.core :as glow]
   [hiccup.core :as hiccup]
   [tech.thomas-sojka.org-parser-hiccup :as org-parser-hiccup]
   [tech.thomas-sojka.org-parser-meta :as org-parser-meta]
   [clojure.set :as set]
   [tech.thomas-sojka.components :as components])
  (:import (java.time Instant)))

(defn org-file? [file]
  (re-find #".org$" (.getName file)))

(defn link [file]
  (str (when (.getParent file) (str/replace (.getParent file) "content" ""))
       "/"
       (str/replace (.getName file) #".org$" ".html")))

(def nav-links (conj (->> (tree-seq (fn [f] (.isDirectory f))
                                    (fn [f] (->> (file-seq f)
                                                (remove #(= % f))))
                                    (io/file "content"))
                          (remove #(.isDirectory %))
                          (filter org-file?)
                          (map (fn [f] (assoc (org-parser-meta/parse f) :link (link f))))
                          (filter (fn [{:keys [content-type]}] (= content-type "page")))
                          (sort-by :nav))
                     {:title "Home"
                      :link "/"
                      :nav 0}))

(def description "data visualizations | frontend development | functional programming")

(defn public-path [file]
  (str "public"
       (when (.getParent file) (str/replace (.getParent file) "content" "")) "/"))

(defn spit-parents [path f]
  (io/make-parents path)
  (spit path f))

(defn html-path [file]
  (str/replace (.getName file) #".org$" ".html"))

(defn date [date-str]
  (.parse
   (java.text.SimpleDateFormat. "<yyyy-MM-dd>")
   date-str))

(def contents (->> (tree-seq (fn [f] (.isDirectory f))
                             (fn [f] (->> (file-seq f)
                                         (remove #(.isDirectory %))))
                             (io/file "content"))
                   (remove #(.isDirectory %))
                   (filter org-file?)))

(def content-projects
  (->> contents
       (map org-parser-meta/parse)
       (filter #(= (:content-type %) "project"))
       (map #(update % :date date))
       (sort-by :date #(compare %2 %1))))

(def content-external-blogs
  (->> contents
       (map org-parser-meta/parse)
       (filter #(= (:content-type %) "external-blog"))
       (map #(update % :date date))
       (sort-by :date #(compare %2 %1))))

(def content-blogs (->> contents
                        (map #(-> %
                                  org-parser-meta/parse
                                  (assoc :link (html-path %))))
                        (filter #(= (:content-type %) "blog"))
                        (map #(update % :date date))
                        (sort-by :date #(compare %2 %1))))

(def content-talks (->> contents
                        (map #(-> %
                                  org-parser-meta/parse
                                  (assoc :link (html-path %))))
                        (filter #(= (:content-type %) "talk"))
                        (map #(update % :date date))
                        (sort-by :date #(compare %2 %1))))

(def author "contact@thomas-sojka.tech (Thomas Sojka)")
(def url "https://thomas-sojka.tech/")

(spit "resources/glow.css" (glow/generate-css
                             {:background "#edf2f7"
                              :exception "#859900"
                              :repeat "#859900"
                              :conditional "#859900"
                              :variable "#268bd2"
                              :core-fn "#586e75"
                              :definition "#cb4b16"
                              :reader-char "#dc322f"
                              :special-form "#859900"
                              :macro "#859900"
                              :number "#2aa198"
                              :boolean "#2aa198"
                              :nil "#2aa198"
                              :s-exp "#586e75"
                              :keyword "#268bd2"
                              :comment "#586e75"
                              :string "#2aa198"
                              :character "#2aa198"
                              :regex "#dc322f"
                              :symbol "#586e75"}))

(spit
 "public/index.xml"
 (apply rss/channel-xml
        {:title "Thomas Sojka" :link url :description description
         :language "en" :webMaster author :lastBuildDate (Instant/now)}
        (map (fn [c] (-> c
                        (update :date (fn [d] (.toInstant d)))
                        (set/rename-keys {:date :pubDate :filetags :category})
                        (dissoc :content-type)
                        (assoc :author author)
                        (update :link #(str url %))))
             content-blogs)))

(spit
 "public/index.html"
 (hiccup/html
  (components/page
   {:active "Home"
    :description description
    :nav-links nav-links
    :main
    (components/home {:blogs content-blogs
                      :projects (map #(set/rename-keys % {:ext-link :link}) content-projects)
                      :talks (map #(set/rename-keys % {:ext-link :link}) content-talks)
                      :external-blogs (map #(set/rename-keys % {:ext-link :link}) content-external-blogs)})})))

(doseq [file (->> (tree-seq (fn [f] (.isDirectory f))
                            (fn [f] (->> (file-seq f)
                                        (remove #(= % f))))
                            (io/file "content"))
                  (remove #(.isDirectory %)))]
  (cond
    (org-file? file)
    (let [{:keys [content-type title]} (org-parser-meta/parse file)
          path (str (public-path file) "/" (str/replace (.getName file) #".org$" ".html"))]
      (case content-type
        "blog" (let [html-str (hiccup/html (components/page {:main (org-parser-hiccup/parse file)
                                                             :description description
                                                             :nav-links nav-links})) ]
                 (spit-parents path html-str))
        "page" (let [html-str (hiccup/html (components/page {:active title
                                                             :main (org-parser-hiccup/parse file)
                                                             :description description
                                                             :nav-links nav-links}))]
                 (spit-parents path html-str))
        nil))
    :else
    (let [path (str (public-path file) (.getName file))]
      (io/make-parents path)
      (io/copy file (io/file path)))))
