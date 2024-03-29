(ns tech.thomas-sojka.components
  (:require
   [clojure.string :as str]
   [hiccup.page]
   [tech.thomas-sojka.constants :as constants]
   [tech.thomas-sojka.i18n :as i18n]))

(defn icon [name]
  [:svg {:width "20" :height "20" :viewbox "0 0 24 24" :fill "dark-gray"}
   [:path {:d (case name
                "talk" "M24 1h-24v16.981h4v5.019l7-5.019h13z"
                "blog" "M18.363 8.464l1.433 1.431-12.67 12.669-7.125 1.436 1.439-7.127 12.665-12.668 1.431 1.431-12.255 12.224-.726 3.584 3.584-.723 12.224-12.257zm-.056-8.464l-2.815 2.817 5.691 5.692 2.817-2.821-5.693-5.688zm-12.318 18.718l11.313-11.316-.705-.707-11.313 11.314.705.709z"
                "course" "M10 23c0-1.105.895-2 2-2h2c.53 0 1.039.211 1.414.586s.586.884.586 1.414v1h-6v-1zm8 0c0-1.105.895-2 2-2h2c.53 0 1.039.211 1.414.586s.586.884.586 1.414v1h-6v-1zm-11.241-14c.649 0 1.293-.213 1.692-.436.755-.42 2.695-1.643 3.485-2.124.216-.131.495-.083.654.113l.008.011c.165.204.146.499-.043.68-.622.597-2.443 2.328-3.37 3.213-.522.499-.822 1.183-.853 1.904-.095 2.207-.261 6.912-.331 8.833-.017.45-.387.806-.837.806h-.001c-.444 0-.786-.347-.836-.788-.111-.981-.329-3.28-.426-4.212-.04-.384-.28-.613-.585-.615-.304-.001-.523.226-.549.609-.061.921-.265 3.248-.341 4.22-.034.442-.397.786-.84.786h-.001c-.452 0-.824-.357-.842-.808-.097-2.342-.369-8.964-.369-8.964l-1.287 2.33c-.14.253-.445.364-.715.26h-.001c-.279-.108-.43-.411-.349-.698l1.244-4.393c.122-.43.515-.727.962-.727h4.531zm6.241 7c1.242 0 2.25 1.008 2.25 2.25s-1.008 2.25-2.25 2.25-2.25-1.008-2.25-2.25 1.008-2.25 2.25-2.25zm8 0c1.242 0 2.25 1.008 2.25 2.25s-1.008 2.25-2.25 2.25-2.25-1.008-2.25-2.25 1.008-2.25 2.25-2.25zm3-1h-14v-2h7v-1h3v1h2v-11h-20v4.356l-2 2v-8.356h24v15zm-7-5h-3v-1h3v1zm-11.626-6c1.241 0 2.25 1.008 2.25 2.25s-1.009 2.25-2.25 2.25c-1.242 0-2.25-1.008-2.25-2.25s1.008-2.25 2.25-2.25zm14.626 4h-6v-1h6v1zm0-2h-6v-1h6v1z"
                "project" "M11.954 11c3.33 0 7.057 6.123 7.632 8.716.575 2.594-.996 4.729-3.484 4.112-1.092-.271-3.252-1.307-4.102-1.291-.925.016-2.379.836-3.587 1.252-2.657.916-4.717-1.283-4.01-4.073.774-3.051 4.48-8.716 7.551-8.716zm10.793-4.39c1.188.539 1.629 2.82.894 5.27-.704 2.341-2.33 3.806-4.556 2.796-1.931-.877-2.158-3.178-.894-5.27 1.274-2.107 3.367-3.336 4.556-2.796zm-21.968.706c-1.044.729-1.06 2.996.082 5.215 1.092 2.12 2.913 3.236 4.868 1.87 1.696-1.185 1.504-3.433-.082-5.215-1.596-1.793-3.824-2.599-4.868-1.87zm15.643-7.292c1.323.251 2.321 2.428 2.182 5.062-.134 2.517-1.405 4.382-3.882 3.912-2.149-.407-2.938-2.657-2.181-5.061.761-2.421 2.559-4.164 3.881-3.913zm-10.295.058c-1.268.451-1.92 2.756-1.377 5.337.519 2.467 2.062 4.114 4.437 3.269 2.06-.732 2.494-3.077 1.377-5.336-1.125-2.276-3.169-3.721-4.437-3.27z"
                "medium" "M2.846 6.887c.03-.295-.083-.586-.303-.784l-2.24-2.7v-.403h6.958l5.378 11.795 4.728-11.795h6.633v.403l-1.916 1.837c-.165.126-.247.333-.213.538v13.498c-.034.204.048.411.213.537l1.871 1.837v.403h-9.412v-.403l1.939-1.882c.19-.19.19-.246.19-.537v-10.91l-5.389 13.688h-.728l-6.275-13.688v9.174c-.052.385.076.774.347 1.052l2.521 3.058v.404h-7.148v-.404l2.521-3.058c.27-.279.39-.67.325-1.052v-10.608z"
                "youtube" "M4.652 0h1.44l.988 3.702.916-3.702h1.454l-1.665 5.505v3.757h-1.431v-3.757l-1.702-5.505zm6.594 2.373c-1.119 0-1.861.74-1.861 1.835v3.349c0 1.204.629 1.831 1.861 1.831 1.022 0 1.826-.683 1.826-1.831v-3.349c0-1.069-.797-1.835-1.826-1.835zm.531 5.127c0 .372-.19.646-.532.646-.351 0-.554-.287-.554-.646v-3.179c0-.374.172-.651.529-.651.39 0 .557.269.557.651v3.179zm4.729-5.07v5.186c-.155.194-.5.512-.747.512-.271 0-.338-.186-.338-.46v-5.238h-1.27v5.71c0 .675.206 1.22.887 1.22.384 0 .918-.2 1.468-.853v.754h1.27v-6.831h-1.27zm2.203 13.858c-.448 0-.541.315-.541.763v.659h1.069v-.66c.001-.44-.092-.762-.528-.762zm-4.703.04c-.084.043-.167.109-.25.198v4.055c.099.106.194.182.287.229.197.1.485.107.619-.067.07-.092.105-.241.105-.449v-3.359c0-.22-.043-.386-.129-.5-.147-.193-.42-.214-.632-.107zm4.827-5.195c-2.604-.177-11.066-.177-13.666 0-2.814.192-3.146 1.892-3.167 6.367.021 4.467.35 6.175 3.167 6.367 2.6.177 11.062.177 13.666 0 2.814-.192 3.146-1.893 3.167-6.367-.021-4.467-.35-6.175-3.167-6.367zm-12.324 10.686h-1.363v-7.54h-1.41v-1.28h4.182v1.28h-1.41v7.54zm4.846 0h-1.21v-.718c-.223.265-.455.467-.696.605-.652.374-1.547.365-1.547-.955v-5.438h1.209v4.988c0 .262.063.438.322.438.236 0 .564-.303.711-.487v-4.939h1.21v6.506zm4.657-1.348c0 .805-.301 1.431-1.106 1.431-.443 0-.812-.162-1.149-.583v.5h-1.221v-8.82h1.221v2.84c.273-.333.644-.608 1.076-.608.886 0 1.18.749 1.18 1.631v3.609zm4.471-1.752h-2.314v1.228c0 .488.042.91.528.91.511 0 .541-.344.541-.91v-.452h1.245v.489c0 1.253-.538 2.013-1.813 2.013-1.155 0-1.746-.842-1.746-2.013v-2.921c0-1.129.746-1.914 1.837-1.914 1.161 0 1.721.738 1.721 1.914v1.656z"
                "comsysto" "M22 2v20h-20v-20h20zm2-2h-24v24h24v-24zm-4 7h-8v1h8v-1zm0 5h-8v1h8v-1zm0 5h-8v1h8v-1zm-10.516-11.304l-.71-.696-2.553 2.607-1.539-1.452-.698.71 2.25 2.135 3.25-3.304zm0 5l-.71-.696-2.552 2.607-1.539-1.452-.698.709 2.249 2.136 3.25-3.304zm0 5l-.71-.696-2.552 2.607-1.539-1.452-.698.709 2.249 2.136 3.25-3.304z"
                "external-blog" "M21 13v10h-21v-19h12v2h-10v15h17v-8h2zm3-12h-10.988l4.035 4-6.977 7.07 2.828 2.828 6.977-7.07 4.125 4.172v-11z"
                "M12 2c5.514 0 10 4.486 10 10s-4.486 10-10 10-10-4.486-10-10 4.486-10 10-10zm0-2c-6.627 0-12 5.373-12 12s5.373 12 12 12 12-5.373 12-12-5.373-12-12-12z")}]])

(defn- nav-link
  [{:keys [title link active]}]
  [:li.mb-0.px-4.py-2.rounded {:class (when (= active title) "bg-gray-700")}
   [:a.text-white.border-0 {:href link} title]])

(defn wave
  ([]
   (wave {}))
  ([{:keys [class fill]}]
   [:svg.w-screen
    {:preserveAspectRatio "none" :width "1440" :height "32" :viewbox "0 0 1440 32" :xmlns "http://www.w3.org/2000/svg"
     :class (str/join " " (if fill [class fill] [class "fill-gray-200"]))}
    [:path
     {:d "M0 0.000244141C0 0.000244141 133 15.5005 178.5 15.5002C224 15.5 315 0.000244141 360 0.000244141C405 0.000244141 496 15.5002 543 15.5002C590 15.5002 669.5 0.000244141 720 0.000244141C770.5 0.000244141 848 15.5002 904 15.5002C960 15.5002 1031.5 0.000244141 1080 0.000244141C1128.5 0.000244141 1196.5 15.5002 1254.5 15.5002C1312.5 15.5002 1440 0.000244141 1440 0.000244141V32.0002H0V0.000244141Z"}]]))

(defn- header [{:keys [nav-links translation-link locale-icon]}]
  [:header.w-full.lg:px-0.absolute.z-30
   [:div.bg-gray-500.pt-3.px-6.w-full
    [:div.max-w-5xl.flex.justify-between.mx-auto.items-center.
     [:h1.mb-0
      [:a.text-white.uppercase.tracking-widest.text-lg.border-0.font-normal
       {:href "/"}
       "Thomas Sojka"]]
     [:nav.flex
      [:ul.list-none.items-center.hidden.md:flex
       nav-links]

      [:div.px-4.my-2.md:border-l-2.md:border-gray-300
       [:a {:href translation-link}
        [:img.w-7.rounded-full.h-7.object-cover.contrast-50.object-left
         {:src locale-icon :alt "Country Flag" }]]]]]]
   [:div.overflow-hidden.max-w-full.-translate-y-px
    (wave {:fill "fill-gray-500"
           :class "rotate-180"})]])

(defn- mobile-nav [{:keys [nav-links]}]
  [:nav.md:hidden.fixed.bottom-0.bg-gray-500.w-full.py-4.border-t.z-20
   [:ul.flex.gap-x-6.list-none.justify-center
    nav-links]])

(defn content [& children]
  [:section.flex.flex-col.items-center.py-8.flex-1.px-6.lg:px-0
   {:class "mt-[68px]"}
   children])

(defn- footer []
  [:footer.bg-gray-500.flex.justify-center.gap-x-6.pt-3.pb-20.md:pb-3
   [:a.text-white.border-0 {:href "https://mobile.twitter.com/rollacaster"} "Twitter"]
   [:a.text-white.border-0 {:href "https://github.com/rollacaster"} "GitHub"]
   [:a.text-white.border-0 {:href "https://www.youtube.com/channel/UCBSMA2iotgxbWPSLTFeUt9g?view_as=subscriber"} "YouTube"]])

(defn page [{:keys [title language author main nav-links description scripts
                    canonical-url alternate-url]}]
  (let [nav-link-lis (map (fn [nav] (nav-link {:active title :title (:title nav) :link (:link nav)})) nav-links)]
    (hiccup.page/html5
     {:lang language}
     (into
      [:head
       [:meta {:charset "utf-8"}]
       [:meta {:content "width=device-width, initial-scale=1" :name "viewport"}]
       [:title constants/title]
       [:meta {:content author :name "author"}]
       [:meta {:content description :name "description"}]
       [:meta {:content "programming emacs clojure javascript blog tech" :name "keywords"}]
       [:link {:href "css/styles.css" :rel "stylesheet" :type "text/css"}]
       [:link {:href "css/glow.css" :rel "stylesheet" :type "text/css"}]
       [:link {:href "css/blog.css" :rel "stylesheet" :type "text/css"}]
       [:link {:href "favicon.ico" :rel "icon" :type "image/x-icon"}]
       canonical-url]
      alternate-url)
     (into [:body.flex.flex-col.h-screen.overflow-y-scroll.bg-gradient-to-r.from-gray-100.to-gray-300
            (header {:active title
                     :nav-links nav-link-lis
                     :translation-link (if (= @i18n/locale :en) "/de/index.html" "/index.html")
                     :locale-icon (if (= @i18n/locale :en) "/images/ui/flag_de.svg" "/images/ui/flag_us.svg")})
            main
            (mobile-nav {:active title :nav-links nav-link-lis})
            (footer)]
           scripts))))
