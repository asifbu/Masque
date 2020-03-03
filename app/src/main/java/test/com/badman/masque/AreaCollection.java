package test.com.badman.masque;

public class AreaCollection {
    String month_year;
    String total;
    String monthly_total;

    public AreaCollection() {
    }

    public AreaCollection(String month_year, String total, String monthly_total) {
        this.month_year = month_year;
        this.total = total;
        this.monthly_total = monthly_total;
    }

    public String getMonth_year() {
        return month_year;
    }

    public void setMonth_year(String month_year) {
        this.month_year = month_year;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMonthly_total() {
        return monthly_total;
    }

    public void setMonthly_total(String monthly_total) {
        this.monthly_total = monthly_total;
    }
}
