package common;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by allen on 10/30/13
 */
public class DatabaseConnection {
    private Connection connection;

    private String jdbcURL;
    private final String stockTableName = "stocks";

    public DatabaseConnection() {
        StringBuilder jdbcURLBuilder = new StringBuilder(100);
        String hostname = "ftmdb.cbeadsxspecl.us-west-2.rds.amazonaws.com";
        String port = "3306";
        String databaseName = "Stocks";
        String username = "ftm";
        String password = "ftm-pass";

        jdbcURLBuilder.append("jdbc:mysql://");
        jdbcURLBuilder.append(hostname);
        jdbcURLBuilder.append(':');
        jdbcURLBuilder.append(port);
        jdbcURLBuilder.append('/');
        jdbcURLBuilder.append(databaseName);
        jdbcURLBuilder.append("?user=");
        jdbcURLBuilder.append(username);
        jdbcURLBuilder.append("&password=");
        jdbcURLBuilder.append(password);

        jdbcURL = jdbcURLBuilder.toString();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(jdbcURL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StockValue getStock(String name, Date date, Time time) {
        StockValue stockValue = null;

        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from " + stockTableName +
                    " where " + StockColumnNames.NAME + "=" + name + " and " +
                                StockColumnNames.DAY + "=" + date + " and " +
                                StockColumnNames.TIME + "=" + time);

            if (resultSet.next()) {
                stockValue = new StockValue(resultSet.getString(StockColumnNames.SYMBOL.toString()),
                        resultSet.getString(StockColumnNames.NAME.toString()),
                        resultSet.getDate(StockColumnNames.DAY.toString()),
                        resultSet.getTime(StockColumnNames.TIME.toString()),
                        resultSet.getDouble(StockColumnNames.VALUE.toString()));
            }
            statement.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        return stockValue;
    }

    public StockValue[] getStockPrices(String[] names, Date dates[], Time times[]) {
        StockValue[] stockBeans = new StockValue[names.length];

        for (int i = 0; i < names.length; i++) {
            stockBeans[i] = getStock(names[i], dates[i], times[i]);
        }

        return stockBeans;
    }

    public ArrayList<StockValue> getAllStocks() {
        ArrayList<StockValue> allStocks = new ArrayList<StockValue>(10000);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from " + stockTableName);

            while(resultSet.next()) {
                allStocks.add(new StockValue(resultSet.getString(StockColumnNames.SYMBOL.toString()),
                        resultSet.getString(StockColumnNames.NAME.toString()),
                        resultSet.getDate(StockColumnNames.DAY.toString()),
                        resultSet.getTime(StockColumnNames.TIME.toString()),
                        resultSet.getDouble(StockColumnNames.VALUE.toString())));
            }

            statement.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        return allStocks;
    }

    public void insertStock(StockValue stockValue) {
        try {
            Statement statement = connection.createStatement();
            statement.execute("insert into " + stockTableName + " values ( " +
                    "'" + stockValue.getSymbol() + "'," +
                    "'" + stockValue.getName() + "'," +
                    "'" + stockValue.getDate() + "'," +
                    "'" + stockValue.getTime() + "'," +
                    stockValue.getValue() + ");");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
