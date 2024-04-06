package Cloning;
public class RunnerStudent {
    public static void main(String[] args) {

        Student student = new Student("John", "Doe", 20, 3);
        Student copyOfStudent = student.clone();

        student.printInfo();
        copyOfStudent.printInfo();

        System.out.println("==============================");

        copyOfStudent.getFullName().setFirstName("Kate");
        copyOfStudent.getFullName().setLastName("Black");
        copyOfStudent.setAge(30);
        copyOfStudent.setCourse(4);

        student.printInfo();
        copyOfStudent.printInfo();
    }
}
