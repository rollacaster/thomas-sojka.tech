(ns tech-thomas-sojka.app
  (:require [three]
            [react]
            [reagent.core :as r]
            [reagent.dom :as dom]
            ["react-three-fiber" :as raf]))

(defn box [props]
  (let [mesh (react/useRef)]
    (raf/useFrame (fn [state]
                    (set! mesh.current.material.opacity (.mapLinear (.-MathUtils three)
                                                                    (.getElapsedTime (.-clock state))
                                                                    0 2 0
                                                                    1))
                    (set! mesh.current.rotation.x (+ mesh.current.rotation.x 0.007))
                    (set! mesh.current.rotation.y (+ mesh.current.rotation.y 0.007))))
    (r/as-element
     [:mesh
      {:ref mesh
       :scale [1 1 1]
       :position (.-position props)}
      [:boxBufferGeometry {:attach "geometry" :args [1 1 1]}]
      [:meshStandardMaterial {:attach "material" :color "#4a5568" :opacity 0
                              :transparent true}]])))

(defn boxes []
  (r/as-element
   (for [x (range (- 30) 33 2.5)
         y (range 13.5 -18 -2.5)]
     [:<> {:key (str x y)}
      [:> box {:position #js [x y 0]}]])))

(defn app []
  [:div {:style {:width "100vw" :height "100vh"}}
   [:> raf/Canvas {:orthographic true :camera #js { :zoom 40 }}
    [:ambientLight]
    [:pointLight {:position [10 10 10]}]
    [:> boxes]]])

(dom/render [app] (js/document.getElementById "app"))
