(ns deshch.views
  (:require
   [clojure.string :as str]
   [goog.string :as gstring]
   [reagent.core :as reagent]
   [re-frame.core :refer [dispatch subscribe]]
   [deshch.events :as events]
   [deshch.subs :as subs]
   ))

(defn city-input [{:keys [title on-save on-stop]}]
  (let [val (reagent/atom title)
        stop #(do (reset! val "")
                  (when on-stop (on-stop)))
        save #(let [v (-> @val str str/trim)]
                 (on-save v)
                 (stop))]
    (fn [props]
      [:input (merge (dissoc props :on-save :on-stop :title)
                     {:type "text"
                      :value @val
                      :auto-focus true
                      :on-blur save
                      :on-change #(reset! val (-> % .-target .-value))
                      :on-key-down #(case (.-which %)
                                      13 (save)
                                      27 (stop)
                                      nil)})])))

(defn city-entry []
  [city-input
   {:id "search-city"
    :placeholder "city name"
    :on-save #(when (seq %)
                (dispatch [::events/fetch-city %]))}])

(defn city-view [{:keys [name lat lon country state]}]
  [:<>
   [:div
    [:p "Name: " name]
    [:p "(lat/lon): " (gstring/format "(%f/%f)" lat lon)]
    [:p "Country: " country]
    [:p "State: " state]]])

(defn main-panel []
  (let [loading (subscribe [::subs/loading])
        city-data (subscribe [::subs/city-data])]
    [:div
     [:h1
      "Hello from " (if @city-data
                      (:name @city-data)
                      "nowhere")]
     [city-entry]
     [:div
      (cond
        @loading "Downloading data..."
        @city-data [city-view @city-data]
        :else nil)]]))
