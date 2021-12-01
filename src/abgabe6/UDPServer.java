package abgabe6;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class UDPServer extends Thread implements Closeable {

    public static final int RECEIVER_PORT = 4711; // < port where we listen for packets
    private static final int BUFFER_SIZE = 1400; // < maximum size of data to be received
    private static final int TIMEOUT = 1000; // < timeout for receiving int and double value

    private DatagramSocket socket;

    UDPServer() throws SocketException {
        socket = new DatagramSocket(RECEIVER_PORT);
    }

    /**
     * Receive a single UDP packet containing a string and print it.
     */
    public boolean receiveString(int timeToWait) {
        DatagramPacket p = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        try {
            socket.setSoTimeout(timeToWait);
            socket.receive(p);

            System.out.println("Received a packet from client " + p.getAddress().getHostAddress() + " port " + p.getPort());
            System.out.println("String: " + new String(p.getData()));
        } catch (SocketTimeoutException te) {
            System.out.println("Nothing received - timeout occured after " + timeToWait + " ms.");
            return false;
        } catch (IOException e) {
            System.err.println("Error occured while receiving a packet.");
            e.printStackTrace();
        }
        return true;
    }



    @Override
    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        while (true) {
            try (UDPServer receiver = new UDPServer()) {
                long timeBegin = System.currentTimeMillis();
                int count = -1;
                while (receiver.receiveString(TIMEOUT)) {
                    count++;
                }
                long timeEnd = System.currentTimeMillis();
                long timeDiff = timeEnd - timeBegin - TIMEOUT;
                if (count != -1) {
                    System.out.println(timeDiff + " Millisekunden sind vergangen.");
                    System.out.println(count + " Packete empfangen.");
                    System.out.println((double)count * 1400. / timeDiff / 10000 + " kB pro Millisekunde.");
                }
            } catch (IOException e) {
                System.err.println("IOException in Receiver!");
                e.printStackTrace();
            }
        }
    }
}
