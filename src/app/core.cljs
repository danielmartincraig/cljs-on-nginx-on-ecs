(ns app.core
  (:require
   [uix.core :as uix :refer [defui $]]
   [uix.dom]
   [app.hooks :as hooks]
   [app.subs]
   [app.handlers]
   [app.fx]
   [app.db]
   [re-frame.core :as rf]
   [clojure.string :as str]
   [goog.string :as gs]
   [goog.string.format]
   [emmy.calculus.manifold :as manifold]
   [emmy.env :as emmy]))

(defui reset-displacements-button []
  ($ :div ($ :button
             {:on-click #(rf/dispatch [:coordinates/reset-app-db])}
             "Reset")))


(defui header []
  ($ :header.app-header
     ($ :div {:width 32}
        ($ :p {:style {:font-family "Montserrat" :font-size 48}} "Hello world"))))

(defui footer []
  ($ :footer.app-footer
     ($ :small "made by you!")))

(defui manifold-point-viewer []
  (let [manifold-point (hooks/use-subscribe [:app/manifold-point])]
    ($ :manifold-point-viewer 
       (str manifold-point))))

(defui coordinate-field [{:keys [on-edit i]}]
  (let [displacement (hooks/use-subscribe [:app/coordinate i])]
    ($ :div
       ($ :input
          {:value displacement
           :type :number
           :min 1
           :max 400
           :placeholder 0
           :style {:width "80%"}
           :on-change (fn [^js e]
                        (on-edit (int (.. e -target -value))))}))))

(defui app []
  (let [todos (hooks/use-subscribe [:app/todos])]
    ($ :.app
       ($ header) 
       ($ footer))))

(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn render []
  (rf/dispatch-sync [:app/init-db app.db/default-db])
  (uix.dom/render-root ($ app) root))

(defn ^:export init []
  (render))
