(ns tech.thomas-sojka.org-parser-hiccup
  (:require [clojure.string :as str]
            [hiccup2.core :as hiccup]
            [org-parser.parser :as parser]
            [glow.core :as glow]))

(defn link-ext-file [[ext-file]]
  (-> ext-file
      (str/replace "file:" "")
      (str/replace "org" "html")))

(defn link-ext-others [parts]
  (let [[[_ scheme] [_ rest]] parts]
    (str scheme ":" rest)))

(defn link [link-parts]
  (let [[[_ [_ [link-ext-type & link-ext]]]
         [_ description]]
        link-parts]
    (if (str/includes? (first link-ext) "png")
      [:img {:src (first link-ext) :alt description}]
      (let [link (case link-ext-type
                   :link-ext-file
                   (link-ext-file link-ext)
                   :link-ext-other (link-ext-others link-ext))]
        [:a {:href link}
         (or description link)]))))

(defn timestamp [timestamp-parts]
  [:span (second (second (second (second (first timestamp-parts)))))])

(defn text [text-parts]
  (mapv
   (fn [[text-type & text-contents]]
     (case text-type
       :text-normal (first text-contents)
       :text-sty-verbatim [:code (first text-contents)]
       :text-sty-bold [:b (first text-contents)]
       :text-sty-italic [:i (first text-contents)]
       :link-format (link text-contents)
       :timestamp (timestamp text-contents)
       (do
         (prn "NOT PARSED:" text-type text-contents)
         "")))
   text-parts))

(defn content [contents]
  (mapv
   (fn [[content-type & contents]]
     (vec
      (cons
       :p.max-w-xl.w-full
       (case content-type
         :text (text contents)))))
   contents))

(defn list-item-line [list-item-line-parts]
  (->> list-item-line-parts
       (map
        (fn [[type & contents]]
          (case type
            :text (text contents)
            nil)))
       (remove nil?)
       (map #(cons :li %))
       first
       vec))

(defn block-endline [org-document-lines hiccup]
  (let [[type from to content] (last hiccup)
        raw-block-lines (map
                         (fn [line]
                           (cond-> line
                             (re-find #"(^\s*,)\*" line) (str/replace-first "," "")))
                         (subvec org-document-lines from to))]
    (concat
     (vec (drop-last hiccup))
     (conj
      (repeat (inc (- to from)) nil)
      (case type
        :src-block [:raw (glow/highlight-html (str/join "\n" raw-block-lines))]
        :quote-block (into [:blockquote] content)
        :export-block [:raw (str/join raw-block-lines)])))))

(defn interleave-all [& seqs]
  (reduce
   (fn [a i]
     (into a (map #(get % i) seqs)))
   []
   (range (apply max (map count seqs)))))

(defn code [str]
  (let [texts (str/split str #"=(.*?)=")
        codes (->> str
                   (re-seq #"=(.*?)=")
                   (mapv (fn [[_ group]] [:code group])))]
    (->> (interleave-all texts codes)
         (remove nil?))))

(defn headline [hiccup headline-parts]
  (let [[[_ stars] [_ & text-parts]] headline-parts]
    (conj (vec hiccup) (into [(keyword (str "h" (count stars) ".w-full.max-w-xl.mb-1.mt-2"))]
                             (if (every? string? (text text-parts))
                               (code (str/join (text text-parts)))
                               (text text-parts))))))

(defn drawer-end-line [hiccup]
  (let [[_ c] (last hiccup)]
    (vec
     (concat
      (vec (drop-last hiccup))
      (vec (repeat c nil))))))

(defn block-begin-line [hiccup parts]
  (let [[[_ block-name]] parts
        start-line (inc (count hiccup))]
    (case (str/lower-case block-name)
      "src" (conj hiccup [:src-block start-line start-line])
      "export" (conj hiccup [:export-block start-line start-line])
      "quote" (conj hiccup [:quote-block start-line start-line []]))))

(defn parse [org-path]
  (let [org-document-str (slurp org-path)
        org-document-lines (str/split-lines org-document-str)]
    (->> (parser/parse org-document-str)
         (drop 1)
         (reduce
          (fn [hiccup [type & remaining]]
            (case type
              :keyword-line (conj hiccup nil)
              :affiliated-keyword-line (conj hiccup nil)
              :drawer-begin-line (conj hiccup [:drawer-begin-line 2])
              :drawer-end-line (drawer-end-line hiccup)
              :empty-line (let [prev (last hiccup)
                                [prev-type prev-from prev-to prev-content]
                                prev]
                            (cond
                              (= prev-type :drawer-begin-line) (conj (vec (drop-last hiccup)) [:drawer-begin-line (inc prev-from)])
                              (= prev-type :src-block) (conj (vec (drop-last hiccup)) [:src-block prev-from (inc prev-to)])
                              (= prev-type :quote-block) (conj (vec (drop-last hiccup)) [:quote-block prev-from (inc prev-to) prev-content])
                              (= prev-type :export-block) (conj (vec (drop-last hiccup)) [:export-block prev-from (inc prev-to)])
                              :else (vec (conj hiccup nil))))
              :content-line (let [prev (last hiccup)
                                  [prev-type prev-from prev-to prev-content]
                                  prev]
                              (cond
                                (= prev-type :drawer-begin-line) (conj (vec (drop-last hiccup)) [:drawer-begin-line (inc prev-from)])
                                (= prev-type :src-block) (conj (vec (drop-last hiccup)) [:src-block prev-from (inc prev-to)])
                                (= prev-type :quote-block)
                                (conj (vec (drop-last hiccup)) [:quote-block prev-from (inc prev-to)
                                                                (vec (apply conj (vec prev-content) (vec (content remaining))))])
                                (= prev-type :export-block) (conj (vec (drop-last hiccup)) [:export-block prev-from (inc prev-to)])
                                :else (apply conj (vec hiccup) (content remaining))))
              :block-begin-line (block-begin-line hiccup remaining)
              :block-end-line (block-endline org-document-lines hiccup)
              :headline (headline hiccup remaining)
              :list-item-line
              (let [prev (last hiccup)
                    [prev-type prev-from prev-to prev-content]
                    prev]
                (cond
                  (= prev-type :src-block) (conj (vec (drop-last hiccup)) [:src-block prev-from (inc prev-to)])
                  (= prev-type :quote-block) (conj (vec (drop-last hiccup)) [:quote-block prev-from (inc prev-to)
                                                                             (vec (conj prev-content (content remaining)))])
                  (= prev-type :export-block) (conj (vec (drop-last hiccup)) [:export-block prev-from (inc prev-to)])
                  (= prev-type :ul.w-full.max-w-xl)
                  (conj
                   (vec (drop-last hiccup))
                   (conj prev (list-item-line remaining)))
                  (not= prev-type :li)
                  (conj hiccup [:ul.w-full.max-w-xl (list-item-line remaining)])))
              (vec (conj hiccup nil))))
          [])
         (remove nil?)
         (map (fn [[type :as element]]
                (case type
                  :raw (hiccup/raw (second element))
                  (vec element)))))))
