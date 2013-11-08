package YFQuerier;

import java.util.Timer;

/**
 * User: tgashby
 * Date: 10/17/13
 * Time: 9:04 AM
 */
public class YFMain {
    public static void main(String[] args)
    {
        String[] stocksToQuery;

        if (args.length > 0)
        {
            stocksToQuery = args;
        }
        else
        {
            stocksToQuery = new String[] {"MSFT", "GOOG", "OLN", "KR"};
        }

        Timer yfRunner = new Timer();
        YFQuerier yfQuerier = new YFQuerier(stocksToQuery, "snl1");

        yfRunner.schedule(yfQuerier, 0, 5000);

        while(true)
            ;

//        yfQuerier.disconnect();
    }
}
