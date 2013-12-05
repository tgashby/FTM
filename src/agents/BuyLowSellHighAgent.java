package agents;


import common.BasicStatistics;
import common.Stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 7:00 AM
 */
public class BuyLowSellHighAgent extends MultipleStockTraderAgent {
    private HashMap<String, ArrayBlockingQueue<Stock>> stockValueQueues;
    private HashMap<String, BasicStatistics> basicStatistics;
    private int sampleSize = 10;


    public BuyLowSellHighAgent(double capital) {
        super(capital);

        stockValueQueues = new HashMap<String, ArrayBlockingQueue<Stock>>(stockSymbolsToTrade.size());
        basicStatistics = new HashMap<String, BasicStatistics>(stockSymbolsToTrade.size());
        for (int i = 0; i < stockSymbolsToTrade.size(); i++) {
            stockValueQueues.put(stockSymbolsToTrade.get(i), new ArrayBlockingQueue<Stock>(sampleSize));
            basicStatistics.put(stockSymbolsToTrade.get(i), new BasicStatistics(sampleSize));
        }
    }

    public void trade(Stock stock) {
        if (stockSymbolsToTrade.contains(stock.getSymbol())) {
            lastValues.put(stock.getSymbol(), stock.getValue());
            if (stockValueQueues.get(stock.getSymbol()).size() < sampleSize) {
                stockValueQueues.get(stock.getSymbol()).add(stock);
                basicStatistics.get(stock.getSymbol()).add(stock.getValue());
            } else {
                double mean = basicStatistics.get(stock.getSymbol()).getMean();
                int currentNumberOfShares = numberOfShares.get(stock.getSymbol());

                if (stock.getValue() < mean) {
                    //Low, buy 1 share
                    if (wallet > stock.getValue()) {
                        super.buy(stock);
                    }
                } else if (stock.getValue() > mean) {
                    //High, sell 1 share
                    if (currentNumberOfShares > 0) {
                        super.buy(stock);
                    }
                }
            }

        }
    }

    public String getAgentName() {
        return "Buy Low Sell High Agent";
    }
}
