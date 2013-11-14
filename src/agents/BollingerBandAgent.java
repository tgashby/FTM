package agents;

import common.Market;
import common.StockValue;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: allen
 * Date: 11/5/13
 * Time: 9:45 PM
 */
public class BollingerBandAgent {
    Market market = new Market();
    StockValue stock;
    double wallet;
    int numShares = 0;
    double curValue;

    private int movingAverageSampleSize;
    private int bandWidthInStandardDeviations;
    Queue<StockValue> stockValueQueue = new ArrayBlockingQueue<StockValue>(movingAverageSampleSize);

    public BollingerBandAgent() {
        movingAverageSampleSize = 20;
        bandWidthInStandardDeviations = 2;
    }

    public BollingerBandAgent(int movingAverageSampleSize, int bandWidthInStandardDeviations) {
        this.movingAverageSampleSize = movingAverageSampleSize;
        this.bandWidthInStandardDeviations = bandWidthInStandardDeviations;
    }

    public void startTrading(int walletInUSDollars) {
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();

        while ((stock = market.getNextValue()) != null && wallet > 0) {
            //initialize q with 1st movingAverageSampleSize stocks. Temporary until market method is created to do this.
            if (stockValueQueue.size() < movingAverageSampleSize) {
                stockValueQueue.add(stock);
                descriptiveStatistics.addValue(stock.getValue());
            }
            else if (stockValueQueue.size() == movingAverageSampleSize) {
                //do normal trade

                //do value swap
            }
            else {
                //remove old value from q
                //remove old value from stats

                //add new value to q
                //add new value to stats

                //calculate mean and standard deviation
                double mean = descriptiveStatistics.getMean();
                double standardDeviation = descriptiveStatistics.getStandardDeviation();
            }
        }
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
