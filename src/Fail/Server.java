package Fail;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class Server {

    public static final int RECEIVER_PORT_UDP = 4711; // < port where we listen for packets
    public static final int RECEIVER_PORT_TCP = 4712;
    private static final int BUFFER_SIZE = 1400; // < maximum size of data to be received
    private static final int TIMEOUT = 1000; // < timeout for receiving int and double value
    private static final String UDPMESSUNG = "UDP_1000_0.txt";
    private static final String TCPMESSUNG = "TCP_1000_0.txt";

    /**
     * UDP Klasse.
     */
    public static class UDP extends Thread implements AutoCloseable {

        private final DatagramSocket socket;

        UDP() throws SocketException {
            socket = new DatagramSocket(RECEIVER_PORT_UDP);
        }

        @Override
        public void close() {
            socket.close();
        }

        /**
         * Receive a single UDP packet containing a string and print it.
         */
        public double receiveString(int timeToWait) {
            int len = 0;
            DatagramPacket p = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            try {
                socket.setSoTimeout(timeToWait);
                socket.receive(p);
                String string = new String(p.getData());
                len = string.getBytes(StandardCharsets.UTF_8).length;
            } catch (SocketTimeoutException te) {
                return -1;
            } catch (IOException e) {
                System.err.println("Error occured while receiving a packet.");
                e.printStackTrace();
            }
            return len/1000.;
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    long timeBegin = System.currentTimeMillis();
                    double lenTotal = 0;
                    double tmp = 0;
                    while (tmp != -1){
                        tmp = this.receiveString(TIMEOUT);
                        lenTotal += tmp;
                    }
                    long timeEnd = System.currentTimeMillis();
                    long timeDiff = timeEnd - timeBegin - TIMEOUT;
                    if (lenTotal != -1) {
                        System.out.println("UDP Connection.");
                        System.out.println(timeDiff + " Millisekunden sind vergangen.");
                        System.out.println(lenTotal + " kB empfangen.");
                        double str = lenTotal/timeDiff;
                        System.out.println(str);
                        PrintWriter writer = new PrintWriter(new FileWriter(UDPMESSUNG, true));
                        writer.append(str + "\n");
                        writer.close();
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
                         BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                         DataInputStream dis = new DataInputStream(s.getInputStream());) {
                        System.out.println("Got Client connection.");
                        long timeBegin = System.currentTimeMillis();
                        StringBuilder content = new StringBuilder();

//                        for (String line = br.readLine(); line != null
//                                && line.length() > 0; line = br.readLine()) {
//                            String receivedString = line;
//                            System.out.println(receivedString);
//                            content.append(receivedString);
//                        }
                        System.out.println("DataInpuStream: " + dis.readAllBytes());
                        String test = dis.readUTF();
                        System.out.println(test);
                        int size = test.getBytes(StandardCharsets.UTF_8).length;
                        //int size = dis.readAllBytes().length;
                        System.out.println("Size: " + size);
                        //int size = content.toString().getBytes(StandardCharsets.UTF_8).length;

                        long timeEnd = System.currentTimeMillis();
                        long timeDiff = timeEnd - timeBegin;
                        double str = ((double)size) / timeDiff / 1000.;
                        System.out.println("TCP Connection.");
                        PrintWriter writer = new PrintWriter(new FileWriter(TCPMESSUNG, true));
                        writer.append(str + "\n");
                        writer.close();
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
            System.out.println("Fehler in main.");
            e.printStackTrace();
        }
    }
}
