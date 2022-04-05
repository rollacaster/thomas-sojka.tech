(ns tech.thomas-sojka.core
  (:require
   [clojure.java.io :as io]
   [tech.thomas-sojka.data :as data]
   [tech.thomas-sojka.pages :as pages])
  (:import
   (java.time Instant)))


(def files (->> (tree-seq (fn [f] (.isDirectory f))
                          (fn [f] (->> (file-seq f)
                                      (remove #(.isDirectory %))))
                          (io/file "content"))
                (remove #(.isDirectory %))))

(defn- org-file? [file]
  (re-find #".org$" (.getName file)))
(def content-files (filter org-file? files))
(def nav-links (data/nav-links files))
(def content (data/content content-files))
(defn build
  ([]
   (build {:files files}))
  ([{:keys [files]}]
   (let [[content-files resource-files] ((juxt filter remove) org-file? files)]
     (doseq [{:keys [path content]} (pages/generate {:last-build-date (Instant/now)
                                                     :content-files content-files
                                                     :content content
                                                     :nav-links nav-links
                                                     :resource-files resource-files})]
       (io/make-parents path)
       (if (string? content)
         (spit path content)
         (io/copy content (io/file path)))))))
(comment
  (time (tech.thomas-sojka.core/build)))
