(ns login-app.auth
  (:require [buddy.hashers :as hashers]
            [next.jdbc :as jdbc]
            [buddy.auth :refer [authenticated?]]
            [login-app.db.core :as db]))


(defn create-user! [login password]
  (jdbc/with-transaction [t-conn db/*db*]
    (if-not (empty? (db/get-user-for-auth* t-conn {:login login}))
      (throw (ex-info "User already exists!"
                      {:login-app/error-id ::duplicate-user
                       :error "User already exists!"}));
      (db/create-user!* t-conn
                        {:login login
                         :password (hashers/derive password)}))))


(defn authenticate-user [login password]
  (let [{hashed :password :as user} (db/get-user-for-auth* {:login login})]
    (when (hashers/check password hashed)
      (dissoc user :password))))

(def rules
  [{:uri "/"
    :handler authenticated?}])


;; (defn login! [{:keys [params session]}]
;;   (when (= user params)
;;     (-> "ok"
;;         response
;;         (content-type "text/html")
;;         (assoc :session (assoc session :identity "foo")))))

