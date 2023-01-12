(ns deshch.config
  (:refer-clojure :exclude [get])
  #?(:clj
     (:require [aero.core :as aero]
               [clojure.java.io :as io])
     :cljs
     (:require [aero.core :as aero]
               [shadow-env.core :as env])))

#?(:clj
   (defn read-env [_]
     (let [config (aero/read-config (io/resource "config-priv.edn"))]
       {:common config
        :clj    config
        :cljs   config})))

(env/link get `read-env)

(def debug?
  ^boolean goog.DEBUG)

(def api-key
  (get :geocoding-api-key))

(def geocoding-api
  "http://api.openweathermap.org/geo/1.0/direct")

(def limit 1)
