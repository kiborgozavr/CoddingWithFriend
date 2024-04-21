package Task1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class task1 {

    public static void main(String[] args) {
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String msg = args[2];

        try (Socket socket = new Socket(serverAddress, serverPort);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeUTF(msg);
            String response = input.readUTF();
            System.out.println(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
