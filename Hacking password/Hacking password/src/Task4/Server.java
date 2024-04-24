package Task4;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static final int PORT = 34522;

    public static final String REAL_LOGIN = "admin";
    public static final String REAL_PASSWORD = "a";

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT)) {

            try (Socket socket = server.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream()))
            {
                System.out.println("The client has connected.");

                while (true) {
                    String jsonAccessData = input.readUTF();
                    verifyAccessData(jsonAccessData, output);
                }
            } catch (Exception e) {
                System.out.println("Error during communication with the client:" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Server startup error:" + e.getMessage());
        }
    }

    private static void verifyAccessData (String jsonAccessData, DataOutputStream output) throws IOException {
        Gson gson = new Gson();
        AccessData accessDataFromClient = gson.fromJson(jsonAccessData, AccessData.class);

        if(accessDataFromClient.login.equals(REAL_LOGIN)) {
            if(accessDataFromClient.password.equals(REAL_PASSWORD)) {
                ServerAnswer serverAnswer = new ServerAnswer("Connection success!");
                sendingJsonAnswer(serverAnswer, output);
            } else {
                ServerAnswer serverAnswer = new ServerAnswer("Wrong password!");
                sendingJsonAnswer(serverAnswer, output);
            }
        } else {
            ServerAnswer serverAnswer = new ServerAnswer("Wrong login!");
            sendingJsonAnswer(serverAnswer, output);
        }
    }

    private static void sendingJsonAnswer(ServerAnswer serverAnswer, DataOutputStream output) throws IOException {
        Gson gson = new Gson();
        String jsonServerAnswer = gson.toJson(serverAnswer);
        output.writeUTF(jsonServerAnswer);
    }

    static class AccessData {
        public String login;
        public String password;

        public AccessData(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    static class ServerAnswer {
        public String result;

        public ServerAnswer(String result) {
            this.result = result;
        }
    }
}

