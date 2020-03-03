package test.com.badman.masque;

public class Paid
{
    String id;
    String month;
    String year;

    public Paid()
    {

    }


    public Paid(String id, String month, String year) {
        this.id = id;
        this.month = month;
        this.year = year;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
