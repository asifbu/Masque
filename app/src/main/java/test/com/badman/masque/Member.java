package test.com.badman.masque;

public class Member
{
    String strID;
    String strAddress;
    String strName;

    public Member() {
    }

    public Member(String strID, String strAddress, String strName) {
        this.strID = strID;
        this.strAddress = strAddress;
        this.strName = strName;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }
}
