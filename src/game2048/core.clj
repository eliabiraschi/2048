(ns game2048.core
  (:gen-class))

(defn board
  []
  (vec (repeat 16 0)))

(defn get-empty-index!
  [board]
  (loop [index (rand-int 16)]
    (let [value (get board index)]
      (if (not= value 0)
        (recur (rand-int 16))
        index))))

(defn get-rand-value!
  []
  (let [score (rand-int 10)]
    (if (> score 8) 4 2)))

(defn add-tile!
  [board]
  (let [index (get-empty-index! board)
        value (get-rand-value!)]
    (assoc board index value)))

(defn draw!
  [board]
  (doseq [[i cell] (map-indexed vector board)]
    (print "| " cell " ")
    (when (= (rem (+ i 1) 4) 0) (print "|\n"))))

(defn handle-input!
  []
  ; TODO do nothing if the input is invalid
  (read-line))

(defn get-columns
  [board]
  (vec (for [x (range 4)]
         (vec (for [y (range 4)]
                (get board (+ x (* y 4))))))))

(defn get-rows
  [board]
  (vec (for [x (range 4)]
         (vec (for [y (range 4)]
                (get board (+ y (* x 4))))))))

(defn collapse
  [line])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
