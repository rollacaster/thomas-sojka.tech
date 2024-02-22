(ns tech.thomas-sojka.i18n
  (:require [tongue.core :as tongue]))

(defonce locale (atom :en))
(def locales [:en :de])

(def dicts {:en {:nav/home "Home"
                 :nav/about "About"
                 :nav/now "Now"
                 :hero/sub-title "Elevate Your Interface Design with Personalized Attention and Expert Craftsmanship. Whether I collaborate and accelerate your existing team or embark on solo endeavors. I'm here to push what's possible for you."
                 :hero/title "Bringing Innovative Projects to Life"
                 :hero/cta "Let's Turn Your Ideas into Reality"
                 :hero/link-1 "writing"
                 :hero/link-2 "talking"
                 :hero/link-3 "building"
                 :main/blogs "Blogs"
                 :main/side-projects "Side Projects"
                 :main/talks "Talks"}
            :de {:nav/home "Home"
                 :nav/about "Über"
                 :nav/now "Now"
                 :hero/sub-title "Erwecken Sie Ihre Visionen zum Leben – mit individuell zugeschnittener UI/UX Gestaltung, die Ihre Projekte nicht nur visuell, sondern auch funktional auf das nächste Level hebt. Ich helfe, die Grenzen des Machbaren gemeinsam mit Ihnen neu zu definieren."
                 :hero/title "Von Idee zur Innovation: Maßgeschneiderte UI-Lösungen"
                 :hero/cta "Jetzt handeln: Verwandeln Sie Ihre Ideen in greifbare Erfolge"
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
