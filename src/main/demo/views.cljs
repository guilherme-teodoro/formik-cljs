(ns demo.views
  (:require [reagent.core :as r]
            [goog.format.EmailAddress :as email-address]
            [demo.formik-cljs :as formik]))

(defn email?
  [value]
  (and (string? value) (email-address/isValidAddress value)))

(defn required?
  [value]
  (not (clojure.string/blank? value)))

(defn input
  [{:keys [errors touched]} name title]
  (let [name (if-not (sequential? name)
               (conj [] name)
               name)]
    [formik/fast-field {:name name
                   :render (fn [props]
                             [:label {:class "mb3 db relative"}
                              [:div {:class "f6 mb2"} title]
                              [:input (merge props {:class "w-100 pa2 ba b--black-20 input-reset outline-0 border-box"})]
                              (when (and (get-in errors name) (get-in touched name))
                                [:div {:class "red absolute right-1 top-2"}
                                 (get-in errors name)])])}]))

(defn button
  [on-click label]
  [:button {:on-click on-click
            :class "pa2 db bg-black w-100 white"}
   label])

(defn home
  []
  [:div
   {:class "flex w-100 h-100 justify-center items-center helvetica"}
   [:div {:class "flex mw8 w-100"}
    [formik/formik
     {:initial-values {:email "al123@xerpa.com.br"
                       :age "23"
                       ;; :name "olar"
                       ;; :friends ["olar" "mundo"]
                       :personal {:name "alex"}}
      :on-submit #(console.log %)
      :validate (fn [values _]
                  (-> [{} values]
                      (formik/test-field :name required? "Campo obrigatório")
                      (formik/test-field :email email? "Não é um email")
                      first
                      clj->js))
      :render (fn [{:keys [handle-submit values errors touched] :as data}]
                [:div.flex.w-100
                 [:form {:on-submit handle-submit
                         :class "w-50"}
                  [input data :name "Name"]
                  [input data :email "Email"]
                  [formik/field-array {:name :friends
                                       :render (fn [helpers]
                                                 [:div.mb3
                                                  (map-indexed
                                                   (fn [idx friend]
                                                     [:div {:key idx :class "flex items-end"}
                                                      [:div.flex-auto.mr3
                                                       [input data [:friends (str idx)] (str "Friend #" (inc idx)) ]]
                                                      [:div.flex-none.mb3.w2
                                                       [button #((:remove helpers) idx) "-"]]])
                                                   (:friends values))
                                                  [button #((:push helpers) "") "+"]
                                                  ])}]
                  [button identity "Send"]]
                 [:code {:class "w-50 ml4 pa3 ba b--black-20"} [:pre {:class "ma0"} (with-out-str (cljs.pprint/pprint values))]]
                 ])}]]])

(defn app-root []
  [home])
