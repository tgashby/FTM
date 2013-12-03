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

    public Stock getStock(String name, Date date, Time time) throws SQLException {
        Stock stock = null;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from " + stockTableName +
                " where " + StockTableColumnNames.NAME + "=" + name + " and " +
                            StockTableColumnNames.DAY + "=" + date + " and " +
                            StockTableColumnNames.TIME + "=" + time);

        if (resultSet.next()) {
            stock = new Stock(resultSet.getString(StockTableColumnNames.SYMBOL.toString()),
                    resultSet.getString(StockTableColumnNames.NAME.toString()),
                    resultSet.getDate(StockTableColumnNames.DAY.toString()),
                    resultSet.getTime(StockTableColumnNames.TIME.toString()),
                    resultSet.getDouble(StockTableColumnNames.VALUE.toString()));
        }
        statement.close();

        return stock;
    }

    public Stock[] getStockPrices(String[] names, Date dates[], Time times[]) throws SQLException {
        Stock[] stockBeans = new Stock[names.length];

        for (int i = 0; i < names.length; i++) {
            stockBeans[i] = getStock(names[i], dates[i], times[i]);
        }

        return stockBeans;
    }

    public ArrayList<Stock> getAllStocks() throws SQLException {
        ArrayList<Stock> allStocks = new ArrayList<Stock>(10000);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from " + stockTableName);

        while(resultSet.next()) {
            allStocks.add(new Stock(resultSet.getString(StockTableColumnNames.SYMBOL.toString()),
                    resultSet.getString(StockTableColumnNames.NAME.toString()),
                    resultSet.getDate(StockTableColumnNames.DAY.toString()),
                    resultSet.getTime(StockTableColumnNames.TIME.toString()),
                    resultSet.getDouble(StockTableColumnNames.VALUE.toString())));
        }

        statement.close();

        return allStocks;
    }

    public ArrayList<Stock> getStocksByDay(Date date) throws SQLException {
        ArrayList<Stock> allStocksToday = new ArrayList<Stock>(50000);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from " + stockTableName +
                " where " + StockTableColumnNames.DAY + "=" + " date('" + date + "')");

        while (resultSet.next()) {
            allStocksToday.add(new Stock(resultSet.getString(StockTableColumnNames.SYMBOL.toString()),
                    resultSet.getString(StockTableColumnNames.NAME.toString()),
                    resultSet.getDate(StockTableColumnNames.DAY.toString()),
                    resultSet.getTime(StockTableColumnNames.TIME.toString()),
                    resultSet.getDouble(StockTableColumnNames.VALUE.toString())));
        }
        statement.close();

        return allStocksToday;
    }

    public void insertStock(Stock stock) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("insert into " + stockTableName + " values ( " +
                "'" + stock.getSymbol() + "'," +
                "'" + stock.getName() + "'," +
                "'" + stock.getDate() + "'," +
                "'" + stock.getTime() + "'," +
                stock.getValue() + ");");
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
