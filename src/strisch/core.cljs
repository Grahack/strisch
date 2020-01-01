(ns ^:figwheel-hooks strisch.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]))

(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))

(def fret-sep 30)
(def string-sep 20)
(def string-h 4)

(defn string [x y l]
  (let [; the 2 is a hack for displaying separators entirely
        line-y (+ 3 y (/ string-h 2))
        fret->x #(* fret-sep (+ 1 %))]
    (if (> l 0)
      [:g
        [:line
         {:x1 (+ fret-sep x) :y1 line-y :x2 (+ x (fret->x l)) :y2 line-y
          :stroke "black" :stroke-width 2}]
        (map #(identity [:line
                         ; the 1 is a hack for displaying entirely
                         {:x1 (+ 1 x (fret->x %)) :y1 (- line-y string-h)
                          :x2 (+ 1 x (fret->x %)) :y2 (+ line-y string-h)
                          :stroke "black" :stroke-width 1}])
             (range (+ l 1)))])
  ))

; content is a list of [offset length [dots]]
(defn diagram [content]
   [:svg
     [:g
       ; strings
       (map #(let [data (nth content %)
                   x    (nth data 0)
                   y     (* % string-sep)
                   l    (nth data 1)
                   dots (nth data 2)
                   ; the 1 is a hack for centering the dot
                   dot->pos (fn [dot]
                                (+ 1 (/ fret-sep 2) (* dot fret-sep)))]
               [:g
                 (string (* x fret-sep) y l)
                 ; the 1 is a hack for centering the dot
                 (map (fn [dot] [:circle {:cx (dot->pos dot)
                                          :cy (+ 1 string-h y) :r 5}])
                      dots)])
            (range (count content)))
      ]])

(defn hello-world []
  [:div
   [:h1 "Pentatoniques"]
   [:h2 "Sur manches en quartes"]
   (diagram [[0 4 [1]]
             [1 3 []]])
   ])

(defn mount [el]
  (reagent/render-component [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
