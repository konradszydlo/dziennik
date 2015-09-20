(ns ^:figwheel-always dziennik.core
  (:require-macros [cljs.core.async.macros :refer (go)])
    (:require [cljs-http.client :as http]
              [cljs.core.async :refer (<!)]
              rum))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(defn nav-link [uri title  ]
    [:li
     [:a {:href uri }
      title]])

(rum/defc navbar < rum/static []
  [:nav.navbar.navbar-inverse.navbar-fixed-top
   [:div.container
    [:div.navbar-header
     [:button.navbar-toggle
      [:span.sr-only]
      [:span.icon-bar]
      [:span.icon-bar]
      [:span.icon-bar]]]
    [:div.navbar-collapse.collapse
     [:ul.nav.navbar-nav
       (nav-link "/" "Home")
       (nav-link "/profiles" "Profiles")]]]])

(def teachers (atom []))
(def teacher-details (atom []))
(def students (atom []))

(defn get-teachers []
  (go (let [response (<! (http/get "/api/teachers"))
            data (:data (:body response))]
        (reset! teachers data))))

(defn get-teacher-details [id]
  (go (let [response (<! (http/get (str "/api/teacher/" id)))
            data (:data (:body response))]
        (reset! teacher-details data))))

(rum/defc li-item < rum/static [[name id]]
  [:li
   [:a
    {:on-click #(get-teacher-details id)} name]])

(rum/defc teacher-details-reactive < rum/reactive []
  (let [{:keys [person/name person/email]} (rum/react teacher-details)]
    (if (not (nil? name))
      [:ul
       [:li "Teacher Details:"]
       [:li "Name: " name]
       [:li "Email: " email]])))

(rum/defc teachers-list < rum/reactive [lst]
  (let [items (rum/react teachers)]
    [:p "Teachers list:"
     [:ul
      (for [item items]
        (li-item item))]
     [:p#teacher-details]]))

(defn get-students []
  (go (let [response (<! (http/get "/api/students"))
            data (:data (:body response))]
        (reset! students data))))

(defn show-students-clicked [old]
  (if (= old "active")
    (do
      (reset! students [])
      "")
    (do
      (get-students)
      "active")))

(defn student-item-click [old]
  (if (= old "success")
    ""
    "success"))

(defn student-added? [class]
  (= class "success"))

(rum/defcs student-item < (rum/local "") [state [name id]]
  (let [local (:rum/local state)]
    [:tr {:class @local}
     [:td name]
     [:td id]
     [:td
      (if (student-added? @local)
        "Added!"
        [:button {:class "btn btn-primary"
                  :on-click #(swap! local student-item-click)}
         "Add student"])]]))

(rum/defc student-table < rum/reactive [students]
  [:table {:class "table table-stripped"}
   [:thead
    [:tr
     [:th "Name"]
     [:th "Id"]
     [:th "Added?"]]]
   [:tbody
    (for [student students]
      (student-item student))]])

(rum/defcs students-button < (rum/local "") [state title]
  (let [local (:rum/local state)]
    [:button {:class @local
              :on-click #(swap! local show-students-clicked)}
     title]))

(rum/defc student-list-comp < rum/reactive []
  [:p
   (students-button "Show students")
   (student-table (rum/react students))])

(defn el [id]
  (.getElementById js/document id))

(defn mount-components []
  (rum/mount (navbar) (el "navbar"))
  (rum/mount (teachers-list) (el "teachers-list"))
  (rum/mount (teacher-details-reactive) (el "teacher-details"))
  (rum/mount (student-list-comp) (el "student-list")))

(defn ^:export init []
  (get-teachers)
  (mount-components))

;; figwheel dev
(defn on-js-reload []
  (mount-components))

