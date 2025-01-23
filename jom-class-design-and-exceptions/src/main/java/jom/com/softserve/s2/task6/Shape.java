package jom.com.softserve.s2.task6;

public abstract class Shape {

    private String name;

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double getArea();

}
