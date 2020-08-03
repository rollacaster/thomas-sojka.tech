(ns tech-thomas-sojka.app
  (:require [three]
            [react]
            [reagent.core :as r]
            [reagent.dom :as dom]
            ["react-three-fiber" :as raf]))

(defn box [props]
  (let [mesh (react/useRef)
        [hovered set-hover] (react/useState false)
        [active set-active] (react/useState false)]
    (raf/useFrame (fn []
                    (set! mesh.current.rotation.x (+ mesh.current.rotation.x 0.007))
                    (set! mesh.current.rotation.y (+ mesh.current.rotation.y 0.007))))
    (r/as-element
     [:mesh
      {:ref mesh :on-click (fn [] (set-active (not active)))
       :scale (if active [1.5 1.5 1.5] [1 1 1])
       :position (.-position props)
       :on-pointer-over (fn [] (set-hover true))
       :on-pointer-out (fn [] (set-hover false))}
      [:boxBufferGeometry {:attach "geometry" :args [1 1 1]}]
      [:meshStandardMaterial {:attach "material" :color (if hovered "hotpink" "#4a5568")}]])))

(defn boxes []
  (r/as-element
   (for [x (range (- 30) 33 3)
         y (range 14 -18 -3)]
     [:> box {:key (str x y) :position #js [x y 0]}])))

(defn app []
  [:div {:style {:width "100vw" :height "100vh"}}
   [:> raf/Canvas {:orthographic true :camera #js { :zoom 40 }}
    [:ambientLight]
    [:pointLight {:position [10 10 10]}]
    [:> boxes]]])

(dom/render [app] (js/document.getElementById "app"))
