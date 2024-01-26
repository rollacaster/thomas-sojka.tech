(ns user
  (:require
   [clojure.string :as str]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.file :as file]
   [ring.middleware.resource :as resource]
   [ring.middleware.refresh :as refresh]
   [shadow.cljs.devtools.api :as shadow]
   [shadow.cljs.devtools.server :as server]))

(defn wrap-dir-index [handler]
  (fn [req] (handler (update req :uri #(if (= "/" %) "/index.html" %)))))

(defn wrap-js [handler]
  (fn [req]
    (file/wrap-file handler "public") req
    (handler req)))

(def handler
  (-> (fn [req] (prn "HI" req)
        (when (not= (:uri req) "/favicon.ico")
          (def req req))
        req)
      (file/wrap-file "public")))

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
