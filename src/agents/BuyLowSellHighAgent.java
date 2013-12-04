package agents;


import common.Stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 7:00 AM
 */
public class BuyLowSellHighAgent extends Agent{


    private ArrayList<String> stockSymbolsToTrade;
    private HashMap<String, Integer> numberOfShares;
    private HashMap<String, Double> lastValues;
    private HashMap<String, ArrayBlockingQueue<Stock>> stockValueQueues;
    private HashMap<String, BasicStatistics> basicStatistics;

    private int sampleSize=10;


    public BuyLowSellHighAgent(double capital){

        super(capital);
        stockSymbolsToTrade = new ArrayList<String>() {{
            add("TWTR");
            add("VZ");
            add("KR");
            add("BKW");
            add("GOOG");
            add("MSFT");
            add("OLN");
            add("BA");
            add("MSI");
            add("TDC");
        }};
        numberOfShares = new HashMap<String, Integer>(stockSymbolsToTrade.size());
        lastValues = new HashMap<String, Double>(stockSymbolsToTrade.size());
        stockValueQueues = new HashMap<String, ArrayBlockingQueue<Stock>>(stockSymbolsToTrade.size());
        basicStatistics = new HashMap<String, BasicStatistics>(stockSymbolsToTrade.size());
        for (int i = 0; i < stockSymbolsToTrade.size(); i++) {
            numberOfShares.put(stockSymbolsToTrade.get(i), 0);
            stockValueQueues.put(stockSymbolsToTrade.get(i), new ArrayBlockingQueue<Stock>(sampleSize));
            basicStatistics.put(stockSymbolsToTrade.get(i), new BasicStatistics(sampleSize));
        }
    }

    public void trade(Stock stock){
        if (stockSymbolsToTrade.contains(stock.getSymbol())) {
            lastValues.put(stock.getSymbol(), stock.getValue());
            if (stockValueQueues.get(stock.getSymbol()).size() < sampleSize) {
                stockValueQueues.get(stock.getSymbol()).add(stock);
                basicStatistics.get(stock.getSymbol()).add(stock.getValue());
            }
            else{
                double mean = basicStatistics.get(stock.getSymbol()).getMean();
                int currentNumberOfShares = numberOfShares.get(stock.getSymbol());

                if (stock.getValue() < mean) {
                    //Low, buy 1 share
                    if (wallet > stock.getValue()) {
                        wallet -= stock.getValue();
                        numberOfShares.put(stock.getSymbol(), currentNumberOfShares + 1);
                        //System.out.println("buying a share at $" + curValue[counter]);
                    }
                }
                else if (stock.getValue() > mean) {
                    //High, sell 1 share
                    if (currentNumberOfShares > 0) {
                        numberOfShares.put(stock.getSymbol(), currentNumberOfShares - 1);
                        wallet += stock.getValue();;
                        //System.out.println("selling a share at $" + curValue[counter]);
                    }
                }
            }

        }
    }
    public String getAgentName(){
        return "Buy Low Sell High Agent";
    }
    public int getTotalNumberOfStocks(){
        int sum = 0;

        for (int i = 0; i < numberOfShares.size(); i++)
            sum += numberOfShares.get(stockSymbolsToTrade.get(i));
        return sum;

    }
    public double getNetWorth(){
        int portfolioWorth = 0;

        for (int i = 0; i < numberOfShares.size(); i++)
            portfolioWorth += numberOfShares.get(stockSymbolsToTrade.get(i)) * lastValues.get(stockSymbolsToTrade.get(i));
        return wallet + portfolioWorth;
    }


    private class BasicStatistics {
        private ArrayBlockingQueue<Double> sample;
        private double mean;
        private double standardDeviation;

        public BasicStatistics(int sampleSize) {
            sample = new ArrayBlockingQueue<Double>(sampleSize);
        }

        public void add(double sampleValue) {
            int oldSampleSize = sample.size();
            sample.add(sampleValue);
            int newSampleSize = sample.size();

            //update mean
            double sum = mean * oldSampleSize;
            mean = (sum + sampleValue) / newSampleSize;
        }

        public void removeOldestValue() {
            int oldSampleSize = sample.size();
            double removedItem = sample.remove();
            int newSampleSize = sample.size();

            //update mean
            double sum = mean * oldSampleSize;
            mean = (sum - removedItem) / newSampleSize;
        }

        public double getMean() {
            return mean;
        }

        public double getStandardDeviation() {
            Iterator<Double> iterator = sample.iterator();
            double mean = getMean();
            double temp = 0;

            while (iterator.hasNext()) {
                double next = iterator.next();
                temp += (mean - next) * (mean - next);
            }

            return Math.sqrt(temp / sample.size());
        }
    }
}
