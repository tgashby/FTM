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
        Timer yfRunner = new Timer();
        YFQuerier yfQuerier = new YFQuerier(new String[]{"MSFT"}, "snl1");

        yfRunner.schedule(yfQuerier, 0, 5000);

        while(true)
            ;

//        yfQuerier.disconnect();
    }
}
