(ns app.handlers
  (:require [re-frame.core :as rf]
            [app.db :as db]
            [app.fx :as fx]))

(def load-app-state (rf/inject-cofx :store/app-state "manifold-experiment/app-state"))
(def store-app-state (fx/store-app-state "manifold-experiment/app-state"))


(rf/reg-event-fx :app/init-db
                 [load-app-state]
                 (fn [{:store/keys [app-state]} [_ default-db]]
                   {:db (update default-db :app-state into app-state)}))

(rf/reg-event-db :coordinates/update-coordinates
                 [store-app-state]
                 (fn [db [_ i new-coordinate-value]]
                   (if (<= (abs new-coordinate-value) 15)
                     (assoc-in db [:app-state :coordinates i] new-coordinate-value)
                     db)))

(rf/reg-event-db :coordinates/reset-app-db
                 [store-app-state]
                 (fn [db _]
                   db/default-db))