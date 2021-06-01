(ns app.daily-co
  (:require
   ;;   ["@daily-co/daily-js/dist/daily-iframe-esm.js" :default DailyIframe])
   ["@daily-co/daily-js" :default DailyIframe])
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
  (reset! call-frame (.createFrame DailyIframe  call-wrapper
                                   #js {:showLeaveButton true
                                        :showFullscreenButton true
                                        }
                                   )))
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
  [{:keys [room-url meeting-token]}]
  (let [base-props {:url room-url
                    :showLeaveButton true}
        props (if meeting-token
                (merge base-props {:token meeting-token})
                base-props)]
    (js/console.log ::join-room props)
    (try
      (.join @call-frame (clj->js props))
      (catch
          js/Object e (.error js/console e)))))

(defn leave
  "Leaves the meeting. If there is no meeting, this method does nothing."
  []
  (js/console.log "LEAVE @call-frame: " @call-frame)
  (try
    (.leave ^js/DailyIframe.callFrame @call-frame)
    (catch
        js/Object e (.error js/console e))))
;;
;; Event listener callbacks and helpers
;;

(defn show-event [e] (.log js/console "callFrame show event" e))
(defn toggle-lobby [e] (.log js/console "callFrame toggle-lobby event" e))
(defn handle-joined-meeting [e] (.log js/console "callFrame handle-joined-meeting event" e))
(defn handle-left-meeting [e] (.log js/console "callFrame handle-left-meeting event" e))

