package agents;

import common.Stock;

import java.util.HashMap;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 7:00 AM
 */
public class DumbTrendAgent extends MultipleStockTraderAgent {
    private HashMap<String, Boolean> first;

    public DumbTrendAgent(double capital) {
        super(capital);

        first = new HashMap<String, Boolean>(stockSymbolsToTrade.size());
        for (int i = 0; i < stockSymbolsToTrade.size(); i++) {
            first.put(stockSymbolsToTrade.get(i), true);
        }
    }

    public void trade(Stock stock) {
        if (stockSymbolsToTrade.contains(stock.getSymbol())) {
            if (first.get(stock.getSymbol())) {
                lastValues.put(stock.getSymbol(), stock.getValue());
                first.put(stock.getSymbol(), false);
            } else {
                int currentNumberOfShares = numberOfShares.get(stock.getSymbol());

                if (stock.getValue() > lastValues.get(stock.getSymbol())) {
                    //increasing, buy 1 share
                    super.buy(stock);
                } else if (stock.getValue() < lastValues.get(stock.getSymbol())) {
                    //decreasing, sell 1 share
                    super.sell(stock);
                }
                lastValues.put(stock.getSymbol(), stock.getValue());
            }
        }

    }

    public String getAgentName() {
        return "Dumb Trend Agent";
    }
}
