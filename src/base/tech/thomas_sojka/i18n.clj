(ns tech.thomas-sojka.i18n
  (:require [tongue.core :as tongue]))

(defonce locale (atom :en))
(def locales [:en :de])

(def dicts {:en {:nav/home "Home"
                 :nav/about "About"
                 :nav/now "now"
                 :hero/sub-title "Hi there 👋"
                 :hero/title "Welcome to my page where I share my thoughts by {link1}, {link2} and {link3}."
                 :hero/link-1 "writing"
                 :hero/link-2 "talking"
                 :hero/link-3 "building"
                 :main/blogs "Blogs"
                 :main/side-projects "Side Projects"
                 :main/talks "Talks"}
            :de {:nav/home "Home"
                 :nav/about "Über"
                 :nav/now "Now"
                 :hero/sub-title "Hallo 👋"
                 :hero/title "Willkommen auf meiner Seite, wo ich meine Gedanken mit {link1}, {link2} und {link3} teile."
                 :hero/link-1 "schreiben"
                 :hero/link-2 "sprechen"
                 :hero/link-3 "bauen"
                 :main/blogs "Blogs"
                 :main/side-projects "Projekte"
                 :main/talks "Vorträge"}})

(defn format-date [date]
  (.format
   (java.text.SimpleDateFormat. "yyyy-MM-dd")
   date))

(defn translate [key & args] ;; [locale key & args] => string
  (apply (tongue/build-translate dicts) @locale key args))
