(ns dziennik.db.core
  (:require [datomic.api :as d]))

(defn read-file [s]
  (read-string (slurp s)))

(def uri "datomic:mem://dziennik")

(defn get-conn [uri]
  (d/connect uri))

(defn get-db [uri]
  (d/db (get-conn uri)))

(defn init-db [uri schema seed-data]
  (let [conn (do (d/delete-database uri)
                 (d/create-database uri)
                 (get-conn uri))]
    @(d/transact conn schema)
    @(d/transact conn seed-data)
    (d/db conn)))

(let [schema (read-file "resources/db/schema.edn")
      seed-data (read-file "resources/db/data.edn")]
  (init-db uri schema seed-data))

(defn find-person-entity-by-name [name]
  (d/q '[:find ?e
         :in $ ?name
         :where [?e :person/name ?name]]
       (get-db uri) name))

(def joseph-conrad
  (ffirst (find-person-entity-by-name "Joseph Conrad")))

(d/pull
  (get-db uri)
  '[*]
  joseph-conrad)

(def tom-hardy
  (ffirst (find-person-entity-by-name "Tom Hardy")))

(d/pull
  (get-db uri)
  '[*]
  tom-hardy)


(defn find-all-teachers []
  (d/q '[:find ?name ?e
         :where [?e :person/role :role.type/teacher ]
         [?e :person/name ?name]]
       (get-db uri)))

(find-all-teachers)

(second (first (find-all-teachers)))

(defn get-teacher-details-by-id [id]
  (d/pull
    (get-db uri)
    '[:person/name :person/email]
    id))


(defn get-students-by-teacher-id [id]
  (d/q '[:find ?name ?s
         :in $ ?id
         :where
         [?s :student/teacher ?id]
         [?s :person/name ?name]]
       (get-db uri) id))

(defn find-all-students []
  (d/q '[:find ?name ?e
         :where [?e :person/role :role.type/student]
         [?e :person/name ?name]]
       (get-db uri)))

(find-all-students)

(d/pull
  (get-db uri)
  '[:person/name :person/email]
  (second (first (find-all-teachers))))


