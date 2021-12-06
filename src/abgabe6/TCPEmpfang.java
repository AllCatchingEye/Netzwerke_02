package abgabe6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPEmpfang {

    public static void main(String[] args) {
        new TCPEmpfang().startTCPEmpfang();
    }

    private void startTCPEmpfang() {
        try (ServerSocket serverSocket = new ServerSocket(8010)) {
            System.out.println("Waiting for clients...");
            while (true) {
                acceptConnection(serverSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnection(ServerSocket serverSocket){
        try (Socket s = serverSocket.accept()) {
            int TIMEOUT = 3000;
            s.setSoTimeout(TIMEOUT);

            int size = 0;
            long timeEnd;
            final long timeStart = System.currentTimeMillis();

            while (true){
                try (BufferedReader br = new BufferedReader((new InputStreamReader(s.getInputStream())))) {
                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        size += line.getBytes().length;
                    }
                } catch (SocketTimeoutException se) {
                    timeEnd = System.currentTimeMillis() - TIMEOUT;
                    break;
                }
            }

            final long seconds = (timeEnd - timeStart) / 1000;
            final double goodPut = (double) size / seconds;

            System.out.println("The goodput was: " + goodPut + "kB");

            writeResults(goodPut);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void getMessage(Socket socket){

    }

    private void writeResults(double goodPut){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("TCP_5sekunden.txt", true));
            writer.append(String.valueOf(goodPut)).append("\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
