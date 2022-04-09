(ns tech.thomas-sojka.app
  (:require ["@react-three/fiber" :as r3f]
            ["d3-hierarchy" :as hierarchy]
            ["d3-scale" :as scale]
            ["d3-shape" :as shape]
            ["react" :as react]
            ["tailwindcss/resolveConfig" :as resolveConfig]
            ["three" :as three]
            [reagent.dom :as dom]))

(def grays
  (->(resolveConfig)
     (js->clj :keywordize-keys true)
     :theme
     :backgroundColor
     :gray))

(def screens
  (->(resolveConfig)
     (js->clj :keywordize-keys true)
     :theme
     :screens))


(def line-curve
  (new three/CatmullRomCurve3
       #js[(new three/Vector3 0.5 0.5 0)
           (new three/Vector3 1 2 0)
           (new three/Vector3 1.5 2.5 0)
           (new three/Vector3 2 1 0)
           (new three/Vector3 2.5 1.5 0)
           (new three/Vector3 3 1 0)
           (new three/Vector3 3.5 1 0)]))

(defn w [x]
  (let [three (r3f/useThree)
        width (.-viewport.width three)
        s (-> (scale/scaleLinear)
              (.domain #js [0 1])
              (.range #js [(- width) width]))]
    (s x)))


(defn h [x]
  (let [three (r3f/useThree)
        height (.-viewport.height three)
        s (-> (scale/scaleLinear)
              (.domain #js [0 1])
              (.range #js [height (- height)]))]
    (s x)))

(defn line-chart []
  [:mesh {:position [(w 0.1) (h 0.3) 0]}
   [:tubeGeometry {:args [line-curve 30 0.2 20]}]
   [:meshStandardMaterial {:color (:800 grays)}]])

(defn bar [{:keys [x y height color]}]
  [:mesh {:position [x y 0]}
   [:boxGeometry {:args [0.5 height 1]}]
   [:meshStandardMaterial {:color color}]])

(defn bar-chart []
  [:group {:position [(w 0.6) (h 0.35) 0]}
   (map-indexed
    (fn [idx d]
      ^{:key idx}
      [bar {:x idx :y (/ idx 4) :height d
            :color (nth (vals grays) idx)}])
    [0.5 1 1.5 2 2.5])])

(def pie-data [1 1 2 3 6])
(def arcs (js->clj ((shape/pie) pie-data)
                   :keywordize-keys true))

(defn arc [{:keys [start-angle end-angle color]}]
  [:mesh {:position [-3 -2 0] :rotation [(/ js/Math.PI 2) 0 0]}
   [:cylinderGeometry {:args [1.5 1.5 1 8 1 false start-angle (- end-angle start-angle)]}]
   [:meshStandardMaterial {:color color}]])

(defn pie-chart []
  [:group {:position [(w 0.35) (h 0.6) 0]}
   (map-indexed
    (fn [idx {:keys [startAngle endAngle]}]
      ^{:key idx}
      [arc {:start-angle startAngle
            :end-angle endAngle
            :color (nth (vals grays) idx)}])
    arcs)])

(def tree-data
  ((-> (hierarchy/tree)
       (.size #js [4 2]))
   (hierarchy/hierarchy
    (clj->js
     {:name "Animal",
      :children
      [{:name "Cat"}
       {:name "Dog",
        :children
        [{:name "Shepherd"}
         {:name "Bulldog"}]}
       {:name "Tiger"}
       {:name "Fish",
        :children [{:name "Koi"}]}
       {:name "Elephant"}]}))))

(defn link [{:keys [source target color]}]
  (let [curve (new three/LineCurve3
                   (new three/Vector3 (.-x source) (- (.-y source)) 0)
                   (new three/Vector3 (.-x target) (- (.-y target)) 0))]
    [:mesh
     [:tubeGeometry {:args [curve 30 0.1 20]}]
     [:meshStandardMaterial {:color color}]]))

(defn node [{:keys [x y color]}]
  [:mesh {:position [x y 0]}
   [:sphereGeometry {:args [0.2]}]
   [:meshStandardMaterial {:color color}]])

(defn tree-chart []
  [:group {:position [(w 0.7) (h 0.7) 0]}
   (map-indexed
    (fn [idx d]
      ^{:key idx}
      [node {:x (.-x d) :y (- (.-y d))
             :color (nth (vals grays) idx)}])
    ^js (.descendants tree-data))
   (map-indexed
    (fn [idx d]
      ^{:key idx}
      [link {:source (.-source d) :target (.-target d)
             :color (nth (vals grays) idx)}])
    ^js (.links tree-data))])

(defn floating [children]
  (let [ref (react/useRef)
        [offset] (react/useState (* (js/Math.random) 2))]
    (r3f/useFrame
     (fn [prop]
       (let [et (+ ^js (.getElapsedTime (.-clock prop)) offset)]
         (when (.-current ref)
           (set! (.-current.position.y ref) (Math/sin (/ et 2)))
           (set! (.-current.rotation.x ref) (/ (Math/sin (/ et 3)) 10))
           (set! (.-current.rotation.y ref) (/ (Math/cos (/ et 2)) 10))
           (set! (.-current.rotation.z ref) (/ (Math/sin (/ et 3)) 10))))))
    [:group {:ref ref}
     children]))

(defn use-screen-width []
  (let [[width set-width] (react/useState js/window.innerWidth)]
    (react/useEffect
     (fn []
       (let [update-width (fn [] (set-width js/window.innerWidth))]
         (js/window.addEventListener "resize"
                 update-width #js {:passive true})
         (fn []
           (js/window.removeEventListener
            "resize"
            update-width
            #js {:passive true})))))
    width))

(defn canvas []
  (let [width (use-screen-width)]
    [:> r3f/Canvas
     (when (> width (js/parseInt (:md screens)))
       [:<>
        [:group {:scale 0.5}
         [:f> floating
          [:f> line-chart]]
         [:f> floating
          [:f> bar-chart]]
         [:f> floating
          [:f> pie-chart]]
         [:f> floating
          [:f> tree-chart]]]
        [:ambientLight]
        [:pointLight {:position [10 10 10]}]])]))

(defn main []
  [:div.absolute.h-screen.w-full.z-10
   [:f> canvas]])

(dom/render
 [main]
 (js/document.getElementById "main"))

