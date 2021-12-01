package abgabe6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

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
                    int count = -1;
                    while (this.receiveString(TIMEOUT)) {
                        count++;
                    }
                    long timeEnd = System.currentTimeMillis();
                    long timeDiff = timeEnd - timeBegin - TIMEOUT;
                    if (count != -1) {
                        System.out.println("UDP Connection.");
                        System.out.println(timeDiff + " Millisekunden sind vergangen.");
                        System.out.println(count + " Packete empfangen.");
                        System.out.println((double) count * 1400. / timeDiff / 10000 + " kB pro Millisekunde.");
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
        final int PORT = 4711;

        @Override
        public void close() {

        }

        public boolean receiveString() {
            try (Socket socket = new Socket(HOST, PORT)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                for (String line = br.readLine(); br.readLine() != null && line.length() > 0; line = br.readLine()) {
                    //System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    long timeBegin = System.currentTimeMillis();
                    int count = -1;
                    while (this.receiveString()) {
                        count++;
                    }
                    long timeEnd = System.currentTimeMillis();
                    long timeDiff = timeEnd - timeBegin - TIMEOUT;
                    if (count != -1) {
                        System.out.println("TCP Connection.");
                        System.out.println(timeDiff + " Millisekunden sind vergangen.");
                        System.out.println(count + " Packete empfangen.");
                        System.out.println((double) count * 1400. / timeDiff / 10000 + " kB pro Millisekunde.");
                    }
                } catch (Exception e) {
                    System.err.println("IOException in Receiver!");
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        try (UDP udpServer = new UDP();
             TCP tcpServer = new TCP()) {
            udpServer.start();
            //tcpServer.start();
            while (true) {

            }
        } catch (Exception e) {
            System.out.println("Fehler.");
        }
    }
}
