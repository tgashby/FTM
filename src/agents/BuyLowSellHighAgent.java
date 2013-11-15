package agents;

import common.Market;
import common.StockValue;

/**
 * User: Tag
 * Date: 10/31/13
 * Time: 7:00 AM
 */
public class BuyLowSellHighAgent {
    public static void main(String[] args) {
        Market market = new Market();
        StockValue stock;
        double wallet = 50000.00;


        int counter = -1;
        int numStocks =10;


        double[] curValue = new double[numStocks];
        int[] numShares = new int[numStocks];
        double[] averages= new double[numStocks];
        //String[] stockNames = new String[numStocks];



        for(int i=0;i<10;i++){
            for(int j=0;j<numStocks;j++){
                stock = market.getNextValue();
                averages[j]+=stock.getValue();
                //stockNames[j]=stock.getSymbol();
            }
        }
        for(int i =0;i<numStocks;i++){
            averages[i]=averages[i]/10;
            //System.out.println(averages[i]);
        }


        while ((stock = market.getNextValue()) != null && wallet > 0) {
            counter=(counter+1)%10;
            curValue[counter] = stock.getValue();

            if (curValue[counter]<averages[counter]) {
                //price is low, buy 1 share
                if (wallet > curValue[counter]) {
                    wallet -= curValue[counter];
                    numShares[counter]++;
                    System.out.println("buying a share at $" + curValue[counter]);
                }
            }
            else if (curValue[counter] > averages[counter]) {
                //price is high, sell 1 share
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
