(ns agent-0.protocol)

(defprotocol Lifecycle
  (start [this])
  (stop [this]))

(defprotocol DataStream
  (stream [this])
  (closed? [this])
  (error? [this])
  (connected? [this]))

(defprotocol OrderBook
  (snapshot [this]))
