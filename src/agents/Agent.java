/**
 * Agent interfacce class. Please make your agent implement all of these
 * methods so that the market can do some comparisons between the different
 * agents!
 *
 * @author Robbie
 */

public abstract class Agent {
   /*Liquid assets of your agent*/
   public double wallet = 0;

   /*Number of shares of stock it owns*/
   public int numShares = 0;

   /*Most Recent Stock Value*/
   public double lastValue;

   /**
    * Default constructor - be sure to call super() in your constructor.
    */
   public Agent(double initialCapital){
      wallet = initialCapital;
      numShares = 0;
   }

   /**
    * Market will call this once for each agent and your agent will be
    * expected to do its reasoning with this next new value it is receiving
    *
    * override this method and call super(value); to make sure lastValue gets
    *  set.
    */
   public void handleNextStockValue(double value){
      lastValue = value;
   }

   /**
    * Buy stock(s)
    */
   public void buyStock(){
      if (wallet > lastValue) {
         wallet -= lastValue;
         numShares++;
      }
   }

   public void buyStocks(int num) {
      for(int i = 0; i < num; i ++)
         buyStock();
   }

   /**
    * Sell Stock(s)
    */
   public void sellStock(){
      if (numShares > 0) {
         wallet += lastValue;
         numShares--;
      }
   }

   public void sellStocks(int num) {
      for (int i = 0; i < num; i++)
         sellStock();
   }


   public int getShares(){
      return numShares;
   }

   public double liquidAssets(){
      return wallet;
   }

   public double netWorth(){
      return wallet + (numShares * lastValue);
   }
}
