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
             ["src/{{sanitized}}/foo.clj" (render "foo.clj" data)])))
