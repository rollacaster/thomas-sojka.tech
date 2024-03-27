(ns tech.thomas-sojka.core
  (:require
   [babashka.fs :as fs]
   [clojure.java.io :as io]
   [clojure.string :as str]
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
(def video-file-paths (read-all-file-paths "resources/videos"))
(def image-file-paths (read-all-file-paths "resources/images"))

(def files (concat content-file-paths
                   video-file-paths
                   image-file-paths
                   [(io/file "resources/styles/glow.css")
                    (io/file "resources/favicon.ico")]))
(defn- content-file? [file]
  (re-find #".org$" (.getName file)))

(defn translated-file? [file]
  (str/includes? (str file) "/de/"))

(def content-files (filter (fn [file]
                             (and (content-file? file) (not (translated-file? file))))
                           content-file-paths))



(defn- copy-to-target [path content]
  (when path
    (io/make-parents path)
    (cond (string? content) (spit path content)
          (not (fs/exists? content)) (prn content "not found")
          :else (io/copy content (io/file path)))))

(defn build-page [nav-links file]
  (cond
    (and (content-file? file) (:content-type (org-parser-meta/parse file)))
    (pages/content-file->html file nav-links)

    (= file "resources/styles/glow.css")
    (pages/syntax-colouring-css file)

    :else
    (pages/copy-resource-file file)))

(defn dest-path [target-folder locale file]
  (cond (some #(str/includes? file %) ["external-blogs" "projects" "talks"]) nil

        :else
        (str target-folder
             (->> (io/file file)
                  fs/components
                  (remove
                   (fn [file]
                     (some #(= (str file) %) ["resources" "public" "content" "de"])))
                  (map (fn [file] (str/replace file #".org$" ".html")))
                  (cons (when (= locale :de) "/de" ))
                  (str/join "/")))))

(defn build  [{:keys [files]}]
  (prn "Build:" (count files))
  (doseq [locale i18n/locales]
    (reset! i18n/locale locale)
    (let [nav-links (data/nav-links content-files)
          last-build-date (Instant/now)
          content (data/content content-files)
          target-folder "public"
          all-files (cond->> files
                      (not= locale :en) (remove
                                         (fn [file]
                                           (some #(= (.getName file) (.getName %)) (read-all-file-paths "resources/content/de"))))
                      (not= locale :en) (concat (read-all-file-paths "public/css")
                                                (read-all-file-paths "public/js")
                                                (read-all-file-paths "resources/content/de")))]
      (doseq [[content path] (keep (fn [file]
                                     ((juxt (partial build-page nav-links)
                                            (partial dest-path target-folder locale))
                                      file))
                                   all-files)]
        (copy-to-target path content))

      (copy-to-target
       (dest-path target-folder locale "index.html")
       (pages/home-page nav-links))

      (copy-to-target
       (dest-path target-folder locale "publications.html")
       (pages/publications nav-links content))

      (copy-to-target
       (dest-path target-folder locale "/index.xml")
       (pages/rss-xml last-build-date content)))))


(defonce new-build (atom false))

(defn main [_]
  (build {:files files})
  (reset! new-build true))

(comment
  (build {:files content-file-paths :target-folder "public"}))
