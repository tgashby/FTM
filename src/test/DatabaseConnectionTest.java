package test;

import common.DatabaseConnection;
import common.Market;
import common.Stock;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * User: Tag
 * Date: 11/19/13
 * Time: 7:25 PM
 */
public class DatabaseConnectionTest {
//    public static void run()
//    {
//        DatabaseConnection dbConn = new DatabaseConnection();
//
//        try {
//            dbConn.connect();
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        testGetStocksBySymbol(dbConn);
//        testGetStocksByDate(dbConn);
//
//        // More tests here if you want
//
//        dbConn.disconnect();
//    }
//
//    private static void testGetStocksByDate(DatabaseConnection dbConn) {
//        Calendar tempCal = Calendar.getInstance();
//        tempCal.set(2013, Calendar.NOVEMBER, 18);
//
//        Date date = new Date(tempCal.getTimeInMillis());
//
//        ArrayList<Stock> stocks = dbConn.getAllStocksByDate(date);
//        assert stocks.size() > 0;
//
//        for (Stock stock : stocks)
//        {
//            assert stock.getDate().equals(date);
//        }
//    }
//
//    private static void testGetStocksBySymbol(DatabaseConnection dbConn) {
//        ArrayList<Stock> googStocks = dbConn.getAllStocksBySymbol("GOOG");
//
//        for (Stock gStock : googStocks)
//        {
//            assert gStock.getSymbol().equals("GOOG");
//        }
//
//        ArrayList<Stock> raytheonStocks = dbConn.getAllStocksBySymbol("RTN");
//
//        for (Stock rStock : raytheonStocks)
//        {
//            assert rStock.getSymbol().equals("RTN");
//        }
//
//        // I'm satisfied with two tests
//    }
}
