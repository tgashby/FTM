package common;

/**
 * Created by allen on 10/30/13
 */
public enum StockTableColumnNames {
    SYMBOL("symbol"),
    NAME("name"),
    DAY("day"),
    TIME("time"),
    VALUE("value");

    /**
     * @param text
     */
    private StockTableColumnNames(final String text) {
        this.text = text;
    }

    private final String text;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
