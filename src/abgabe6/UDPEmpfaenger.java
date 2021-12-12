package abgabe6;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class UDPEmpfaenger {
    private static final int BUFFER_SIZE = 1400;
    private static final int TIMEOUT = 1000;

    private int receiveSingle() {
        DatagramPacket p = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        int len = 0;
        try (DatagramSocket socket = new DatagramSocket(4711)){
            socket.setSoTimeout(TIMEOUT);
            socket.receive(p);
            len = p.getData().length;
        } catch (SocketTimeoutException te) {
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return len;
    }

    public void listen() {
        while (true){
            try {
                long timeBegin = System.currentTimeMillis();
                int len = 0;
                int tmp = 0;
                while (tmp != -1) {
                    tmp = this.receiveSingle();
                    len += tmp;
                }

                long receiveTime = System.currentTimeMillis() - timeBegin - TIMEOUT;
                double kB_Goodput = (double) len / receiveTime / 125;
                if (len != -1) {
                    System.out.println("Uebertragungsdauer: " + receiveTime + " Millisekunden.");
                    System.out.println(len + " Bytes empfangen");
                    System.out.println("Durchsatz: " + kB_Goodput + "kB pro Sekunde");

                    try {
                        PrintWriter writer = new PrintWriter(new FileWriter("UDP_100_100.txt", true));
                        writer.append(kB_Goodput + "\n");
                        writer.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        new UDPEmpfaenger().listen();
    }
}