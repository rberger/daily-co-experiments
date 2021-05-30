(ns app.call
  (:require
   [reagent.core :as r]
   [refn.core :as rf :refer [<< >>]]
   [app.subs :as subs]
   [app.events :as events]
   [app.daily-co :as d]
   ["react-bootstrap" :refer [Container Row Col Form Button Form.Control Form.Group Form.Label Form.Text]]))


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
       [:> Container
        [:> Row
         [:> Col {:id "call-wrapper"
                  :ref (fn [el]
                         (reset! call-wrapper-ref el))}]]
        [:> Row
         [:> Col
          [:> Form
           [:> Form.Group {:controlId "room-url"}]
           [:> Form.Text (str "Current Room url: " (<< [:room-url]))]
           [:> Form.Label "Room URL"]
           [:> Form.Control {:onChange (fn [e]
                                         (let [value (-> e .-target .-value)]
                                           (js/console.log "form onchange room-url: ", value)
                                           (>> [:room-url value])))}]]
          ]]
        [:> Row
         [:> Col
          [:> Button {:id "join-button"
                      :on-click (fn [e]
                                  (js/console.log "Jon On-click e: ", e)
                                  (js/console.log "call-wrapper-ref: ", @call-wrapper-ref, " call-frame: ", @d/call-frame)
                                  (when (and @call-wrapper-ref @d/call-frame )
                                    (d/join-room (<< [:room-url])))
                                  )} "Join Call"]]]
        ]])))
