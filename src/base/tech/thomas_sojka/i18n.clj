(ns tech.thomas-sojka.i18n
  (:require [tongue.core :as tongue]
            [hiccup.core :as hiccup]))

(defonce locale (atom :en))
(def locales [:en :de])

(def dicts {:en {:nav/home "Home"
                 :nav/publications "Publications"
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
                 :main/talks "Talks"
                 :about/title "Hi 👋"
                 :about/paragraph-1 "My name is Thomas. I am a programmer living in Munich and I am a freelance software engineer."
                 :about/paragraph-2 (fn []
                                      (str "Before that, I traveled with my wife for six months through North and South America. I worked at "
                                           (hiccup/html [:a {:href "https://pitch.com/"} "Pitch"]) " and helped to build a world-class editor for presentations and worked at "
                                           (hiccup/html [:a {:href "https://comsystoreply.de/"} "comSysto"]) " and wrote business applications for "
                                           (hiccup/html [:a {:href "https://comsystoreply.de/referenzen"} "various projects"]) ". I studied Information Systems at "
                                           (hiccup/html [:a {:href "https://www.tum.de/en/"} "TUM"]) "."))
                 :about/paragraph-3 (fn []
                                      (str "I am interested in "
                                           (hiccup/html [:a {:href "https://www.youtube.com/playlist?list=PLB3sLatZtqYms9T85gf_PTyneg1SLvsEa"} "Data"]) " "
                                           (hiccup/html [:a {:href "https://medium.com/nightingale/steal-like-a-data-visualiser-2ec7fb470896?source=friends_link&sk=8ab6fa936d6e61dbdec2c2a7f607d1a0"} "Viz"]) ", "
                                           (hiccup/html [:a {:href "https://www.youtube.com/watch?v=juMLwOTxnvw"} "Functional"]) " "
                                           (hiccup/html [:a {:href "https://www.youtube.com/watch?v=juMLwOTxnvw"} "Programming"]) ", Cloud computing, and "
                                           (hiccup/html [:a {:href "https://rollacaster.github.io/sketches/"} "art"]) " "
                                           (hiccup/html [:a {:href "https://twitter.com/rollacaster/status/1351486650992439296"} "with"]) " "
                                           (hiccup/html [:a {:href "https://fire-hands.now.sh/"} "code"]) "."))
                 :about/image-alt "Picture of Thomas Sojka"}
            :de {:nav/home "Home"
                 :nav/publications "Publikationen"
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
                 :main/talks "Vorträge"
                 :about/title "Hallo 👋"
                 :about/paragraph-1 "Mein Name ist Thomas. Ich bin ein freiberuflicher Programmierer aus München."
                 :about/paragraph-2 (fn []
                                      (str "Zuvor reiste ich mit meiner Frau sechs Monate lang durch Nord- und Südamerika. Ich habe bei "
                                           (hiccup/html [:a {:href "https://pitch.com/"} "Pitch"]) " gearbeitet und dort geholfen, einen erstklassigen Editor für Präsentationen zu entwickeln. Außerdem war ich bei "
                                           (hiccup/html [:a {:href "https://comsystoreply.de/"} "comSysto"]) " tätig und habe Geschäftsanwendungen für "
                                           (hiccup/html [:a {:href "https://comsystoreply.de/referenzen"} "verschiedene Projekte"]) " geschrieben. Ich habe Informationswirtschaft an der "
                                           (hiccup/html [:a {:href "https://www.tum.de/en/"} "TUM"]) " studiert."))
                 :about/paragraph-3 (fn []
                                      (str "Ich interessiere mich für "
                                           (hiccup/html [:a {:href "https://www.youtube.com/playlist?list=PLB3sLatZtqYms9T85gf_PTyneg1SLvsEa"} "Datenvisualisierung"]) ", "
                                           (hiccup/html [:a {:href "https://www.youtube.com/watch?v=juMLwOTxnvw"} "Funktionale"]) " "
                                           (hiccup/html [:a {:href "https://www.youtube.com/watch?v=juMLwOTxnvw"} "Programmierung"]) ", Cloud computing, und "
                                           (hiccup/html [:a {:href "https://rollacaster.github.io/sketches/"} "Kunst"]) " "
                                           (hiccup/html [:a {:href "https://twitter.com/rollacaster/status/1351486650992439296"} "mit"]) " "
                                           (hiccup/html [:a {:href "https://fire-hands.now.sh/"} "Code"]) "."))
                 :about/image-alt "Picture of Thomas Sojka" }})

(defn format-date [date]
  (.format
   (java.text.SimpleDateFormat. "yyyy-MM-dd")
   date))

(defn translate [key & args] ;; [locale key & args] => string
  (apply (tongue/build-translate dicts) @locale key args))
