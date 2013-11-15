package common;

import java.util.ArrayList;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 6:47 AM
 */
public class Market {
    private ArrayList<Stock> stocks;
    private int stockNdx;

    public Market() {
        DatabaseConnection dbCon = new DatabaseConnection();

        dbCon.connect();
        stocks = dbCon.getAllStocks();
        dbCon.disconnect();

        stockNdx = 0;
    }

    public Stock getNextValue()
    {
        Stock toReturn = null;

        if (hasNextValue())
        {
            toReturn = stocks.get(stockNdx);
            stockNdx++;
        }

        return toReturn;
    }

    public boolean hasNextValue() {
        return stockNdx < stocks.size();
    }
}
