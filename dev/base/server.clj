(ns server
  (:require [nrepl.cmdline :as nrepl]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.file :as file]))

(def server (atom nil))

(def handler
  (-> identity
      (file/wrap-file "public")))

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
