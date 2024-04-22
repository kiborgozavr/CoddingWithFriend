package Task3;

import java.io.*;

public class TestingRainbowSearching {

    public static final String DICTIONARY_FILE_PATH = "passwords.txt";
    public static final String REAL_PASSWORD = "AbCDeF";
    public static  String findPassword = "";

    public static boolean isPasswordFound = false;

    public static void main(String[] args) {
        try {
            checkRainbowTable();
            System.out.println("Password is: " + findPassword);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error");
        }
    }


    public static void checkRainbowTable() throws IOException {
        try (FileReader fileReader = new FileReader(DICTIONARY_FILE_PATH);
             BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String dictionaryPassword;
            while ((dictionaryPassword = reader.readLine()) != null) {

                char[] dictionaryPasswordArray = dictionaryPassword.toLowerCase().toCharArray();

                int cycle = 0;

                checkAllCasesRecursive(dictionaryPasswordArray, cycle);
                if (isPasswordFound) {
                    break;
                }
            }
        }
    }

    private static void checkAllCasesRecursive(char[] passwordGuess, int cycle) {
        if (isPasswordFound) {
            return;
        }
        if (cycle == passwordGuess.length - 1) {
            isPasswordFound = isPasswordCorrect(passwordGuess);
            if (isPasswordFound) {
                findPassword = new String(passwordGuess);
            } else {
                makeCharUpperCase(passwordGuess, cycle);
                isPasswordFound = isPasswordCorrect(passwordGuess);
                if (isPasswordFound) {
                    findPassword = new String(passwordGuess);
                    return;
                }
                makeCharLowerCase(passwordGuess, cycle);
            }
        } else {
            checkAllCasesRecursive(passwordGuess, cycle + 1);

            makeCharUpperCase(passwordGuess, cycle);
            checkAllCasesRecursive(passwordGuess, cycle + 1);
            makeCharLowerCase(passwordGuess, cycle);
        }
    }

    private static boolean isPasswordCorrect(char[] dictionaryPassword) {
        String passwordString = new String(dictionaryPassword);
        return (passwordString.equals(REAL_PASSWORD));
    }

    private static void makeCharUpperCase(char[] charArray, int charIndex) {
        char targetChar = charArray[charIndex];
        if (Character.isLetter(targetChar)) {
            charArray[charIndex] = (Character.toUpperCase(targetChar));
        }
    }

    private static void makeCharLowerCase(char[] charArray, int charIndex) {
        char targetChar = charArray[charIndex];
        if (Character.isLetter(targetChar)) {
            charArray[charIndex] = (Character.toLowerCase(targetChar));
        }
    }
}