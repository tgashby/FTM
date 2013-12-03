package agents;

import common.Stock;

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
            add("GOOG");
            add("OLN");
            add("KR");
            add("RTN");
            add("TDC");
            add("MSI");
            add("VZ");
            add("BA");
            add("BKW");
            add("MSFT");
        }};

        numberOfShares = new HashMap<String, Integer>(stockSymbolsToTrade.size());
        lastValues = new HashMap<String, Double>(stockSymbolsToTrade.size());

        for (int i = 0; i < stockSymbolsToTrade.size(); i++)
            numberOfShares.put(stockSymbolsToTrade.get(i), 0);
    }

    //TODO: buy as a function of wallet size
    protected void buy(Stock stock) {
        if (wallet - stock.getValue() > 0) {
            wallet -= stock.getValue();
            numberOfShares.put(stock.getSymbol(), numberOfShares.get(stock.getSymbol()) + 1);
        }
    }

    //TODO: sell as function of number of shares
    protected void sell(Stock stock) {
        if (numberOfShares.get(stock.getSymbol()) > 0) {
            numberOfShares.put(stock.getSymbol(), numberOfShares.get(stock.getSymbol()) - 1);
            wallet += stock.getValue();
        }
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
