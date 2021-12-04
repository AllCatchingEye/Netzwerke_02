package abgabe6;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;

public class TCPEmpfaenger {

    private final int TIMEOUT = 1000;
    private long timeBegin;
    private long timeEnd;

    public static void main(String[] args) {
        new TCPEmpfaenger().startTCPEmpfaenger();
    }

    private void startTCPEmpfaenger() {
        try (ServerSocket servSock = new ServerSocket(4712)) {
            System.out.println("Waiting for clients...");
            try (Socket s = servSock.accept()) {
                try (DataInputStream dis = new DataInputStream(s.getInputStream());
                     BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                     BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))){
                    System.out.println("Got tcp connection.");

                    //byte bytesReceived = dis.readFully();
                    System.out.println(br.readLine());

                    bw.write("Bytes erhalten");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(timeEnd - timeBegin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
