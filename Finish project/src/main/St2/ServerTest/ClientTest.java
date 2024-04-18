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
                crackPassword(input, output);
            } catch (IOException e) {
                System.out.println("Exception:" + e.getMessage() + " " + e.getClass().getSimpleName());
            }
        } catch (IOException e) {
            System.out.println("Connection to server exception:" + e.getMessage());
        }
    }

    public static void crackPassword(DataInputStream input, DataOutputStream output) throws IOException {
        for (int length = 1; length <= MAX_LENGTH; length++) {
            crackPasswordRecursive("", length, input, output);
            if (passwordFound)
            {
                break;
            }
        }
    }

    private static void crackPasswordRecursive(String findPassword, int length, DataInputStream input, DataOutputStream output) throws IOException {
        if (findPassword.length() == length) {
            output.writeUTF(findPassword);
            String response = input.readUTF();
            if (response.equals("Connection success!")) {
                System.out.println(findPassword);
                System.out.println("pass");
                passwordFound = true;
            }
        }
        else {
            for (char c : CHARACTERS) {
                crackPasswordRecursive(findPassword + c, length, input, output);
                if (passwordFound)
                {
                    break;
                }
            }
        }
    }
}
