(ns tech.thomas-sojka.pages.home
  (:require [hiccup.core :as hiccup]
            [tech.thomas-sojka.i18n :as i18n]))

(defn- blog [{:keys [title link date description]}]
  [:a.text-white.cursor-pointer.block.border-0
   {:href link}
   [:div.flex.flex-col.rounded-lg.h-full.shadow-inner.shadow-xl
    [:div.py-6.px-6.bg-gray-600.rounded-lg.rounded-b-none.flex-1
     [:div.text-xl.text-gray-100.font-normal.mb-0 title]
     [:p description]]
    [:div.flex.justify-between.py-2.px-6.bg-gray-200.rounded-lg.rounded-t-none
     [:span.text-gray-700 (i18n/format-date date)]]]])

(defn main [{:keys [blogs]}]
  [:div
   [:div#main.absolute.w-full.h-screen {:class "mt-[68px]"}]
   [:div.w-full.flex.flex-col.justify-center.items-center.pb-44.lg:pb-0.z-20.relative.h-screen
    {:class "pt-[68px]"}
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
     (i18n/translate :hero/cta)]
    [:div.absolute.bottom-0.w-screen.max-w-full
     [:svg.fill-gray-200
      {:viewbox "0 0 1440 64" :xmlns "http://www.w3.org/2000/svg"}
      [:path
       {:d "m 0.88987591,22.913181
c 0,0 89.73411409,17.861168 156.60917409,20.269747 39.71097,1.430235 147.67411,-28.420126 204.52513,-28.386591 46.45288,0.0274 222.10347,26.038055 268.81529,26.364725 48.05738,0.33608 283.76326,-23.270731 336.12069,-24.153131 37.55854,-0.632988 215.98834,19.136613 274.72684,21.222866 31.2225,1.108947 201.8725,-20.180313 201.8725,-20.180313
L 1440,64
H 0
Z"}]]]]



   [:section.flex-1.relative.mb-32
    [:div.bg-gray-200.py-16.md:py-32
     [:div.flex.md:gap-16.justify-center.items-center.flex-wrap.md:flex-nowrap.px-6.lg:px-0.max-w-5xl.mx-auto.
      [:div
       [:p.mt-0.text-4xl (i18n/translate :about/title)]
       [:p (i18n/translate :about/paragraph-1)]
       [:p (i18n/translate :about/paragraph-2)]
       [:p (i18n/translate :about/paragraph-3)]]
      [:div
       [:figure.w-60 {:class "saturate-[.70]"}
        [:a {:href "images/me.png" :alt (i18n/translate :about/image-alt)}
         [:img.p-0.float-left.rounded {:src "images/me.png" :alt (i18n/translate :about/image-alt)}]]]]]]
    [:div.absolute.w-screen.max-w-full.rotate-180
     [:svg.fill-gray-200
      {:viewbox "0 0 1440 64" :xmlns "http://www.w3.org/2000/svg"}
      [:path
       {:d "m 0.88987591,22.913181
c 0,0 89.73411409,17.861168 156.60917409,20.269747 39.71097,1.430235 147.67411,-28.420126 204.52513,-28.386591 46.45288,0.0274 222.10347,26.038055 268.81529,26.364725 48.05738,0.33608 283.76326,-23.270731 336.12069,-24.153131 37.55854,-0.632988 215.98834,19.136613 274.72684,21.222866 31.2225,1.108947 201.8725,-20.180313 201.8725,-20.180313
L 1440,64
H 0
Z"}]]]]

   [:section.max-w-5xl.mx-auto.py-16.flex-1.px-6.lg:px-0
    (into [:div.flex.flex-col.gap-8.md:grid.md.md:gap-0
           {:class "grid-cols-[1fr_0.2fr_1fr_3fr_1fr_0.5fr_1fr_4fr_0.5fr_1fr]
 grid-rows-[1fr_3fr_1fr_0.5fr_2fr_1fr_1fr]
md:height-[520px]"}]
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
             [:a.bg-gray-600.text-center.text-white.p-3.lg:p-4.shadow-xl.rounded-lg.col-start-4.col-end-5.row-start-6.row-end-7
              {:href (if (= @i18n/locale :en) "/publications.html" "/de/publications.html")}
              (i18n/translate :main/show-publications)]]))]])
