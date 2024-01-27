(ns tech.thomas-sojka.app
  (:require ["@react-three/drei" :as drei]
            ["@react-three/fiber" :as r3f]
            ["d3-hierarchy" :as hierarchy]
            ["d3-scale" :as scale]
            ["d3-shape" :as shape]
            ["mobile-detect" :as md]
            ["react" :as react]
            ["tailwindcss/resolveConfig" :as resolveConfig]
            ["three" :as three]
            ["three-stdlib" :as three-stdlib]
            [reagent.dom :as dom]))

(r3f/extend #js {"UnrealBloomPass" three-stdlib/UnrealBloomPass
                 "FilmPass" three-stdlib/FilmPass
                 "LUTPass" three-stdlib/LUTPass})
(let [config (resolveConfig #js{})]
  (def grays
    (-> config
        (js->clj :keywordize-keys true)
        :theme
        :backgroundColor
        :gray))

  (def screens
    (-> config
        (js->clj :keywordize-keys true)
        :theme
        :screens)))

(defn v [v & {:keys [] :as breakpoints}]
  (let [three (r3f/useThree)
        screen-width (.-size.width three)
        screens-ordered (->> screens
                             (map (fn [[breakpoint breakpoint-width]]
                                    [breakpoint (js/parseFloat breakpoint-width)]))
                             (sort-by second >))
        screens-order (mapv first screens-ordered)
        screen-size (or (some (fn [[breakpoint breakpoint-width]]
                                (when (> screen-width (js/parseFloat breakpoint-width))
                                  breakpoint))
                              screens-ordered)
                        :sm)]
    (or (some
         (fn [breakpoint] (get breakpoints breakpoint))
         (subvec
          screens-order
          (.indexOf screens-order screen-size)))
        v)))

(defn use-fade-in-material []
  (let [ref (react/useRef)]
    (r3f/useFrame
     (fn [prop]
       (let [duration 2
             et ^js (.getElapsedTime ^js (.-clock prop))
             opacity (-> (scale/scaleLinear)
                         (.domain #js [0 duration])
                         (.range #js [0 1]))]
         (when (and (.-current ref) (<= et duration))
           (set! (.-current.transparent ref) true)
           (set! (.-current.opacity ref) (opacity et))))))
    ref))

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
        mobile? (.mobile (new md js/window.navigator.userAgent))
        s (-> (scale/scaleLinear)
              (.domain #js [0 1])
              (.range #js [(* (if mobile? (.-viewport.dpr three) 1) (- width))
                           (*  (if mobile? (.-viewport.dpr three) 1) 1 width)]))]
    (s x)))

(defn r [v]
  (* (* 2 js/Math.PI) v))

(defn h [x]
  (let [three (r3f/useThree)
        height (.-viewport.height three)
        mobile? (.mobile (new md js/window.navigator.userAgent))
        s (-> (scale/scaleLinear)
              (.domain #js [0 1])
              (.range #js [(* (if mobile? (.-viewport.dpr three) 1) height)
                           (* (if mobile? (.-viewport.dpr three) 1) (- height))]))]
    (s x)))

(defn use-center-pos []
  (let [ref (react/useRef)
        three (r3f/useThree)
        screen-width (.-size.width three)
        [[x y] set-pos] (react/useState [0 0])]
    (react/useEffect
     (fn []
       (let [bbox (.setFromObject (new three/Box3) (.-current ref))
             width (- (.-max.x bbox) (.-min.x bbox))
             height (- (.-max.y bbox) (.-min.y bbox))]
         (set-pos  [(- (/ width 2))
                    (- (/ height 2))]))
       identity)
     #js [screen-width set-pos])
    {:ref ref :dx x :dy y}))

(defn box* [{[x y] :position
             :keys [center scale rotation]
             :or {scale 1 rotation [0 0 0]}}
            children]
  (let [{:keys [ref dx dy]} (use-center-pos)]
    [:group {:position [(+ x (if center dx 0))
                        (+ y (if center dy 0))
                        0]
             :rotation rotation
             :scale scale
             :ref ref}
     children]))

(defn box [props children]
  [:f> box* props children])

(defn line-chart []
  (let [ref (use-fade-in-material)]
    [box {:center true
          :position [(w (v 0.44 :md 0.35))
                     (h (v 0.415 :md 0.44 :lg 0.4))
                     0]
          :rotation [(r 0.1) (r 0.05) (r 0)]
          :scale (v 0.38 :md 0.5)}
     [:mesh
      [:tubeGeometry {:args [line-curve 30 0.2 20]}]
      [:meshStandardMaterial {:color (:800 grays) :ref ref}]]]))

(defn bar [{:keys [x y height color]}]
  (let [ref (use-fade-in-material)]
    [:mesh {:position [x y 0]}
     [:boxGeometry {:args [0.5 height 1]}]
     [:meshStandardMaterial {:color color :ref ref}]]))

(defn bar-chart []
  [box {:center true
        :position [(w (v 0.56 :md 0.61))
                   (h (v 0.408 :md 0.42 :lg 0.35))
                   0]
        :rotation [(v (r 0.11) :md (r 0.1))
                   (v (r -0.1) :md (r -0.1))
                   (v (r 0.05) :md (r 0))]
        :scale (v 0.25 :md 0.4)}
   (map-indexed
    (fn [idx d]
      ^{:key idx}
      [:f> bar {:x idx :y (/ idx 4) :height d
                :color (nth (vals grays) idx)}])
    [0.5 1 1.5 2 2.5])])

(def pie-data [1 1 2 3 6])
(def arcs (js->clj ((shape/pie) pie-data)
                   :keywordize-keys true))

(defn arc [{:keys [start-angle end-angle color]}]
  (let [ref (use-fade-in-material)]
    [:mesh {:rotation [(/ js/Math.PI 2) 0 0]}
     [:cylinderGeometry {:args [1.5 1.5 1 8 1 false start-angle (- end-angle start-angle)]}]
     [:meshStandardMaterial {:color color :ref ref}]]))

(defn pie-chart []
  [box {:position [(w (v 0.43 :md 0.36))
                   (h (v 0.54 :md 0.55 :lg 0.62))
                   0]
        :scale (v 0.33 :md 0.5)
        :rotation [(r (v -0.1 :md -0.1 :lg -0.2))
                   (r (v 0.1 :md 0.1 :lg 0.2))
                   0]}
   (map-indexed
    (fn [idx {:keys [startAngle endAngle]}]
      ^{:key idx}
      [:f> arc {:start-angle startAngle
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
                   (new three/Vector3 (.-x target) (- (.-y target)) 0))
        ref (use-fade-in-material)]
    [:mesh
     [:tubeGeometry {:args [curve 30 0.1 20]}]
     [:meshStandardMaterial {:color color :ref ref}]]))

(defn node [{:keys [x y color]}]
  (let [ref (use-fade-in-material)]
    [:mesh {:position [x y 0]}
     [:sphereGeometry {:args [0.2]}]
     [:meshStandardMaterial {:color color :ref ref}]]))

(defn tree-chart []
  [box {:position [(w (v 0.55 :md 0.6))
                   (h (v 0.512 :md 0.515 :lg 0.55))
                   0]
        :center true
        :scale (v 0.325 :md 0.45)
        :rotation [(r (v -0.1 :md -0.1 :lg -0.1))
                   (r (v -0.1 :md -0.1 :lg -0.2))
                   0]}
   [:<>
    (map-indexed
     (fn [idx d]
       ^{:key idx}
       [:f> node {:x (.-x d) :y (- (.-y d))
                  :color (nth (vals grays) idx)}])
     ^js (.descendants tree-data))
    (map-indexed
     (fn [idx d]
       ^{:key idx}
       [:f> link {:source (.-source d) :target (.-target d)
                  :color (nth (vals grays) idx)}])
     ^js (.links tree-data))]])

(defn floating [children]
  (let [ref (react/useRef)
        [offset] (react/useState (* (js/Math.random) 2))]
    (r3f/useFrame
     (fn [prop]
       (let [et (+ ^js (.getElapsedTime ^js (.-clock prop)) offset)]
         (when (.-current ref)
           (set! (.-current.position.y ref) (/ (Math/sin et) 4))
           (set! (.-current.rotation.x ref) (/ (Math/sin (/ et 3)) 10))
           (set! (.-current.rotation.y ref) (/ (Math/cos (/ et 2)) 10))
           (set! (.-current.rotation.z ref) (/ (Math/sin (/ et 3)) 20))))))
    [:group {:ref ref}
     children]))

(defn lights []
  (let [pointlight-ref (react/useRef)
        spotlight-ref (react/useRef)]
    (when false
      (drei/useHelper pointlight-ref three/PointLightHelper 1 "red")
      (drei/useHelper spotlight-ref three/SpotLightHelper "red"))
    [:<>
     [:ambientLight {:intensity 1}]
     [:pointLight {:ref pointlight-ref :intensity 30 :distance 60}]]))

  (defn canvas []
    [:> r3f/Canvas
     [:<>
      [:group
       [:f> floating
        [:f> line-chart]]
       [:f> floating
        [:f> bar-chart]]
       [:f> floating
        [:f> pie-chart]]
       [:f> floating
        [:f> tree-chart]]]
      [:f> lights]
      [:> drei/OrbitControls]
      [:> drei/Effects #_{:disableGamma true}
       #_[:unrealBloomPass {:strength 0.2
                            :kernelSize 25
                            :sigma 1500}]
         #_[:filmPass]
         #_[:lUTPass]]]])

  (defn main []
    [:div.absolute.h-screen.w-full.z-10.pointer-events-none
     [:f> canvas]])

  (dom/render
   [main]
   (js/document.getElementById "main"))
