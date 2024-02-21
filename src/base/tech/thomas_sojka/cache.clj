(ns tech.thomas-sojka.cache
  (:require [babashka.fs :as fs]))

(def cache-table (atom {}))

(defn clear-cache [] (reset! cache-table {}))

(defn cached-process-file
  "Makes that a function which processes a file is only called if it hasn't
  already processed the same file in the past."
  [cache-key process-file file-name]
  (if (not (and (get-in @cache-table [cache-key file-name])
                (zero? (.compareTo (fs/last-modified-time file-name) (get-in @cache-table [cache-key file-name :last-modified])))))
    (let [result (process-file file-name)]
      (swap! cache-table assoc-in [cache-key file-name] {:result result
                                                         :last-modified (fs/last-modified-time file-name)})
      result)
    (get-in @cache-table [cache-key file-name :result])))
