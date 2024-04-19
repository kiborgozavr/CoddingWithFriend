package Task2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientTest {
    private static final int MAX_LENGTH = 5;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    private static volatile boolean passwordFound = false;
    public static String finalPassword = "";

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 34522;

        try (Socket socket = new Socket(serverAddress, serverPort);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            try {
                crackPassword(input, output);

                System.out.println("Пароль: " + finalPassword);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void crackPassword(DataInputStream input, DataOutputStream output) throws IOException {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            crackPasswordRecursive("", length, input, output);
            if (passwordFound) {
                break;
            }
        }
    }

    private static void crackPasswordRecursive(String findPassword, int length, DataInputStream input, DataOutputStream output) throws IOException {
        if (findPassword.length() == length) {
            output.writeUTF(findPassword);
            String response = input.readUTF();
            if (response.equals("Connection success!")) {
                finalPassword = findPassword;
                passwordFound = true;
            }
        } else {
            if (passwordFound) {
                return;
            }
            for (char c : CHARACTERS) {
                crackPasswordRecursive(findPassword + c, length, input, output);
            }
        }
    }
}
