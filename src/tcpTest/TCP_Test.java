package tcpTest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_Test {
    public static void main(String[] args) {
            System.out.println("opening Socket.");
            try (ServerSocket servSock = new ServerSocket(4712)) {
                System.out.println("ServSock online");
                try (Socket s = servSock.accept();
                     BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                    System.out.println("Got Client connection.");
                    StringBuilder content = new StringBuilder();
                    for (String line = br.readLine(); line != null
                            && line.length() > 0; line = br.readLine()) {
                        content.append(line);
                    }
                    System.out.println(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
