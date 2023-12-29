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

(defn summable-cells?
  [a b]
  (= a b))

(defn slideable-cells?
  [a b]
  (and (= a 0) (not= b 0)))

(defn cells-stuck?
  [a b]
  (or
   (and (= a 0) (= b 0))
   (and
    (not (summable-cells? a b))
    (not (slideable-cells? a b)))))

(defn line-is-stuck?
  [line]
  (loop [line line result true]
    (let [a (first line)
          b (second line)]
      (if (= b nil) result
          (recur (rest line) (and result (cells-stuck? a b)))))))

(defn collapse-cells
  [line]
  (->>
   (cond
     (and
      (summable-cells? (first line) (second line))
      (summable-cells? (nth line 2) (nth line 3)))
     [(+ (first line) (second line)) (+ (nth line 2) (nth line 3)) 0 0]
     (summable-cells? (first line) (second line))
     [(+ (first line) (second line)) (nth line 2) (nth line 3) 0]
     (summable-cells? (second line) (nth line 2))
     [(first line) (+ (second line) (nth line 2)) (nth line 3) 0]
     (summable-cells? (nth line 2) (nth line 3))
     [(first line) (second line) (+ (nth line 2) (nth line 3)) 0]
     :else line)
   (filter #(not= % 0))
   ((fn [x] (concat x (repeat 4 0))))
   (take 4)
   (vec)))


(defn collapse-line
  [line]
  (cond
    (every? #(= 0 %) line) line
    (line-is-stuck? line) line
    :else (collapse-cells line)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
