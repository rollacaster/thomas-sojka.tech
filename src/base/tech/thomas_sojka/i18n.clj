(ns tech.thomas-sojka.i18n
  (:require [tongue.core :as tongue]
            [hiccup.core :as hiccup]))

(defonce locale (atom :en))
(def locales [:en :de])

(def dicts {:en {:nav/home "Home"
                 :nav/publications "Publications"
                 :nav/now "Now"
                 :hero/sub-title "With individually custom frontend solutions. Modern, fast, user-oriented."
                 :hero/title "From Idea to Innovation: Tailor-made UI Solutions"
                 :hero/cta "Bring your Vision to Life"
                 :hero/mail-subject "Project Inquiry"
                 :hero/mail-body "Hello Thomas! Can you support me? This is my idea:"
                 :hero/link-1 "writing"
                 :hero/link-2 "talking"
                 :hero/link-3 "building"
                 :main/posts "Posts"
                 :main/posts-description "A selection of the articles I wrote in the past. Writing
about something is the best way to truly learn something. It helps to
structure thoughts and uncover areas that are worth exploring deeper."
                 :main/posts-1-description "You can use your development skills to improve the things you develop. Let's explore this productivity cycle."
                 :main/posts-2-description "Telling the story of GrootLapse timelapse camera for plant watching in built."
                 :main/posts-3-description "When I first learned about macros, I recognized how powerful they are but had no idea what to build with them. Thankfully this changed."
                 :main/show-publications "Show all publications"
                 :main/side-projects "Side Projects"
                 :main/side-projects-description "I am really grateful that I could make my hobby my
profession. But it's still a hobby for me as well and here are projects that I
built for others or myself."
                 :main/side-projects-1-description "One of my rare projects that
                 gifted me more lifetime than it took to build it. Shopping
                 Cards helps me to plan my meals and creates shopping lists for
                 me. The shopping lists are sorted by the aisles of my local
                 supermarket. It also helped me to reduce my food waste
                 significantly. Technically is just yet another CRUD-App but it
                 had a big impact on my life, which makes it my favorite side
                 project."
                 :main/side-projects-2-description "I built a time-lapse camera
                 to track paprikas growing in a hydroponic system. This was one
                 of my first physical projects and taught me about video
                 processing as well. Although the final paprikas didn't taste
                 that great, it was fun to watch them grow and to be able to
                 touch the thing I built."
                 :main/side-projects-3-description "My first serious attempt to
                 build something not only for one person but to help others with
                 a problem that was nagging me as well. It's a snippet
                 collection to build charts in Clojure using the D3.js library.
                 While building it, I discovered many practical use cases of
                 Clojure macros for the first time."
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
                 :main/posts "Beiträge"
                 :main/posts-description "Eine Auswahl der Artikel, die ich in
                 der Vergangenheit geschrieben habe. Über etwas zu schreiben ist
                 der beste Weg, um etwas wirklich zu lernen. Es hilft, Gedanken
                 zu strukturieren und Bereiche aufzudecken, die es wert sind,
                 näher erkundet zu werden."
                 :main/posts-1-description "Du kannst deine Entwicklerfähigkeiten nutzen, um die von dir erstellten Dinge zu verbessern. Lass uns diesen Produktivitätszyklus erkunden."
                 :main/posts-2-description "Erzählt die Geschicht der GrootLapse-Zeitrafferkamera, diese beobachtet Pflanzen während sie wachsen."
                 :main/posts-3-description "Als ich zum ersten Mal von Makros gehört habe, erkannte ich, wie mächtig sie sind, wusste aber nicht, was ich damit bauen sollte. Zum Glück hat sich das geändert."
                 :main/show-publications "Alle Publikationen"
                 :main/side-projects "Projekte"
                 :main/side-projects-description "Ich bin wirklich dankbar, dass
                 ich mein Hobby zu meinem Beruf machen konnte. Aber es ist für
                 mich immer noch auch ein Hobby, und hier sind Projekte, die ich
                 für andere oder für mich selbst gebaut habe."
                 :main/side-projects-1-description
                 "Eines meiner seltenen Projekte, das mir mehr Lebenszeit
                 geschenkt hat, als es gebraucht hat, um es zu bauen. Shopping
                 Cards hilft mir, mein Essen zu planen und erstellt
                 Einkaufslisten für mich. Die Einkaufslisten sind nach den
                 Gängen meines Supermarkts nebanan sortiert. Es hat mir auch
                 geholfen, meine Lebensmittelverschwendung zu reduzieren.
                 Technisch gesehen ist es nur eine weitere CRUD-Anwendung, aber
                 es hatte einen großen Einfluss auf mein Leben, weshalb es mein
                 Lieblings-Side-Projekt ist."
                 :main/side-projects-2-description
                 "Ich habe eine Zeitraffer-Kamera gebaut, um Paprikas in einem
Hypdroponischen System beim wachsen zu zusehen. Das war eines meiner ersten
physischen Projekte und hat mir auch einiges über Videobearbeitung
beigebracht. Obwohl die endgültigen Paprikas nicht sehr gut geschmeckt haben, hat
es Spaß gemacht sie wachsen zu sehen."
                 :main/side-projects-3-description
                 "Mein erster ernsthafter Versuch, etwas zu bauen, das nicht nur
                 für eine Person gedacht ist, sondern anderen mit einem Problem
                 zu helfen, das mich auch plagte. Es ist eine
                 Code-Schnipselsammlung zum Erstellen von Diagrammen in Clojure
                 mit der D3.js-Bibliothek. Beim Bauen habe ich zum ersten Mal
                 viele praktische Anwendungsfälle von Clojure-Makros entdeckt."
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
