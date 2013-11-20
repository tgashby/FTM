package agents;

import java.lang.Double;
import java.util.LinkedList;

public class TrendAgent {
   private static int NUM_VALUES = 50;
   private LinkedList<Double> values = new LinkedList<Double>();
   private LinkedList<Double> stockProvider;
   private int numStocks;
   private double wallet;
   private double lastValue;
   
   public static void main(String [] args) {
      TrendAgent tA = new TrendAgent();
      tA.run();
   }
   
   public void run() {
      double lastAverage;
      double thisAverage;
      
      for (int i = 0; i < NUM_VALUES; i ++) {
         values.addFirst(getNextStockValue());
      }
      
      lastAverage = getAverage();
      
      while(stockProvider.size() > 0) {
         thisAverage = getAverage();
         if (thisAverage > lastAverage) {
            buy();
         }
         else if (thisAverage < lastAverage) {
            sell();
         }
         values.removeLast();
         values.addFirst(getNextStockValue());
      }   
   }
   
   private void buy() {
      if (wallet > lastValue) {
         wallet -= lastValue;
         numStocks++;
      }
   }
   
   private void sell() {
      if (numStocks > 0) {
         wallet += lastValue;
         numStocks--;
      }
   }
   
   private double getAverage() {
      double total = 0;
      
      for (int i = 0; i < NUM_VALUES; i++) {
         total += values.get(i); 
      }
      
      return total / NUM_VALUES;
   }
   
   private double getNextStockValue(){
      if (stockProvider == null) {
         stockProvider = new LinkedList<Double>();
         //get the stock provider
         //While there are stocks add them to the end of the stockProvider list
      }
      lastValue = stockProvider.getFirst();
      return lastValue;
   }
}
