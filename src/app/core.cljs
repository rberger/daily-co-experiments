(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [refn.core :as rf :refer [<< >>]] ;; Using refn wrapper for re-frame
            [app.hello :refer [hello]]
            [app.call :refer [call]]))

(def default-db
  {:room-url "https://visx.daily.co/robtest000"
   :reset-count 0})

(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (rd/render [call] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic."
  []
  (rf/init! default-db)
  (render))
