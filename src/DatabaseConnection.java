
import java.sql.*;

/**
 * Created by allen on 10/30/13
 */
public class DatabaseConnection implements StockColumnNames {
    private Connection connection;

    private String jdbcURL;
    private StringBuilder jdbcURLBuilder = new StringBuilder(100);
    private final String hostname = "ftmdb.cbeadsxspecl.us-west-2.rds.amazonaws.com";
    private final String port = "3306";
    private final String databaseName = "Stocks";
    private final String username = "ftm";
    private final String password = "ftm-pass";

    private final String stockTableName = "stocks";

    public DatabaseConnection() {
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
                " where " + NAME + "=" + name + " and " +
                            DAY + "=" + date + " and " +
                            TIME + "=" + time);

        if (resultSet.next()) {
            stockValue = new StockValue(resultSet.getString(SYMBOL), resultSet.getString(NAME), resultSet.getDate(DAY),
                    resultSet.getTime(TIME), resultSet.getDouble(VALUE));
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

    public void setStock(StockValue stockValue) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("insert into " + stockTableName + " values ( " +
                "'" + stockValue.getSymbol() + "'," +
                "'" + stockValue.getName() + "'," +
                "'" + stockValue.getDate() + "'," +
                "'" + stockValue.getTime() + "'," +
                "'" + stockValue.getValue() + "');");
    }

    public void disconnect() throws SQLException {
        connection.close();
    }
}
