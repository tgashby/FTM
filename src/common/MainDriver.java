package common;

import java.sql.SQLException;

/**
 * User: allen
 * Date: 11/29/13
 * Time: 9:38 PM
 */
public class MainDriver {
    public static void main(String[] args) throws SQLException {
        Market market = new Market();

        market.executeTrades();
        market.printResults();
    }
}
