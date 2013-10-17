import java.util.Date;

/**
 * User: tgashby
 * Date: 10/17/13
 * Time: 9:11 AM
 */
public class StockBean {
    private Date date;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
    private Double adj_close;

    public StockBean() {}

    public StockBean(Date date, Double open, Double high, Double low, Double close, Double volume, Double adj_close) {

        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.adj_close = adj_close;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getAdj_close() {
        return adj_close;
    }

    public void setAdj_close(Double adj_close) {
        this.adj_close = adj_close;
    }
}
