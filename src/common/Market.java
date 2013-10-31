package common;

import java.util.ArrayList;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 6:47 AM
 */
public class Market {
    private ArrayList<StockValue> stocks;
    private int stockNdx;

    public Market() {
        DatabaseConnection dbCon = new DatabaseConnection();

        dbCon.connect();
        stocks = dbCon.getAllStocks();
        dbCon.disconnect();

        stockNdx = 0;
    }

    public StockValue getNextValue()
    {
        StockValue toReturn = null;

        if (stockNdx < stocks.size())
        {
            toReturn = stocks.get(stockNdx);
            stockNdx++;
        }

        return toReturn;
    }
}
