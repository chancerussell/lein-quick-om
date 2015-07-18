(ns {{name}}.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [cljs.core.async :refer [<! >! put! chan]]))

(defonce state (atom {:example-data (vec (range 1 5))}))

(defn button
  [on-click-fn text]
  (dom/input #js {:type "button" 
                  :onClick #(on-click-fn) 
                  :value text}))

(defn child-component
  [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (str "example component " data)))))

(defn master-component
  [data owner]
  (reify
    om/IRender
    (render [_]
      (apply dom/div nil 
             (dom/h1 nil "Hello from {{name}}...")
             (om/build-all child-component (:example-data data))))))

(om/root master-component state {:target (gdom/getElement "app")})
