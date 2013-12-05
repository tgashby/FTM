package common;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: allen
 * Date: 12/4/13
 * Time: 9:25 PM
 */
public class BasicStatistics {
    private ArrayBlockingQueue<Double> sample;
    private double mean;

    public BasicStatistics(int sampleSize) {
        sample = new ArrayBlockingQueue<Double>(sampleSize);
    }

    public void add(double sampleValue) {
        int oldSampleSize = sample.size();
        sample.add(sampleValue);
        int newSampleSize = sample.size();

        //update mean
        double sum = mean * oldSampleSize;
        mean = (sum + sampleValue) / newSampleSize;
    }

    public void removeOldestValue() {
        int oldSampleSize = sample.size();
        double removedItem = sample.remove();
        int newSampleSize = sample.size();

        //update mean
        double sum = mean * oldSampleSize;
        mean = (sum - removedItem) / newSampleSize;
    }

    public double getMean() {
        return mean;
    }

    public double getStandardDeviation() {
        Iterator<Double> iterator = sample.iterator();
        double mean = getMean();
        double temp = 0;

        while (iterator.hasNext()) {
            double next = iterator.next();
            temp += (mean - next) * (mean - next);
        }

        return Math.sqrt(temp / sample.size());
    }
}

