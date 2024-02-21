(ns tech.thomas-sojka.cache
  (:require [babashka.fs :as fs]))

(def cache-table (atom {}))

(defn clear-cache [] (reset! cache-table {}))

(defn cached-process-file
  "Makes that a function which processes a file is only called if it hasn't
  already processed the same file in the past."
  [process-file file-name]
  (when (not
         (and (get @cache-table file-name)
              (zero? (.compareTo (fs/last-modified-time file-name) (get @cache-table file-name)))))
    (process-file)
    (swap! cache-table assoc (str file-name) (fs/last-modified-time file-name))))
