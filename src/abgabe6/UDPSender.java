package abgabe6;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class UDPSender implements Closeable {

    public static final int DESTPORT = Server.RECEIVER_PORT_UDP;
    public static final String DESTHOST = "localhost";
    public static final int SENDTIME = 3000;
    public static final int N = 10; // Nach 1000 packeten gibt es eine Verzoegerung
    public static final int K = 1000; // Verzoegerung in Millisekunden
    public static final String TCP = "TCP";
    public static final String UDP = "UDP";
    public static final String FILEPATH = "C:\\Users\\Kicos\\IdeaProjects\\abgabe2-grp2-02\\src\\abgabe6\\loremIpsum.txt";
    private final DatagramSocket socket;

    UDPSender() throws SocketException {
        socket = new DatagramSocket();
    }

    /**
     * Sends a UDP-Packet with some data to the specified destination.
     *
     * @param destHost   Destination host (hostname or IP address)
     * @param destPort   Destination port on the destination host
     * @param dataToSend Array of bytes containing the data to be sent
     */
    public void sendSingleUdpPacket(final String destHost, final int destPort, final byte[] dataToSend) {
        try {
            DatagramPacket p = new DatagramPacket(dataToSend, 800, InetAddress.getByName(destHost),
                    destPort);
            socket.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Example how to send multi-byte primitive data types.
     * <p>
     * In order to send integer or double values in a UDP packet, these need to be
     * converted and stored in a byte array. One method for doing this is
     * illustrated here.
     *
     * @param destHost Destination host (hostname or IP address)
     * @param destPort Destination port on the destination host
     */
    public void sendTextUDP(final String destHost, final int destPort, int N, int k) {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {

            Path path = Paths.get(FILEPATH);
            String content = Files.readString(path, StandardCharsets.US_ASCII);

            dos.writeBytes(content);
            dos.flush();

            long time = System.currentTimeMillis();
            int count = 0;
            int num = 0;
            while (System.currentTimeMillis() - time < SENDTIME) {
                if (num < N) {
                    sendSingleUdpPacket(destHost, destPort, bos.toByteArray());
                    num++;
                    count++;
                } else {
                    try {
                        System.out.println("Wait " + N + " seconds.");
                        TimeUnit.MILLISECONDS.sleep(k);
                        num = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            long timeDiff = System.currentTimeMillis() - time;
            System.out.println(System.currentTimeMillis() - time + " Millisekunden sind vergangen.");
            System.out.println(count + " Packete gesendet."); //TODO: ist der packetverlust logisch -> 0.03 Prozent ?
            System.out.println((double) count * 1400. / timeDiff / 10000 + " kB pro Sekunde.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendTextTCP(int N, int k) {
        int count = 0;
        int num = 0;
        long time = System.currentTimeMillis();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bos)) {
            Path path = Paths.get(FILEPATH);
            String content = Files.readString(path, StandardCharsets.US_ASCII);

            if (num < N) {
                try (Socket s = new Socket(DESTHOST, 4712)) {
                    while (System.currentTimeMillis() - time < SENDTIME) {
                        try {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                            bw.write(content);
                            bw.flush();
                            System.out.println("TCP gesendet");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        count++;
                        num++;
                    }
                } catch (IOException e) {
                    System.out.println("There is a Problem with the Server. Make sure it is online.");
                    e.printStackTrace();
                }
            } else {
                try {
                    System.out.println("Wait " + k + " milliseconds.");
                    TimeUnit.MILLISECONDS.sleep(k);
                    num = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        long timeDiff = System.currentTimeMillis() - time;
        System.out.println(System.currentTimeMillis() - time + " Millisekunden sind vergangen.");
        System.out.println(count + " Packete gesendet."); //TODO: ist der packetverlust logisch -> 0.03 Prozent ?
        System.out.println((double) count * 1400. / timeDiff / 10000 + " kB pro Sekunde.");
    }


    @Override
    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        String Protocol = UDP;
        for (int i = 0; i < 1; i++) {
            try (UDPSender sender = new UDPSender()) {
                if (Protocol.equals(UDP)) {
                    sender.sendTextUDP(DESTHOST, DESTPORT, N, K);
                } else {
                    sender.sendTextTCP(N, K);
                }
            } catch (IOException e1) {
                System.err.println("IOException in Sender!");
                e1.printStackTrace();
            }
            Protocol = TCP;
        }
    }
}
