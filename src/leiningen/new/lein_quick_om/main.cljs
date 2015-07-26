(ns {{name}}.main
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [{{name}}.devbar :as db]
            [{{name}}.state-viewer :as sv]
            [{{name}}.example-components :as ex]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [<! >! put! chan]]))

(def app-name "{{name}}")

;control atoms for dev bar and state viewer
(defonce dev-mode (atom true))
(defonce state-viewer (atom false))

(def om-link
  (html [:a {:href "https://github.com/omcljs/om"} "Om"]))

(def intro-text
  (html
    [:p "This is an example application using "
     om-link
     ". You can modify this example by editing "
     [:code (str "src/" app-name "/core.cljs")]
     "."]))

(def init-state 
  {:example-data (mapv (fn [n] {:value n}) (range 1 5))})

(defonce state (atom (init-state)))


(def devbar-opts
  {:buttons [[#(reset! state (init-state)) "reset state"]
             [#(db/toggle-dev dev-mode state) "toggle devmode"]
             [#(sv/toggle-state-view state-viewer state) "toggle state viewer"]]}) 

(reset! db/devbar-opts devbar-opts) 

;force conditional code to be called on each reload
;TODO: replace this with a function that figwheel can call on refresh
(db/cond-render-dev-bar dev-mode state)
(sv/cond-render-state-view state-viewer state)


;start an Om render loop using the example master component
(om/root ex/master-component state {:target (gdom/getElement "app")
                                    :shared {:app-header (str "Welcome to " app-name)
                                             :intro-text intro-text}})
