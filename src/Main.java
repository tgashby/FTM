import java.util.ArrayList;
import java.util.Timer;

/**
 * User: tgashby
 * Date: 10/17/13
 * Time: 9:04 AM
 */
public class Main {
    public static void main(String[] args)
    {
//        ArrayList<StockBean> stocks = null;
//
//        try {
//           stocks = CSVParser.parse("data/OLIN.csv");
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//
//        if (stocks != null)
//        {
//            for (StockBean stock : stocks)
//            {
//                System.out.println("Stock: " + stock.getDate() + " " + stock.getHigh());
//            }
//        }
        Timer yfRunner = new Timer();

        yfRunner.schedule(new YFQuerier(new String[]{"MSFT", "JNJ"}, "snl1"), 0, 5000);

        while (true)
            ;
    }
}
