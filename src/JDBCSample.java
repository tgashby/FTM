import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//add the JDBC driver to your classpath, located in lib/mysql-connector
public class JDBCSample {
    private Connection connection;

    private String jdbcURL;
    private StringBuilder jdbcURLBuilder = new StringBuilder(100);
    private String hostname = "ftmdb.cbeadsxspecl.us-west-2.rds.amazonaws.com";
    private String port = "3306";
    private String dbName = "Stocks";
    private String username = "ftm";
    private String password = "ftm-pass";

    public JDBCSample() {
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

    public void executeSQLStatement(String sqlStatement) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sqlStatement);
    }
}
