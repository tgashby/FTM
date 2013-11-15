package common; /**
 * This is our basic stock value class.
 *
 * FYI tag is a horrible horrible person
 */

import java.sql.Time;
import java.sql.Date;

public class Stock {
   protected String symbol;
   protected String name;
   protected Date date;
   protected Time time;
   protected double value;

   public Stock(String s, String n, Date d, Time t, double v) {
      symbol = s;
      name = n;
      date = d;
      time = t;
      value = v;
   }

   public double getValue() {
      return value;
   }

   void setValue(Double v) {
      value = v;
   }

   public String getSymbol() {
      return symbol;
   }

   void setSymbol(String s) {
      symbol = s;
   }

   public String getName() {
      return name;
   }

   void setName(String n) {
      name = n;
   }

   public Date getDate() {
      return date;
   }

   void setDate(Date d) {
      date = d;
   }

   public Time getTime() {
      return time;
   }

   void setTime(Time t) {
      time = t;
   }
}
