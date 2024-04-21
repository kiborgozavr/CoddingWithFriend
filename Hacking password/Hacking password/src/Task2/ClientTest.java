package main.St2.ServerTest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientTest {
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int MAX_LENGTH = 5;
    private static volatile boolean passwordFound = false;


    public static void main(String[] args) {
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(serverAddress, serverPort);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            try {
                crackPasswordLoop(input, output);
            } catch (IOException e) {
                System.out.println("Exception:" + e.getMessage() + " " + e.getClass().getSimpleName());
            }
        } catch (IOException e) {
            System.out.println("Connection to server exception:" + e.getMessage());
        }
    }

    public static void crackPasswordLoop(DataInputStream input, DataOutputStream output) throws IOException {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            StringBuilder builder = new StringBuilder();
            if (checkPasswordRecursive(0, builder, input, output, length))
            {
                System.out.println(builder.toString());
                break;
            }
        }
    }

    public static boolean checkPasswordRecursive(int i, StringBuilder passwordAttempt, DataInputStream input, DataOutputStream output, int length) throws IOException
    {
        passwordAttempt.append("a");
        for (char c : CHARACTERS) {
            passwordAttempt.setCharAt(i, c);
            if (passwordAttempt.length() != length) {
                if (checkPasswordRecursive(i + 1, passwordAttempt, input, output, length)) {
                    return true;
                }
            } else {
                if (checkPassword(passwordAttempt.toString(), input, output)) {
                    return true;
                }
            }
        }
        passwordAttempt.deleteCharAt(i);
        return false;
    }

    public static  boolean checkPassword(String password, DataInputStream input, DataOutputStream output) throws IOException
    {
        output.writeUTF(password);
        String response = input.readUTF();
        if (response.equals("Connection success!")) {
            return true;
        }
        return false;
    }

    public static void crackPassword(DataInputStream input, DataOutputStream output) throws IOException {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            if (crackPasswordRecursive("", length, input, output)) {
                break;
            }
        }
    }

    private static boolean crackPasswordRecursive(String findPassword, int length, DataInputStream input, DataOutputStream output) throws IOException {
        if (findPassword.length() == length) {
            output.writeUTF(findPassword);
            String response = input.readUTF();
            if (response.equals("Connection success!")) {
                System.out.println(findPassword);
                return true;
            }
            return false;
        } else {
            for (char c : CHARACTERS) {
                if (crackPasswordRecursive(findPassword + c, length, input, output))
                {
                    return true;
                }
            }
            return false;
        }
    }
}
