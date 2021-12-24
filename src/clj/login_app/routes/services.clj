(ns login-app.routes.services
  (:require [login-app.middleware :as middleware]
            [login-app.auth :as auth]
            [clojure.pprint :refer [pprint]]
            [ring.util.http-response :as response]))

(defn service-routes []
  ["/api"
   {:middleware [middleware/wrap-formats]}
   ["/login"
    {:post    {:params
               {:login string?
                :password string?}
               :responses
               {200
                {:body
                 {:identity
                  {:login string?
                   :created-at inst?}}}
                401
                {:body
                 {:message string?}}}
               :handler
               (fn [{{:keys [login password]} :params
                     session :session}]
                ;;  (pprint {login password})
                 (if-some [user (auth/authenticate-user login password)]
                   (-> (response/found "/")
                       (assoc :session (assoc session
                                              :identity
                                              user))
                       (assoc :body {:identity user}))
                   (response/unauthorized
                    {:message "Incorrect login or password."})))}}]

   ["/register"
    {:post {:params
            {:login string?
             :password string?
             :confirm string?}}
     :responses
     {200
      {:body
       {:message string?}}
      400
      {:body
       {:message string?}}
      409
      {:body
       {:message string?}}}
     :handler
     (fn [{{:keys [login password confirm]} :params}]
       (if-not (= password confirm)
         (response/bad-request {:message "Password and confirm do not match."})
         (try
           (auth/create-user! login password)
           (-> (response/found "/login")
               (assoc :message "User registration successful. Please log in.")
               )
           (catch clojure.lang.ExceptionInfo e
             (if (= (:login-app/error-id (ex-data e))
                    ::auth/duplicate-user)
               (response/conflict
                {:message "Registration failed! User with login already exists"})
               (throw e))))))}]
   ["/logout"
    {:post {:handler
            (fn [_]
              (-> (response/found "/login")
                  (assoc :session nil)))}}]])
