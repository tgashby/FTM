package common;

import agents.AgentInterface;
import agents.BollingerBandAgent;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 6:47 AM
 */
public class Market {
    private ArrayList<StockValue> stockValues;
    private int stockNdx;
    /**
     * Use this to select different days from today in the getStocksByDay method
     */
    private final int millisecondsInADay = 86400000;
    private ArrayList<AgentInterface> agents;

    public Market() throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        databaseConnection.connect();
        stockValues = databaseConnection.getStocksByDay(new Date(System.currentTimeMillis()));
        databaseConnection.disconnect();

        /**
         * refer to here for an explanation of this trick:
         * http://stackoverflow.com/questions/924285/efficiency-of-java-double-brace-initialization
         *
         * Add an agent to the list so it gets invoked.
         */
        agents = new ArrayList<AgentInterface>()
        {{
            add(new BollingerBandAgent());
        }};
    }

    public void executeTrades() {
        StockValue currentStockValue = stockValues.get(stockNdx++);

        while (hasNextStockValue())
            for (int i = 0; i < agents.size(); i++)
                agents.get(i).trade(currentStockValue);
    }

    public void printResults() {
        for (int i = 0; i < agents.size(); i++)
            System.out.print(agents.get(i).getResults());
    }

    private boolean hasNextStockValue() {
        return stockNdx < stockValues.size();
    }
}
