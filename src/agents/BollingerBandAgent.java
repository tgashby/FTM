package agents;

import common.Market;
import common.StockValue;

import java.util.Iterator;
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
            }
            else if (stockValueQueue.size() == movingAverageSampleSize) {
                doTrade(stock);
                refreshQ(stock);
                doTrade(stock);
            }
            else {
                refreshQ(stock);
                doTrade(stock);
            }
        }
    }

    private void doTrade(StockValue stockValue) {
        double mean = getMean();
        double standardDeviation = getStandardDeviation();
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

    private void refreshQ(StockValue newValue) {
        stockValueQueue.remove();
        stockValueQueue.add(newValue);
    }

    //TODO: extend apache class to allow removing oldest value
    private double getMean() {
        Iterator<StockValue> iterator = stockValueQueue.iterator();
        int sum = 0;

        while (iterator.hasNext()) {
            sum += iterator.next().getValue();
        }

        return sum / stockValueQueue.size();
    }

    private double getStandardDeviation() {
        Iterator<StockValue> iterator = stockValueQueue.iterator();
        double mean = getMean();
        double temp = 0;

        while (iterator.hasNext()) {
            double next = iterator.next().getValue();
            temp += (mean - next) * (mean - next);
        }

        return Math.sqrt(temp / stockValueQueue.size());
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
