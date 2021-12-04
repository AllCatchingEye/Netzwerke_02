package abgabe6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Server {

    public static final int RECEIVER_PORT = 4711; // < port where we listen for packets
    private static final int BUFFER_SIZE = 1400; // < maximum size of data to be received
    private static final int TIMEOUT = 1000; // < timeout for receiving int and double value

    /**
     * UDP Klasse.
     */
    public static class UDP extends Thread implements AutoCloseable {

        private final DatagramSocket socket;

        UDP() throws SocketException {
            socket = new DatagramSocket(RECEIVER_PORT);
        }

        @Override
        public void close() {
            socket.close();
        }

        /**
         * Receive a single UDP packet containing a string and print it.
         */
        public boolean receiveString(int timeToWait) {
            DatagramPacket p = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            try {
                socket.setSoTimeout(timeToWait);
                socket.receive(p);

                //System.out.println("Received a packet from client " + p.getAddress().getHostAddress() + " port " + p.getPort());
                //System.out.println("String: " + new String(p.getData()));
            } catch (SocketTimeoutException te) {
                //System.out.println("Nothing received - timeout occured after " + timeToWait + " ms.");
                return false;
            } catch (IOException e) {
                System.err.println("Error occured while receiving a packet.");
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    long timeBegin = System.currentTimeMillis();
                    int len = 0;
                    int tmp = 0;
                    while (tmp != -1){
                        tmp = this.receiveString(TIMEOUT);
                        len += tmp;
                        //System.out.println("Length of this package: " + tmp);
                    }
                    long timeEnd = System.currentTimeMillis();
                    long timeDiff = timeEnd - timeBegin - TIMEOUT;
                    if (count != -1) {
                        System.out.println("UDP Connection.");
                        System.out.println(timeDiff + " Millisekunden sind vergangen.");
                        System.out.println(len + " Bytes empfangen.");
                        System.out.println((double) len/timeDiff/1000+ " kB pro Sekunde.");
                    }
                } catch (Exception e) {
                    System.err.println("IOException in Receiver!");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * TCP Klasse.
     */
    public static class TCP extends Thread implements AutoCloseable {

        final String HOST = "localhost";

        @Override
        public void close() {

        }

        @Override
        public void run() {
            while (!interrupted()){
                try (ServerSocket servSock = new ServerSocket(RECEIVER_PORT_TCP)) {
                    try (Socket s = servSock.accept();
                         BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
                        System.out.println("Got Client connection.");
                        long timeBegin = System.currentTimeMillis();
                        StringBuilder content = new StringBuilder();
                        for (String line = br.readLine(); line != null
                                && line.length() > 0; line = br.readLine()) {
                            content.append(line);
                        }
                        int size = content.toString().getBytes(StandardCharsets.UTF_8).length;
                        System.out.println("The size of the package was: " + size);
                        long timeEnd = System.currentTimeMillis();
                        long timeDiff = timeEnd - timeBegin;
                        System.out.println("TCP Connection.");
                        System.out.println(timeDiff + " Millisekunden sind vergangen.");
                        System.out.println((double) size / timeDiff / 1024 + " kB pro Sekunde.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        try (UDP udpServer = new UDP();
             TCP tcpServer = new TCP()) {
            tcpServer.start();
            udpServer.start();
            tcpServer.join();
            udpServer.join();
        } catch (Exception e) {
            System.out.println("Fehler.");
        }
    }
}
