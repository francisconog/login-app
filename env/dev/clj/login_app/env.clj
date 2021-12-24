(ns login-app.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [login-app.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[login-app started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[login-app has shut down successfully]=-"))
   :middleware wrap-dev})
