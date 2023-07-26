(ns tech.thomas-sojka.pages
  (:require
   [clj-rss.core :as rss]
   [clojure.string :as str]
   [glow.core :as glow]
   [hiccup.core :as hiccup]
   [tech.thomas-sojka.components :as components]
   [tech.thomas-sojka.org-parser-hiccup :as org-parser-hiccup]
   [tech.thomas-sojka.org-parser-meta :as org-parser-meta]
   [tech.thomas-sojka.rss :as thomas-sojka.rss]))

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
       (when (.getParent file) (str/replace (.getParent file) "content" "")) "/"))

(defn generate [{:keys [last-build-date content-files content nav-links resource-files target-folder]}]
  (concat
   [{:path "resources/glow.css"
     :content (glow/generate-css syntax-coloring)}
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
                          reverse)}))}
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
        :main (components/home content)}))}]
   (->> content-files
        (filter #(#{"blog" "page"} (:content-type (org-parser-meta/parse %))))
        (map
         (fn [file]
           (let [{:keys [content-type title]} (org-parser-meta/parse file)]
             {:path (str (public-path target-folder file) "/" (str/replace (.getName file) #".org$" ".html"))
              :content (hiccup/html
                        (components/page {:title title
                                          :language language
                                          :author author
                                          :active (when (= content-type "page") title)
                                          :main (components/content (org-parser-hiccup/parse file))
                                          :description description
                                          :nav-links nav-links}))}))))
   (map
    (fn [file]
      {:path (str (public-path target-folder file) (.getName file))
       :content file})
    resource-files)))
