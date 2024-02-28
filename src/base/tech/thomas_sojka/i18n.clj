(ns tech.thomas-sojka.i18n
  (:require [tongue.core :as tongue]))

(defonce locale (atom :en))
(def locales [:en :de])

(def dicts {:en {:nav/home "Home"
                 :nav/about "About"
                 :nav/now "Now"
                 :hero/sub-title "With individually custom frontend solutions. Modern, fast, user-oriented."
                 :hero/title "From Idea to Innovation: Tailored-made UI Solutions"
                 :hero/cta "Bring your Vision to Life"
                 :hero/mail-subject "Project Inquiry"
                 :hero/mail-body "Hello Thomas! Can you support me? This is my idea:"
                 :hero/link-1 "writing"
                 :hero/link-2 "talking"
                 :hero/link-3 "building"
                 :main/blogs "Blogs"
                 :main/side-projects "Side Projects"
                 :main/talks "Talks"}
            :de {:nav/home "Home"
                 :nav/about "Über"
                 :nav/now "Now"
                 :hero/sub-title "Mit individuell zugeschnittenen Frontends. Modern, schnell, benutzerorientiert."
                 :hero/title "Von Idee zur Innovation: Maßgeschneiderte UI-Lösungen"
                 :hero/cta "Erwecke die Vision zum Leben"
                 :hero/mail-subject "Projekt Anfrage"
                 :hero/mail-body "Hallo Thomas! Kannst du mich unterstützten? Das ist meine Idee:"
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
