package abgabe6;

import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class UDPEmpfaenger {
    private static final int PORT = 4711;
    private static final int BUFFER_SIZE = 1400;
    private static final int TIMEOUT = 2000;
    private final DatagramSocket socket;

    UDPEmpfaenger() throws SocketException {
        socket = new DatagramSocket(PORT);
    }

    public int receiveSingle() {
        DatagramPacket p = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        int len = 0;
        try {
            socket.setSoTimeout(TIMEOUT);
            socket.receive(p);
            String string = new String(p.getData());
            len = string.getBytes(StandardCharsets.UTF_8).length;
        } catch (SocketTimeoutException te) {
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return len;
    }

    public void listen() {

        try {
            long timeBegin = System.currentTimeMillis();
            int len = 0;
            int tmp = 0;
            while (tmp != -1) {
                tmp = this.receiveSingle();
                len += tmp;
            }
            long timeEnd = System.currentTimeMillis();
            long receiveTime = timeEnd - timeBegin - TIMEOUT;
            double durchsatz = len / receiveTime / 1000;
            if (len != -1) {
                System.out.println("Uebertragungsdauer: " + receiveTime + " Millisekunden.");
                System.out.println(len + " Bytes empfangen");
                System.out.println("Durchsatz: " + durchsatz + "kB pro Sekunde");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        try {
            UDPEmpfaenger udpEmpfaenger = new UDPEmpfaenger();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}