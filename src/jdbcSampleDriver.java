import java.sql.SQLException;

/**
 * Created by allen on 10/22/13
 */
public class JDBCSampleDriver {

    public static void main(String[] args) {
        JDBCSample jdbcSample = new JDBCSample();
        try {
            jdbcSample.connect();
            //crashes since query cannot be empty
            jdbcSample.executeSQLStatement("");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
