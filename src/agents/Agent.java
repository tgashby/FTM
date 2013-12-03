package agents;

import common.Stock;

/**
 * Agent interfacce class. Please make your agent implement all of these
 * methods so that the market can do some comparisons between the different
 * agents!
 *
 * @author Robbie
 *
 * Fixed the agent abstract class up a bit. Removed trading managment for more flexability
 * and made necessary methods abstract so the compiler forces them to be implemented.
 *
 * - Allen
 */

public abstract class Agent {
    /*Liquid assets of your agent*/
    protected double wallet = 0;
    private double initialWallet;

    /**
     * Default constructor - be sure to call super() in your constructor.
     */
    public Agent(double initialCapital) {
        wallet = initialWallet = initialCapital;
    }

    public abstract void trade(Stock stock);
    public abstract String getAgentName();
    public abstract int getTotalNumberOfStocks();
    public abstract double getNetWorth();

    public void printResults() {
        System.out.println("Agent name: " + getAgentName());
        System.out.printf("Initial wallet is $%.2f\n", initialWallet);
        System.out.printf("Final wallet is $%.2f\n", wallet);
        System.out.println("Portfolio has " + getTotalNumberOfStocks() + " stocks");
        System.out.printf("Net worth is $%.2f\n", getNetWorth());
    }

    public double getWallet() {
        return wallet;
    }
}
