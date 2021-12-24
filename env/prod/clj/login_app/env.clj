(ns login-app.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[login-app started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[login-app has shut down successfully]=-"))
   :middleware identity})
