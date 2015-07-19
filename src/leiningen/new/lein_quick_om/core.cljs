(ns {{name}}.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [{{name}}.devbar :as db]
            [{{name}}.state-viewer :as sv]
            [om-bootstrap.button :as b]
            [cljs.core.async :refer [<! >! put! chan]]))

(defonce app-name "{{name}}")
(defonce intro-text
  ["This is an example application using "
   (dom/a #js {:href "https://github.com/omcljs/om"} "Om")
   ". You can modify this example by editing "
   (dom/code nil (str "src/" app-name "/core.cljs"))
   "."])

(def init-state 
  {:example-data (mapv (fn [n] {:value n}) (range 1 5))})

(defonce state (atom init-state))

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

;force conditional code to be called on each reload
;TODO: replace this with a function that figwheel can call on refresh
(cond-render-dev-bar)
(cond-render-state-view)

(defn small-button
  [f label]
  (b/button {:bs-style "primary"
             :bs-size "xsmall"
             :onClick f
             :style #js {:width "15pt" 
                         :marginRight "5px"
                         :marginLeft "5px"}}
            label))

(defn reset-state
  []
  (js/alert "okay")
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
      (dom/div #js {:className "row"
                    :style #js {:marginBottom "20pt"
                                :borderTop "1px solid"
                                :borderLeft "1px solid"}}
               (dom/div #js {:className "col-xs-4"}
                        (dom/b nil (str "example component " (:value data))))
                        (small-button #(om/transact! data :value dec) "-")  
                        (small-button #(om/transact! data :value inc) "+")))))

(def jumbotron
  (dom/div #js {:className "jumbotron"}
           (dom/div #js {:className "container"}
                    (dom/h1 nil (str "welcome to " app-name))
                    (dom/p nil intro-text))))

(defn main-component
  [data owner]
  (reify
    om/IRender
    (render [_]
    (dom/div #js {:className "container"}
             (dom/div #js {:className "col-md-3"} nil)
             (apply dom/div #js {:className "col-md-6"} 
                    (om/build-all child-component (:example-data data))) 
             (dom/div #js {:className "col-md-3"} nil)))))

(defn master-component
  [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil 
             jumbotron
             (om/build main-component data)))))

(om/root master-component state {:target (gdom/getElement "app")})
