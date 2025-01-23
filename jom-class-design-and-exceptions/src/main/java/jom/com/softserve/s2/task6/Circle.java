package jom.com.softserve.s2.task6;

class Circle extends Shape {

    private double radius;

    public Circle(String name, double radius) {
        super(name);
        this.radius = radius;
    }

    @Override
    public double getArea() {
        double area = Math.PI * Math.pow(this.getRadius(), 2);
        return area;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Circle circle = (Circle) obj;
        boolean b = this.radius == circle.radius;
        return b;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(radius);
    }

    @Override
    public String toString() {
        return ("Circle [radius=" + this.getRadius() + "]");
    }
}