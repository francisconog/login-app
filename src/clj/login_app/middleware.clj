(ns login-app.middleware
  (:require
   [login-app.env :refer [defaults]]
   [clojure.tools.logging :as log]
   [login-app.layout :refer [error-page]]
   [login-app.auth :as auth]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [login-app.middleware.formats :as formats]
   [muuntaja.middleware :refer [wrap-format wrap-params]]
   [login-app.config :refer [env]]
   [ring.middleware.flash :refer [wrap-flash]]
   [ring.util.http-response :as response]
   [ring.util.response :refer [redirect]]
   [immutant.web.middleware :refer [wrap-session]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
   [buddy.auth.accessrules :refer [restrict wrap-access-rules]]
   [buddy.auth :refer [authenticated?]]
   [buddy.auth.backends.session :refer [session-backend]]))

(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (log/error t (.getMessage t))
        (error-page {:status 500
                     :title "Something very bad has happened!"
                     :message "We've dispatched a team of highly trained gnomes to take care of the problem."})))))

(defn wrap-csrf [handler]
  (wrap-anti-forgery
   handler
   {:error-response
    (error-page
     {:status 403
      :title "Invalid anti-forgery token"})}))


(defn wrap-formats [handler]
  (let [wrapped (-> handler wrap-params (wrap-format formats/instance))]
    (fn [request]
      ;; disable wrap-formats for websockets
      ;; since they're not compatible with this middleware
      ((if (:websocket? request) handler wrapped) request))))

(defn on-error [request response]
  (error-page
   {:status 403
    :title (str "Access to " (:uri request) " is not authorized")}))

(defn wrap-restricted [handler]
  (restrict handler {:handler authenticated?
                     :on-error on-error}))

(defn on-error-redirect [request response uri]
  (redirect uri))

(defn wrap-redirect 
  "redirect page if not authenticated. Default uri is to /login"
  [handler & [uri]]
  (let [uri (or uri "/login")]
    (restrict handler {:handler authenticated?
                       :on-error #(on-error-redirect %1 %2 uri)})))


(defn wrap-redirect-if-authenticated
  "redirect page if authenticated. Default uri is to /login"
  [handler & [uri]]
  (let [uri (or uri "/")]
    (restrict handler {:handler #(not (authenticated? %))
                       :on-error #(on-error-redirect %1 %2 uri)})))

(defn wrap-auth [handler]
  (let [backend (session-backend)]
    (-> handler
        (wrap-authentication backend)
        (wrap-authorization backend))))

(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      wrap-auth
      wrap-flash
      (wrap-session {:cookie-attrs {:http-only true}})
      (wrap-defaults
       (-> site-defaults
           (assoc-in [:security :anti-forgery] false)
           (dissoc :session)))
      wrap-internal-error))
