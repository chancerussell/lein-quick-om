(ns {{name}}.devbar
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [om-bootstrap.button :as b]))

;this isn't a very flexible way of doing things...
(defonce dev-mode (atom true))

(def devbar-opts (atom {}))

;generate a (hopefully) unique string for the body div id
(defonce container-id
  (str "{{name}}-devbar-" (.getTime (js/Date.))))

(defn button
  [on-click-fn text]
  (dom/input #js {:type "button" 
                  :onClick #(on-click-fn) 
                  :value text}))

(defn new-container-el
  "get a new container element"
  []
  (let [new-el (.createElement js/document "nav")]
    (.setAttribute new-el "id" container-id) 
    (.setAttribute new-el "class" "navbar navbar-default") 
    new-el))

(def bar-attrs
  {:className "container-fluid"})

(defn get-body-div
  "gets a container div at the top of the body, creating it if necessary."
  []
  (let [el (or (gdom/getElement container-id) (new-container-el))
        body (.-body js/document) 
        first-child (.-firstElementChild body)]
    (if (not= el (.-firstElementChild body))
      (.insertBefore body el first-child))
    el))

(defn dev-button
  [[f label]]
  (dom/li nil 
          (dom/a #js {:href "#"
                      :onClick f} 
                 label)))

(defn button-section
  [button-pairs]
  (apply dom/ul #js {:className "nav navbar-nav navbar-right"}  
         (mapv dev-button button-pairs))) 

(def app-title
  (dom/h3 #js {:style #js {:className "navbar-brand"}}
          (str "dev: “{{name}}”")))


(defn dev-bar
  [button-pairs]
  (fn [state data]
    (reify
      om/IRender
      (render [_]
        (dom/div (clj->js bar-attrs) 
                 (dom/div #js {:className "navbar-header"}
                          app-title)
                 (button-section button-pairs)
                 )))))

(defn add-dev-bar
  "renders a development toolbar onto the page.
  by default, the bar will be added as the first child of the page <body>" 
  ([state]
   (add-dev-bar state {})) 
  ([state opts]
   (let [target (or (:target opts)
                    (get-body-div))]
   (om/root (dev-bar (:buttons opts)) state {:target target}))))

(defn remove-dev-bar
  "pull the dev bar from the DOM"
  []
  (if-let [el (gdom/getElement container-id)]
    (.remove el)))

(defn cond-render-dev-bar
  [bool-atom state]
  (if @bool-atom
    (add-dev-bar state @devbar-opts)
    (remove-dev-bar))) 

(defn toggle-dev 
  "Toggles the \"devmode\" bar by flipping the atom's value and calling the cond-render-dev-bar."
  [bool-atom state]  
   (swap! bool-atom not)
   (cond-render-dev-bar bool-atom state))

