package agents;

import common.Market;
import common.StockValue;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 7:00 AM
 */
public class AgentMain {
    public static void main(String[] args) {
        Market market = new Market();
        StockValue stock;
        double wallet = 500.00;
        double lastValue = 0.00;
        double curValue = 0.00;
        int numShares = 0;

        while ((stock = market.getNextValue()) != null && wallet > 0) {
            //System.out.println(stock.getValue());
            lastValue = curValue;
            curValue = stock.getValue();

            if (lastValue > curValue) {
                //price dropped, buy 1 share
                if (wallet > curValue) {
                    wallet -= curValue;
                    numShares++;
                }
            }
            else if (lastValue < curValue) {
                //price rose, sell 1 share
                if (numShares > 0) {
                    numShares--;
                    wallet += curValue;
                }
            }
        }

        System.out.printf("Final wallet is %.2f", wallet);
    }
}
