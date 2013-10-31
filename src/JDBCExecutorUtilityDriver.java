import java.sql.SQLException;

/**
 * Created by allen on 10/22/13
 */
public class JDBCExecutorUtilityDriver {

    public static void main(String[] args) {
        JDBCExecutorUtility jdbcSample = new JDBCExecutorUtility();
        try {
            jdbcSample.connect();
            jdbcSample.executeSQLStatement("create table stocks ( " +
                    "symbol VARCHAR(255) not null," +
                    "name VARCHAR(255) not null," +
                    "day DATE not null," +
                    "time TIME not null," +
                    "value DECIMAL not null)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
