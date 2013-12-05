package agents;

import common.EnhancedSimpleRegression;
import common.Stock;
import common.Tuple;
import javanpst.data.structures.sequence.NumericSequence;
import javanpst.tests.goodness.A_DTest.A_DTest;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * User: allen
 * Date: 11/21/13
 * Time: 1:45 PM
 */
//TODO: separate alpha levels. durbin-watson, anderson-darling, levene's test
public class AR1Agent extends MultipleStockTraderAgent {
    private int sampleSize;
    private double alphaLevel;
    private HashMap<String, EnhancedSimpleRegression> enhancedSimpleRegressions = new HashMap<String, EnhancedSimpleRegression>();
    private HashMap<String, ArrayList<Stock>> stockLists = new HashMap<String, ArrayList<Stock>>();

    public AR1Agent(double capital) {
        super(capital);
        sampleSize = 30;
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

            if (currentStockValues.size() >= sampleSize && currentRegression.independenceMet(alphaLevel) &&
                    currentRegression.normalityMet(alphaLevel) && currentRegression.linearityMet(alphaLevel) &&
                    currentRegression.equalVariancesMet(alphaLevel)) {

                currentRegression.regress();

                if (currentRegression.getSignificance() < alphaLevel) {
                    Tuple<Double, Double> tuple = currentRegression.
                            getPredictionInterval(currentRegression.getTimeCounter_x() + 1, alphaLevel);
                    if (stock.getValue() < tuple.x) {
                        super.sell(stock);
                    } else if (stock.getValue() > tuple.y) {
                        super.buy(stock);
                    }
                }
            }
        }
    }

    @Override
    public String getAgentName() {
        return "First Order Autoregressive Model Agent";
    }
}
