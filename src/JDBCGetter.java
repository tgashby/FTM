
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by allen on 10/30/13
 */
public class JDBCGetter {
    private Connection connection;

    private String jdbcURL;
    private StringBuilder jdbcURLBuilder = new StringBuilder(100);
    private String hostname = "ftmdb.cbeadsxspecl.us-west-2.rds.amazonaws.com";
    private String port = "3306";
    private String dbName = "Stocks";
    private String username = "ftm";
    private String password = "ftm-pass";

    public JDBCGetter() {
        //String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
        jdbcURLBuilder.append("jdbc:mysql://");
        jdbcURLBuilder.append(hostname);
        jdbcURLBuilder.append(':');
        jdbcURLBuilder.append(port);
        jdbcURLBuilder.append('/');
        jdbcURLBuilder.append(dbName);
        jdbcURLBuilder.append("?user=");
        jdbcURLBuilder.append(username);
        jdbcURLBuilder.append("&password=");
        jdbcURLBuilder.append(password);
        jdbcURL = jdbcURLBuilder.toString();
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(jdbcURL);
    }

    public StockBean getStock(String name) throws SQLException {
        StockBean stockBean = null;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select latest_price from " + dbName + " where name=''" + name + "''");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        while (resultSet.next()) {
            stockBean = new StockBean(resultSet.getDate(0), resultSet.getDouble(1), resultSet.getDouble(2),
                    resultSet.getDouble(3), resultSet.getDouble(4), resultSet.getDouble(5), resultSet.getDouble(6));
        }

        return stockBean;
    }

    public StockBean[] getStockPrices(String[] names) throws SQLException {
        StockBean[] stockBeans = new StockBean[names.length];

        for (int i = 0; i < names.length; i++) {
            stockBeans[i] = getStock(names[i]);
        }

        return stockBeans;
    }
}
