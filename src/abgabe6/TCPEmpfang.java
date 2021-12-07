package abgabe6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

public class TCPEmpfang {

    public static void main(String[] args) {
        new TCPEmpfang().startTCPEmpfang();
    }

    private void startTCPEmpfang() {
        try (ServerSocket serverSocket = new ServerSocket(4712)) {
            System.out.println("Waiting for clients...");
            while (true) {
                try(Socket s = serverSocket.accept()){
                    acceptConnection(s);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnection(Socket s){
        try{
            System.out.println("NEU");
            int size = 0;
            long timeEnd;
            final long timeStart = System.currentTimeMillis();

            try (BufferedInputStream input = new BufferedInputStream(s.getInputStream())) {
                while (input.read() != -1){
                    size += input.read();
                    TimeUnit.MILLISECONDS.sleep(1);
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            timeEnd = System.currentTimeMillis();
            final long timeDiff = timeEnd - timeStart;
            final double goodPut = (double) size / timeDiff / 125;

            System.out.println("The goodput was: " + goodPut + " kbit/s");

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
