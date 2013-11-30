package agents;

import common.Stock;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * User: allen
 * Date: 11/21/13
 * Time: 1:45 PM
 */
public class AR1Agent {
    int sampleSize;
    double alphaLevel;
    ArrayList<Stock> stocks = new ArrayList<Stock>(1000);
    EnhancedSimpleRegression simpleRegression = new EnhancedSimpleRegression();

    public AR1Agent() {
        sampleSize = 50;
        alphaLevel = 0.10;
    }

    public AR1Agent(int sampleSize, double alphaTestLevels) {
        this.sampleSize = sampleSize;
        this.alphaLevel = alphaTestLevels;
    }

    public void trade(Stock stock) {
        stocks.add(stock);
        simpleRegression.addData(stocks.size(), stock.getValue());

        if (stocks.size() < 40)
            return;

        fixIndependenceAssumption();
        if (simpleRegression.testForLinearity(alphaLevel) && simpleRegression.testForNormality(alphaLevel) &&
                simpleRegression.testForEqualVariances(alphaLevel)) {
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
            //if still autocorrelated, move on
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

    private class EnhancedSimpleRegression extends SimpleRegression {
        ArrayList<Double> residuals = new ArrayList<Double>(100);
        SummaryStatistics xValues = new SummaryStatistics();

        @Override
        public void addData(final double x, final double y) {
            super.addData(x, y);
            residuals.add(y - super.predict(x));
            xValues.addValue(x);
        }

        public double getDurbinWatsonStatistic() {
            int durbinWatsonStatisticNumerator = 0;
            for (int t = 2; t < residuals.size(); t++)
                durbinWatsonStatisticNumerator += residuals.get(t) - residuals.get(t - 1);
            return durbinWatsonStatisticNumerator / super.getSumSquaredErrors();
        }

        public double[][] getPredictionInterval(double x, double alphaLevel) throws NoSuchFieldException, IllegalAccessException {
            double pointPrediction = super.predict(x);
            double moe = getMarginOfError(x, alphaLevel);
            return new double[][] { { pointPrediction - moe }, { pointPrediction + moe } };
        }

        private double getMarginOfError(double x, double alphaLevel) throws NoSuchFieldException, IllegalAccessException {
            double residualStandardDeviation = super.getSumSquaredErrors() / (super.getN() - 2);
            double standardizedSquareOfX = Math.pow(x - getXBar(), 2);
            double degreesOfFreedom = super.getN() - 1;

            return getTCriticalValue(degreesOfFreedom, alphaLevel) * residualStandardDeviation * Math.sqrt(1 + (1 / super.getN()) + (standardizedSquareOfX /
                    (degreesOfFreedom * getSquareSampleStandardDeviationOfX())));
        }

        private double getXBar() throws IllegalAccessException, NoSuchFieldException {
            Field xBarField = this.getClass().getSuperclass().getDeclaredField("xbar");
            xBarField.setAccessible(true);
            Double xBar = (Double) xBarField.get(this);

            return xBar;
        }

        private double getTCriticalValue(double degreesOfFreedom, double alphaLevel) {
            TDistribution tDistribution = new TDistribution(degreesOfFreedom);
            return tDistribution.cumulativeProbability(alphaLevel);
        }

        private double getSquareSampleStandardDeviationOfX() {
            return xValues.getStandardDeviation();
        }

        //true if null hypothesis rejected, false if cannot reject null hypothesis
        public boolean testForLinearity(double alphaLevel) {
            return false;
        }

        public boolean testForNormality(double alphaLevel) {
            return false;
        }

        public boolean testForEqualVariances(double alphaLevel) {
            return false;
        }
    }
}
