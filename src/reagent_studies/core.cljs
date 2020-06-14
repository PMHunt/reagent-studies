(ns reagent-studies.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [clojure.string :as str]))

(enable-console-print!)

(defn hello [] "Hello Baby")

(defn simple-component []
  [:div
   [:p "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red "] "text."]])

(defn simple-parent []
  [:div
   [:p "I include simple-component."]
   [simple-component]])

(defn hello-component [name]
  [:p "Hello, " name "!"])

(defn say-hello []
  [hello-component "world"])

(defn green-button [txt]
  [:button.green txt])

(defn make-button []
  (green-button "Click Me"))

(defn counting-button [txt]
  (let [state (r/atom 0)]
    (fn [txt]
      [:button.green ; `.green` hiccup shortcut class of the button
       {:on-click #(swap! state inc)} ; attribute of the button
       (str txt ": " @state)])))

(defn render-count-button []
  (rdom/render
   [counting-button "Count"]
   (.-body js/document)))

;;; BMI example

(defn calc-bmi [{:keys [height weight bmi] :as data}]
  "Destructures a a map `data`  and calcs `weight` or `BMI`
returns a new map with all three keys calculated"
  (let [h (/ height 100)]
    (if (nil? bmi)
      (assoc data :bmi (/ weight (* h h)))
      (assoc data :weight (* bmi h h)))))

(def bmi-data (r/atom (calc-bmi {:height 175 :weight 75})))

(defn slider [param value min max invalidates]
  "JS range slider, that swap!s new values into bmi-data map"
  [:input {:type "range" :value value :min min :max max
           :style {:width "100%"}
           :on-change (fn [e]
                        (let [new-value (js/parseInt (.. e -target -value))]
                          (swap! bmi-data
                                 (fn [data]
                                   (-> data
                                       (assoc param new-value)
                                       (dissoc invalidates)
                                       calc-bmi)))))}])

(defn bmi-component []
  "Component allowing the user to interact with bmi-data"
  (let [{:keys [weight height bmi]} @bmi-data
        [color diagnose] (cond
                           (< bmi 18.5) ["orange" "underweight"]
                           (< bmi 25) ["inherit" "normal"]
                           (< bmi 30) ["orange" "overweight"]
                           :else ["red" "obese"])]
    [:div
     [:h3.bmihead "BMI Calculator"]
     [:div
      "Height: " (int height) "cm"
      [slider :height height 100 220 :bmi]]
     [:div
      "Weight: " (int weight) "kg"
      [slider :weight weight 30 150 :bmi]]
     [:div
      "BMI: " (int bmi) " "
      [:span {:style {:color color}} diagnose]
      [slider :bmi bmi 10 50 :weight]]]))


(defn ^:export run []
  "Render the React DOM component"
  (rdom/render [bmi-component] (js/document.getElementById "app1")))

;;; --- end of BMI app, can't get render to work as expected, need dox

;; (set! (.-innerHTML (js/document.getElementById "app1")) (render-count-button))


(run)
