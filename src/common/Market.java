package common;

import agents.AR1Agent;
import agents.BollingerBandAgent;
import agents.Agent;

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
    private ArrayList<Stock> stocks;
    private final int millisecondsInADay = 86400000;
    private ArrayList<Agent> agents;
    private int walletInUSDollars = 5000;

    public Market() throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        databaseConnection.connect();
        //stocks = databaseConnection.getStocksByDay(new Date(System.currentTimeMillis()));
        stocks = databaseConnection.getAllStocks();
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
                add(new AR1Agent(walletInUSDollars));
        }};
    }

    public void executeTrades() {
        Iterator<Stock> stockValueIterator = stocks.iterator();

        while (stockValueIterator.hasNext()) {
            Stock currentStock = stockValueIterator.next();

            for (int i = 0; i < agents.size(); i++) {
                agents.get(i).trade(currentStock);
            }
        }
    }

    public void printResults() {
        for (int i = 0; i < agents.size(); i++) {
            agents.get(i).printResults();
            System.out.println();
        }
    }
}
