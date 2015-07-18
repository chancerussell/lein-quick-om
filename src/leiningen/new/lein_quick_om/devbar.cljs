(ns {{name}}.devbar
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [om-bootstrap.button :as b]))

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
  (let [new-el (.createElement js/document "div")]
    (.setAttribute new-el "id" container-id) 
    (.setAttribute new-el "class" "container") 
    new-el))

(def bar-attrs
  {:style {:width "100%"
           :borderStyle "solid"
           :borderWidth "1px"
           :borderTop "none"
           ;:heig;t "50pt"
           }
   :className "row"})

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
  (b/button {:bs-style "primary"
             :bs-size "xsmall"
             :onClick f} 
            label)
  )

(defn button-section
  [button-pairs]
  (apply b/toolbar nil 
         (mapv dev-button button-pairs) 
         ))

(defn app-title
  [title]
  (dom/h3 #js {:style #js {
                           :marginTop 0
                           :marginBottom 0}}
(str "dev: " "“" title "”")))


(defn dev-bar
  [button-pairs]
  (fn [state data]
    (reify
      om/IRender
      (render [_]
        (dom/div (clj->js bar-attrs) 
                 (dom/div #js {:className "col-xs-3"}
                         (app-title "{{name}}"))
                 (dom/div #js {:className "col-xs-5"} nil)
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
