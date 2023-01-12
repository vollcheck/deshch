(ns deshch.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [deshch.events :as events]
   [deshch.views :as views]
   [deshch.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (prn config/api-key)
  (dev-setup)
  (mount-root))
