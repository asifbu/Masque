package test.com.badman.masque;

public class PaidHistory {

    String id;
    String receivedby;
    String taka;
    String paid;

    public PaidHistory() {
    }

    public PaidHistory(String id, String receivedby, String taka, String paid) {
        this.id = id;
        this.receivedby = receivedby;
        this.taka = taka;
        this.paid = paid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceivedby() {
        return receivedby;
    }

    public void setReceivedby(String receivedby) {
        this.receivedby = receivedby;
    }

    public String getTaka() {
        return taka;
    }

    public void setTaka(String taka) {
        this.taka = taka;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }
}
