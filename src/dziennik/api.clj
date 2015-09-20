(ns dziennik.api
  (:require [ring.util.http-response :refer [ok]]
    [compojure.api.sweet :refer [defapi context* GET*]]
            [dziennik.db.core :refer [find-all-students find-all-teachers get-teacher-details-by-id get-students-by-teacher-id]]
            ))

(defapi api-routes
  {:formats [:edn]}
  (context* "/api" []
    (GET* "/teachers" [] (ok {:data (find-all-teachers)}))
    (GET* "/teacher/:id" [] :path-params [id :- Long] (ok {:data (get-teacher-details-by-id id)}))
    (GET* "/teacher/:id/students" [] :path-params [id :- Long] (ok {:data (get-students-by-teacher-id id)}))
    (GET* "/students" [] (ok {:data (find-all-students)}))))


