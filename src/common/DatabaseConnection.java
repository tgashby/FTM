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

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(jdbcURL);
    }

    public StockValue getStock(String name, Date date, Time time) throws SQLException {
        StockValue stockValue = null;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from " + stockTableName +
                " where " + StockTableColumnNames.NAME + "=" + name + " and " +
                            StockTableColumnNames.DAY + "=" + date + " and " +
                            StockTableColumnNames.TIME + "=" + time);

        if (resultSet.next()) {
            stockValue = new StockValue(resultSet.getString(StockTableColumnNames.SYMBOL.toString()),
                    resultSet.getString(StockTableColumnNames.NAME.toString()),
                    resultSet.getDate(StockTableColumnNames.DAY.toString()),
                    resultSet.getTime(StockTableColumnNames.TIME.toString()),
                    resultSet.getDouble(StockTableColumnNames.VALUE.toString()));
        }
        statement.close();

        return stockValue;
    }

    public StockValue[] getStockPrices(String[] names, Date dates[], Time times[]) throws SQLException {
        StockValue[] stockBeans = new StockValue[names.length];

        for (int i = 0; i < names.length; i++) {
            stockBeans[i] = getStock(names[i], dates[i], times[i]);
        }

        return stockBeans;
    }

    public ArrayList<StockValue> getAllStocks() throws SQLException {
        ArrayList<StockValue> allStocks = new ArrayList<StockValue>(10000);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from " + stockTableName);

        while(resultSet.next()) {
            allStocks.add(new StockValue(resultSet.getString(StockTableColumnNames.SYMBOL.toString()),
                    resultSet.getString(StockTableColumnNames.NAME.toString()),
                    resultSet.getDate(StockTableColumnNames.DAY.toString()),
                    resultSet.getTime(StockTableColumnNames.TIME.toString()),
                    resultSet.getDouble(StockTableColumnNames.VALUE.toString())));
        }

        statement.close();

        return allStocks;
    }

    public ArrayList<StockValue> getStocksByDay(Date date) throws SQLException {
        ArrayList<StockValue> allStocksToday = new ArrayList<StockValue>(50000);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from " + stockTableName +
                " where " + StockTableColumnNames.DAY + "=" + " date('" + date + "')");

        while (resultSet.next()) {
            allStocksToday.add(new StockValue(resultSet.getString(StockTableColumnNames.SYMBOL.toString()),
                    resultSet.getString(StockTableColumnNames.NAME.toString()),
                    resultSet.getDate(StockTableColumnNames.DAY.toString()),
                    resultSet.getTime(StockTableColumnNames.TIME.toString()),
                    resultSet.getDouble(StockTableColumnNames.VALUE.toString())));
        }
        statement.close();

        return allStocksToday;
    }

    public void insertStock(StockValue stockValue) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("insert into " + stockTableName + " values ( " +
                "'" + stockValue.getSymbol() + "'," +
                "'" + stockValue.getName() + "'," +
                "'" + stockValue.getDate() + "'," +
                "'" + stockValue.getTime() + "'," +
                stockValue.getValue() + ");");
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
