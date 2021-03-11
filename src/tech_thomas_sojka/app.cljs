(ns tech-thomas-sojka.app
  (:require [three]
            [react]
            [reagent.core :as r]
            [reagent.dom :as dom]
            ["react-three-fiber" :as raf]
            ["three/examples/jsm/loaders/GLTFLoader" :as gltf-loader]))
(defonce donut (r/atom nil))
(.load (new (.-GLTFLoader gltf-loader))
       "/models/donut.glb"
       (fn [donut-gltf] (reset! donut donut-gltf))
       nil
       prn)
(defn box [props]
  (let [mesh (react/useRef)]
    (raf/useFrame (fn []
                    (set! mesh.current.rotation.x (+ mesh.current.rotation.x 0.007))
                    (set! mesh.current.rotation.y (+ mesh.current.rotation.y 0.007))))
    (when @donut
      (r/as-element
       [:primitive
        {:ref mesh
         :object (.clone (.-scene @donut) true)
         :scale [20 20 20]
         :position (.-position props)}]))))

(defn boxes []
  (r/as-element
   (for [x (range (- 30) 33 5)
         y (range 13.5 -18 -4)]
     [:<> {:key (str x y)}
      [:> box {:position #js [x y 0]}]])))

(defn app []
  [:div {:style {:width "100vw" :height "100vh"}}
   [:> raf/Canvas {:orthographic true :camera #js { :zoom 40 }}
    [:ambientLight]
    [:pointLight {:position [10 10 10]}]
    [:> boxes]]])

(dom/render [app] (js/document.getElementById "app"))
