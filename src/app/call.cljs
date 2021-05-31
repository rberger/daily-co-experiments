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
      [:<>
       [:> Container {:fluid true }
        [:> Row {:style {:height "100vh"}}
         [:> Col {:sm 9
                  :id "call-wrapper"
                  :ref (fn [el]
                         (reset! call-wrapper-ref el))}]
         [:> Col
          [:> Form
           [:> Form.Row
            [:> Form.Group {:controlId "room-url"}]
            [:> Form.Text (str "Current Room url: " (<< [:room-url]))]
            [:> Form.Label "Room URL"]
            [:> Form.Control {:onChange (fn [e]
                                          (let [value (-> e .-target .-value)]
                                            (js/console.log "form onchange room-url: ", value)
                                            (>> [:room-url value])))}]]]]
         [:> Row
          [:> Col
           [:> Button {:id "start-button"
                       :on-click (fn [e]
                                   (js/console.log "Start On-click e: ", e)
                                   (when (and @call-wrapper-ref @d/call-frame )
                                     (d/join-room (<< [:room-url])))
                                   )} "Start Call"]]
          [:> Col
           [:> Button {:id "stop-button"
                       :on-click (fn [e]
                                   (js/console.log "Reset On-click e: ", e)
                                   (js/console.log "call-wrapper-ref: ", @call-wrapper-ref, " call-frame: ", @d/call-frame)
                                   (when (and @call-wrapper-ref @d/call-frame )
                                     (d/leave))
                                   )} "Reset Call"]]]]]]
      )))
