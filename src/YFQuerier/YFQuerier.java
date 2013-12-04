package YFQuerier;

import common.DatabaseConnection;
import common.Stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.TimerTask;

/**
 * User: Tag
 * Date: 10/28/13
 * Time: 7:44 AM
 */

/**
 * Possible URL options, not all of these work... Yahoo doesn't officially support this anymore so they won't tell me
 * what works and what doesn't.
 a	 Ask	                                    a2	 Average Daily Volume	                    a5	 Ask Size
 b	 Bid	                                    b2	 Ask (Real-time)	                        b3	 Bid (Real-time)
 b4	 Book Value	                                b6	 Bid Size	                                c	 Change & Percent Change
 c1	 Change	                                    c3	 Commission	                                c6	 Change (Real-time)
 c8	 After Hours Change (Real-time)	            d	 Dividend/Share	                            d1	 Last Trade Date
 d2	 Trade Date	                                e	 Earnings/Share	                            e1	 Error Indication (returned for symbol changed / invalid)
 e7	 EPS Estimate Current Year	                e8	 EPS Estimate Next Year	                    e9	 EPS Estimate Next Quarter
 f6	 Float Shares	                            g	 Day's Low	                                h	 Day's High
 j	 52-week Low	                            k	 52-week High	                            g1	 Holdings Gain Percent
 g3	 Annualized Gain	                        g4	 Holdings Gain	                            g5	 Holdings Gain Percent (Real-time)
 g6	 Holdings Gain (Real-time)	                i	 More Info	                                i5	 Order Book (Real-time)
 j1	 Market Capitalization	                    j3	 Market Cap (Real-time)	                    j4	 EBITDA
 j5	 Change From 52-week Low	                j6	 Percent Change From 52-week Low	        k1	 Last Trade (Real-time) With Time
 k2	 Change Percent (Real-time)	                k3	 Last Trade Size	                        k4	 Change From 52-week High
 k5	 Percebt Change From 52-week High	        l	 Last Trade (With Time)	                    l1	 Last Trade (Price Only)
 l2	 High Limit	                                l3	 Low Limit	                                m	 Day's Range
 m2	 Day's Range (Real-time)	                m3	 50-day Moving Average	                    m4	 200-day Moving Average
 m5	 Change From 200-day Moving Average	        m6	 Percent Change From 200-day Moving Average	m7	 Change From 50-day Moving Average
 m8	 Percent Change From 50-day Moving Average	n	 Name	                                    n4	 Notes
 o	 Open	                                    p	 Previous Close	                            p1	 Price Paid
 p2	 Change in Percent	                        p5	 Price/Sales	                            p6	 Price/Book
 q	 Ex-Dividend Date	                        r	 P/E Ratio	                                r1	 Dividend Pay Date
 r2	 P/E Ratio (Real-time)	                    r5	 PEG Ratio	                                r6	 Price/EPS Estimate Current Year
 r7	 Price/EPS Estimate Next Year	            s	 Symbol	                                    s1	 Shares Owned
 s7	 Short Ratio	                            t1	 Last Trade Time	                        t6	 Trade Links
 t7	 Ticker Trend	                            t8	 1 yr Target Price	                        v	 Volume
 v1	 Holdings Value	                            v7	 Holdings Value (Real-time)	                w	 52-week Range
 w1	 Day's Value Change	                        w4	 Day's Value Change (Real-time)	            x	 Stock Exchange
 y	 Dividend Yield
 */
public class YFQuerier extends TimerTask {
    private String urlOptions;
    private DatabaseConnection dbCon;

    public YFQuerier(String[] stocks, String options) throws SQLException
    {
        urlOptions = "";

        for (String stock : stocks)
        {
            urlOptions += stock + "+";
        }
        urlOptions = urlOptions.substring(0, urlOptions.length() - 1);

        urlOptions += "&f=" + options;

        dbCon = new DatabaseConnection();
        dbCon.connect();
    }

    @Override
    public void run() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("EST"));

        Calendar exchangeOpen = Calendar.getInstance();
        exchangeOpen.clear();
        exchangeOpen.setTimeZone(TimeZone.getTimeZone("EST"));
        exchangeOpen.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 9, 30);

        Calendar exchangeClose = Calendar.getInstance();
        exchangeClose.clear();
        exchangeClose.setTimeZone(TimeZone.getTimeZone("EST"));
        exchangeClose.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 16, 0);

        System.out.println("\nBeginning Stock Query " + cal.getTime());

        if (cal.after(exchangeOpen) && cal.before(exchangeClose) &&
                cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
        {
            try {
                System.out.println("Attempting to add the stock information to the DB");

                URL yahooFinance = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + urlOptions);
                URLConnection yc = yahooFinance.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null)
                {
                    String[] stockParts = inputLine.split(",");

                    for (int i = 0; i < stockParts.length; i++)
                        stockParts[i] = stockParts[i].replaceAll("\"", "");

                    Stock stock = new Stock(stockParts[0], stockParts[1], new Date(new java.util.Date().getTime()),
                            new Time(System.currentTimeMillis()), new Double(stockParts[2]));

                    try {
                    dbCon.insertStock(stock);
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }

                    System.out.println(stockParts[0] + " added");
                }

                System.out.println("All Stocks Added");

                in.close();
            }
            catch (MalformedURLException urlException)
            {
                urlException.printStackTrace();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
        else
        {
            if (!cal.after(exchangeOpen))
            {
                System.out.println("No Transaction: Before market open (" + exchangeOpen.getTime() + ")");
            }
            else if (!cal.before(exchangeClose))
            {
                System.out.println("No Transaction: After market close (" + exchangeClose.getTime() + ")");
            }
            else
            {
                System.out.println("No Transaction: It's the weekend");
            }
        }

        System.out.println("End Stock Query");
    }

    public void disconnect()
    {
        dbCon.disconnect();
    }
}
