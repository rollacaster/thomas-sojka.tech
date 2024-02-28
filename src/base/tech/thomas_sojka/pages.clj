(ns tech.thomas-sojka.pages
  (:require
   [clj-rss.core :as rss]
   [glow.core :as glow]
   [hiccup.core :as hiccup]
   [tech.thomas-sojka.components :as components]
   [tech.thomas-sojka.constants :as constants]
   [tech.thomas-sojka.i18n :as i18n]
   [tech.thomas-sojka.org-parser-hiccup :as org-parser-hiccup]
   [tech.thomas-sojka.org-parser-meta :as org-parser-meta]
   [tech.thomas-sojka.pages.home :as home]
   [tech.thomas-sojka.rss :as thomas-sojka.rss]))


(def url "https://thomas-sojka.tech/")
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

(defn content-file->html [file nav-links]
  (let [{:keys [title i18n-key]} (org-parser-meta/parse file)
        title-translated (if i18n-key (i18n/translate (keyword i18n-key)) title)]
    (hiccup/html
        (components/page {:title title-translated
                          :language (name @i18n/locale)
                          :author author
                          :main (components/content (org-parser-hiccup/parse file))
                          :description description
                          :nav-links nav-links}))))

(defn copy-resource-file [file]
  file)

(defn syntax-colouring-css [_]
  (glow/generate-css syntax-coloring))

(defn rss-xml [last-build-date content]
  (apply rss/channel-xml
         (thomas-sojka.rss/generate
          {:title constants/title
           :language (name @i18n/locale)
           :url url
           :author author
           :description description
           :lastBuildDate last-build-date
           :items (->> (concat (:blogs content)
                               (:external-blogs content))
                       (sort-by :date)
                       reverse)})))

(defn home-page [nav-links content]
  (hiccup/html
      (components/page
       {:title "Home"
        :language (name @i18n/locale)
        :author author
        :description description
        :nav-links nav-links
        :main (home/main content)
        :scripts [:<>
                  [:script {:src "js/libs.js"}]
                  [:script {:src "js/main.js"}]]})))
