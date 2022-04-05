(ns user
  (:require
   [ring.adapter.jetty :as jetty]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.file :as file]
   [ring.middleware.refresh :as refresh]))

(defn wrap-dir-index [handler]
  (fn [req] (handler (update req :uri #(if (= "/" %) "/index.html" %)))))

(def server
  (jetty/run-jetty
   (-> identity
       (file/wrap-file "public")
       content-type/wrap-content-type
       (refresh/wrap-refresh ["public"])
       wrap-dir-index)
   {:port 8080 :join? false}))

(comment
  (.stop server))
