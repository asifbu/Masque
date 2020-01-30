package test.com.badman.masque;

public class Sub_admin
{
    String name;
    String code;

    public Sub_admin()
    {

    }

    public Sub_admin(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
