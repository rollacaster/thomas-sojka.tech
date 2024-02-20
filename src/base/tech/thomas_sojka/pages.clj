(ns tech.thomas-sojka.pages
  (:require
   [clj-rss.core :as rss]
   [clojure.string :as str]
   [glow.core :as glow]
   [hiccup.core :as hiccup]
   [tech.thomas-sojka.components :as components]
   [tech.thomas-sojka.org-parser-hiccup :as org-parser-hiccup]
   [tech.thomas-sojka.org-parser-meta :as org-parser-meta]
   [tech.thomas-sojka.rss :as thomas-sojka.rss]
   [tech.thomas-sojka.pages.home :as home]))

(def title "Thomas Sojka")
(def url "https://thomas-sojka.tech/")
(def language "en")
(def author "contact@thomas-sojka.tech (Thomas Sojka)")
(def description "data visualizations | frontend development | functional programming")
(def syntax-coloring {:background "#edf2f7"
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
                      :symbol "#586e75"})

(defn- public-path [target-folder file]
  (str target-folder
       (when (.getParent file)
         (-> (.getParent file)
             (str/replace "resources" "")
             (str/replace "content" "")))
       "/"))

(defn content-file->html [file target-folder nav-links]
  (let [{:keys [content-type title]} (org-parser-meta/parse file)]
    {:path (str (public-path target-folder file) "/" (str/replace (.getName file) #".org$" ".html"))
     :content (hiccup/html
                  (components/page {:title title
                                    :language language
                                    :author author
                                    :active (when (= content-type "page") title)
                                    :main (components/content (org-parser-hiccup/parse file))
                                    :description description
                                    :nav-links nav-links}))}))

(defn copy-resource-file [file target-folder]
  {:path (str (public-path target-folder file) (.getName file))
   :content file})

(defn syntax-colouring-css [file]
  {:path file
   :content (glow/generate-css syntax-coloring)})

(defn rss-xml [target-folder last-build-date content]
  {:path (str target-folder "/index.xml")
     :content
     (apply rss/channel-xml
            (thomas-sojka.rss/generate
             {:title title
              :language language
              :url url
              :author author
              :description description
              :lastBuildDate last-build-date
              :items (->> (concat (:blogs content)
                                  (:external-blogs content))
                          (sort-by :date)
                          reverse)}))})

(defn home-page [target-folder nav-links content]
  {:path (str target-folder "/index.html")
   :content
   (hiccup/html
       (components/page
        {:title title
         :language language
         :author author
         :active "Home"
         :description description
         :nav-links nav-links
         :main (home/main content)
         :scripts [:<>
                   [:script {:src "js/libs.js"}]
                   [:script {:src "js/main.js"}]]}))})
