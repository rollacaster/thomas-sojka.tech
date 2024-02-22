(ns tech.thomas-sojka.core
  (:require
   [babashka.fs :as fs]
   [clojure.java.io :as io]
   [tech.thomas-sojka.data :as data]
   [tech.thomas-sojka.i18n :as i18n]
   [tech.thomas-sojka.org-parser-meta :as org-parser-meta]
   [tech.thomas-sojka.pages :as pages]
   [tech.thomas-sojka.cache :as cache])
  (:import
   (java.time Instant)))

(defn- read-all-file-paths [folder]
  (->> (tree-seq (fn [f] (.isDirectory f))
                          (fn [f] (->> (file-seq f)
                                      (remove #(.isDirectory %))))
                          (io/file folder))
                (remove #(.isDirectory %))))


(def content-file-paths (read-all-file-paths "resources/content"))
(def video-file-paths (read-all-file-paths "resources/videos"))
(def image-file-paths (read-all-file-paths "resources/images"))

(def files (concat content-file-paths
                   video-file-paths
                   image-file-paths
                   [(fs/file "resources/styles/glow.css")]))
(defn- content-file? [file] (re-find #".org$" (.getName file)))

(def content-files (filter content-file? content-file-paths))



(defn- copy-to-target [{:keys [path content]}]
  (io/make-parents path)
  (if (string? content)
    (spit path content)
    (io/copy content (io/file path))))

(defn build-page [target-folder nav-links file]
  (cond
    (and (content-file? file) (:content-type (org-parser-meta/parse file)))
    (pages/content-file->html file target-folder nav-links)

    (= file "resources/styles/glow.css")
    (pages/syntax-colouring-css file)

    :else
    (pages/copy-resource-file file target-folder)))

(defn build  [{:keys [files]}]
  (prn "Build:" (count files))
  (doseq [locale i18n/locales]
    (reset! i18n/locale locale)
    (let [nav-links (data/nav-links content-files)
          last-build-date (Instant/now)
          content (data/content content-files)
          target-folder (cond-> "public"
                          (not= locale :en) (str "/" (name locale) "/"))
          all-files (cond->> files
                      (not= locale :en)
                      (concat (read-all-file-paths "public/css")
                              (read-all-file-paths "public/js")))]
      (doseq [res (conj (keep (fn [file] (cache/cached-process-file locale (partial build-page target-folder nav-links) file)) all-files)
                        (pages/home-page target-folder nav-links content)
                        (pages/rss-xml target-folder last-build-date content))]
        (copy-to-target res)))))


(defonce new-build (atom false))

(defn main [_]
  (build {:files files})
  (reset! new-build true))

(comment
  (build {:files content-file-paths :target-folder "public"}))
