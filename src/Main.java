import java.util.ArrayList;

/**
 * User: tgashby
 * Date: 10/17/13
 * Time: 9:04 AM
 */
public class Main {
    public static void main(String[] args)
    {
        ArrayList<StockBean> stocks = null;

        try {
           stocks = CSVParser.parse("data/OLIN.csv");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if (stocks != null)
        {
            for (StockBean stock : stocks)
            {
                System.out.println("Stock: " + stock.getDate() + " " + stock.getHigh());
            }
        }
    }
}
