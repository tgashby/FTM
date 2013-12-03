package YFQuerier;

import java.sql.SQLException;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * User: tgashby
 * Date: 10/17/13
 * Time: 9:04 AM
 */
public class YFMain {
    public static void main(String[] args) throws SQLException {
        String[] stocksToQuery;

        if (args.length > 0)
        {
            stocksToQuery = args;
        }
        else
        {
            stocksToQuery = new String[] {"MSFT", "GOOG", "OLN", "KR", "RTN", "TDC", "MSI", "VZ", "BA", "BKW"};
        }

        Timer yfRunner = new Timer();
        YFQuerier yfQuerier = new YFQuerier(stocksToQuery, "snl1");

        yfRunner.schedule(yfQuerier, 0, 5000);

        while(true)
        {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        yfQuerier.disconnect();
    }
}
