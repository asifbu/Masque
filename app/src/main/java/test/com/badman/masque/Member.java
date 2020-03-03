package test.com.badman.masque;

public class Member {
    String id;
    String address;
    String name;
    String paid;
    String mobile;
    String amountofmoney;
    String email;
    String image;
    String receivedby;

    public Member()
    {

    }

    public Member(String id, String address, String name, String paid, String mobile, String amountofmoney, String email, String image,String receivedby) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.paid = paid;
        this.mobile = mobile;
        this.amountofmoney = amountofmoney;
        this.email = email;
        this.image = image;
        this.receivedby=receivedby;
    }

    public String getReceivedby() {
        return receivedby;
    }

    public void setReceivedby(String receivedby) {
        this.receivedby = receivedby;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmountofmoney() {
        return amountofmoney;
    }

    public void setAmountofmoney(String amountofmoney) {
        this.amountofmoney = amountofmoney;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}