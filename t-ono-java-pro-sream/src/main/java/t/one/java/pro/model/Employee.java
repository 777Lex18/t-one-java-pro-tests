package t.one.java.pro.model;

public class Employee {
    String name;
    int age;
    String position;

    public Employee(String name, int age, String position) {
        this.name = name;
        this.age = age;
        this.position = position;
    }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPosition() { return position; }
}
