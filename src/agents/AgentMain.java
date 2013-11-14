package agents;

public class AgentMain {
    public static void main(String[] args) {
        BollingerBandAgent bollingerBandAgent = new BollingerBandAgent();

        bollingerBandAgent.startTrading(500);
        System.out.printf("Final wallet is $%.2f\n", bollingerBandAgent.getFinalWallet());
        System.out.println("Portfolio has " + bollingerBandAgent.getFinalStockCount() + " shares.");
        System.out.printf("Net worth is $%.2f\n", bollingerBandAgent.getNetWorth());
    }
}
