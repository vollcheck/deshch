(ns deshch.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [deshch.db :as db]
   [deshch.config :as config]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-fx
 ::fetch-city
 (fn [{:keys [db]} [_ val]]
   (prn val) ;; TODO: remove
   {:db (assoc db :loading true)
    :http-xhrio {:method :get
                 :uri config/geocoding-api
                 :params {"q" val
                          "limit" config/limit
                          "appid" config/api-key}
                 :timeout 8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::fetch-city-success]
                 :on-failure [:bad-http-result]}}))

(re-frame/reg-event-db
 ::fetch-city-success
 (fn [db [_ data]]
   (let [record (dissoc (first data) :local_names)]
     ;; (tap> record)
     (prn record)
     (assoc db
            :loading false
            :city-data record))))
