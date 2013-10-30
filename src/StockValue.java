import java.sql.Time;
import java.sql.Date;

public class StockValue {
   protected String id;
   protected Date date;
   protected Time time;
   protected double value;

   StockValue(String i, Date d, Time t, double v) {
      id = i;
      date = d;
      time = t;
      value = v;
   }

   double getValue() {
      return value;
   }

   void setValue(Double v) {
      value = v;
   }

   String getId() {
      return id;
   }

   void setId(String i) {
      id = i;
   }

   Date getDate() {
      return date;
   }

   void setDate(Date d) {
      date = d;
   }

   Time getTime() {
      return time;
   }

   void setTime(Time t) {
      time = t;
   }
}
