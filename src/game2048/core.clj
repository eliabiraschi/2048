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

(defn get-columns [board]
  (let [size (Math/sqrt (count board))]
    (vec (map (fn [i] (vec (take-nth size (drop i board)))) (range size)))))


(defn get-rows [board]
  (->> board
       (partition 4)
       (vec)
       (map vec)))

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
   line
   (filter #(not= % 0))
   ((fn [x] (loop [line x result []]
              (let [a (first line)
                    b (second line)]
                (if (= b nil) (concat result line)
                    (if (summable-cells? a b)
                      (recur (rest (rest line)) (conj result (+ a b)))
                      (recur (rest line) (conj result a))))))))
   ((fn [x] (concat x (repeat 4 0))))
   (take 4)
   (vec)))

(defn collapse-line
  [line]
  (cond
    (every? #(= 0 %) line) line
    (line-is-stuck? line) line
    :else (collapse-cells line)))

(defn back-collapse-line
  [line]
  (cond
    (every? #(= 0 %) line) line
    (line-is-stuck? line) line
    :else (->>
           (reverse line)
           (collapse-cells)
           (reverse)
           (vec))))

(defn collapse-left
  [board]
  (->>
   (for [line (get-rows board)]
     (collapse-line line))
   (flatten)
   (vec)))

(defn collapse-up
  [board]
  (->>
   (for [line (get-columns board)]
     (collapse-line line))
   (apply interleave)
   (vec)))

(defn collapse-right
  [board]
  (->>
   (for [line (get-rows board)]
     (back-collapse-line line))
   (flatten)
   (vec)))

(defn collapse-down
  [board]
  (->>
   (for [line (get-columns board)]
     (back-collapse-line line))
   (apply interleave)
   (vec)))

(defn -main
  []
  (loop [board (add-tile! (board))]
    (draw! board)
    (println "Enter a command (q to quit):")
    (let [input (handle-input!)]
      (cond
        (= input "q") (println "Quitting...")
        (= input "w") (recur (add-tile! (collapse-up board)))
        (= input "a") (recur (add-tile! (collapse-left board)))
        (= input "s") (recur (add-tile! (collapse-down board)))
        (= input "d") (recur (add-tile! (collapse-right board)))
        :else (do (println "Command recognized:" input) (recur board))))))