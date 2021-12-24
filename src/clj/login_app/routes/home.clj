(ns login-app.routes.home
  (:require
   [clojure.pprint :refer [pprint]]
   [login-app.layout :as layout]
   [login-app.db.core :as db]
   [clojure.java.io :as io]
   [login-app.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]))

(defn home-page [request]
  (println (:identity request))
  (layout/render request "main.html"))

(defn about-page [request]
  (layout/render request "about.html"))

(defn login-page [request]
  (layout/render request "login.html"))

(defn register-page [request]
  (layout/render request "register.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page
         :middleware [middleware/wrap-redirect]}]
   ["/login" {:get login-page
         :middleware [middleware/wrap-redirect-if-authenticated]}]
   ["/register" {:get register-page
         :middleware [middleware/wrap-redirect-if-authenticated]}]
   ["/about" {:get about-page}]])


