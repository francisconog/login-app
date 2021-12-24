(ns login-app.repl
  (:require [user]
            [mount.core :as mount]
            [login-app.db.core]))

;; Rodar esses comandos no namespace user
(user/start)
(mount/start #'login-app.db.core/*db*)
(user/create-migration "add-feed-migration")