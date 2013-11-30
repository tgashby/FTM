package common;

import org.apache.commons.lang.StringEscapeUtils;

import java.sql.Date;
import java.sql.Time;

public class StockValue {
    private String symbol;
    private String name;
    private Date date;
    private Time time;
    private double value;

    public StockValue(String symbol, String name, Date date, Time time, double value) {
        this.symbol = StringEscapeUtils.escapeSql(symbol);
        this.name = StringEscapeUtils.escapeSql(name);
        this.date = date;
        this.time = time;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public double getValue() {
        return value;
    }
}
