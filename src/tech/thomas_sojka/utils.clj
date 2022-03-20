(ns tech.thomas-sojka.utils)

(defn format-date [date]
  (.format
   (java.text.SimpleDateFormat. "yyyy-MM-dd")
   date))

