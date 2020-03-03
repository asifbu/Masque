package test.com.badman.masque;

public class Sub_admin
{
    String name;
    String code;
    String area;
    String id;

    public Sub_admin()
    {

    }

    public Sub_admin(String name, String code, String area,String id) {
        this.name = name;
        this.code = code;
        this.area = area;
        this.id   = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
