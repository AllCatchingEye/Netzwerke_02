package abgabe6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPEmpfaenger {

    private final int TIMEOUT = 1000;

    public static void main(String[] args) {
        new TCPEmpfaenger().startTCPEmpfaenger();
    }

    private void startTCPEmpfaenger() {
        try (ServerSocket servSock = new ServerSocket(4712)) {

            System.out.println("Waiting for clients...");
            while (true) {
                try (Socket s = servSock.accept()) {
                    long timeStart = System.currentTimeMillis();
                    StringBuilder stringBuilder = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                        for (String line = br.readLine(); line != null && line.length() > 0; line = br.readLine()){
                            stringBuilder.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int size = stringBuilder.toString().getBytes(StandardCharsets.UTF_8).length;
                    long timeEnd = System.currentTimeMillis();
                    long timeDiff = timeEnd - timeStart;
                    double kB_Goodput = (double) size / timeDiff / 1000;
                    System.out.println("The goodput was: " + kB_Goodput + "kB");
                    try {
                        PrintWriter writer = new PrintWriter(new FileWriter("TCP_5sekunden.txt", true));
                        writer.append(kB_Goodput + "\n");
                        writer.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            //long dataSize = data.toString().getBytes().length;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
