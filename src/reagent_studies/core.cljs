(ns reagent-studies.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

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

(defn render-simple []
  (rdom/render
   [simple-parent]
   (.-body js/document)))

(set! (.-innerHTML (js/document.getElementById "app")) (render-simple))

(println (hello))
