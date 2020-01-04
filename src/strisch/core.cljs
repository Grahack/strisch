(ns ^:figwheel-hooks strisch.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]))

(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))

(def fret-sep 40)
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

(def fourxfour [[1 4] [1 3] [2 3] [1 4]])
(def threexfive [[0 0] [0 4] [1 3] [1 4]])
(def domC [[0 0] [0 3] [1 3] [0 4]])
(def domA threexfive)
(def domG [[0 0] [1 4] [1 3] [2 3]])
(def domE [[0 0] [2 3] [1 4] [2 3]])
(def domD [[0 0] [1 3] [1 4] [1 3]])

; ♭ ♮ ♯ ø
(defn hello-world []
  [:div
   [:h1 "Pentatoniques"]
   [:p "De la penta " [:a {:href "#M"} "majeure"]
       " (do ré mi sol la), notée M, découlent :"]
   [:ul
     [:li "la penta " [:a {:href "#m"} "mineure"] ", "
          "la do ré mi sol, notée « m » ;"]
     [:li "la penta « " [:a {:href "#d"} "dorien"] " », "
          "ré mi sol la do, notée « d » ;"]
     [:li "la penta " [:a {:href "#M7"} "M7"] ", "
          "(fa) sol la do ré mi ;"]
     [:li "la penta " [:a {:href "#M711"} "M7♯11"] ", "
          "(si♭) do ré mi sol la ;"]
     [:li "la penta " [:a {:href "#alt"} "altérée"] ", "
          "(fa♯) sol la do ré mi, notée « alt » ;"]
   ]
   [:p "De la penta « " [:a {:href "#dom"} "dominante"] " » "
       "(do ré mi sol si♭), notée « dom » :"]
   [:ul
     [:li "la penta « " [:a {:href "#m7b5"} "demi-diminué"] " », "
          "mi sol si♭ do ré, notée « ø » ;"]
     [:li "la penta « " [:a {:href "#altM"} "altérée majeure"] " », "
          "(fa♯) sol si♭ (ou la♯) do ré mi , notée « alt M » ;"]
   ]
   [:p "Les deux premiers diagrammes sont des formes simples extraites du
       « méga diagramme », les six autres sont des « voisinages » que l’on peut
       noter ainsi (système CAGED) :" [:br]
       "D E" [:br]
       "A C" [:br]
       "E G" [:br]]
   [:h2 "Sur manches en quartes"]
   [:h3 {:id "M"} "M"]
   [:p (diagram twoxfour [[4 1] [2 3]]) (diagram threexthree [[1 1] [4 4]])]
   [:p (diagram MD [[1 1]]) (diagram ME [[4 1]])]
   [:p (diagram MA [[2 2]]) (diagram MC [[4 2]])]
   [:p (diagram ME [[2 3]]) (diagram MG [[4 3]])]
   [:hr]
   [:h3 {:id "m"} "m"]
   [:p (diagram twoxfour [[1 1]]) (diagram threexthree [[3 2] [1 4]])]
   [:p (diagram ME [[1 1]]) (diagram MG [[3 1]])]
   [:p (diagram MC [[1 2]]) (diagram MD [[3 2]])]
   [:p (diagram MG [[1 3]]) (diagram MA [[4 3]])]
   [:hr]
   [:h3 {:id "d"} "d"]
   [:p (diagram twoxfour [[1 0] [4 3]]) (diagram threexthree [[3 1] [1 3]])]
   [:p (diagram MC [[1 1]]) (diagram MD [[3 1]])]
   [:p (diagram MG [[1 2]]) (diagram MA [[4 2]])]
   [:p (diagram MD [[1 3]]) (diagram ME [[4 3]])]
   [:hr]
   [:h3 {:id "M7"} "M7"]
   [:p (diagram twoxfour [[2 2] [4 0]]) (diagram threexthree [[1 0] [4 3]])]
   [:p (diagram MA [[2 1]]) (diagram MC [[4 1]])]
   [:p (diagram ME [[2 2]]) (diagram MG [[4 2]])]
   [:p (diagram MC [[2 3]]) (diagram MD [[4 3]])]
   [:hr]
   [:h3 {:id "M711"} "M7♯11"]
   [:p (diagram twoxfour [[2 1]]) (diagram threexthree [[4 2] [2 4]])]
   [:p (diagram ME [[2 1]]) (diagram MG [[4 1]])]
   [:p (diagram MC [[2 2]]) (diagram MD [[4 2]])]
   [:p (diagram MG [[2 3]]) (diagram MA [[5 3]])]
   [:hr]
   [:h3 {:id "alt"} "alt"]
   [:p (diagram twoxfour [[3 2]]) (diagram threexthree [[0 2]])]
   [:p (diagram MG [[0 1]]) (diagram MA [[3 1]])]
   [:p (diagram MD [[0 2]]) (diagram ME [[3 2]])]
   [:p (diagram MA [[1 3]]) (diagram MC [[3 3]])]
   [:hr]
   [:h3 {:id "dom"} "dom"]
   [:p (diagram fourxfour [[4 2]]) (diagram threexfive [[1 2]])]
   [:p (diagram domD [[1 1]]) (diagram domE [[4 1]])]
   [:p (diagram domA [[1 2]]) (diagram domC [[3 2]])]
   [:p (diagram domE [[2 3]]) (diagram domG [[4 3]])]
   [:hr]
   [:h3 {:id "m7b5"} "ø"]
   [:p (diagram fourxfour [[3 1] [1 3]]) (diagram threexfive [[0 1] [3 4]])]
   [:p (diagram domA [[0 1]]) (diagram domC [[2 1]])]
   [:p (diagram domE [[1 2]]) (diagram domG [[3 2]])]
   [:p (diagram domC [[0 3]]) (diagram domD [[3 3]])]
   [:hr]
   [:h3 {:id "altM"} "alt M"]
   [:p (diagram fourxfour [[3 3] [0 0]]) (diagram threexfive [[0 3] [2 1]])]
   [:p (diagram domG [[0 1]]) (diagram domA [[3 1]])]
   [:p (diagram domD [[0 2]]) (diagram domE [[3 2]])]
   [:p (diagram domA [[0 3]]) (diagram domC [[2 3]])]
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
