package jom.com.softserve.s2.task6;

class Rectangle extends Shape {
    private double height;
    private double width;

    public Rectangle(String name, double height, double width) {
        super(name);
        this.height = height;
        this.width = width;
    }


    @Override
    public double getArea() {
        double result = this.getHeight() * this.getWidth();
        return result;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Rectangle rectangle = (Rectangle) obj;
        return Double.compare(rectangle.height, this.height) == 0 && Double.compare(rectangle.width, this.width) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(height, width);
    }

    @Override
    public String toString() {
        return "Rectangle [height=" + this.getHeight() + ", width=" + this.getWidth() + "]";
    }
}