(ns app.call
  (:require
   [clojure.string :as str]
   [reagent.core :as r]
   [refn.core :as rf :refer [<< >>]]
   [app.subs :as subs]
   [app.events :as events]
   [app.daily-co :as d]
   ["react-bootstrap" :refer [Container
                              Row
                              Col
                              Button
                              Form
                              Form.Control
                              Form.Row
                              Form.Group
                              Form.Label
                              Form.Text
                              ResponsiveEmbed]]))


(defn status-update [event-type event]
  (let [action (if event
                 (str ": action: " (:action (js->clj event :keywordize-keys true)))
                 "")
        event-description (str (str/capitalize (name event-type)) action)]
    (js/console.log event-type " event: " event)
    (>> [:status-description event-description])))

(defn show-event [e] (status-update :show-event e))
(defn toggle-lobby [e] (status-update :toggle-lobby e))
(defn handle-joined-meeting [e] (status-update :handle-joined-meeting e))
(defn handle-left-meeting [e] (status-update :handle-left-meeting e))

(defn call []
  (let [call-wrapper-ref (atom nil)]
    (fn []
      (let [call-ready (and @call-wrapper-ref @d/call-frame )]
        ;; (js/console.log "Top of call  @call-wrapper-ref: ", @call-wrapper-ref " call-frame: ", @d/call-frame)
        (when (and @call-wrapper-ref (not @d/call-frame))
          ;; (js/console.log "calling create-call-frame @call-wrapper-ref: ", @call-wrapper-ref " call-frame: ", @d/call-frame)
          (let [result (d/create-call-frame @call-wrapper-ref {:show-event show-event
                                                               :loaded show-event
                                                               :started-camera show-event
                                                               :camera-error show-event
                                                               :joining-meeting toggle-lobby
                                                               :joined-meeting handle-joined-meeting
                                                               :left-meeting handle-left-meeting})]
            result))
        [:> Container {:fluid true }
         [:> Row {:style {:height "100vh"}}
          [:> Col {:id "call-wrapper"
                   :lg 7
                   :ref (fn [el]
                          (reset! call-wrapper-ref el))}]
          [:> Col {:id "controls-col"}
           [:hr]
           [:> Row
            [:> Col
             [:h1 "Call Overview"]]
            ^{key (<< [:reset-count])} ;; To force a refresh of page
            [:> Col
             (<< [:status-description])]]
           [:hr]
           [:> Form
            [:> Form.Group {:as Row
                            :controlId "room-url"}
             [:> Col {:sm 3}
              [:> Form.Label "Room URL"]]
             [:> Col 
              [:> Form.Control {:defaultValue (<< [:room-url])
                                :onChange (fn [e]
                                            (let [value (-> e .-target .-value)]
                                              ;; (js/console.log "form onchange room-url: ", value)
                                              (>> [:room-url value])))}]]]

            [:> Form.Group {:as Row
                            :controlId "meeting-token"}
             [:> Col {:sm 3}
              [:> Form.Label "Meeting Token"]]
             [:> Col
              [:> Form.Control {:as "textarea"
                                :defaultValue (<< [:meeting-token])
                                :onChange (fn [e]
                                            (let [value (-> e .-target .-value)]
                                              ;; (js/console.log "form onchange meeting-token: ", value)
                                              (>> [:meeting-token value])))}]]]

            [:> Form.Group {:as Row
                            :controlId "rtmp-url"}
             [:> Col {:sm 3}
              [:> Form.Label "RTMP Base URL"]]
             [:> Col
              [:> Form.Control {:defaultValue (<< [:rtmp-url])
                                :onChange (fn [e]
                                            (let [value (-> e .-target .-value)]
                                              ;; (js/console.log "form onchange rtmp-url: ", value)
                                              (>> [:rtmp-url value])))}]]]
            [:> Form.Group {:as Row
                            :controlId "rtmp-session-key"}
             [:> Col {:sm 3}
              [:> Form.Label "RTMP Session Key"]]
             [:> Col
              [:> Form.Control {:defaultValue (<< [:rtmp-session-key])
                                :onChange (fn [e]
                                            (let [value (-> e .-target .-value)]
                                              ;; (js/console.log "form onchange rtmp-session-key: ", value)
                                              (>> [:rtmp-session-key value])))}]]]]

           [:> Row
            [:> Col {:sm "auto"}
             [:> Button {:id "start-button"
                         :disabled (empty? (<< [:room-url]))
                         :on-click (fn [e]
                                     (d/join-room {:room-url (<< [:room-url])
                                                   :meeting-token (<< [:meeting-token])})
                                     )} "Start Call"]]
            [:> Col {:sm "auto"}
             ;; (js/console.log "rtmp-full-url: " (<< [:rtmp-full-url]))
             [:> Button {:id "start-streaming"
                         :variant "success"
                         :disabled (not (and call-ready (<< [:rtmp-full-url])))
                         :on-click (fn [e]
                                     (d/start-live-streaming (<< [:rtmp-full-url])))}
              "Start Stream"]]

            [:> Col {:sm "auto"}
             [:> Button {:id "stop-streaming"
                         :variant "warning"
                         :on-click (fn [e]
                                     (d/stop-live-streaming))}
              "Stop Stream"]]

            [:> Col {:sm "auto"}
             [:> Button {:id "reset-button"
                         :variant "warning"
                         :on-click (fn [e]
                                     (d/leave)
                                     (status-update :reset nil)
                                     ;; This is to force a refresh of the page
                                     (>> [:inc-reset-count])
                                     )} "Reset Call"]]]]]]
        ))))
