package common;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * User: allen
 * Date: 12/3/13
 * Time: 3:35 PM
 */
public class EnhancedSimpleRegressionTest {
    private EnhancedSimpleRegression enhancedSimpleRegression;

    @BeforeMethod
    public void initializeObject() {
        enhancedSimpleRegression = new EnhancedSimpleRegression();
    }

    @Test
    public void testGetDurbinWatsonStatistic() throws Exception {
        enhancedSimpleRegression.addData(1, 1);
        enhancedSimpleRegression.addData(2, 0);
        enhancedSimpleRegression.addData(3, 1);
        enhancedSimpleRegression.addData(4, 0);
        enhancedSimpleRegression.addData(5, 1);
        Assert.assertEquals(enhancedSimpleRegression.getDurbinWatsonStatistic(), 5);
    }

    @Test
    public void testGetPointPrediction() throws Exception {

    }

    @Test
    public void testGetPredictionInterval() throws Exception {

    }

    @Test
    public void testIndependenceMet() throws Exception {

    }

    @Test
    public void testLinearityMet() throws Exception {

    }

    @Test
    public void testNormalityMet() throws Exception {

    }

    @Test
    public void testEqualVariancesMet() throws Exception {

    }
}
