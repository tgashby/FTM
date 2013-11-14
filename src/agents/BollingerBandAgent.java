package agents;

import common.BasicStatistics;
import common.Market;
import common.StockValue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: allen
 * Date: 11/5/13
 * Time: 9:45 PM
 */
public class BollingerBandAgent {
    private Market market = new Market();
    private StockValue stock;
    private double wallet;
    private int numShares = 0;
    private double curValue;

    private int movingAverageSampleSize = 20;
    private int bandWidth = 2;
    private ArrayBlockingQueue<StockValue> stockValueQueue = new ArrayBlockingQueue<StockValue>(movingAverageSampleSize);
    private BasicStatistics basicStatistics = new BasicStatistics(movingAverageSampleSize);

    public BollingerBandAgent() {}

    public BollingerBandAgent(int movingAverageSampleSize, int bandWidth) {
        this.movingAverageSampleSize = movingAverageSampleSize;
        this.bandWidth = bandWidth;
    }

    public void startTrading(int walletInUSDollars) {
        while ((stock = market.getNextValue()) != null && wallet > 0) {
            //initialize q with 1st movingAverageSampleSize stocks. Temporary until market method is created to do this.
            if (stockValueQueue.size() < movingAverageSampleSize) {
                stockValueQueue.add(stock);
                basicStatistics.add(stock.getValue());
            }
            else if (stockValueQueue.size() == movingAverageSampleSize) {
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

    private void doTrade(StockValue stockValue) {
        double mean = basicStatistics.getMean();
        double standardDeviation = basicStatistics.getStandardDeviation();
        double lowerBound = mean - standardDeviation * bandWidth;
        double upperBound = mean + standardDeviation * bandWidth;

        //undervalued
        if (stockValue.getValue() < lowerBound) {
            //buy
            if (wallet - stockValue.getValue() > 0) {
                wallet -= stockValue.getValue();
                numShares++;
            }
        }
        //overvalued
        else if (stockValue.getValue() > upperBound) {
            //sell
            if (numShares > 0) {
                numShares--;
                wallet += stockValue.getValue();
            }
        }
    }

    private void refreshQAndStats(StockValue newValue) {
        stockValueQueue.remove();
        stockValueQueue.add(newValue);
        basicStatistics.removeOldestValue();
        basicStatistics.add(newValue.getValue());
    }

    public double getFinalWallet() {
        return wallet;
    }

    public int getFinalStockCount() {
        return numShares;
    }

    public double getNetWorth() {
        return wallet + numShares * curValue;
    }
}
