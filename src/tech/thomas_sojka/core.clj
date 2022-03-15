(ns tech.thomas-sojka.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :as hiccup]
            [tech.thomas-sojka.org-parser-hiccup :as org-parser-hiccup]))

(defn header
  ([] (header nil))
  ([{:keys [active]}]
   [:header
    [:h1 [:a {:href "/"} "Thomas Sojka"]]
    [:nav
     [:ul
      [:li {:class (when (= active "Home") "bg-gray-700")}
       [:a {:href "/"} "Home"]]
      [:li {:class (when (= active "About") "bg-gray-700")}
       [:a {:href "/about.html"} "About"]]
      [:li {:class (when (= active "Now") "bg-gray-700")}
       [:a {:href "/now.html"} "Now"]]]]]))

(defn content [children]
  [:div#content
   [:section.max-w-5xl
    children]])

(defn footer []
  [:footer
   [:div
    [:a {:href "https://mobile.twitter.com/rollacaster"} "Twitter"]
    [:a {:href "https://github.com/rollacaster"} "GitHub"]
    [:a {:href "https://www.youtube.com/channel/UCBSMA2iotgxbWPSLTFeUt9g?view_as=subscriber"} "YouTube"]]])

(defn page [children]
  [:html {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:content "width=device-width, initial-scale=1" :name "viewport"}]
    [:title "Thomas Sojka"]
    [:meta {:content "Thomas Sojka" :name "author"}]
    [:meta {:content "data visualizations | frontend development | functional programming" :name "description"}]
    [:meta {:content "programming emacs clojure javascript blog tech" :name "keywords"}]
    [:link {:href "css/styles.css" :rel "stylesheet" :type "text/css"}]
    [:link {:href "css/blog.css" :rel "stylesheet" :type "text/css"}]]
   [:body
    (header)
    (-> children content vec)
    (footer) ]])

(doseq [file (->> (file-seq (io/file "content"))
                  (remove #(.isDirectory %)) )
        :let [html-str (hiccup/html (page (org-parser-hiccup/parse file)))
              path (str "public/" (str/replace (.getName file) #".org$" ".html"))]]
  (spit path html-str))


