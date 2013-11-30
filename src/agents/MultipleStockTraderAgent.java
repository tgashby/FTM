package agents;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: allen
 * Date: 11/30/13
 * Time: 12:11 PM
 */
public abstract class MultipleStockTraderAgent extends Agent {
    protected ArrayList<String> stockSymbolsToTrade;
    protected HashMap<String, Integer> numberOfShares;
    protected HashMap<String, Double> lastValues;

    public MultipleStockTraderAgent(double initialCapital) {
        super(initialCapital);

        stockSymbolsToTrade = new ArrayList<String>() {{
            add("TWTR");
            add("VZ");
            add("KR");
            add("BKW");
            add("GOOG");
            add("MSFT");
            add("OLN");
            add("BA");
            add("MSI");
            add("TDC");
        }};

        numberOfShares = new HashMap<String, Integer>(stockSymbolsToTrade.size());
        lastValues = new HashMap<String, Double>(stockSymbolsToTrade.size());

        for (int i = 0; i < stockSymbolsToTrade.size(); i++)
            numberOfShares.put(stockSymbolsToTrade.get(i), 0);
    }

    @Override
    public int getTotalNumberOfStocks() {
        int sum = 0;

        for (int i = 0; i < numberOfShares.size(); i++)
            sum += numberOfShares.get(stockSymbolsToTrade.get(i));
        return sum;
    }

    @Override
    public double getNetWorth() {
        int portfolioWorth = 0;

        for (int i = 0; i < numberOfShares.size(); i++)
            portfolioWorth += numberOfShares.get(stockSymbolsToTrade.get(i)) * lastValues.get(stockSymbolsToTrade.get(i));
        return wallet + portfolioWorth;
    }

    @Override
    public void printResults() {
        super.printResults();
        String leftAlignFormat = "| %-8s | %-9d |%n";

        System.out.format("+----------+------------+%n");
        System.out.printf("| Stock    | Frequency  |%n");
        System.out.format("+----------+------------+%n");
        for (int i = 0; i < stockSymbolsToTrade.size(); i++)
            System.out.format(leftAlignFormat, stockSymbolsToTrade.get(i), numberOfShares.get(stockSymbolsToTrade.get(i)));
        System.out.format("+----------+------------+%n");
    }
}
