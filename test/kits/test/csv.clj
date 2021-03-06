(ns kits.test.csv
  (:use clojure.test)
  (:require
   [kits.csv :as csv]
   [clojure.java.io :as io]
   [kits.foundation :as f]))


(def sample-csv (str (System/getProperty "user.dir")
                     "/samples/sample.csv"))
(def sample-field-reader-opts
  {:key-fn :id
   :val-fn identity
   0 {:label :id :reader f/parse-int}
   1 {:label :left :reader f/parse-int}
   2 {:label :right :reader f/parse-int}
   3 {:label :split_var :reader identity}
   4 {:label :split_point :reader identity}
   5 {:label :status :reader identity}
   6 {:label :prediction :reader identity}})

(deftest parsing-csv-into-nested-maps
  (testing "Given a csv file, generate map of maps {k-fn, the row} "
    (is (= {1
            {:status "1",
             :split_point "1990",
             :prediction "NA",
             :split_var "most_expensive_product_in_cart",
             :right 3,
             :left 2,
             :id 1},
            2
            {:status "1",
             :split_point "0.5",
             :prediction "NA",
             :split_var "os.Windows",
             :right 5,
             :left 4,
             :id 2},
            3
            {:status "1",
             :split_point "1.5",
             :prediction "NA",
             :split_var "cart_actions",
             :right 7,
             :left 6,
             :id 3},
            4
            {:status "1",
             :split_point "8983.5",
             :prediction "NA",
             :split_var "price",
             :right 9,
             :left 8,
             :id 4}}
           (with-open [rdr (io/reader sample-csv)]
             (csv/csv-rows->map (csv/read-csv
                                 rdr
                                 {:skip-header true :delimiter \space})
                                sample-field-reader-opts))))))


(deftest parsing-csv-into-nested-maps-with-columns-excluded
  (testing "Given a csv file, generate map of maps {k-fn, the row} excludes columns specified"
    (is (= {1
            {:status "1",
             :split_point "1990",
             :split_var "most_expensive_product_in_cart",
             :right 3,
             :id 1},
            2
            {:status "1",
             :split_point "0.5",
             :split_var "os.Windows",
             :right 5,
             :id 2},
            3
            {:status "1",
             :split_point "1.5",
             :split_var "cart_actions",
             :right 7,
             :id 3},
            4
            {:status "1",
             :split_point "8983.5",
             :split_var "price",
             :right 9,
             :id 4}}
           (with-open [rdr (io/reader sample-csv)]
             (csv/csv-rows->map (csv/read-csv
                                 rdr
                                 {:skip-header true :delimiter \space})
                                (assoc sample-field-reader-opts
                                  :exclude-columns #{1 6})))))))
