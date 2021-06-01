(ns app.call
  (:require
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


(defn call []
  (let [call-wrapper-ref (atom nil)]
    (fn []
      (js/console.log "Top of call  @call-wrapper-ref: ", @call-wrapper-ref " call-frame: ", @d/call-frame)
      (when (and @call-wrapper-ref (not @d/call-frame))
        (js/console.log "calling create-call-frame @call-wrapper-ref: ", @call-wrapper-ref " call-frame: ", @d/call-frame)
        (let [result (d/create-call-frame @call-wrapper-ref {:loaded d/show-event
                                                             :started-camera d/show-event
                                                             :camera-error d/show-event
                                                             :joining-meeting d/toggle-lobby
                                                             :joined-meeting d/handle-joined-meeting
                                                             :left-meeting d/handle-left-meeting})]
          (js/console.log "RESULT: ", result)
          result))
      [:> Container {:fluid true }
       [:> Row {:style {:height "100vh"}}
        [:> Col {:id "call-wrapper"
                 :sm 8
                 :ref (fn [el]
                        (reset! call-wrapper-ref el))}]
        [:> Col {:id "controls-col"}
         [:hr]
         [:h1 "Call Overview"]
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
                                            (js/console.log "form onchange room-url: ", value)
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
                                            (js/console.log "form onchange meeting-token: ", value)
                                            (>> [:meeting-token value])))}]]]]
         [:> Row
          [:> Col {:sm 3}
           [:> Button {:id "start-button"
                       :on-click (fn [e]
                                   (js/console.log "Start On-click e: ", e)
                                   (when (and @call-wrapper-ref @d/call-frame )
                                     (d/join-room {:room-url (<< [:room-url])
                                                   :meeting-token (<< [:meeting-token])}))
                                   )} "Start Call"]]
          [:> Col {:sm 3}
           [:> Button {:id "stop-button"
                       :on-click (fn [e]
                                   (js/console.log "Reset On-click e: ", e)
                                   (js/console.log "call-wrapper-ref: ", @call-wrapper-ref, " call-frame: ", @d/call-frame)
                                   (when (and @call-wrapper-ref @d/call-frame )
                                     (d/leave))
                                   )} "Reset Call"]]]]]]
      )))
