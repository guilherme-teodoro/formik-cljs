(ns demo.core
  (:require [reagent.core :as reagent]
            [demo.views :as views]))

(defn mount-root []
  (reagent/render [views/app-root] (.getElementById js/document "app")))
