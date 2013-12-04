package agents;

import java.lang.Double;
import java.util.LinkedList;

import common.Stock;

public class TrendAgent extends Agent{
   private static int NUM_VALUES = 50;
   
   private LinkedList<Double> values;
   private int numStocks;
   private double lastValue;
   private double lastAverage;
   private double thisAverage;
   
   public TrendAgent(double initialCapital){
      super(initialCapital);
      values = new LinkedList<Double>();
   }
   
   public void trade(Stock stock){
      lastValue = stock.getValue();
   
      //if we dont have 50 values yet just add them to the list
      if (values.size() < NUM_VALUES) {
         values.add(lastValue);
         //If we finally have 50 values lets make it the last average
         if (values.size() == NUM_VALUES) {
            lastAverage = getAverage();
         }
      }
      //Actually make some decisions and look at the trend
      else {
         values.removeLast();
         values.addFirst(lastValue);
         thisAverage = getAverage();
         
         if (thisAverage > lastAverage) {
            buy();
         }
         else if (thisAverage < lastAverage) {
            sell();
         }
         
         lastAverage = thisAverage;
      }
   }
   
   public String getAgentName(){
      return "Trend Agent";
   }
   
   public int getTotalNumberOfStocks(){
      return numStocks;
   }
   
   public double getNetWorth(){
      return wallet + (numStocks*lastValue);
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
}
