package agents;

import common.BasicStatistics;
import common.Market;
import common.Stock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: allen
 * Date: 11/5/13
 * Time: 9:45 PM
 */
public class BollingerBandAgent {
    private final int millisecondsInADay = 86400000;
    private Market market = new Market(new java.sql.Date(System.currentTimeMillis() - millisecondsInADay));
    private Stock stock;
    private ArrayList<String> stockSymbolsToTrade;
    private double wallet;
    private HashMap<String, Integer> numberOfShares;
    private HashMap<String, Double> lastValues;

    private int movingAverageSampleSize;
    private int bandWidth;
    private HashMap<String, ArrayBlockingQueue<Stock>> stockValueQueues;
    private HashMap<String, BasicStatistics> basicStatistics;

    public BollingerBandAgent(ArrayList<String> stockSymbolsToTrade, int wallet, int movingAverageSampleSize, int bandWidth) {
        this.stockSymbolsToTrade = stockSymbolsToTrade;
        this.wallet = wallet;
        this.movingAverageSampleSize = movingAverageSampleSize;
        this.bandWidth = bandWidth;
        numberOfShares = new HashMap<String, Integer>(stockSymbolsToTrade.size());
        lastValues = new HashMap<String, Double>(stockSymbolsToTrade.size());
        stockValueQueues = new HashMap<String, ArrayBlockingQueue<Stock>>(stockSymbolsToTrade.size());
        basicStatistics = new HashMap<String, BasicStatistics>(stockSymbolsToTrade.size());

        for (int i = 0; i < stockSymbolsToTrade.size(); i++) {
            stockValueQueues.put(stockSymbolsToTrade.get(i), new ArrayBlockingQueue<Stock>(movingAverageSampleSize));
            basicStatistics.put(stockSymbolsToTrade.get(i), new BasicStatistics(movingAverageSampleSize));
            numberOfShares.put(stockSymbolsToTrade.get(i), 0);
        }

        System.out.println("Initial wallet is $" + wallet);
    }

    public void startTrading() {
        while (market.hasNextValue() && wallet >= 0) {
            stock = market.getNextValue();
            //only trade the stocks specified
            if (!stockSymbolsToTrade.contains(stock.getSymbol()))
                continue;
            //set last values
            lastValues.put(stock.getSymbol(), stock.getValue());
            //initialize q with 1st movingAverageSampleSize stocks. Temporary until market method is created to do this.
            if (stockValueQueues.get(stock.getSymbol()).size() < movingAverageSampleSize) {
                stockValueQueues.get(stock.getSymbol()).add(stock);
                basicStatistics.get(stock.getSymbol()).add(stock.getValue());
            }
            else if (stockValueQueues.get(stock.getSymbol()).size() == movingAverageSampleSize) {
                doTrade(stock);
                refreshQAndStats(stock);
                doTrade(stock);
            }
            else {
                refreshQAndStats(stock);
                doTrade(stock);
            }
        }
    }

    private void doTrade(Stock stock) {
        double mean = basicStatistics.get(stock.getSymbol()).getMean();
        double standardDeviation = basicStatistics.get(stock.getSymbol()).getStandardDeviation();
        double lowerBound = mean - standardDeviation * bandWidth;
        double upperBound = mean + standardDeviation * bandWidth;
        int currentNumberOfShares = numberOfShares.get(stock.getSymbol());

        //undervalued
        if (stock.getValue() < lowerBound) {
            //buy
            if (wallet - stock.getValue() > 0) {
                wallet -= stock.getValue();
                numberOfShares.put(stock.getSymbol(), currentNumberOfShares + 1);
            }
        }
        //overvalued
        else if (stock.getValue() > upperBound) {
            //sell
            if (currentNumberOfShares > 0) {
                numberOfShares.put(stock.getSymbol(), currentNumberOfShares - 1);
                wallet += stock.getValue();
            }
        }
    }

    private void refreshQAndStats(Stock newValue) {
        stockValueQueues.get(newValue.getSymbol()).remove();
        stockValueQueues.get(newValue.getSymbol()).add(newValue);
        basicStatistics.get(newValue.getSymbol()).removeOldestValue();
        basicStatistics.get(newValue.getSymbol()).add(newValue.getValue());
    }

    public double getFinalWallet() {
        return wallet;
    }

    public int getFinalStockCounts() {
        int sum = 0;

        for (int i = 0; i < numberOfShares.size(); i++)
            sum += numberOfShares.get(stockSymbolsToTrade.get(i));
        return sum;
    }

    public void printStockNameAndFrequencyOutput() {
        String leftAlignFormat = "| %-8s | %-9d |%n";

        System.out.format("+----------+------------+%n");
        System.out.printf("| Stock    | Frequency  |%n");
        System.out.format("+----------+------------+%n");
        for (int i = 0; i < stockSymbolsToTrade.size(); i++) {
            System.out.format(leftAlignFormat, stockSymbolsToTrade.get(i), numberOfShares.get(stockSymbolsToTrade.get(i)));
        }
        System.out.format("+----------+------------+%n");
    }

    public double getNetWorth() {
        int portfolioWorth = 0;

        for (int i = 0; i < numberOfShares.size(); i++) {
           portfolioWorth +=  numberOfShares.get(stockSymbolsToTrade.get(i)) * lastValues.get(stockSymbolsToTrade.get(i));
        }
        return wallet + portfolioWorth;
    }
}
