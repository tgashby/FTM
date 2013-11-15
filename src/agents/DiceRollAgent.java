package agents;

import common.Market;
import common.StockValue;
import java.util.Random;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 7:00 AM
 */
public class DiceRollAgent {
    public static void main(String[] args) {
        Market market = new Market();
        StockValue stock;
        double wallet = 50000.00;

        int counter = -1;
        int numStocks =10;

        double[] curValue = new double[numStocks];
        int[] numShares = new int[numStocks];
        //String[] stockNames = new String[numStocks];
        Random rand = new Random();
        int randInt;

        while ((stock = market.getNextValue()) != null && wallet > 0) {
            counter=(counter+1)%10;
            curValue[counter] = stock.getValue();
            randInt = 1+rand.nextInt(6);
            if (randInt==6) {
                //rolled a 6, buy 1 share
                if (wallet > curValue[counter]) {
                    wallet -= curValue[counter];
                    numShares[counter]++;
                    System.out.println("buying a share at $" + curValue[counter]);
                }
            }
            else if (randInt==1) {
                //rolled a 1, sell 1 share
                if (numShares[counter] > 0) {
                    numShares[counter]--;
                    wallet += curValue[counter];
                    System.out.println("selling a share at $" + curValue[counter]);
                }
            }
        }

        System.out.println();
        System.out.printf("Final wallet is $%.2f\n", wallet);

        double totalShares = 0;
        for(int i =0;i<numStocks;i++){
            totalShares += numShares[i];
            //System.out.println(averages[i]);
        }
        System.out.println("Portfolio has " + totalShares + " shares.");

        double sum=0;
        for(int i =0;i<numStocks;i++){
            sum+=(numShares[i]*curValue[i]);
            //System.out.println(averages[i]);
        }

        System.out.println("Portfolio is worth $" + sum);
        System.out.println();
        double netWorth = wallet + sum;
        System.out.printf("Net worth is $%.2f\n", netWorth);
    }
}
