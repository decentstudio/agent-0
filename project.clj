(defproject agent-0 "0.1.0-SNAPSHOT"
  :description "Order book agent"
  :url ""
  :license {}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [bookie "0.0.1-SNAPSHOT"]]
  :main ^:skip-aot agent-0.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:aot :all
                   :plugins [[lein-gorilla "0.4.0"]]
                   :dependencies []}})
