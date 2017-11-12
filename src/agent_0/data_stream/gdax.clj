(ns agent-0.data-stream.gdax
  (:require [agent-0.protocol :refer [Lifecycle start stop
                                      DataStream stream]]
            [keychain.exchange.gdax :refer [subscribe]]
            [clojure.core.async :refer [go-loop alts!]]))

(defn get-event-channels [state]
  (let [get-event-ch #(-> state deref :subscription %)]
    (into [] (map get-event-ch [:connected :closed :errored]))))

(defn start-event-log-appender! [state]
  (let [event-channels (get-event-channels state)]
    (go-loop [[e _] (alts! event-channels)]
      (when e
        (swap! state update-in [:event-log] conj e)
        (recur (alts! event-channels))))))

(defn current-event-type [event-log]
  (-> event-log last second))

(defn status? [this k]
  (= k (current-event-type (-> this :state deref :event-log))))

(defn get-subscription [args]
  (subscribe (select-keys args [:products :channels])))

(defrecord GDAXDataStream [args]

  Lifecycle
  (start [this]
    (let [state (atom {:subscription (get-subscription args)
                       :event-log []})
          _ (start-event-log-appender! state)]
      (assoc this :state state)))

  (stop [this]
    ((-> this :state deref :subscription :close)))

  DataStream
  (stream [this] (-> this :state deref :subscription :feed))

  (connected? [this] (status? this :connected))

  (closed? [this] (status? this :closed))

  (error? [this] (status? this :error)))
