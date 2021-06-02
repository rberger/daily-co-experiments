(ns app.events
  (:require
   [re-frame.core :as rf
    :refer [reg-event-db reg-event-fx]]
   [refn.core :refer [>> << defx
                      assoc-db]]))

(reg-event-db :rf-count (assoc-db :rf-count))

(reg-event-db :room-url (assoc-db :room-url))
(reg-event-db :meeting-token (assoc-db :meeting-token))
(reg-event-db :rtmp-url (assoc-db :rtmp-url))
(reg-event-db :rtmp-session-key (assoc-db :rtmp-session-key))

(reg-event-db :status-description (assoc-db :status-description))

(reg-event-db :inc-reset-count
              (fn [db _]
                (update db :reset-count inc)))
