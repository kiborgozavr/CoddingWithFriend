package main.div;

import java.util.Scanner;

public class RunnerDiv {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {

        double a = 0;
        double b = 0;

        try {
            a = inputDouble();
            b = inputDouble();
        } catch (NumberFormatException exception) {
            System.out.println(exception.getMessage());
            System.exit(0);
        }

        System.out.println("Result of dividing is " + div(a, b));

    }

    public static double inputDouble() {
        System.out.print("Input number: ");
        String a = SCANNER.nextLine();
        return Double.parseDouble(a);
    }

    public static double div(double a, double b) {
        return a / b;
    }
}
