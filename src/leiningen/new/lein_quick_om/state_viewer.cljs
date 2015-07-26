(ns {{name}}.state-viewer
  (:require [om.core :as om]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [ankha.core :as ankha]
            [om-bootstrap.button :as b]))

(defonce container-id
  (str "{{name}}-state-viewer" (.getTime (js/Date.))))

(defonce popup-window-handle (atom nil))

(defn get-popup-window
  []
  (if (and @popup-window-handle
           (.-document @popup-window-handle)) ;document prop is nulled out when window is closed
    @popup-window-handle
    (let [new-window (.open js/window)]
      (reset! popup-window-handle new-window)
      new-window)))


(defn new-container-el
  "get a new container element"
  []
  (let [new-el (.createElement js/document "div")]
    (.setAttribute new-el "id" container-id) 
    (.setAttribute new-el "class" "container") 
    new-el))

(defn get-popup-div
  "gets a container div at the top of the body, creating it if necessary."
  []
  (let [window (get-popup-window) 
        document (.-document window)
        body (.-body document)
        el (or (.getElementById document container-id) (new-container-el)) 
        first-child (.-firstElementChild body)]
    (if (not= el (.-firstElementChild body))
      (.insertBefore body el first-child))
    el))

(defn open-state-viewer
  [state opts]
  (let [target (or (:target opts)
                   (get-popup-div))]
  (om/root ankha/inspector state {:target target})))

(defn close-state-viewer
  []
  (if (and @popup-window-handle
           (.-document @popup-window-handle))
    (.close @popup-window-handle))
  (reset! popup-window-handle nil))

(defn cond-render-state-view
  [bool-atom state]
  (if @bool-atom
    (open-state-viewer state {})
    (close-state-viewer))) 

(defn toggle-state-view [bool-atom state]
  (swap! bool-atom not)
  (cond-render-state-view bool-atom state))
