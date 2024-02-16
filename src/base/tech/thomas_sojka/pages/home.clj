(ns tech.thomas-sojka.pages.home
  (:require [hiccup.core :as hiccup]
            [tech.thomas-sojka.i18n :as i18n]
            [tech.thomas-sojka.components :as c]))

(defn- content-item [{:keys [title link date content-type]}]
  [:li.mb-0
   [:a.text-white.cursor-pointer.block.border-0
    {:href link}
    [:div.flex.flex-col.rounded-lg.h-full.shadow-inner
     [:div.py-6.px-6.bg-gray-600.rounded-lg.rounded-b-none.flex-1
      [:h2.text-xl.text-gray-100.font-normal.mb-0 title]]
     [:div.flex.justify-between.py-2.px-6.bg-gray-200.rounded-lg.rounded-t-none
      [:span.text-gray-700 (i18n/format-date date)]
      (c/icon content-type)]]]])

(defn main [{:keys [blogs projects talks external-blogs]}]
  [:div
   [:div#main]
   [:div.w-full.h-screen.flex.flex-col.justify-center.items-center.pb-44.lg:pb-12
    [:p.font-bold.text-3xl (i18n/translate :hero/sub-title)]
    [:h1.text-center.text-4xl.lg:text-6xl.font-thin.px-3.lg:px-0
     {:class "lg:w-2/3"}
     (i18n/translate :hero/title
                     (update-vals
                      {:link1 [:a.cursor-pointer {:href "#writing"} (i18n/translate :hero/link-1)]
                       :link2 [:a.cursor-pointer {:href "#talking"} (i18n/translate :hero/link-2)]
                       :link3 [:a.cursor-pointer {:href "#building"} (i18n/translate :hero/link-3)]}
                      (fn [a] (hiccup/html a))))]]
   [:section.max-w-5xl.mx-auto.py-8.flex-1.px-6.lg:px-0
    [:div.mb-8
     [:h2#writing.mb-2.font-normal (i18n/translate :main/blogs)]
     [:ul.list-none.pl-0.grid.md:grid-cols-2.lg:grid-cols-3.gap-4
      (->> (concat blogs external-blogs)
           (sort-by :date)
           reverse
           (map content-item))]]
    [:div.mb-8
     [:h2#building.mb-2.font-normal (i18n/translate :main/side-projects)]
     [:ul.list-none.pl-0.grid.md:grid-cols-2.lg:grid-cols-3.gap-4
      (map content-item projects)]]
    [:div.mb-8
     [:h2#talking.mb-2.font-normal (i18n/translate :main/talks)]
     [:ul.list-none.pl-0.grid.md:grid-cols-2.lg:grid-cols-3.gap-4
      (map content-item talks)]]]
   [:script {:src "js/libs.js"}]
   [:script {:src "js/main.js"}]])
