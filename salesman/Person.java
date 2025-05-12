package salesman;

public class Person {
    protected String id;
    protected String name;
    protected String phone;

    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getID() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    public String getProfile() {
        return "ID: " + id + "\nName: " + name + "\nPhone: " + phone;
    }
}
