(ns server
  (:require [nrepl.cmdline :as nrepl]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.file :as file]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def server (atom nil))

(def handler
  (wrap-defaults identity (assoc-in site-defaults [:static :files] "public")))

(defn -main [& args]
  (reset! server
          (try
            (jetty/run-jetty
             #'handler
             {:port 8080 :join? false})
            (catch Exception e
              (prn "8080 server already running"
                   e))))
  (apply nrepl/-main args))
