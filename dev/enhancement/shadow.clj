(ns shadow
  (:require [shadow.cljs.devtools.api :as shadow]
            [shadow.cljs.devtools.server :as server]))

(defn -main
  "Connects to a given build-id. Defaults to `:app`."
  [& _args]
  (server/start!)
  (shadow/watch :app)
  (shadow/nrepl-select :app))
