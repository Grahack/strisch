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
          :class "str"}]
        (map #(identity [:line
                         ; the 1 is a hack for displaying entirely
                         {:x1 (+ 1 x (fret->x %)) :y1 (- line-y string-h)
                          :x2 (+ 1 x (fret->x %)) :y2 (+ line-y string-h)
                          :class "fret"}])
             (range (+ l 1)))])
  ))

(defn dot
  ([x y] (dot x y 4 true))
  ([x y r] (dot x y r false))
  ([x y r filled]
    (let [; the 1 is a hack for centering the dot
          dot-x (fn [x] (+ 1 (/ fret-sep 2) (* x fret-sep)))
          dot-y (fn [y] (+ 1 string-h (* string-sep y)))]
      [:circle {:cx (dot-x x) :cy (dot-y y) :r r
                :fill (if filled "black" "white")}])))

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
       ; dots
       (map #(apply dot %) dots)]
      ])

(def sixxfour  [[0 4] [0 4] [0 4] [0 4] [0 4] [0 4]])
(def sixxfive  [[0 5] [0 5] [0 5] [0 5] [0 5] [0 5]])
(def sixxsix   [[0 6] [0 6] [0 6] [0 6] [0 6] [0 6]])
(def sixxseven [[0 7] [0 7] [0 7] [0 7] [0 7] [0 7]])
(def GM-C [[0 5] [1 5] [3 5] [0 4] [2 4] [3 4] [0 3] [2 3] [3 3]
           [0 2] [2 2]       [0 1] [1 1] [3 1] [0 0] [1 0] [3 0]])
(def GM-A [[0 5] [2 5] [4 5] [1 4] [2 4] [4 4] [1 3] [2 3] [4 3]
           [1 2] [3 2] [4 2] [2 1] [4 1] [5 1] [2 0] [4 0] [6 0]])
(def GM-G [[1 5] [3 5] [4 5] [1 4] [3 4] [4 4] [1 3] [3 3]
           [0 2] [1 2] [3 2] [1 1] [2 1] [4 1] [1 0] [3 0] [4 0]])
(def GM-E [[0 5] [1 5] [3 5] [0 4] [1 4] [3 4] [0 3] [2 3] [3 3]
           [0 2] [2 2] [3 2]       [1 1] [3 1] [0 0] [1 0] [3 0]])
(def GM-D [[1 5] [3 5] [4 5] [1 4] [3 4]       [0 3] [1 3] [3 3]
           [0 2] [1 2] [3 2] [1 1] [3 1] [4 1] [1 0] [3 0] [4 0]])

(def GM-3-C  [[0 5] [1 5] [3 5] [0 4] [2 4] [3 4] [0 3] [2 3] [3 3]
              [0 2] [2 2] [4 2] [1 1] [3 1] [5 1] [1 0] [3 0] [5 0]])
(def GM-3-A  [[0 5] [2 5] [4 5] [1 4] [2 4] [4 4] [1 3] [2 3] [4 3]
              [1 2] [3 2] [4 2] [2 1] [4 1] [5 1] [2 0] [4 0] [6 0]])
(def GM-3-AG [[0 5] [2 5] [4 5] [0 4] [2 4] [4 4] [0 3] [2 3] [4 3]
              [1 2] [2 2] [4 2] [2 1] [3 1] [5 1] [2 0] [4 0] [5 0]])
(def GM-3-GE [[0 5] [2 5] [3 5] [0 4] [2 4] [3 4] [0 3] [2 3] [4 3]
              [0 2] [2 2] [4 2] [1 1] [3 1] [5 1] [2 0] [3 0] [5 0]])
(def GM-3-E  [[0 5] [1 5] [3 5] [0 4] [1 4] [3 4] [0 3] [2 3] [3 3]
              [0 2] [2 2] [3 2] [1 1] [3 1] [5 1] [1 0] [3 0] [5 0]])
(def GM-3-ED [[0 5] [2 5] [4 5] [0 4] [2 4] [4 4] [1 3] [2 3] [4 3]
              [1 2] [2 2] [4 2] [2 1] [4 1] [5 1] [2 0] [4 0] [5 0]])
(def GM-3-DC [[0 5] [2 5] [3 5] [0 4] [2 4] [4 4] [0 3] [2 3] [4 3]
              [0 2] [2 2] [4 2] [2 1] [3 1] [5 1] [2 0] [3 0] [5 0]])

(def GM-Io-3 [[0 4] [2 4] [4 4] [0 3] [2 3] [4 3]
              [1 2] [2 2] [4 2] [2 1] [3 1] [5 1] [2 0] [4 0] [5 0]])
(def GM-Do-3 [[0 4] [2 4] [3 4] [0 3] [2 3] [4 3]
              [0 2] [2 2] [4 2] [1 1] [3 1] [5 1] [2 0] [3 0] [5 0]])
(def GM-Ph-3 [[0 4] [1 4] [3 4] [0 3] [2 3] [3 3]
              [0 2] [2 2] [3 2] [1 1] [3 1] [5 1] [1 0] [3 0] [5 0]])
(def GM-Ly-3 [[0 4] [2 4] [4 4] [1 3] [2 3] [4 3]
              [1 2] [2 2] [4 2] [2 1] [4 1] [5 1] [2 0] [4 0] [5 0]])
(def GM-Mi-3 [[0 4] [2 4] [4 4] [0 3] [2 3] [4 3]
              [0 2] [2 2] [4 2] [2 1] [3 1] [5 1] [2 0] [3 0] [5 0]])
(def GM-Ae-3 [[0 4] [2 4] [3 4] [0 3] [2 3] [3 3]
              [0 2] [2 2] [4 2] [1 1] [3 1] [5 1] [1 0] [3 0] [5 0]])
(def GM-Lo-3 [[0 4] [1 4] [3 4] [0 3] [1 3] [3 3]
              [0 2] [2 2] [3 2] [1 1] [3 1] [4 1] [1 0] [3 0] [5 0]])

(def P-C     [[0 5] [3 5] [0 4] [3 4] [0 3] [2 3]
              [0 2] [2 2] [1 1] [3 1] [0 0] [3 0]])
(def P-C-bis [[1 5] [4 5] [1 4] [4 4] [1 3] [3 3]
              [1 2] [3 2] [2 1] [4 1] [1 0] [4 0]])
(def P-A     [[1 5] [3 5] [1 4] [3 4] [0 3] [3 3]
              [0 2] [3 2] [1 1] [3 1] [1 0] [3 0]])
(def P-G     [[0 5] [3 5] [0 4] [2 4] [0 3] [2 3]
              [0 2] [2 2] [0 1] [3 1] [0 0] [3 0]])
(def P-G-bis [[1 5] [4 5] [1 4] [3 4] [1 3] [3 3]
              [1 2] [3 2] [1 1] [4 1] [1 0] [4 0]])
(def P-E     [[1 5] [3 5] [0 4] [3 4] [0 3] [3 3]
              [0 2] [2 2] [1 1] [3 1] [1 0] [3 0]])
(def P-D     [[1 5] [3 5] [1 4] [3 4] [1 3] [3 3]
              [0 2] [3 2] [1 1] [4 1] [1 0] [3 0]])

(def P-dom-C     [[0 5] [3 5] [1 4] [3 4] [0 3] [2 3]
                  [0 2] [3 2] [1 1] [3 1] [0 0] [3 0]])
(def P-dom-C-bis [[1 5] [4 5] [2 4] [4 4] [1 3] [3 3]
                  [1 2] [4 2] [2 1] [4 1] [1 0] [4 0]])
(def P-dom-A     [[1 5] [4 5] [1 4] [3 4] [0 3] [3 3]
                  [1 2] [3 2] [1 1] [3 1] [1 0] [4 0]])
(def P-dom-G     [[1 5] [3 5] [0 4] [2 4] [0 3] [3 3]
                  [0 2] [2 2] [0 1] [3 1] [1 0] [3 0]])
(def P-dom-G-bis [[2 5] [4 5] [1 4] [3 4] [1 3] [4 3]
                  [1 2] [3 2] [1 1] [4 1] [2 0] [4 0]])
(def P-dom-E     [[1 5] [3 5] [0 4] [3 4] [1 3] [3 3]
                  [0 2] [2 2] [1 1] [4 1] [1 0] [3 0]])
(def P-dom-D     [[1 5] [3 5] [1 4] [4 4] [1 3] [3 3]
                  [0 2] [3 2] [2 1] [4 1] [1 0] [3 0]])

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

(def onexfive [[0 0] [0 0] [1 5]])
(def minmelE [[1 1] [2 1] [4 1] [1 2] [3 2] [4 2]])
(def minmel4 [[1 2] [2 2] [4 2] [5 2]])
(def GminmelC [[1 0] [3 0] [0 1] [1 1] [3 1] [4 1] [0 2] [2 2]
               [0 3] [1 3] [3 3] [0 4] [2 4] [3 4] [1 5] [3 5]])
(def GminmelA [[1 0] [3 0] [1 1] [2 1] [4 1] [0 2] [2 2] [3 2]
               [1 3] [3 3] [0 4] [1 4] [3 4] [4 4] [1 5] [3 5]])
(def GminmelG [[1 0] [3 0] [4 0] [1 1] [2 1] [4 1] [0 2] [1 2] [3 2] [4 2]
               [1 3] [3 3] [1 4] [2 4] [4 4] [1 5] [3 5] [4 5]])
(def GminmelE [[0 0] [1 0] [3 0] [4 0] [1 1] [3 1] [0 2] [1 2] [3 2]
               [0 3] [2 3] [3 3] [1 4] [3 4] [0 5] [1 5] [3 5] [4 5]])
(def GminmelD [[1 0] [2 0] [4 0] [1 1] [3 1] [4 1] [1 2] [3 2]
               [0 3] [1 3] [3 3] [4 3] [1 4] [3 4] [1 5] [2 5] [4 5]])

; ♭ ♮ ♯ ø
(defn hello-world []
  [:div
   [:h1 "Schémas pour quelques gammes"]
   [:ul
     [:li "La " [:a {:href "#maj"} "gamme majeure"] " et ses modes en CAGED :"
       [:ul
         [:li [:a {:href "#dor"} "dorien"]]
         [:li [:a {:href "#phr"} "phrygien"]]
         [:li [:a {:href "#lyd"} "lydien"]]
         [:li [:a {:href "#mix"} "mixolydien"]]
         [:li [:a {:href "#aeo"} "aéolien"]]
         [:li [:a {:href "#loc"} "locrien"]]
       ]
     ]
     [:li "La " [:a {:href "#maj3"} "gamme majeure"] " "
          "en 3 notes par corde sur 6 cordes, "
          "puis ses modes sur 5 cordes :"
       [:ul
         [:li [:a {:href "#ion3"} "ionien"]     " (extrait du AG"
                                                " de la gamme majeure)"]
         [:li [:a {:href "#dor3"} "dorien"]     " (GE)"]
         [:li [:a {:href "#phr3"} "phrygien"]   " (E)"]
         [:li [:a {:href "#lyd3"} "lydien"]     " (ED)"]
         [:li [:a {:href "#mix3"} "mixolydien"] " (DC)"]
         [:li [:a {:href "#aeo3"} "aéolien"]    " (C)"]
         [:li [:a {:href "#loc3"} "locrien"]    " (A)"]
       ]
     ]
     [:li "Les gammes " [:a {:href "#penta"} "pentatoniques "]
       [:ul
         [:li [:a {:href "#m"} "mineure"]]
         [:li [:a {:href "#d"} "dorien"]]
         [:li [:a {:href "#M7"} "M7"]]
         [:li [:a {:href "#M711"} "M7♯11"]]
         [:li [:a {:href "#alt"} "altérée"]]
         [:li [:a {:href "#dom"} "dominante"]]
         [:li [:a {:href "#m7b5"} "demi-diminué"]]
         [:li [:a {:href "#altM"} "altérée majeure"]]
         [:li [:a {:href "#morph"} "morphing"]]
       ]]
     [:li "La " [:a {:href "#melmodes"}
                 "gamme mineure mélodique et ses modes :"]
       [:ul
         [:li [:a {:href "#mel"} "mineur mélodique"]]
         [:li [:a {:href "#lydb7"} "lydien ♭7"]]
         [:li [:a {:href "#suploc"} "super locrien"] " (ou « altéré »)"]
       ]
     ]
   ]
   [:h1 {:id "maj"} "Gamme majeure"]
   [:h2 "C"]
   [:p (diagram sixxfour  (conj GM-C [1 1 5] [3 4 5]))]
   [:h2 "A"]
   [:p (diagram sixxseven (conj GM-A [4 2 5] [2 4 5]))]
   [:h2 "G"]
   [:p (diagram sixxfive  (conj GM-G [4 0 5] [1 2 5] [4 5 5]))]
   [:h2 "E"]
   [:p (diagram sixxfour  (conj GM-E [1 0 5] [3 3 5] [1 5 5]))]
   [:h2 "D"]
   [:p (diagram sixxfive  (conj GM-D [4 1 5] [1 3 5]))]

   [:h1 "Les modes de la gamme majeure"]
   [:p "Donnés dans cet ordre : CAGED."]

   [:h2 {:id "dor"} "Dorien"]
   [:p (diagram sixxseven (conj GM-A [2 1 5] [4 4 5]))]
   [:p (diagram sixxfive  (conj GM-G [3 2 5] [1 4 5]))]
   [:p (diagram sixxfour  (conj GM-E [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfive  (conj GM-D [1 0 5] [3 3 5] [1 5 5]))]
   [:p (diagram sixxfour  (conj GM-C [3 1 5] [0 3 5]))]

   [:h2 {:id "phr"} "Phrygien"]
   [:p "Mêmes positions que pour le lydien."]
   [:p (diagram sixxfive  (conj GM-G [1 1 5] [3 4 5]))]
   [:p (diagram sixxfour  (conj GM-E [2 2 5] [0 4 5]))]
   [:p (diagram sixxfive  (conj GM-D [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfour  (conj GM-C [0 0 5] [2 3 5] [0 5 5]))]
   [:p (diagram sixxseven (conj GM-A [4 1 5] [1 3 5]))]

   [:h2 {:id "lyd"} "Lydien"]
   [:p "Mêmes positions que pour le phrygien."]
   [:p (diagram sixxfive  (conj GM-G [2 1 5] [4 4 5]))]
   [:p (diagram sixxfour  (conj GM-E [3 2 5] [1 4 5]))]
   [:p (diagram sixxfive  (conj GM-D [4 0 5] [1 2 5] [4 5 5]))]
   [:p (diagram sixxfour  (conj GM-C [1 0 5] [3 3 5] [1 5 5]))]
   [:p (diagram sixxseven (conj GM-A [5 1 5] [2 3 5]))]

   [:h2 {:id "mix"} "Mixolydien"]
   [:p (diagram sixxfour  (conj GM-E [1 1 5] [3 4 5]))]
   [:p (diagram sixxfive  (conj GM-D [3 2 5] [1 4 5]))]
   [:p (diagram sixxfour  (conj GM-C [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxseven (conj GM-A [2 0 5] [4 3 5] [2 5 5]))]
   [:p (diagram sixxfive  (conj GM-G [4 1 5] [1 3 5]))]

   [:h2 {:id "aeo"} "Aéolien"]
   [:p (diagram sixxfive  (conj GM-D [1 1 5] [3 4 5]))]
   [:p (diagram sixxfour  (conj GM-C [2 2 5] [0 4 5]))]
   [:p (diagram sixxseven (conj GM-A [4 0 5] [1 2 5] [4 5 5]))]
   [:p (diagram sixxfive  (conj GM-G [1 0 5] [3 3 5] [1 5 5]))]
   [:p (diagram sixxfour  (conj GM-E [3 1 5] [0 3 5]))]

   [:h2 {:id "loc"} "Locrien"]
   [:p "Mêmes positions que pour le ionien."]
   [:p (diagram sixxfour  (conj GM-C [0 1 5] [2 4 5]))]
   [:p (diagram sixxseven (conj GM-A [3 2 5] [1 4 5]))]
   [:p (diagram sixxfive  (conj GM-G [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfour  (conj GM-E [0 0 5] [2 3 5] [0 5 5]))]
   [:p (diagram sixxfive  (conj GM-D [3 1 5] [0 3 5]))]

   [:h1 {:id "maj3"} "La gamme majeure en 3 notes par corde"]
   [:h2 "C"]
   [:p (diagram sixxsix   (conj GM-3-C [1 1 5] [3 4 5]))]
   [:h2 "A"]
   [:p (diagram sixxseven (conj GM-3-A [4 2 5] [2 4 5]))]
   [:h2 "AG"]
   [:p (diagram sixxsix   (conj GM-3-AG [5 0 5] [2 2 5] [0 4 5]))]
   [:h2 "GE"]
   [:p (diagram sixxsix   (conj GM-3-GE [3 0 5] [0 2 5] [3 5 5]))]
   [:h2 "E"]
   [:p (diagram sixxsix   (conj GM-3-E [1 0 5] [3 3 5] [1 5 5]))]
   [:h2 "ED"]
   [:p (diagram sixxsix   (conj GM-3-ED [5 1 5] [2 3 5] [0 5 5]))]
   [:h2 "DC"]
   [:p (diagram sixxsix   (conj GM-3-DC [3 1 5] [0 3 5]))]

   [:h1 "Les modes de la gamme majeure en 3 notes par corde, sur 5 cordes"]
   [:h2 {:id "ion3"} "Ionien (extrait de AG)"]
   [:p (diagram sixxsix (conj GM-Io-3 [5 0 5] [2 2 5] [0 4 5]))]
   [:h2 {:id "dor3"} "Dorien (extrait de GE)"]
   [:p (diagram sixxsix (conj GM-Do-3 [5 0 5] [2 2 5] [0 4 5]))]
   [:h2 {:id "phr3"} "Phrygien (extrait de E)"]
   [:p (diagram sixxsix (conj GM-Ph-3 [5 0 5] [2 2 5] [0 4 5]))]
   [:h2 {:id "lyd3"} "Lydien (extrait de ED)"]
   [:p (diagram sixxsix (conj GM-Ly-3 [5 0 5] [2 2 5] [0 4 5]))]
   [:h2 {:id "mix3"} "Mixolydien (extrait de DC)"]
   [:p (diagram sixxsix (conj GM-Mi-3 [5 0 5] [2 2 5] [0 4 5]))]
   [:h2 {:id "aeo3"} "Aéolien (extrait de C)"]
   [:p (diagram sixxsix (conj GM-Ae-3 [5 0 5] [2 2 5] [0 4 5]))]
   [:h2 {:id "loc3"} "Locrien (extrait de A)"]
   [:p (diagram sixxsix (conj GM-Lo-3 [5 0 5] [2 2 5] [0 4 5]))]

   [:h1 {:id "penta"} "Pentatoniques"]
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
   [:p (diagram MG [[4 3]]) (diagram ME [[2 3]])]
   [:p (diagram MC [[4 2]]) (diagram MA [[2 2]])]
   [:p (diagram ME [[4 1]]) (diagram MD [[1 1]])]
   [:p "La penta majeure sur tout le manche d’une guitare :"]
   [:p (diagram sixxfour  (conj P-C [1 1 5] [3 4 5]))]
   [:p (diagram sixxfour  (conj P-A [3 2 5] [1 4 5]))]
   [:p (diagram sixxfour  (conj P-G [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfour  (conj P-E [1 0 5] [3 3 5] [1 5 5]))]
   [:p (diagram sixxfive  (conj P-D [4 1 5] [1 3 5]))]
   [:hr]
   [:h3 {:id "m"} "m"]
   [:p (diagram twoxfour [[1 1]]) (diagram threexthree [[3 2] [1 4]])]
   [:p (diagram MA [[4 3]]) (diagram MG [[1 3]])]
   [:p (diagram MD [[3 2]]) (diagram MC [[1 2]])]
   [:p (diagram MG [[3 1]]) (diagram ME [[1 1]])]
   [:p "La penta mineure sur tout le manche d’une guitare :"]
   [:p (diagram sixxfive  (conj P-D [1 1 5] [3 4 5]))]
   [:p (diagram sixxfour  (conj P-C [2 2 5] [0 4 5]))]
   [:p (diagram sixxfour  (conj P-A [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfour  (conj P-G [0 0 5] [2 3 5] [0 5 5]))]
   [:p (diagram sixxfour  (conj P-E [3 1 5] [0 3 5]))]
   [:hr]
   [:h3 {:id "d"} "d"]
   [:p (diagram twoxfour [[1 0] [4 3]]) (diagram threexthree [[3 1] [1 3]])]
   [:p (diagram ME [[4 3]]) (diagram MD [[1 3]])]
   [:p (diagram MA [[4 2]]) (diagram MG [[1 2]])]
   [:p (diagram MD [[3 1]]) (diagram MC [[1 1]])]
   [:p "La penta dorienne sur tout le manche d’une guitare :"]
   [:p (diagram sixxfour  (conj P-A [1 1 5] [3 4 5]))]
   [:p (diagram sixxfour  (conj P-G [2 2 5] [0 4 5]))]
   [:p (diagram sixxfour  (conj P-E [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfive  (conj P-D [1 0 5] [3 3 5] [1 5 5]))]
   [:p (diagram sixxfour  (conj P-C [3 1 5] [0 3 5]))]
   [:hr]
   [:h3 {:id "M7"} "M7"]
   [:p (diagram twoxfour [[2 2] [4 0]]) (diagram threexthree [[1 0] [4 3]])]
   [:p (diagram MD [[4 3]]) (diagram MC [[2 3]])]
   [:p (diagram MG [[4 2]]) (diagram ME [[2 2]])]
   [:p (diagram MC [[4 1]]) (diagram MA [[2 1]])]
   [:p "La penta M7 sur tout le manche d’une guitare :"]
   [:p (diagram sixxfour  (conj P-G [1 1 5] [3 4 5]))]
   [:p (diagram sixxfour  (conj P-E [3 2 5] [1 4 5]))]
   [:p (diagram sixxfive  (conj P-D [4 0 5] [1 2 5] [4 5 5]))]
   [:p (diagram sixxfour  (conj P-C [1 0 5] [3 3 5] [1 5 5]))]
   [:p (diagram sixxfive  (conj P-A [4 1 5] [1 3 5]))]
   [:hr]
   [:h3 {:id "M711"} "M7♯11"]
   [:p (diagram twoxfour [[2 1]]) (diagram threexthree [[4 2] [2 4]])]
   [:p (diagram MA [[5 3]]) (diagram MG [[2 3]])]
   [:p (diagram MD [[4 2]]) (diagram MC [[2 2]])]
   [:p (diagram MG [[4 1]]) (diagram ME [[2 1]])]
   [:p "La penta M7♯11 sur tout le manche d’une guitare :"]
   [:p (diagram sixxfive  (conj P-D [2 1 5] [4 4 5]))]
   [:p (diagram sixxfour  (conj P-C [3 2 5] [1 4 5]))]
   [:p (diagram sixxfive  (conj P-A [1 2 5] [4 5 5] [4 0 5]))]
   [:p (diagram sixxfour  (conj P-G [3 3 5] [1 5 5] [1 0 5]))]
   [:p (diagram sixxfive  (conj P-E [4 1 5] [1 3 5]))]
   [:hr]
   [:h3 {:id "alt"} "alt"]
   [:p (diagram twoxfour [[3 2]]) (diagram threexthree [[0 2]])]
   [:p (diagram MC [[3 3]]) (diagram MA [[1 3]])]
   [:p (diagram ME [[3 2]]) (diagram MD [[0 2]])]
   [:p (diagram MA [[3 1]]) (diagram MG [[0 1]])]
   [:p "La penta alt sur tout le manche d’une guitare :"]
   [:p (diagram sixxfour  (conj P-E [0 1 5] [2 4 5]))]
   [:p (diagram sixxfive  (conj P-D [2 2 5] [0 4 5]))]
   [:p (diagram sixxfive  (conj P-C-bis [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfour  (conj P-A [0 0 5] [2 3 5] [0 5 5]))]
   [:p (diagram sixxfive  (conj P-G-bis [3 1 5] [0 3 5]))]
   [:p "La penta alt « habille » les accords 7 des cordes 2, 3 et 4 ainsi :"]
   [:p (diagram sixxfive  (conj P-G [2 1 5] [4 2 5] [3 3 5]))]
   [:p (diagram sixxfour  (conj P-E [0 1 5] [2 2 5] [1 3 5]))]
   [:p (diagram sixxfive  (conj P-D [2 1 5] [0 2 5] [2 3 5]))]
   [:p (diagram sixxfour  (conj P-C [2 1 5] [3 2 5] [2 3 5]))]
   [:p (diagram sixxfour  (conj P-A [3 1 5] [1 2 5] [2 3 5]))]
   [:hr]
   [:h3 {:id "dom"} "dom"]
   [:p (diagram fourxfour [[4 2]]) (diagram threexfive [[1 2]])]
   [:p (diagram domG [[4 3]]) (diagram domE [[2 3]])]
   [:p (diagram domC [[3 2]]) (diagram domA [[1 2]])]
   [:p (diagram domE [[4 1]]) (diagram domD [[1 1]])]
   [:p "La penta dom sur tout le manche d’une guitare :"]
   [:p (diagram sixxfour  (conj P-dom-C [1 1 5] [3 4 5]))]
   [:p (diagram sixxfive  (conj P-dom-A [3 2 5] [1 4 5]))]
   [:p (diagram sixxfour  (conj P-dom-G [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfive  (conj P-dom-E [1 0 5] [3 3 5] [1 5 5]))]
   [:p (diagram sixxfive  (conj P-dom-D [4 1 5] [1 3 5]))]
   [:p "La penta dom « habille » les accords 7 des cordes 2, 3 et 4 ainsi :"]
   [:p (diagram sixxfour  (conj P-dom-C [1 1 5] [3 2 5] [2 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-A [3 1 5] [1 2 5] [3 3 5]))]
   [:p (diagram sixxfour  (conj P-dom-G [0 1 5] [0 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-E [1 1 5] [2 2 5] [1 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-D [2 1 5] [0 2 5] [1 3 5]))]
   [:hr]
   [:h3 {:id "m7b5"} "ø"]
   [:p (diagram fourxfour [[3 1] [1 3]]) (diagram threexfive [[0 1] [3 4]])]
   [:p (diagram domD [[3 3]]) (diagram domC [[0 3]])]
   [:p (diagram domG [[3 2]]) (diagram domE [[1 2]])]
   [:p (diagram domC [[2 1]]) (diagram domA [[0 1]])]
   [:p "La penta demi-diminuée sur tout le manche d’une guitare :"]
   [:p (diagram sixxfour  (conj P-dom-G [0 1 5] [2 4 5]))]
   [:p (diagram sixxfive  (conj P-dom-E [2 2 5] [0 4 5]))]
   [:p (diagram sixxfive  (conj P-dom-D [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfour  (conj P-dom-C [0 0 5] [2 3 5] [0 5 5]))]
   [:p (diagram sixxfive  (conj P-dom-A [3 1 5] [0 3 5]))]
   [:p "La penta demi-diminuée « habille » les accords m7♭5 des cordes"
       " 1, 2, 3 et 4 ainsi :"]
   [:p (diagram sixxfour  (conj P-dom-G [1 0 5] [0 1 5] [2 2 5] [0 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-E [3 0 5] [1 1 5] [2 2 5] [1 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-D [3 0 5] [2 1 5] [3 2 5] [3 3 5]))]
   [:p (diagram sixxfour  (conj P-dom-C [0 0 5] [0 2 5] [0 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-A [1 0 5] [1 1 5] [1 2 5] [0 3 5]))]
   [:p "Pour les cordes 2, 3, 4 et 5 :"]
   [:p (diagram sixxfour  (conj P-dom-G [3 1 5] [2 2 5] [3 3 5] [2 4 5]))]
   [:p (diagram sixxfive  (conj P-dom-E [1 1 5] [0 2 5] [1 3 5] [0 4 5]))]
   [:p (diagram sixxfive  (conj P-dom-D [2 1 5] [0 2 5] [3 3 5] [1 4 5]))]
   [:p (diagram sixxfour  (conj P-dom-C [3 1 5] [0 2 5] [2 3 5] [1 4 5]))]
   [:p (diagram sixxfive  (conj P-dom-A [3 1 5] [1 2 5] [3 3 5] [3 4 5]))]
   [:hr]
   [:h3 {:id "altM"} "alt M"]
   [:p (diagram fourxfour [[3 3] [0 0]]) (diagram threexfive [[0 3] [2 1]])]
   [:p (diagram domC [[2 3]]) (diagram domA [[0 3]])]
   [:p (diagram domE [[3 2]]) (diagram domD [[0 2]])]
   [:p (diagram domA [[2 1]]) (diagram domG [[0 1]])]
   [:p "La penta alt M sur tout le manche d’une guitare :"]
   [:p (diagram sixxfive  (conj P-dom-E [0 1 5] [2 4 5]))]
   [:p (diagram sixxfive  (conj P-dom-D [2 2 5] [0 4 5]))]
   [:p (diagram sixxfive  (conj P-dom-C-bis [3 0 5] [0 2 5] [3 5 5]))]
   [:p (diagram sixxfive  (conj P-dom-A [0 0 5] [2 3 5] [0 5 5]))]
   [:p (diagram sixxfive  (conj P-dom-G-bis [3 1 5] [0 3 5]))]
   [:p "La penta alt M « habille » les accords 7 des cordes 2, 3 et 4 ainsi :"]
   [:p (diagram sixxfour  (conj P-dom-C [1 1 5] [3 2 5] [2 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-A [3 1 5] [1 2 5] [3 3 5]))]
   [:p (diagram sixxfour  (conj P-dom-G [0 1 5] [0 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-E [1 1 5] [2 2 5] [1 3 5]))]
   [:p (diagram sixxfive  (conj P-dom-D [2 1 5] [0 2 5] [1 3 5]))]
   [:h3 {:id "morph"} "Morphing"]
   [:p "On passe :"]
   [:ul
     [:li "maj/dom"
       [:ul
         [:li "de la majeure à la dom en diésant la 6"]
         [:li "de la dom à la majeure en bémolisant la ♭7"]
       ]
     ]
     [:li "m/d"
       [:ul
         [:li "de la mineure à la dorien en bémolisant la 3"]
         [:li "de la dorien la mineure en diésant la 2"]
       ]
     ]
     [:li "ø/alt"
       [:ul
         [:li "de la demi-diminuée à l’altérée en diésant la 1"]
         [:li "de l’altérée à la demi-diminuée en bémolisant la ♭2"]
       ]
     ]
     [:li "alt/alt M"
       [:ul
         [:li "de l’altérée à l’altérée majeure en diésant la 3"]
         [:li "de l’altérée majeure à l’altérée en bémolisant la 3"]
       ]
     ]
   ]
   [:p [:strong "Exercice"]
       " : pour chaque morphing, dessiner les 5 schémas où on voit
       les deux notes à modifier."]

   [:h1 {:id "melmodes"} "Mineur mélodique et ses modes intéressants"]
   [:p "Seuls le lydien ♭7 et le super locrien sont considérés intéressants."]
   [:p "Les deux premiers diagrammes sont des formes simples extraites du
       « méga diagramme ». TODO : les six autres sont des « voisinages » que
       l’on peut noter ainsi (système CAGED) :" [:br]
       "D E" [:br]
       "A C" [:br]
       "E G" [:br]]
   [:p "Puis viennent les 5 diagrammes sur tout le travers du manche,
       dans l’ordre CAGED."]
   [:h2 {:id "mel"} "Mineur mélodique"]
   [:p (diagram twoxfour (conj minmelE [4 2 5]))
       (diagram onexfive (conj minmel4 [2 2 5]))]
   [:p (diagram sixxfive (conj GminmelC [1 1 5] [3 4 5]))
       (diagram sixxfive (conj GminmelA [3 2 5] [1 4 5]))
       (diagram sixxfive (conj GminmelG [4 0 5] [1 2 5] [4 5 5]))
       (diagram sixxfive (conj GminmelE [1 0 5] [3 3 5] [1 5 5]))
       (diagram sixxfive (conj GminmelD [4 1 5] [1 3 5]))]
   [:h2 {:id "lydb7"} "Lydien ♭7"]
   [:p (diagram twoxfour (conj minmelE [4 1 5] [2 3 5]))
       (diagram onexfive (conj minmel4 [2 1 5]))]
   [:p (diagram sixxfive (conj GminmelG [2 1 5] [4 4 5]))
       (diagram sixxfive (conj GminmelE [3 2 5] [1 4 5]))
       (diagram sixxfive (conj GminmelD [4 0 5] [1 2 5] [4 5 5]))
       (diagram sixxfive (conj GminmelC [1 0 5] [3 3 5] [1 5 5]))
       (diagram sixxfive (conj GminmelA [4 1 5] [1 3 5]))]
   [:h2 {:id "suploc"} "Super locrien (altéré)"]
   [:p (diagram twoxfour (conj minmelE [3 2 5]))
       (diagram onexfive (conj minmel4 [1 2 5]))]
   [:p (diagram sixxfive (conj GminmelC [0 1 5] [2 4 5]))
       (diagram sixxfive (conj GminmelA [2 2 5] [0 4 5]))
       (diagram sixxfive (conj GminmelG [3 0 5] [0 2 5] [3 5 5]))
       (diagram sixxfive (conj GminmelE [0 0 5] [2 3 5] [0 5 5]))
       (diagram sixxfive (conj GminmelD [3 1 5] [0 3 5]))]
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
