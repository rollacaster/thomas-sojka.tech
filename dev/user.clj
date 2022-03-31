(ns user
  (:require
   [ring.adapter.jetty :as jetty]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.file :as file]
   [ring.middleware.refresh :as refresh]))

(def server
  (jetty/run-jetty
   (-> identity
       (file/wrap-file "public")
       content-type/wrap-content-type
       (refresh/wrap-refresh ["public"]))
   {:port 8080 :join? false}))

(comment
  (.stop server))
