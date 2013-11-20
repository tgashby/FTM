package common;

import java.sql.Date;
import java.util.ArrayList;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 6:47 AM
 */
public class Market {
    private ArrayList<StockValue> stocks;
    private int stockNdx;

    /**
     * Market Constructor for making markets of different types
     *
     * @param type
     * One of MarketTypeEnum:
     * ALL_STOCKS
     *  Market will consist of all stocks ever put in the database.
     *
     * STOCKS_BY_SYMBOL
     *  Market will consist of all stocks with the given symbols. Order is all stocks of
     *  a certain symbol before the next symbol begins.
     *
     * ALL_STOCKS_BY_DATE
     *  Market will consist of all stocks from a given day. Making a market this way requires
     *  a little trickery for the values array, outlined below.
     *
     * @param values
     * Based on type parameter:
     * ALL_STOCKS
     *  values are unused
     *
     * STOCKS_BY_SYMBOL
     *  Any number of symbols seperated by commas.
     *  Ex: Market m = new Market(MarketTypeEnum.STOCKS_BY_SYMBOL, "GOOG", "MFST", "RTN");
     *  Ex: Market m = new Market(MarketTypeEnum.STOCKS_BY_SYMBOL, "GOOG");
     *
     * ALL_STOCKS_BY_DATE
     *  A date in milliseconds converted to a string. This class does the SQL Date conversion,
     *  it just needs a time in milliseconds, which can be acquired using the Calendar class or
     *  or System.currentTimeMillis() or a variety of other methods.
     *  Ex: Market m = new Market(MarketTypeEnum.ALL_STOCKS_BY_DATE, Long.toString(System.currentTimeMillis()));
     *
     */
    public Market(MarketTypeEnum type, String... values) {
        DatabaseConnection dbCon = new DatabaseConnection();
        stocks = new ArrayList<StockValue>(10000);

        dbCon.connect();
        switch (type) {
            case ALL_STOCKS:
                stocks.addAll(dbCon.getAllStocks());
                break;

            case STOCKS_BY_SYMBOL:
                for (String stockSym : values)
                {
                    stocks.addAll(dbCon.getAllStocksBySymbol(stockSym));
                }
                break;

            case ALL_STOCKS_BY_DATE:
                    stocks.addAll(dbCon.getAllStocksByDate(new Date(Long.valueOf(values[0]))));
        }
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
