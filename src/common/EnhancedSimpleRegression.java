package common;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;

/**
 * User: allen
 * Date: 11/21/13
 * Time: 3:44 PM
 */
public class EnhancedSimpleRegression extends SimpleRegression {
    ArrayList<Double> residuals = new ArrayList<Double>(100);

    @Override
    public void addData(final double x, final double y) {
        super.addData(x, y);
        residuals.add(y - super.predict(x));
    }

    @Override
    public void removeData(final double x, final double y) {
        super.removeData(x, y);
        residuals.remove(y - super.predict(x));
    }

    public double getDurbinWatsonStatistic() {
        int durbinWatsonStatisticNumerator = 0;
        for (int t = 2; t < residuals.size(); t++)
            durbinWatsonStatisticNumerator += residuals.get(t) - residuals.get(t - 1);
        return durbinWatsonStatisticNumerator / super.getSumSquaredErrors();
    }

    public int[][] getPredictionInterval(double x) {
        double pointPrediction = super.predict(x);
        //calculate margin of error
        return new int[][] { { 0 }, { 0 } };
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
