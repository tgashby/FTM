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
}
