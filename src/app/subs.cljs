(ns app.subs
  (:require [re-frame.core :as rf]
            [emmy.env :as emmy]
            [emmy.calculus.manifold :as m]
            [emmy.matrix :as matrix]))
            

(rf/reg-sub :app/todos
  (fn [db _]
    (:todos db)))


(rf/reg-sub :app/app-state
            (fn [db _]
              (:app-state db)))

(rf/reg-sub :app/coordinates
            :<- [:app/app-state]
            (fn [app-state _]
              (:coordinates app-state)))

(rf/reg-sub :app/coordinate
            :<- [:app/coordinates]
            (fn [coordinates [_ i]]
              (get coordinates i)))

(rf/reg-sub :app/manifold-point
            :<- [:app/coordinates]
            (fn [coordinates _] 
              (let [SO3-Point (:spec ((m/point m/Euler-angles) (apply emmy/up coordinates)))] 
                (emmy/freeze (emmy/simplify SO3-Point)))))

(comment
  @(rf/subscribe [:app/app-state])

  @(rf/subscribe [:app/coordinates])

  @(rf/subscribe [:app/manifold-point])

  (def my-coordinate-system m/R2-rect)

  (def my-manifold (m/manifold m/R2-rect))

  (def my-manifold-function (m/constant-manifold-function 2))

  (def my-manifold-point (m/coords->point my-coordinate-system [1 1]))

  (m/manifold-point? my-manifold-point)

  (my-manifold-function my-manifold-point)

  #_(let [SO3-Point ((m/point m/Euler-angles) (emmy/up coordinates))]) 

  m/SO3

  (let [SO3-Point ((m/point m/Euler-angles) (apply emmy/up [1 1 1]))
        SO3-Point-matrix (:spec SO3-Point)]
    (emmy/print-expression SO3-Point-matrix))



  (m/coordinate-system-at m/SO3 :Euler :Euler-patch)
  )



  





   