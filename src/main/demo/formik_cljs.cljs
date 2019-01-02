(ns demo.formik-cljs
  (:require ["formik" :refer [Formik Field FieldArray FastField]]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [reagent.core :as r]))

(defn recursive-camelcase->kebab-keyword
  "Convert all keywords in coll from kebab to camel case"
  [coll]
  (walk/postwalk #(if (and (keyword? %) (not (namespace %)))
                    (camelcase->kebab-keyword %)
                    %)
                 coll))

(defn camelcase->kebab-keyword [k]
  (some-> k
          (name)
          (string/replace #"([a-z])([A-Z])" "$1-$2")
          (string/lower-case)
          (keyword)))

(defn keywordize-map
  [m]
  (->> m
       (map (fn [[k v]]
              [k (js->clj v :keywordize-keys true)]))
       (into {})))

(defn normalize-data
  [data]
  (-> data
       (recursive-camelcase->kebab-keyword)
       (keywordize-map)))

(defn key-or-path->name
  [k-or-path]
  "Give key or path return a string to be used as name for inputs"
  (if (sequential? k-or-path)
    (->> (map name k-or-path)
         (string/join "."))
    (name (or k-or-path ""))))

(defn formik
  [{:keys [render initial-values on-submit validate] :as props}]
  [:> Formik {:initial-values initial-values
              :on-submit #(on-submit (js->clj % :keywordize-keys true))
              :validate #(validate (-> %1 (js->clj :keywordize-keys true)
                                       (recursive-camelcase->kebab-keyword)))}
   #(r/as-element (render (-> %
                              (js->clj :keywordize-keys true)
                              (normalize-data))))])


(defn field-array
  [{:keys [render name]}]
  [:> FieldArray {:name   (key-or-path->name name)
                  :render #(r/as-element (render (-> %
                                                     (js->clj :keywordize-keys true)
                                                     (recursive-camelcase->kebab-keyword))))}])
(defn field
  [{:keys [render name]}]
  [:> Field {:name   (key-or-path->name name)
             :render #(r/as-element (render (-> (.-field %)
                                                (js->clj :keywordize-keys true)
                                                (recursive-camelcase->kebab-keyword))))}])
(defn fast-field
  [{:keys [render name]}]
  [:> FastField {:name   (key-or-path->name name)
             :render #(r/as-element (render (-> (.-field %)
                                                (js->clj :keywordize-keys true)
                                                (recursive-camelcase->kebab-keyword))))}])

(defn assoc-if
  [m test path v]
  (if test
    (assoc-in m path v)
    m))

(defn test-field
  [[m values] k-or-path test message]
  (let [path (if-not (sequential? k-or-path)
               (conj [] k-or-path)
               k-or-path)]
    [(assoc-if m (-> (get-in values path)
                     test
                     not)
               path message) values]))
