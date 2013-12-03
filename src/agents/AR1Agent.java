package agents;

import common.Stock;
import common.Tuple;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: allen
 * Date: 11/21/13
 * Time: 1:45 PM
 */
//TODO: filter stocks
public class AR1Agent extends MultipleStockTraderAgent {
    private int sampleSize;
    private double alphaLevel;
    private HashMap<String, EnhancedSimpleRegression> enhancedSimpleRegressions = new HashMap<String, EnhancedSimpleRegression>();
    private HashMap<String, ArrayList<Stock>> stockLists = new HashMap<String, ArrayList<Stock>>();

    public AR1Agent(double capital) {
        super(capital);
        sampleSize = 50;
        alphaLevel = 0.10;

        for (int i = 0; i < stockSymbolsToTrade.size(); i++) {
            enhancedSimpleRegressions.put(stockSymbolsToTrade.get(i), new EnhancedSimpleRegression());
            stockLists.put(stockSymbolsToTrade.get(i), new ArrayList<Stock>(100));
        }
    }

    //TODO: clean up this code
    @Override
    public void trade(Stock stock) {
        if (stockSymbolsToTrade.contains(stock.getSymbol())) {
            //update last values to calculate final net worth later
            lastValues.put(stock.getSymbol(), stock.getValue());
            //add stock to right stock list
            stockLists.get(stock.getSymbol()).add(stock);

            ArrayList<Stock> currentStockValues = stockLists.get(stock.getSymbol());
            EnhancedSimpleRegression currentRegression = enhancedSimpleRegressions.get(stock.getSymbol());

            currentRegression.addData(stock.getValue());

            if (currentStockValues.size() >= sampleSize && currentRegression.testForIndependence(alphaLevel) &&
                    currentRegression.testForLinearity(alphaLevel) && currentRegression.testForNormality(alphaLevel) &&
                    currentRegression.testForEqualVariances(alphaLevel)) {

                currentRegression.regress();

                if (currentRegression.getSignificance() < alphaLevel) {
                    Tuple<Double, Double> tuple = currentRegression.
                            getPredictionInterval(currentRegression.getTimeCounter_x() + 1, alphaLevel);
                    if (stock.getValue() < tuple.x) {
                        super.buy(stock);
                    } else if (stock.getValue() > tuple.y) {
                        super.sell(stock);
                    }
                }
            }
        }
    }

    @Override
    public String getAgentName() {
        return "First Order Autoregressive Model Agent";
    }

    private class EnhancedSimpleRegression extends SimpleRegression {
        private int timeCounter_x = 0;
        private ArrayList<Double> residuals = new ArrayList<Double>(100);
        private SummaryStatistics xValues = new SummaryStatistics();

        public void addData(final double y) {
            addData(timeCounter_x++, y);
        }

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

        public Tuple<Double, Double> getPredictionInterval(double x, double alphaLevel) {
            double pointPrediction = super.predict(x);
            double moe = 0;

            try {
                moe = getMarginOfError(x, alphaLevel);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            return new Tuple<Double, Double>(pointPrediction - moe, pointPrediction + moe);
        }

        public int getTimeCounter_x() {
            return timeCounter_x;
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

            return (Double) xBarField.get(this);
        }

        private double getTCriticalValue(double degreesOfFreedom, double alphaLevel) {
            return new TDistribution(degreesOfFreedom).cumulativeProbability(alphaLevel);
        }

        private double getSquareSampleStandardDeviationOfX() {
            return xValues.getStandardDeviation();
        }

        public boolean testForIndependence(double alphaLevel) {
            //look at lower & upper Durbin-Watson bounds. D < dL -> reject; D > dU -> fail to reject;   //df
            double lowerBound = 0;
            double upperBound = 0;
            double durbinWatsonTestStatistic = getDurbinWatsonStatistic();

            if (durbinWatsonTestStatistic < lowerBound) {
                //return true;
            } else if (durbinWatsonTestStatistic > upperBound) {
                //return false;
            } else {
                //test inconclusive
                //return false;
            }

            return false;
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

        public void firstDifferencesMethodToFixAutocorrelation() {
            //TODO: implement
        }
    }
}
