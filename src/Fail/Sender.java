package Fail;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class Sender implements Closeable {

    public static final int DESTPORT = Server.RECEIVER_PORT_UDP;
    public static final String DESTHOST = "localhost";
    public static final int SENDTIME = 2000;
    public static final int N = 1000; // Nach 1000 packeten gibt es eine Verzoegerung
    public static final int K = 0; // Verzoegerung in Millisekunden
    public static final String TCP = "TCP";
    public static final String UDP = "UDP";
    public static final String FILEPATH = "/Users/nicolaslerch/IdeaProjects/abgabe2-grp2-02/src/abgabe6/loremIpsum.txt";
    private final DatagramSocket socket;

    Sender() throws SocketException {
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
    public void sendTextUDP(final String destHost, final int destPort, int N, int k, String filename) {

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
                        TimeUnit.MILLISECONDS.sleep(k);
                        num = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            long timeDiff = System.currentTimeMillis() - time;
            long size = content.getBytes().length * count;
            System.out.println(System.currentTimeMillis() - time + " Millisekunden sind vergangen.");
            String str = (double) size / timeDiff / 1000 + "";
            System.out.println(str);
            PrintWriter writer = new PrintWriter(new FileWriter(filename, true));
            writer.append(str + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendTextTCP(int N, int k, String filename) {
        int size = 0;
        int num = 0;
        int count = 0;
        long time = System.currentTimeMillis();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();){
             //DataOutputStream dos = new DataOutputStream(bos)) {
            Path path = Paths.get(FILEPATH);
            String content = Files.readString(path, StandardCharsets.US_ASCII);
            size = content.getBytes(StandardCharsets.UTF_8).length;
            try (Socket s = new Socket(DESTHOST, 4712)) {
                while (System.currentTimeMillis() - time < SENDTIME) {
                    if (num < N) {
                        try (DataOutputStream dos = new DataOutputStream(bos)){
//                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//                            bw.write(content);
//                            bw.flush();
                            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
                            dos.write(bytes);
                            dos.flush();
                            count++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        num++;
                    } else {
                        try {
                            TimeUnit.MILLISECONDS.sleep(k);
                            num = 0;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("There is a Problem with the Server. Make sure it is online.");
                e.printStackTrace();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        long timeDiff = System.currentTimeMillis() - time;
        System.out.println("Anzahl Pakete: " + count);
        System.out.println("Groesse der Pakete: " + size);
        size = count * size;
        System.out.println(System.currentTimeMillis() - time + " Millisekunden sind vergangen.");
        String str = (double) size / timeDiff / 1000 + "kB";
        System.out.println(str);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(filename, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.append(str + "\n");
        writer.close();
    }


    @Override
    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        String Protocol = TCP;
        for (int j = 0; j < 1; j++) {
            String file = Protocol + "_Sender_" + N + "_" + K + ".txt";
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(file, true));
                writer.append("N: " + N + " , k: " + K + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 5; i++) {
                try (Sender sender = new Sender()) {
                    if (Protocol.equals(UDP)) {
                        sender.sendTextUDP(DESTHOST, DESTPORT, N, K, file);
                    } else {
                        sender.sendTextTCP(N, K, file);
                    }
                } catch (IOException e1) {
                    System.err.println("IOException in Sender!");
                    e1.printStackTrace();
                }
            }
            Protocol = UDP;
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
