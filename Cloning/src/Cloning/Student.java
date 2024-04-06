package Cloning;

public class Student extends Person implements Cloneable {

    private int course;

    public Student(String firstName, String lastName, int age, int course) {
        super(firstName, lastName, age);
        this.course = course;
    }

    @Override
    public String activity() {
        return "Cloning.Student " + getFullName().getFirstName() + " is studying.";
    }

    @Override
    public void info() {
        System.out.println("First name: " + getFullName().getFirstName() + ", Last name: " + getFullName().getLastName() + ", Age: " + getAge() + ", Course: " + course);
    }

    @Override
    public Student clone() {
        try {
            Student clonedStudent = (Student) super.clone();
            return clonedStudent;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }
}
