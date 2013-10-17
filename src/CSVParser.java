import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * User: tgashby
 * Date: 10/17/13
 * Time: 9:09 AM
 */
public class CSVParser {
    public static ArrayList<StockBean> parse(String fileName) throws Exception
    {
        ArrayList<StockBean> stocks = new ArrayList<StockBean>();

        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);

            // Get rid of header, manually creating mappings for these
            beanReader.getHeader(true);

            final String[] header = new String[] {"date", "open", "high", "low", "close", "volume", "adj_close"};
            final CellProcessor[] processors = getProcessors();

            StockBean stock;
            while ((stock = beanReader.read(StockBean.class, header, processors)) != null) {
                stocks.add(stock);
            }

        }
        finally {
            if(beanReader != null) {
                beanReader.close();
            }
        }

        return stocks;
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new ParseDate("yyyy-mm-dd"), // Date
                new ParseDouble(), // Open
                new ParseDouble(), // High
                new ParseDouble(), // Low
                new ParseDouble(), // Close
                new ParseDouble(), // Volume
                new ParseDouble() // Adj Close
        };

        return processors;
    }
}
