(ns deshch.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::loading
 (fn [db]
   (:loading db)))

(re-frame/reg-sub
 ::city-data
 (fn [db]
   (:city-data db)))
