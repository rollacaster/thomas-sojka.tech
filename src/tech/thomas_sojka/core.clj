(ns tech.thomas-sojka.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :as hiccup]
            [tech.thomas-sojka.org-parser-hiccup :as org-parser-hiccup]))

(defn header
  ([] (header nil))
  ([{:keys [active]}]
   [:header.w-full.bg-gray-500.py-3
    [:div.max-w-5xl.flex.justify-between.mx-auto
     [:h1 [:a.text-white.uppercase.tracking-widest.text-lg {:href "/"} "Thomas Sojka"]]
     [:nav
      [:ul.flex.gap-x-6
       [:li {:class (when (= active "Home") "bg-gray-700")}
        [:a.text-white {:href "/"} "Home"]]
       [:li {:class (when (= active "About") "bg-gray-700")}
        [:a.text-white {:href "/about.html"} "About"]]
       [:li {:class (when (= active "Now") "bg-gray-700")}
        [:a.text-white {:href "/now.html"} "Now"]]]]]]))

(defn content [children]
  [:section.max-w-5xl.mx-auto.py-8.flex-1
   children])

(defn footer []
  [:footer.bg-gray-500.flex.justify-center.gap-x-6.py-3
   [:a.text-white {:href "https://mobile.twitter.com/rollacaster"} "Twitter"]
   [:a.text-white {:href "https://github.com/rollacaster"} "GitHub"]
   [:a.text-white {:href "https://www.youtube.com/channel/UCBSMA2iotgxbWPSLTFeUt9g?view_as=subscriber"} "YouTube"]])

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
   [:body.flex.flex-col.h-screen
    (header)
    (-> children content vec)
    (footer) ]])

(defn public-path [file]
  (str "public"
       (when (.getParent file) (str/replace (.getParent file) "content" "")) "/"))

(defn spit-parents [path f]
  (io/make-parents path)
  (spit path f))

(defn org-file? [file]
  (re-find #".org$" (.getName file)))

(doseq [file (->> (tree-seq (fn [f] (.isDirectory f))
                            (fn [f] (->> (file-seq f)
                                        (remove #(= % f))))
                            (io/file "content"))
                  (remove #(.isDirectory %)))]
  (cond
    (org-file? file)
    (let [html-str (hiccup/html (page (org-parser-hiccup/parse file)))
          path (str (public-path file) "/" (str/replace (.getName file) #".org$" ".html"))]
      (spit-parents path html-str))
    :else
    (let [path (str (public-path file) (.getName file))]
      (io/make-parents path)
      (io/copy file (io/file path)))))


