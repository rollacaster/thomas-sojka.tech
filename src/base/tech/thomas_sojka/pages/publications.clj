(ns tech.thomas-sojka.pages.publications
  (:require [tech.thomas-sojka.i18n :as i18n]
            [tech.thomas-sojka.components :as c]))

(defn- content-item [{:keys [title link date content-type]}]
  [:li.mb-0
   [:a.text-white.cursor-pointer.block.border-0
    {:href link}
    [:div.flex.flex-col.rounded-lg.h-full.shadow-inner
     [:div.py-6.px-6.bg-gray-600.rounded-lg.rounded-b-none.flex-1
      [:h3.text-xl.text-gray-100.font-normal.mb-0 title]]
     [:div.flex.justify-between.py-2.px-6.bg-gray-200.rounded-lg.rounded-t-none
      [:span.text-gray-700 (i18n/format-date date)]
      (c/icon content-type)]]]])

(defn main [{:keys [blogs projects talks external-blogs]}]
  [:div {:class "mt-[68px]"}
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
      (map content-item talks)]]]])
