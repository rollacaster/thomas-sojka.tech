(ns user
  (:require
   [clojure.string :as str]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.file :as file]
   [ring.middleware.refresh :as refresh]
   [shadow.cljs.devtools.api :as shadow]
   [shadow.cljs.devtools.server :as server]))

(defn wrap-dir-index [handler]
  (fn [req] (handler (update req :uri #(if (= "/" %) "/index.html" %)))))

(defn wrap-js [handler]
  (fn [req]
    (if (str/starts-with? (:uri req) "/js")
      ((file/wrap-file handler "public-dev") req)
      (handler req))))

(def handler
  (-> identity
      (file/wrap-file "public-dev/static")
      content-type/wrap-content-type
      (refresh/wrap-refresh ["public-dev/static"])
      wrap-js
      wrap-dir-index))

(defn cljs-repl
  "Connects to a given build-id. Defaults to `:app`."
  ([]
   (cljs-repl :dev))
  ([build-id]
   (server/start!)
   (shadow/watch build-id)
   (shadow/nrepl-select build-id)))

(def server
  (try
    (jetty/run-jetty
     #'handler
     {:port 8080 :join? false})
    (catch Exception e
      (prn "8080 server already running"
           e))))

(comment
  (.stop server))
