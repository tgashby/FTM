package common;

import javanpst.data.structures.sequence.NumericSequence;
import javanpst.tests.goodness.A_DTest.A_DTest;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * User: allen
 * Date: 12/3/13
 * Time: 3:33 PM
 */
public class EnhancedSimpleRegression extends SimpleRegression {
    private int timeCounter_x = 1;
    private ArrayList<Double> residuals = new ArrayList<Double>(100);
    private SummaryStatistics xValues = new SummaryStatistics();
    private DurbinWatsonSignificanceTable durbinWatsonSignificanceTable = new DurbinWatsonSignificanceTable();

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

    public double getPointPrediction(double x) {
        return super.predict(x);
    }

    public Tuple<Double, Double> getPredictionInterval(double x, double alphaLevel) {
        double pointPrediction = getPointPrediction(x);
        double moe = 0;

        moe = getMarginOfError(x, alphaLevel);

        return new Tuple<Double, Double>(pointPrediction - moe, pointPrediction + moe);
    }

    public int getTimeCounter_x() {
        return timeCounter_x;
    }

    private double getMarginOfError(double x, double alphaLevel) {
        double residualStandardDeviation = super.getSumSquaredErrors() / (super.getN() - 2);
        double standardizedSquareOfX = Math.pow(x - getXBar(), 2);
        double degreesOfFreedom = super.getN() - 1;

        return getTCriticalValue(degreesOfFreedom, alphaLevel) * residualStandardDeviation * Math.sqrt(1 + (1 / super.getN()) + (standardizedSquareOfX /
                (degreesOfFreedom * getSquareSampleStandardDeviationOfX())));
    }

    /**
     * I have to catch the exceptions here since the trade() method is inherited and cannot throw exceptions
     */
    private double getXBar() {
        double xbar;

        try {
            Field xBarField = this.getClass().getSuperclass().getDeclaredField("xbar");
            xBarField.setAccessible(true);
            xbar = (Double) xBarField.get(this);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return xbar;
    }

    private double getTCriticalValue(double degreesOfFreedom, double alphaLevel) {
        return new TDistribution(degreesOfFreedom).cumulativeProbability(alphaLevel);
    }

    private double getSquareSampleStandardDeviationOfX() {
        return xValues.getStandardDeviation();
    }

    public boolean independenceMet(double alphaLevel) {
        Tuple<Double, Double> bounds = durbinWatsonSignificanceTable.getBounds(residuals.size(), alphaLevel);
        double durbinWatsonTestStatistic = getDurbinWatsonStatistic();

        if (durbinWatsonTestStatistic < bounds.x)
            return true;
        else if (durbinWatsonTestStatistic > bounds.y)
            //fix for autocorrelation here
            return false;
        else
            return false;
    }

    //true if null hypothesis rejected, false if cannot reject null hypothesis
    public boolean linearityMet(double alphaLevel) {
        //TODO: lack of fit test. needs data to be blocked
        return true;
    }

    public boolean normalityMet(double alphaLevel) {
        A_DTest andersonDarlingTest = new A_DTest(new NumericSequence(residuals));

        andersonDarlingTest.adjustNormal();
        //return andersonDarlingTest.getPValue() > alphaLevel;
        return true;
    }

    public boolean equalVariancesMet(double alphaLevel) {
        //TODO: levene's test or Bartlett's test
        return true;
    }

    private class DurbinWatsonSignificanceTable {
        private ArrayList<Tuple<Integer, Tuple<Double, Double>>> significanceValues01
                = new ArrayList<Tuple<Integer, Tuple<Double, Double>>>(210);
        private ArrayList<Tuple<Integer, Tuple<Double, Double>>> significanceValues025
                = new ArrayList<Tuple<Integer, Tuple<Double, Double>>>(210);
        private ArrayList<Tuple<Integer, Tuple<Double, Double>>> significanceValues05
                = new ArrayList<Tuple<Integer, Tuple<Double, Double>>>(210);

        public DurbinWatsonSignificanceTable() {
            String onePercentDurbinWatsonValues = "/resources/durbin_watson_01.txt";
            String twoAndAHalfPercentDurbinWatsonValues = "/resources/durbin_watson_025.txt";
            String fivePercentDurbinWatsonValues = "/resources/durbin_watson_05.txt";

            initializeList(onePercentDurbinWatsonValues, significanceValues01);
            initializeList(twoAndAHalfPercentDurbinWatsonValues, significanceValues025);
            initializeList(fivePercentDurbinWatsonValues, significanceValues05);
        }

        //user shouldn't specify alpha level above 0.05.
        public Tuple<Double, Double> getBounds(int n, double alpha) {
            ArrayList<Tuple<Integer, Tuple<Double, Double>>> currentSignificanceValues;

            if (alpha < 0.01)
                currentSignificanceValues = significanceValues01;
            else if (alpha < 0.025)
                currentSignificanceValues = significanceValues025;
            else
                currentSignificanceValues = significanceValues05;

            Collections.sort(currentSignificanceValues, new SignificanceTableComparator(n));
            return currentSignificanceValues.get(0).y;
        }

        private void initializeList(String filePath, ArrayList<Tuple<Integer, Tuple<Double, Double>>> significanceValues) {
            Scanner scanner = new Scanner(getClass().getResourceAsStream(filePath));

            //skip over header. 3 or more use a for.
            for (int i = 0; i < 3; i++)
                scanner.nextLine();

            while (scanner.hasNext()) {
                int currentN = Integer.parseInt(scanner.next().replace(".", ""));
                scanner.next();
                double lowerBound = scanner.nextDouble();
                double upperBound = scanner.nextDouble();
                scanner.nextLine();
                significanceValues.add(new Tuple<Integer, Tuple<Double, Double>>(currentN,
                        new Tuple<Double, Double>(lowerBound, upperBound)));
            }
        }
    }

    private class SignificanceTableComparator implements Comparator<Tuple<Integer, Tuple<Double, Double>>> {
        private int n;

        public SignificanceTableComparator(int n) {
            this.n = n;
        }

        @Override
        public int compare(Tuple<Integer, Tuple<Double, Double>> tuple1, Tuple<Integer, Tuple<Double, Double>> tuple2) {
            //sort by the absolute value of subtraction from n
            return Math.abs(tuple1.x - n) - Math.abs(tuple2.x - n);
        }
    }
}
