(ns {{name}}.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [{{name}}.devbar :as db]
            [{{name}}.state-viewer :as sv]
            [cljs.core.async :refer [<! >! put! chan]]))

(defonce state (atom {:example-data (vec (range 1 5))}))

(declare reset-state cond-render-dev-bar cond-render-state-view)

(defonce dev-mode (atom true))
(defonce state-viewer (atom false))



(defn toggle-dev []  
   (swap! dev-mode not)
   (cond-render-dev-bar))

(defn toggle-state-view []
  (swap! state-viewer not)
  (cond-render-state-view))

(defn cond-render-state-view
  []
  (if @state-viewer
    (sv/open-state-viewer state {})
    (sv/close-state-viewer))) 

(defn cond-render-dev-bar
  []
  (if @dev-mode
    (db/add-dev-bar state
                    {:buttons [
                               [reset-state "reset state"]
                               [toggle-dev "toggle devmode"]
                               [toggle-state-view "toggle state viewer"]]})
    (db/remove-dev-bar))) 

(cond-render-dev-bar)
(cond-render-state-view)

(def init-state 
  {:example-data (vec (range 1 5))})



(defn reset-state
  []
  (reset! state init-state))

(defn add-inc
  [coll]
  (fn [o]
    (let [last-val(if (empty? o) 0
                    (last o))]
      (conj o (inc last-val)))))

(defn child-component
  [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (dom/span nil (str "example component " data))
))))

(defn master-component
  [data owner]
  (reify
    om/IRender
    (render [_]
      (apply dom/div nil 
             (dom/h1 nil "Hello from {{name}}...")
             (om/build-all child-component (:example-data data))))))

(om/root master-component state {:target (gdom/getElement "app")})
