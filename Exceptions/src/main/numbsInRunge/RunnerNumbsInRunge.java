package main.numbsInRunge;

import java.util.Scanner;

public class RunnerNumbsInRunge {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int start = 5;
        int end = 50;

        int numberOfNumbs = 10;

        try {
            for (int i = 0; i < numberOfNumbs; i++) {
                readNumber(start, end);
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void readNumber(int start, int end) throws IllegalArgumentException {

        String str = SCANNER.nextLine();
        int numb = Integer.parseInt(str); //TODO: cover this problem

        if (numb < start || numb > end) {
            throw new IllegalArgumentException("The number isn't in the runge between " + start + " and " + end);
        } else {
            System.out.println("You have just input the number " + numb);
        }
    }
}
