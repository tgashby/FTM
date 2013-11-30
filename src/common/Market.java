package common;

import agents.Agent;
import agents.BollingerBandAgent;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 6:47 AM
 */
public class Market {
    private ArrayList<StockValue> stockValues;
    private final int millisecondsInADay = 86400000;
    private ArrayList<Agent> agents;
    private int walletInUSDollars = 5000;

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
        agents = new ArrayList<Agent>()
        {{
            add(new BollingerBandAgent(walletInUSDollars));
        }};
    }

    public void executeTrades() {
        Iterator<StockValue> stockValueIterator = stockValues.iterator();

        while (stockValueIterator.hasNext()) {
            StockValue currentStockValue = stockValueIterator.next();

            for (int i = 0; i < agents.size(); i++) {
                agents.get(i).trade(currentStockValue);
            }
        }
    }

    public void printResults() {
        for (int i = 0; i < agents.size(); i++)
            agents.get(i).printResults();
    }
}
