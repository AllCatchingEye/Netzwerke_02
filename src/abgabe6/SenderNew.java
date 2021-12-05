package abgabe6;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class SenderNew {
    public static final String FILEPATH = "C:\\Users\\Kicos\\IdeaProjects\\abgabe2-grp2-02\\src\\Fail\\loremIpsum.txt";
    public static final String FILEPATH_TCP = "C:\\Users\\Kicos\\IdeaProjects\\abgabe2-grp2-02\\src\\Fail\\loremIpsumTCP.txt";
    private static final int k = 100;
    private static final int N = 100;

    public static void main(String[] args) {
        new SenderNew().sendTCPPacket();

/*        int num = 0;
        while (num < 5){
            new SenderNew().sendUDPPacket();
            try{
                TimeUnit.MILLISECONDS.sleep(1200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            num++;
            System.out.println("Paketnr: " + num);
        }*/
    }

    private void sendTCPPacket() {
        int messageSize = 0;
        long timeStart = System.currentTimeMillis();

        int messagesToSent = 5;
        while(messagesToSent > 0){
            try (Socket socket = new Socket("localhost", 4712)) {
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                    Path path = Paths.get(FILEPATH_TCP);
                    String content = Files.readString(path, StandardCharsets.US_ASCII);
                    messageSize = content.getBytes(StandardCharsets.UTF_8).length;
                    bw.write(content);
                    bw.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            long timeEnd = System.currentTimeMillis();
            long timeDiff = timeEnd - timeStart;
            System.out.println("Size of message sent: " + messageSize);
            double kB_Goodput = (double) messageSize / timeDiff / 1000;
            System.out.println("The goodput was " + kB_Goodput +"kB");

            try {
                PrintWriter writer = new PrintWriter(new FileWriter("TCP_sender_5s.txt", true));
                writer.append(kB_Goodput + "\n");
                writer.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            messagesToSent--;
        }
    }

    public byte[] initData() throws IOException {
        Path path = Paths.get(FILEPATH);
        String content = Files.readString(path, StandardCharsets.US_ASCII);
        return content.getBytes();
    }

    private void sendUDPPacket() {
        long timeStart = System.currentTimeMillis();
        int timeLimit = 5000;
        int timesSent = 0;

        int num = 0;
        while (System.currentTimeMillis() - timeStart < timeLimit){
            if(num < N || k == 0){
                timesSent = sentMessage(timesSent);
                num++;
            }else{
                try {
                    num = 0;
                    TimeUnit.MILLISECONDS.sleep(k);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        long timeDiff = System.currentTimeMillis() - timeStart;
        int size = timesSent * 1400;
        double goodput = (double) size / timeDiff / 1000;
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("UDP_Sender_100_100.txt", true));
            writer.append(goodput + "\n");
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Size of message was: " + size + " bytes");
        System.out.println("Übertragungsdauer war: " + timeDiff + " Millisekunden");
        System.out.println("Durchsatz: " + goodput + "kB");
    }

    private int sentMessage(int timesSent) {
        try{
            byte[] data = initData();

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), 4711);
            socket.send(p);
            socket.close();
            timesSent++;
        } catch (IOException e){
            e.printStackTrace();
        }
        return timesSent;
    }
}
