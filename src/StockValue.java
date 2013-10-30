import java.sql.Time;
import java.sql.Date;

public class StockValue {
   protected String id;
   protected String name;
   protected Date date;
   protected Time time;
   protected double value;

   StockValue(String i, String n, Date d, Time t, double v) {
      id = i;
      name = n;
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

   String getName() {
      return name;
   }

   void setName(String n) {
      name = n;
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
