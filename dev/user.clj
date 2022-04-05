(ns user
  (:require
   [ring.adapter.jetty :as jetty]
   [ring.middleware.content-type :as content-type]
   [ring.middleware.file :as file]
   [ring.middleware.refresh :as refresh]
   [shadow.cljs.devtools.api :as shadow]
   [shadow.cljs.devtools.server :as server]))

(defn wrap-dir-index [handler]
  (fn [req] (handler (update req :uri #(if (= "/" %) "/index.html" %)))))

(def server
  (try
    (jetty/run-jetty
     (-> identity
         (file/wrap-file "public")
         content-type/wrap-content-type
         (refresh/wrap-refresh ["public"])
         wrap-dir-index)
     {:port 8080 :join? false})
    (catch Exception e)))

(defn cljs-repl
  "Connects to a given build-id. Defaults to `:app`."
  ([]
   (cljs-repl :app))
  ([build-id]
   (server/start!)
   (shadow/watch build-id)
   (shadow/nrepl-select build-id)))

(comment
  (.stop server))
