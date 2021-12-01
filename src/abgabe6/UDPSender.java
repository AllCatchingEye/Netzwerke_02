package abgabe6;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class UDPSender implements Closeable {

    public static final int DESTPORT = UDPServer.RECEIVER_PORT;
    public static final String DESTHOST = "localhost";
    public static final int SENDTIME = 3000;
    public static final int N = 1000;
    public static final int K = 1000;
    public static final String TCP = "TCP";
    public static final String UDP = "UDP";
    private DatagramSocket socket;

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
            DatagramPacket p = new DatagramPacket(dataToSend, 1400, InetAddress.getByName(destHost),
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

            Path path = Paths.get("/Users/nicolaslerch/IdeaProjects/abgabe2-grp2-02/src/abgabe6/loremIpsum.txt");
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
                        System.out.println("Wait " + k + " seconds.");
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
            System.out.println((double) count * 1400. / timeDiff / 10000 + " kB pro Millisekunde.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendTextTCP(final String destHost, final int destPort, int N, int k) {
        try(
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos)) {
            Path path = Paths.get("/Users/nicolaslerch/IdeaProjects/abgabe2-grp2-02/src/abgabe6/loremIpsum.txt");
            String content = Files.readString(path, StandardCharsets.US_ASCII);

            dos.writeBytes(content);
            dos.flush();

            long time = System.currentTimeMillis();
            int count = 0;
            int num = 0;
            while (System.currentTimeMillis() - time < SENDTIME) {
                if (num < N) {
                    try (Socket s = new Socket(DESTHOST, DESTPORT)){
                        try {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                            bw.write(content);
                            bw.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        System.out.println("There is a Problem with the Server. Make sure it is online.");
                    }
                    count++;
                    num++;
                } else {
                    try {
                        System.out.println("Wait " + k + " seconds.");
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
            System.out.println((double) count * 1400. / timeDiff / 10000 + " kB pro Millisekunde.");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        String Protocol = UDP;
        try (UDPSender sender = new UDPSender()) {
            if (Protocol.equals(UDP)) {
                sender.sendTextUDP(DESTHOST, DESTPORT, N, K);
            } else {
                sender.sendTextTCP(DESTHOST, DESTPORT, N, K);
            }
        } catch (IOException e1) {
            System.err.println("IOException in Sender!");
            e1.printStackTrace();
        }
    }
}
