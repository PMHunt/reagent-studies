(ns reagent-studies.core)

(enable-console-print!)

(defn hello [] "Hello Baby")

(set! (.-innerHTML (js/document.getElementById "app")) (hello))

(println (hello))
