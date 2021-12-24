;; (ns login-app.testingcode
;;   (:require
;;    [buddy.auth.middleware :refer [wrap-authentication]]
;;    [buddy.auth.backends.session :refer [session-backend]]
;;    [buddy.auth.accessrules :refer [restrict]]
;;    [buddy.auth :refer [authenticated?]]
;;    [login-app.middleware :as middleware]
;;    [reitit.ring :as ring]
;;    [reitit.core :as r]))


;; (defn handler [_]
;;   {:status 200, :body "ok"})
;; (handler :1)
;; (def router
;;   (ring/router
;;    ["/ping" {:get handler}]))


;; (def app (ring/ring-handler router))

;; (app {:request-method :get, :uri "/ping"})

;; (-> app (ring/get-router) (r/match-by-name ::ping) (r/match->path))

;; (defn handler [{::keys [acc]}]
;;   {:status 200, :body (conj acc :handler)})

;; (handler [3])

;; (defn wrap [handler id]
;;   (fn [request]
;;     (handler (update request ::acc (fnil conj []) id))))

;; (defn handler [{::keys [acc]}]
;;   {:status 200, :body (conj acc :handler)})

;; (handler {:keys :acc})
;; {::keys [:acc]}
;; ::keys
;; (def app
;;   (ring/ring-handler
;;    (ring/router
;;       ;; a middleware function
;;     ["/api" {:middleware [#(wrap % :api)]}
;;      ["/ping" handler]
;;        ;; a middleware vector at top level
;;      ["/admin" {:middleware [[wrap :admin]]}
;;       ["/db" {:middleware [[wrap :db]]
;;                 ;; a middleware vector at under a method
;;               :delete {:middleware [[wrap :delete]]
;;                        :handler handler}}]]])))


;; (app {:request-method :, :uri "/api/admin/db"})

;; (handler [#(wrap % :db)])


