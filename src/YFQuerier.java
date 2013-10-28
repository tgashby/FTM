import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: Tag
 * Date: 10/28/13
 * Time: 7:44 AM
 */
public class YFQuerier {
    public static void RunQuery(String options)
    {

        // TODO FIX
        String placeHolder = "XOM+BBDb.TO+JNJ+MSFT&f=snd1l1";
        options = placeHolder;

        try {
            URL yahooFinance = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + options);
            URLConnection yc = yahooFinance.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);

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
}
