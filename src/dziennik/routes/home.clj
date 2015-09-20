(ns dziennik.routes.home
  (:require [dziennik.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render "home.html"))

(defn profiles-page []
  (layout/render "profiles.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/profiles" [] (profiles-page))
  (GET "/docs" [] (ok (-> "docs/docs.md" io/resource slurp))))

