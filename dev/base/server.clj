(ns server
  (:require [nrepl.cmdline :as nrepl]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.file :as file]
            [ring.util.response :as response]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def server (atom nil))

(defn handler [req]
  (if (= (:uri req) "/")
    (response/file-response "index.html" {:root "public"})
    ((wrap-defaults identity (assoc-in site-defaults [:static :files] "public"))
     req)))

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
