package common;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 6:47 AM
 */
public class Market {
    private ArrayList<StockValue> stockValues;
    private int stockNdx;

    public Market(Date date) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        databaseConnection.connect();
        try {
            stockValues = databaseConnection.getStocksByDay(date);
        } catch (SQLException e) {
             throw new RuntimeException(e);
        }
        databaseConnection.disconnect();

        stockNdx = 0;
    }

    public StockValue getNextValue()
    {
        StockValue toReturn = null;

        if (hasNextValue())
        {
            toReturn = stockValues.get(stockNdx);
            stockNdx++;
        }
        else
        {
            throw new RuntimeException("No more stock values!");
        }

        return toReturn;
    }

    public boolean hasNextValue() {
        return stockNdx < stockValues.size();
    }
}
