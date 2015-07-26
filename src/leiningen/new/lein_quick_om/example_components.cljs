(ns {{name}}.example-components
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [{{name}}.ui :as ui]
            [om-bootstrap.button :as b]))

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
                        (ui/small-button #(om/transact! data :value dec) "-")  
                        (ui/small-button #(om/transact! data :value inc) "+")))))

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
      (let [{:keys [app-header intro-text]} (om/get-shared owner)]
        (dom/div nil 
                 (ui/jumbotron app-header intro-text)
                 (om/build main-component data))))))
