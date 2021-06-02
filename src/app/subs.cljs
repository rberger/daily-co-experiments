(ns app.subs
  (:require
   [re-frame.core :as rf]
   [refn.core :refer [defsub]]))

(defsub :rf-count)

(defsub :room-url)

(defsub :meeting-token)

(defsub :rtmp-url)
(defsub :rtmp-session-key)

(rf/reg-sub
 :rtmp-full-url
 ;; input signals with syntactic sugar as per
 ;; https://day8.github.io/re-frame/subscriptions/#syntactic-sugar
 :<- [:rtmp-url]
 :<- [:rtmp-session-key]

 (fn [[rtmp-url rtmp-session-key] _]
   (str rtmp-url "/" rtmp-session-key)))


(defsub :status-description)

;; Used to force page refresh after reset
(defsub :reset-count)
