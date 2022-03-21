(ns tech.thomas-sojka.core
  (:require
   [clojure.java.io :as io]
   [tech.thomas-sojka.pages :as pages])
  (:import
   (java.time Instant)))


(def files (->> (tree-seq (fn [f] (.isDirectory f))
                          (fn [f] (->> (file-seq f)
                                      (remove #(.isDirectory %))))
                          (io/file "content"))
                (remove #(.isDirectory %))))

(defn build [_]
  (doseq [{:keys [path content]} (pages/generate {:last-build-date (Instant/now)
                                                  :files files})]
    (io/make-parents path)
    (if (string? content)
      (spit path content)
      (io/copy content (io/file path)))))
