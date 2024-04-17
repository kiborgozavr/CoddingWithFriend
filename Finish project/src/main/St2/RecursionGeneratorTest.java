package main.St2;

public class RecursionGeneratorTest {
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int MAX_LENGTH = 5;
    public static String finalPassword = "";

    public static void main(String[] args) {
        String realPassword = "99999";

        crackPassword(realPassword);
        System.out.println("Пароль подобран: " + finalPassword);


    }

    public static void crackPassword(String knownPassword) {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            crackPasswordRecursive("", knownPassword, length);
        }
    }

    private static void crackPasswordRecursive(String findPassword, String knownPassword, int length) {
        if (findPassword.length() == length) {
            if (findPassword.equals(knownPassword)) {
                finalPassword = findPassword;
                return;
            }
            return;
        }

        for (char c : CHARACTERS) {
            crackPasswordRecursive(findPassword + c, knownPassword, length);
        }
    }
}