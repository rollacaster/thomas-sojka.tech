(ns tech.thomas-sojka.core
  (:require
   [babashka.fs :as fs]
   [clojure.java.io :as io]
   [tech.thomas-sojka.data :as data]
   [tech.thomas-sojka.pages :as pages])
  (:import
   (java.time Instant)))


(def files (->> (tree-seq (fn [f] (.isDirectory f))
                          (fn [f] (->> (file-seq f)
                                      (remove #(.isDirectory %))))
                          (io/file "resources/content"))
                (remove #(.isDirectory %))))

(defn- org-file? [file]
  (re-find #".org$" (.getName file)))
(def content-files (filter org-file? files))
(def nav-links (data/nav-links content-files))
(def content (data/content content-files))

(defn build  [{:keys [files target-folder] :or {target-folder "public"}}]
  (prn "Build:" (count files))
  (fs/copy-tree "resources/images" "public/images" {:replace-existing true})
  (let [[content-files resource-files] ((juxt filter remove) org-file? files)]
    (doseq [{:keys [path content]} (pages/generate {:last-build-date (Instant/now)
                                                    :content-files content-files
                                                    :content content
                                                    :nav-links nav-links
                                                    :resource-files resource-files
                                                    :target-folder target-folder})]
      (io/make-parents path)
      (if (string? content)
        (spit path content)
        (io/copy content (io/file path))))))

(defn main [_]
  (build {:files files}))

(comment
  (build {:files files :target-folder "public"}))
