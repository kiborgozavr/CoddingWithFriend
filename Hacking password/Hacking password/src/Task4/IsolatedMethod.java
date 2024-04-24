package Task4;

import java.io.*;

public class IsolatedMethod {
    public static final String REAL_PASSWORD = "aAv55d";
    public static final String REAL_LOGIN = "admin";


    public static final String DICTIONARY_PASSWORDS_FILE_PATH = "passwords.txt";
    public static final String DICTIONARY_LOGINS_FILE_PATH = "logins.txt";
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!_@".toCharArray();


    private static final int MAX_LENGTH = 10;
    private static boolean isPasswordFound = false;

    public static void main(String[] args) {


        try {
            long startTime = System.currentTimeMillis();

            findCorrectLogin(); // looking for correct login
            checkRainbowTable(); // looking for correct password by checking rainbow table
            crackPasswordBruteForce(); // looking for correct password by brute force

            System.out.println("The login is: " + AccessData.findLogin);
            System.out.println("The password is: " + AccessData.findPassword);
            long endTime = System.currentTimeMillis();

            System.out.println("Time: " + (endTime - startTime) + " ms");

        } catch (IOException e) {
            System.out.println("Exception:" + e.getMessage() + " " + e.getClass().getSimpleName());
        }
    }

    private static void findCorrectLogin() throws IOException {
        try (FileReader fileReader = new FileReader(DICTIONARY_LOGINS_FILE_PATH);
             BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String dictionaryLogin;
            while ((dictionaryLogin = reader.readLine()) != null) {

                boolean isCorrectLoginFind = isLoginCorrect(dictionaryLogin);

                if (isCorrectLoginFind) {
                    break;
                }
            }
        }
    }

    private static boolean isLoginCorrect(String loginFromTable) {

        if (loginFromTable.equals(REAL_LOGIN)) {
            AccessData.findLogin = loginFromTable;
            return true;
        }
        return false;
    }

    private static void checkRainbowTable() throws FileNotFoundException {
        try (FileReader fileReader = new FileReader(DICTIONARY_PASSWORDS_FILE_PATH);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkAllCasesRecursive(char[] passwordGuess, int cycle) {
        if (isPasswordFound) {
            return;
        }
        if (cycle == passwordGuess.length - 1) {
            isPasswordFound = isPasswordCorrect(passwordGuess);
            if (isPasswordFound) {
                AccessData.findPassword = new String(passwordGuess);
            } else {
                makeCharUpperCase(passwordGuess, cycle);
                isPasswordFound = isPasswordCorrect(passwordGuess);
                if (isPasswordFound) {
                    AccessData.findPassword = new String(passwordGuess);
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

    private static boolean isPasswordCorrect(char[] arrayForWord) {
        String passwordString = new String(arrayForWord);

        if (passwordString.equals(REAL_PASSWORD)) {
            AccessData.findPassword = passwordString;
            isPasswordFound = true;
            return true;
        }
        return false;
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

    private static void crackPasswordBruteForce() {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            crackPasswordRecursive("", length);
            if (isPasswordFound) {
                break;
            }
        }
    }

    private static void crackPasswordRecursive(String findPassword, int length) {
        if (findPassword.length() == length) {
            if (findPassword.equals(REAL_PASSWORD)) {
                AccessData.findPassword = findPassword;
                isPasswordFound = true;
            }
        } else {
            for (char c : CHARACTERS) {
                crackPasswordRecursive(findPassword + c, length);
                if (isPasswordFound) {
                    break;
                }
            }
        }
    }


    static class AccessData {
        public static String findLogin;
        public static String findPassword;

        String login;
        String password;

        public AccessData(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }
}
