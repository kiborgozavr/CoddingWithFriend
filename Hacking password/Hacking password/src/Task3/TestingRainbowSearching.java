package Task3;

import java.io.*;

public class TestingRainbowSearching {

    public static boolean is_password_found = false;
    public static final String PATTERN = "(\\w)";

    public static void main(String[] args) {
        String realPassword = "AbCDeF";

        try {
            checkRainbowTable(realPassword);
            System.out.println(is_password_found);
        } catch (IOException e) {
            System.out.println("Error");
        }

    }

    public static void checkRainbowTable(String realPassword) throws IOException {
        String dictionaryFilePath = "D:/0 КАТЯ/ОБУЧЕНИЕ JAVA/ВСЯКИЕ ПРОГИ/Программирование с Сергеем/CoddingWithFriend/Hacking password/Hacking password/passwords.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFilePath))) {
            String password;
            while ((password = reader.readLine()) != null) {

                char[] arrayForWord = password.toLowerCase().toCharArray();

                int cycle = 0;
                int lengthOfPassword = arrayForWord.length;

                findCaseRecursive(arrayForWord, cycle, lengthOfPassword, realPassword);
                if (is_password_found) {
                    break;
                }
            }
        }
    }

    private static void findCaseRecursive(char[] arrayForWord, int cycle, int lengthOfPassword, String realPassword) {
        if (is_password_found) {
            return;
        }
        if (cycle == lengthOfPassword - 1) {
            is_password_found = checkPassword(arrayForWord, realPassword);
            if (is_password_found) {
                System.out.println("Password found: " + new String(arrayForWord));
            } else {
                arrayForWord = makeUpperCase(arrayForWord, cycle);
                is_password_found = checkPassword(arrayForWord, realPassword);
                if (is_password_found) {
                    System.out.println("Password found: " + new String(arrayForWord));
                    return;
                }
                arrayForWord = makeLowerCase(arrayForWord, cycle);

            }
        } else {
            findCaseRecursive(arrayForWord, cycle+1, lengthOfPassword, realPassword);

            arrayForWord = makeUpperCase(arrayForWord, cycle);
            findCaseRecursive(arrayForWord, cycle+1, lengthOfPassword, realPassword);
            arrayForWord = makeLowerCase(arrayForWord, cycle);

        }
    }

    private static boolean checkPassword(char[] arrayForWord, String realPassword) {
        String temp = new String(arrayForWord);
        return (temp.equals(realPassword));
    }

    private static char[] makeUpperCase(char[] arrayForWord, int cycle) {
        char tempChar = arrayForWord[cycle];
        if (String.valueOf(tempChar).matches(PATTERN)) {
            arrayForWord[cycle] = (Character.toUpperCase(tempChar));
            return arrayForWord;
        }
        return arrayForWord;
    }

    private static char[] makeLowerCase(char[] arrayForWord, int cycle) {
        char tempChar = arrayForWord[cycle];
        if (String.valueOf(tempChar).matches(PATTERN)) {
            arrayForWord[cycle] = (Character.toLowerCase(tempChar));
            return arrayForWord;
        }
        return arrayForWord;
    }

}
