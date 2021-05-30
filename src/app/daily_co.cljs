(ns app.daily-co
  (:require
   ["@daily-co/daily-js/dist/daily-iframe-esm.js" :default DailyIframe])
  )

(js/console.log "DailyIframe: " DailyIframe)
;; (js/console.log "createFrame: " createFrame)
(defonce call-frame (atom nil))

(defn create-call-frame [call-wrapper {:keys [loaded
                                              started-camera
                                              camera-error
                                              joining-meeting
                                              joined-meeting
                                              left-meeting]}]

  (js/console.log "Top create-call-frame call-wrapper: " call-wrapper)
  (.createFrame DailyIframe  call-wrapper)
  )
;; (->
;;  (.createFrame DailyIframe  call-wrapper)
;;  (.then (fn [callFrame]
;;           (reset! call-frame callFrame)
;;           (->
;;            ((.-on callFrame) "loaded" loaded)
;;            (.on "started-camera" started-camera)
;;            (.on "camera-error" camera-error)
;;            (.on "joining-meeting" joining-meeting)
;;            (.on "joined-meeting" joined-meeting)
;;            (.on "left-meeting" left-meeting))))))

(defn join-room
  "Join the room using the supplied room url
  `room-url` - daily.co room url"
  [room-url]
  (try
    (.join @call-frame #js {:url room-url
                            :showLeaveButton true})
    (catch
        js/Object e (.error js/console e))))

;;
;; Event listener callbacks and helpers
;;

(defn show-event [e] (.log js/console "callFrame show event" e))
(defn toggle-lobby [e] (.log js/console "callFrame toggle-lobby event" e))
(defn handle-joined-meeting [e] (.log js/console "callFrame handle-joined-meeting event" e))
(defn handle-left-meeting [e] (.log js/console "callFrame handle-left-meeting event" e))

