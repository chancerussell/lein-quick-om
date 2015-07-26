(ns {{name}}.ui
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [sablono.core :as html :refer-macros [html]]
            [om-bootstrap.button :as b]))

(defn small-button
  [f label]
  (b/button {:bs-style "primary"
             :bs-size "xsmall"
             :onClick f
             :style #js {:width "15pt" 
                         :marginRight "5px"
                         :marginLeft "5px"}}
            label))

(defn jumbotron
  [header message]
  (html [:div.jumbotron
         [:div.container
          [:h1 header]
          [:p message]]]))
