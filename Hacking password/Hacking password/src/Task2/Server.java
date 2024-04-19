package Task2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 34522;

    public static void main(String[] args) {
        String realPassword = "99";

        try (ServerSocket server = new ServerSocket(PORT)) {

            try (Socket socket = server.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream()))
            {
                System.out.println("Клиент подключился.");
                while (true) {
                    String tempPassword = input.readUTF();

                    if (realPassword.equals(tempPassword)) {
                        System.out.println("Пароль верный");
                        output.writeUTF("Connection success!");
                    } else {
                        output.writeUTF("fail!");
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка во время общения с клиентом: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Ошибка запуска сервера: " + e.getMessage());
        }
    }
}

