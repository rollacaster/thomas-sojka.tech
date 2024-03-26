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

(defn- wave
  ([]
   (wave {}))
  ([{:keys [class]}]
   [:svg.fill-gray-200.w-screen
    {:preserveAspectRatio "none" :width "1440" :height "32" :viewbox "0 0 1440 32" :xmlns "http://www.w3.org/2000/svg"
     :class class}
    [:path
     {:d "M0 0.000244141C0 0.000244141 133 15.5005 178.5 15.5002C224 15.5 315 0.000244141 360 0.000244141C405 0.000244141 496 15.5002 543 15.5002C590 15.5002 669.5 0.000244141 720 0.000244141C770.5 0.000244141 848 15.5002 904 15.5002C960 15.5002 1031.5 0.000244141 1080 0.000244141C1128.5 0.000244141 1196.5 15.5002 1254.5 15.5002C1312.5 15.5002 1440 0.000244141 1440 0.000244141V32.0002H0V0.000244141Z"}]]))

(defn- side-project [{:keys [title description images flipped]}]
  (let [text-content [:div.px-6.lg:px-0 {:class "lg:w-1/2"}
                      [:div.text-2xl.mb-8 title]
                      [:p.mb-12 description]
                      #_[:div.flex.justify-between.px-16
                       [:a.bg-gray-600.text-center.text-white.p-3.lg:p-4.shadow-xl.rounded-lg
                        "Show code"]
                       [:a.bg-gray-600.text-center.text-white.p-3.lg:p-4.shadow-xl.rounded-lg
                        "Learn more"]]]]
    (into [:div.max-w-5xl.mx-auto.flex.gap-8.flex-wrap.lg:flex-nowrap
           {:class (if flipped "py-32" "py-16")}]
          (if flipped
            [images
             text-content]
            [text-content
             images]))))

(defn- section-title [{:keys [title description]}]
  [:section.relative
    [:div.pt-16.pb-24.bg-gray-500.text-white
     [:div.max-w-5xl.mx-auto.px-6.lg:px-0
      [:h2.mb-2.font-normal.text-4xl title]
      [:p.max-w-xl description]]]
    [:div.absolute.bottom-0.overflow-hidden.max-w-full.translate-y-px
     (wave)]])

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
    [:div.absolute.bottom-0.overflow-hidden.max-w-full
     (wave)]]

   [:section.flex-1.relative
    [:div.bg-gray-200.py-16
     [:div.flex.md:gap-16.justify-between.items-center.flex-wrap.md:flex-nowrap.px-6.lg:px-0.max-w-5xl.mx-auto.
      [:div.max-w-xl
       [:p.mt-0.text-4xl (i18n/translate :about/title)]
       [:p (i18n/translate :about/paragraph-1)]
       [:p (i18n/translate :about/paragraph-2)]
       [:p (i18n/translate :about/paragraph-3)]]
      [:div
       [:figure.w-60 {:class "saturate-[.70]"}
        [:a {:href "images/me.png" :alt (i18n/translate :about/image-alt)}
         [:img.p-0.float-left.rounded {:src "images/me.png" :alt (i18n/translate :about/image-alt)}]]]]]]]

   (section-title
    {:title (i18n/translate :main/posts)
     :description (i18n/translate :main/posts-description)})

   [:section
    [:div.bg-gray-200.py-8.md:py-16
     (into [:div.flex.flex-col.gap-8.md:grid.md.md:gap-0.max-w-5xl.mx-auto
            {:class "grid-cols-[1fr_0.2fr_1fr_3fr_1fr_0.5fr_1fr_4fr_0.5fr_1fr]
 grid-rows-[1fr_3fr_1fr_0.5fr_2fr_1fr_1fr]
md:height-[520px]"}]
           (let [[post1 post2 post3] (->> blogs
                                          (sort-by :date)
                                          reverse
                                          (take 3))]
             [[:div.col-start-2.col-end-6.row-start-2.row-end-5
               (blog (merge post1
                            {:description (i18n/translate :main/posts-1-description)}))]
              [:div.col-start-8.col-end-11.row-start-1.row-end-3
               (blog (merge post2
                            {:description (i18n/translate :main/posts-2-description)}))]
              [:div.col-start-7.col-end-9.row-start-4.row-end-8
               (blog (merge post3
                            {:description (i18n/translate :main/posts-3-description)}))]
              [:a.bg-gray-600.text-center.text-white.p-3.lg:p-4.shadow-xl.rounded-lg.col-start-4.col-end-5.row-start-6.row-end-7
               {:href (if (= @i18n/locale :en) "/publications.html" "/de/publications.html")}
               (i18n/translate :main/show-publications)]]))]]

   (section-title
    {:title (i18n/translate :main/side-projects)
     :description (i18n/translate :main/side-projects-description)})

   [:section.bg-gray-200.relative
    (side-project
     {:title"Shopping Cards"
      :description (i18n/translate :main/side-projects-1-description)
      :images
      [:div.flex.gap-4.px-6.lg:px-0 {:class "lg:w-1/2"}
       [:img.min-w-0
        {:src "images/home/shopping-cards-weekplan.png" :alt "Screenshot of the Weekplan"}]
       [:img.min-w-0.hidden.md:block
        {:src "images/home/shopping-cards-ingredients.png" :alt "Screenshot of the Ingredients Selection"}]]})]

   [:section.relative
    [:div.absolute.top-0.rotate-180.overflow-hidden.max-w-full
     (wave)]
    (side-project
     {:flipped true
      :title "Grootlapse"
      :description (i18n/translate :main/side-projects-2-description)
      :images
      [:div.flex.gap-4.px-6.lg:px-0
       {:class "lg:w-1/2"}
       [:img.min-w-0
        {:src "images/home/grootlapse-1.png" :alt "Picture fo the time-lapse camera"}]
       [:img.hidden.md:block.min-w-0
        {:src "images/home/grootlapse-2.png" :alt "Screenshot of the main menu"}]]})
    [:div.absolute.bottom-0.overflow-hidden.max-w-full
     (wave)]]

   [:section.bg-gray-200.relative
    (side-project
     {:title "hiccup-d3"
      :description (i18n/translate :main/side-projects-3-description)
      :images
      [:div.px-6.lg:px-0 {:class "lg:w-1/2"}
       [:img.min-w-0
        {:src "images/home/hiccup-d3.png" :alt "Screenshot of the hiccup-d3 website. Showing multiple charts"}]]})]])
