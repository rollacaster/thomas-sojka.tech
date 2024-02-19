(ns server
  (:require [clojure.java.io :as io]
            [clojure.tools.namespace.repl]
            [compojure.core :refer [defroutes GET]]
            [nrepl.cmdline :as nrepl]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.refresh :as refresh]
            [ring.util.response :as response]
            [tech.thomas-sojka.core :as core]))

(def server (atom nil))

(def wrap-with-script #'refresh/wrap-with-script)
(def refresh-script (slurp (io/resource "ring/js/refresh.js")))

(defroutes app
  (GET "/__source_changed" [_]
    (let [res (str @core/new-build)]
      (when @core/new-build (reset! core/new-build false))
      res))
  (GET "/" []
    (-> (response/file-response "index.html" {:root "public"})
        (assoc-in [:headers "Content-Type"] "text/html"))))

(def handler
  (-> app
      (wrap-file "public" {:index-files? false})
      (wrap-with-script refresh-script)))

(defn -main [& args]
  (reset! server (jetty/run-jetty #'handler {:port 18080 :join? false}))
  (apply nrepl/-main args))
