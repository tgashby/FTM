package agents;

import common.StockValue;

/**
 * User: allen
 * Date: 11/29/13
 * Time: 8:09 PM
 */
public interface AgentInterface {
    public void trade(StockValue stockValue);
    public String getResults();
}
