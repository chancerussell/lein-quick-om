(ns leiningen.new.lein-quick-om
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "lein-quick-om"))

(defn lein-quick-om
  "FIXME: write documentation"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' lein-quick-om project.")
    (->files data
             ["src/{{sanitized}}/main.cljs" (render "main.cljs" data)]
             ["src/{{sanitized}}/devbar.cljs" (render "devbar.cljs" data)]
             ["src/{{sanitized}}/state_viewer.cljs" (render "state_viewer.cljs" data)]
             ["src/{{sanitized}}/ui.cljs" (render "ui.cljs" data)]
             ["src/{{sanitized}}/example_components.cljs" (render "example_components.cljs" data)]
             ["project.clj" (render "project.clj" data)]
             ["Dockerfile" (render "Dockerfile" data)]
             ["Makefile" (render "Makefile" data)]
             [".dockerignore" (render ".dockerignore" data)]
             ["resources/public/index.html" (render "index.html" data)]
             ["resources/public/css/style.css" (render "style.css" data)]
             )))
