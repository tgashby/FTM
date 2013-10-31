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

        while ((stock = market.getNextValue()) != null) {
            System.out.println(stock.getValue());
        }
    }
}
