package agents;

import common.Stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 7:00 AM
 */
public class DiceRollAgent extends MultipleStockTraderAgent {
    private Random rand;

    public DiceRollAgent(double capital) {
        super(capital);
        rand = new Random();
    }

    public void trade(Stock stock) {
        if (stockSymbolsToTrade.contains(stock.getSymbol())) {
            lastValues.put(stock.getSymbol(), stock.getValue());
            int randInt = 1 + rand.nextInt(6);
            int currentNumberOfShares = numberOfShares.get(stock.getSymbol());

            if (randInt == 6) {
                //rolled a 6, buy 1 share
                if (wallet > stock.getValue()) {
                    super.buy(stock);
                }
            } else if (randInt == 1) {
                //rolled a 1, sell 1 share
                if (currentNumberOfShares > 0) {
                    super.sell(stock);
                }
            }


        }
    }

    public String getAgentName() {
        return "Dice Rolling Agent";
    }
}
