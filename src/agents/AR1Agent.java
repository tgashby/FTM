package agents;

import common.EnhancedSimpleRegression;
import common.Stock;
import org.apache.commons.math3.stat.regression.RegressionResults;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: allen
 * Date: 11/21/13
 * Time: 1:45 PM
 */
public class AR1Agent {
    int sampleSize;
    double alphaLevels;
    ArrayList<Stock> stocks = new ArrayList<Stock>(1000);
    EnhancedSimpleRegression simpleRegression = new EnhancedSimpleRegression();

    public AR1Agent(int sampleSize, double alphaTestLevels) {
        this.sampleSize = sampleSize;
        this.alphaLevels = alphaTestLevels;
    }

    public void trade(Stock stock) {
        stocks.add(stock);
        simpleRegression.addData(stocks.size(), stock.getValue());

        if (stocks.size() < 40)
            return;

        fixIndependenceAssumption();
        if (simpleRegression.testForLinearity(0.05) && simpleRegression.testForNormality(0.05) &&
                simpleRegression.testForEqualVariances(0.05)) {
            RegressionResults results = simpleRegression.regress();
            simpleRegression.getSignificance();
            //if time is a significant predictor, then construct a PI for the next time period ahead.
            //if PI bounds are both above or below current stock price, then execute trade
        }
    }

    private void fixIndependenceAssumption() {
        double durbinWatsonTestStatistic = calculateDurbinWatsonTestStatistic();
        //get p-value from looking at lower & upper Durbin-Watson bounds. D < dL -> reject; D > dU -> fail to reject;
        boolean rejectNullHypothesis = testForAutocorrelation(durbinWatsonTestStatistic, 5);  //stat, degrees of freedom
        //if we fail to reject, good to forecast
        if (rejectNullHypothesis) {
            //fix autocorrelation (estimate ro or assume ro is 1) and check for under or over autocorrelation
            //if still autocorrelated
        }
    }

    private double calculateDurbinWatsonTestStatistic() {
        //get residuals
        //calc a sum: for (int t = 2; t < residuals.length; residuals++) { sum += residuals[t] - residuals[t - 1]; }
        //divide by SSE

        return 0;
    }

    private boolean testForAutocorrelation(double durbinWatsonTestStatistic, int n) {
        return false;
    }
}
