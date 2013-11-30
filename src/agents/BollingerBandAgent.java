package agents;

import common.StockValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: allen
 * Date: 11/5/13
 * Time: 9:45 PM
 */
public class BollingerBandAgent implements AgentInterface {
    private StockValue stockValue;
    private ArrayList<String> stockSymbolsToTrade;
    private double wallet;
    private HashMap<String, Integer> numberOfShares;
    private HashMap<String, Double> lastValues;
    private double intiialWallet;

    private int movingAverageSampleSize;
    private int bandWidth;
    private HashMap<String, ArrayBlockingQueue<StockValue>> stockValueQueues;
    private HashMap<String, BasicStatistics> basicStatistics;

    public BollingerBandAgent() {
        ArrayList<String> stockSymbolsToTrade = new ArrayList<String>()
        {{
        add("VZ");
        add("KR");
        add("BKW");
        add("GOOG");
        add("MSFT");
        add("OLN");
        add("BA");
        add("TDC");
        }};

        initValues(stockSymbolsToTrade, 50000, 40, 2);
    }

    public BollingerBandAgent(ArrayList<String> stockSymbolsToTrade, int wallet, int movingAverageSampleSize, int bandWidth) {
        initValues(stockSymbolsToTrade, wallet, movingAverageSampleSize, bandWidth);
    }

    private void initValues(ArrayList<String> stockSymbolsToTrade, int wallet, int movingAverageSampleSize, int bandWidth) {
        this.stockSymbolsToTrade = stockSymbolsToTrade;
        this.wallet = wallet;
        this.movingAverageSampleSize = movingAverageSampleSize;
        this.bandWidth = bandWidth;
        numberOfShares = new HashMap<String, Integer>(stockSymbolsToTrade.size());
        lastValues = new HashMap<String, Double>(stockSymbolsToTrade.size());
        stockValueQueues = new HashMap<String, ArrayBlockingQueue<StockValue>>(stockSymbolsToTrade.size());
        basicStatistics = new HashMap<String, BasicStatistics>(stockSymbolsToTrade.size());

        for (int i = 0; i < stockSymbolsToTrade.size(); i++) {
            stockValueQueues.put(stockSymbolsToTrade.get(i), new ArrayBlockingQueue<StockValue>(movingAverageSampleSize));
            basicStatistics.put(stockSymbolsToTrade.get(i), new BasicStatistics(movingAverageSampleSize));
            numberOfShares.put(stockSymbolsToTrade.get(i), 0);
        }

        this.intiialWallet = wallet;
    }

    @Override
    public void trade(StockValue stockValue) {
        if (stockSymbolsToTrade.contains(stockValue.getSymbol())) {
            //set last values
            lastValues.put(stockValue.getSymbol(), stockValue.getValue());
            //initialize q with 1st movingAverageSampleSize stocks. Temporary until market method is created to do this.
            if (stockValueQueues.get(stockValue.getSymbol()).size() < movingAverageSampleSize) {
                stockValueQueues.get(stockValue.getSymbol()).add(stockValue);
                basicStatistics.get(stockValue.getSymbol()).add(stockValue.getValue());
            }
            else if (stockValueQueues.get(stockValue.getSymbol()).size() == movingAverageSampleSize) {
                doTrade(stockValue);
                refreshQAndStats(stockValue);
                doTrade(stockValue);
            }
            else {
                refreshQAndStats(stockValue);
                doTrade(stockValue);
            }
        }
    }

    @Override
    public String getResults() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        printStream.print("Initial wallet is $%.2f" + intiialWallet);
        printStream.printf("Final wallet is $%.2f" + wallet);
        printStream.print("Portfolio has " + getFinalStockCounts());
        printStream.printf("Net worth is $%.2f", getNetWorth());

        String leftAlignFormat = "| %-8s | %-9d |%n";

        printStream.format("+----------+------------+%n");
        printStream.printf("| Stock    | Frequency  |%n");
        printStream.format("+----------+------------+%n");
        for (int i = 0; i < stockSymbolsToTrade.size(); i++) {
            printStream.format(leftAlignFormat, stockSymbolsToTrade.get(i), numberOfShares.get(stockSymbolsToTrade.get(i)));
        }
        printStream.format("+----------+------------+%n");

        return byteArrayOutputStream.toString();
    }

    private void doTrade(StockValue stockValue) {
        double mean = basicStatistics.get(stockValue.getSymbol()).getMean();
        double standardDeviation = basicStatistics.get(stockValue.getSymbol()).getStandardDeviation();
        double lowerBound = mean - standardDeviation * bandWidth;
        double upperBound = mean + standardDeviation * bandWidth;
        int currentNumberOfShares = numberOfShares.get(stockValue.getSymbol());

        //undervalued
        if (stockValue.getValue() < lowerBound) {
            //buy
            if (wallet - stockValue.getValue() > 0) {
                wallet -= stockValue.getValue();
                numberOfShares.put(stockValue.getSymbol(), currentNumberOfShares + 1);
            }
        }
        //overvalued
        else if (stockValue.getValue() > upperBound) {
            //sell
            if (currentNumberOfShares > 0) {
                numberOfShares.put(stockValue.getSymbol(), currentNumberOfShares - 1);
                wallet += stockValue.getValue();
            }
        }
    }

    private void refreshQAndStats(StockValue newValue) {
        stockValueQueues.get(newValue.getSymbol()).remove();
        stockValueQueues.get(newValue.getSymbol()).add(newValue);
        basicStatistics.get(newValue.getSymbol()).removeOldestValue();
        basicStatistics.get(newValue.getSymbol()).add(newValue.getValue());
    }

    private int getFinalStockCounts() {
        int sum = 0;

        for (int i = 0; i < numberOfShares.size(); i++)
            sum += numberOfShares.get(stockSymbolsToTrade.get(i));
        return sum;
    }

    private double getNetWorth() {
        int portfolioWorth = 0;

        for (int i = 0; i < numberOfShares.size(); i++) {
           portfolioWorth +=  numberOfShares.get(stockSymbolsToTrade.get(i)) * lastValues.get(stockSymbolsToTrade.get(i));
        }
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
