(ns tech.thomas-sojka.core
  (:require
   [babashka.fs :as fs]
   [clojure.java.io :as io]
   [tech.thomas-sojka.data :as data]
   [tech.thomas-sojka.i18n :as i18n]
   [tech.thomas-sojka.org-parser-meta :as org-parser-meta]
   [tech.thomas-sojka.pages :as pages])
  (:import
   (java.time Instant)))

(defn- read-all-file-paths [folder]
  (->> (tree-seq (fn [f] (.isDirectory f))
                          (fn [f] (->> (file-seq f)
                                      (remove #(.isDirectory %))))
                          (io/file folder))
                (remove #(.isDirectory %))))


(def content-file-paths (read-all-file-paths "resources/content"))

(defn- content-file? [file] (re-find #".org$" (.getName file)))

(def content-files (filter content-file? content-file-paths))
(def nav-links (data/nav-links content-files))


(defn- copy-to-target [path content]
  (io/make-parents path)
  (if (string? content)
    (spit path content)
    (io/copy content (io/file path))))

(def target-folder "public")

(defn build-page [file]
  (cond
    (and (content-file? file) (:content-type (org-parser-meta/parse file)))

    (pages/content-file->html file target-folder nav-links)
    (= file "resources/styles/glow.css") (pages/syntax-colouring-css file)

    :else (pages/copy-resource-file file target-folder)))

(defn build  [{:keys [files]}]
  (prn "Build:" (count files))
  (fs/copy-tree "resources/images" "public/images" {:replace-existing true})
  (fs/copy-tree "resources/videos" "public/videos" {:replace-existing true})
  (doseq [locale i18n/locales]
    (reset! i18n/locale locale)
    (let [last-build-date (Instant/now)
          content (data/content content-files)]
      (doseq [{:keys [path content]} [(pages/home-page target-folder nav-links content)
                                      (pages/rss-xml target-folder last-build-date content)
                                      (map build-page (conj files (fs/file "resources/styles/glow.css")))]]
        (copy-to-target path content)))))

(defonce new-build (atom false))

(defn main [_]
  (build {:files content-file-paths})
  (reset! new-build true))

(comment
  (build {:files content-file-paths :target-folder "public"}))
