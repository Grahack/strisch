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
        fret->x #(* fret-sep %)]
    (if (> l 0)
      [:g
        [:line
         {:x1 x :y1 line-y :x2 (+ x (fret->x l)) :y2 line-y
          :stroke "black" :stroke-width 2}]
        (map #(identity [:line
                         ; the 1 is a hack for displaying entirely
                         {:x1 (+ 1 x (fret->x %)) :y1 (- line-y string-h)
                          :x2 (+ 1 x (fret->x %)) :y2 (+ line-y string-h)
                          :stroke "black" :stroke-width 1}])
             (range (+ l 1)))])
  ))

; strings is a list of [x l] where x is the offset and l the length
; dots is a list of coordinates
(defn diagram [strings dots]
   [:svg
     [:g
       ; strings
       (map #(let [data (nth strings %)
                   x    (nth data 0)
                   y    (* % string-sep)
                   l    (nth data 1)]
                 (string (* x fret-sep) y l))
            (range (count strings)))
       (map #(let [data (nth dots %)
                   x    (nth data 0)
                   y    (nth data 1)
                   ; the 1 is a hack for centering the dot
                   dot-x (fn [x] (+ 1 (/ fret-sep 2) (* x fret-sep)))
                   ; the 1 is a hack for centering the dot
                   dot-y (fn [y] (+ 1 string-h (* string-sep y)))]
                 [:circle {:cx (dot-x x) :cy (dot-y y) :r 5}])
             (range (count dots)))]
      ])

(def twoxfour [[0 0] [1 4] [1 4]])
(def threexthree [[0 0] [1 3] [1 3] [1 3]])
(def MC [[0 0] [1 3] [1 4] [1 4]])
(def MA [[0 0] [1 4] [2 3] [2 3]])
(def MG [[0 0] [1 3] [1 3] [1 4]])
(def ME [[0 0] [1 4] [1 4] [2 3]])
(def MD threexthree)

(defn hello-world []
  [:div
   [:h1 "Pentatoniques"]
   [:h2 "Sur manches en quartes"]
   [:h3 "m"]
   [:p (diagram twoxfour [[1 1]]) (diagram threexthree [[3 2] [1 4]])]
   [:p (diagram ME [[1 1]]) (diagram MG [[3 1] [1 3]])]
   [:p (diagram MC [[1 2]]) (diagram MD [[3 2]])]
   [:p (diagram MG [[1 3]]) (diagram MA [[4 3]])]
   [:h3 "M"]
   [:p (diagram twoxfour [[4 1] [2 3]]) (diagram threexthree [[1 1] [4 4]])]
   [:p (diagram MD [[1 1]]) (diagram ME [[4 1]])]
   [:p (diagram MA [[2 2]]) (diagram MC [[4 2]])]
   [:p (diagram ME [[2 3]]) (diagram MG [[4 3]])]
   [:h3 "d"]
   [:p (diagram twoxfour [[1 0] [4 3]]) (diagram threexthree [[3 1] [1 3]])]
   [:p (diagram MC [[1 1]]) (diagram MD [[3 1]])]
   [:p (diagram MG [[1 2]]) (diagram MA [[4 2]])]
   [:p (diagram MD [[1 3]]) (diagram ME [[4 3]])]
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
