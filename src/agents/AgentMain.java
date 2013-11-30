package agents;

import java.util.ArrayList;

public class AgentMain {
    public static void main(String[] args) {
        ArrayList<String> valuesToTrade = new ArrayList<String>(10);
        valuesToTrade.add("VZ");
        valuesToTrade.add("KR");
        valuesToTrade.add("BKW");
        valuesToTrade.add("GOOG");
        valuesToTrade.add("MSFT");
        valuesToTrade.add("OLN");
        valuesToTrade.add("BA");
        valuesToTrade.add("TDC");

        //20 and 2 are default values
        BollingerBandAgent bollingerBandAgent = new BollingerBandAgent(valuesToTrade, 50000, 40, 2);

        bollingerBandAgent.startTrading();
        System.out.printf("Final wallet is $%.2f\n", bollingerBandAgent.getFinalWallet());
        System.out.println("Portfolio has " + bollingerBandAgent.getFinalStockCounts() + " shares.");
        bollingerBandAgent.printStockNameAndFrequencyOutput();
        System.out.printf("Net worth is $%.2f\n", bollingerBandAgent.getNetWorth());
    }
}
