(defproject {{sanitized}} "0.1.0-SNAPSHOT"
  :description "FIXME: project description"
  :url "http://need.realurl.net"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-3308"] 
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"] 
                 [org.omcljs/om "0.9.0"] 
                 [racehub/om-bootstrap "0.5.0"]
                 [cljs-http "0.1.35"]
                 [sablono "0.3.4"]
                 [ankha "0.1.4"]]
  :local-repo "local-m2"
  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.7"]]
  :source-paths ["src"]
  :clean-targets ^ {:protect false} ["resources/public/js/compiled"]
  :cljsbuild {
              :builds [
                       {:id "dev"
                        :source-paths ["src"]
                        :figwheel {
                                   ;uncomment/update to serve figwheel to other machines on network
                                   ;:websocket-host "192.168.1.4"
                                   }
                        :compiler {
                                   :main {{name}}.main
                                   :asset-path "js/compiled/out"
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :source-map-timestamp true
                                   :optimizations :none
                                   :source-map true}}]}
  )
