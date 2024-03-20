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
      [:h3.text-xl.text-gray-100.font-normal.mb-0 title]]
     [:div.flex.justify-between.py-2.px-6.bg-gray-200.rounded-lg.rounded-t-none
      [:span.text-gray-700 (i18n/format-date date)]
      (c/icon content-type)]]]])

(defn- blog [{:keys [title link date description]}]
  [:a.text-white.cursor-pointer.block.border-0
   {:href link}
   [:div.flex.flex-col.rounded-lg.h-full.shadow-inner.shadow-xl
    [:div.py-6.px-6.bg-gray-600.rounded-lg.rounded-b-none.flex-1
     [:div.text-xl.text-gray-100.font-normal.mb-0 title]
     [:p description]]
    [:div.flex.justify-between.py-2.px-6.bg-gray-200.rounded-lg.rounded-t-none
     [:span.text-gray-700 (i18n/format-date date)]]]])

(defn main [{:keys [blogs projects talks external-blogs]}]
  [:div
   [:div#main]
   [:div.w-full.h-screen.flex.flex-col.justify-center.items-center.pb-44.lg:pb-12.z-20.relative
    [:h2.font-bold.text-center.text-2xl.lg:text-5xl.px-3.lg:px-0.mb-2.lg:mb-8
     {:class "lg:w-2/3"}
     (i18n/translate :hero/title
                     (update-vals
                      {:link1 [:a.cursor-pointer {:href "#writing"} (i18n/translate :hero/link-1)]
                       :link2 [:a.cursor-pointer {:href "#talking"} (i18n/translate :hero/link-2)]
                       :link3 [:a.cursor-pointer {:href "#building"} (i18n/translate :hero/link-3)]}
                      (fn [a] (hiccup/html a))))]
    [:p.mt-0.mb-4.lg:mb-10.font-thin.lg:text-3xl.text-center.max-w-4xl.px-3 (i18n/translate :hero/sub-title)]
    [:a.font-bold.lg:text-2xl
     {:class "bg-gray-800 text-white p-3 lg:p-4 shadow-xl rounded-lg"
      :href (str "mailto:contact@thomas-sojka.tech?subject="
                 (i18n/translate :hero/mail-subject) "&body="
                 (i18n/translate :hero/mail-body))}
     (i18n/translate :hero/cta)]]



   [:section.max-w-5xl.mx-auto.pb-16.flex-1.px-6.lg:px-0
    [:div.flex.md:gap-16.items-center
     [:div
      [:p.text-4xl (i18n/translate :about/title)]
      [:p (i18n/translate :about/paragraph-1)]
      [:p (i18n/translate :about/paragraph-2)]
      [:p (i18n/translate :about/paragraph-3)]]
     [:div
      [:figure.w-60 {:class "saturate-[.70]"}
       [:a {:href "images/me.png" :alt (i18n/translate :about/image-alt)}
        [:img.p-0.float-left.rounded {:src "images/me.png" :alt (i18n/translate :about/image-alt)}]]]]]]

   [:section.max-w-5xl.mx-auto.py-16.flex-1.px-6.lg:px-0
    (into [:div.grid
           {:class "grid-cols-[1fr_0.2fr_1fr_3fr_1fr_0.5fr_1fr_4fr_0.5fr_1fr]
 grid-rows-[1fr_3fr_1fr_0.5fr_2fr_1fr_1fr]"
            :style {:height "530px"}}]
          (let [[post1 post2 post3] (->> blogs
                                         (sort-by :date)
                                         reverse
                                         (take 3))]
            [[:h2#writing.mb-2.font-normal.text-4xl.col-start-1.col-end-3
              (i18n/translate :main/blogs)]
             [:div.col-start-2.col-end-6.row-start-2.row-end-5
              (blog post1)]
             [:div.col-start-8.col-end-11.row-start-1.row-end-3
              (blog post2)]
             [:div.col-start-7.col-end-9.row-start-4.row-end-8
              (blog post3)]
             [:button.bg-gray-600.text-white.p-3.lg:p-4.shadow-xl.rounded-lg.col-start-4.col-end-5.row-start-6.row-end-7
              "Show all publications"]]))]

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
