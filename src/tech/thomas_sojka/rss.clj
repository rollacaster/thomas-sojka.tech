(ns tech.thomas-sojka.rss
  (:require [clojure.set :as set]))

(defn generate [{:keys [url description author items lastBuildDate title language]}]
  (conj
   (map (fn [c] (-> c
                   (update :date (fn [d] (.toInstant d)))
                   (set/rename-keys {:date :pubDate :filetags :category})
                   (dissoc :content-type)
                   (assoc :author author)
                   (update :link #(str url %))))
        items)
   {:title title :link url :description description
    :language language :webMaster author :lastBuildDate lastBuildDate}))
