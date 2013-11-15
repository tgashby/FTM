package common;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 6:47 AM
 */
public class Market {
    private ArrayList<Stock> stocks;
    private int stockNdx;
    private final int millisecondsInADay = 86400000;

    public Market() {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        databaseConnection.connect();
        try {
            stocks = databaseConnection.getStocksByDay(new java.sql.Date(System.currentTimeMillis() - millisecondsInADay));
        } catch (SQLException e) {
             throw new RuntimeException(e);
        }
        databaseConnection.disconnect();

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
