package abgabe6;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SenderNew {
    public static final String FILEPATH = "./loremIpsum.txt";
    public static final String FILEPATH_TCP = "./loremIpsumTCP.txt";
    private static final int k = 0;
    private static final int N = 100;
    private static final String TARGETHOST = "localhost";

    public static void main(String[] args) {
        String protocol = "TCP";
        int num = 0;
        while (num < 10){
            SenderNew senderNew = new SenderNew();
            senderNew.chooseProtocol(protocol);
            senderNew.delay(1500);
            num++;
            System.out.println("Messagenr: : " + num);
        }
    }

    private void chooseProtocol(String protocol){
        SenderNew senderNew = new SenderNew();
        if(Objects.equals(protocol, "TCP"))
            senderNew.sendTCP();
        else if(Objects.equals(protocol, "UDP"))
            senderNew.sendUDPPacket();
        else
            System.out.println("Unknown Protocol.");
    }

    private void sendTCP() {

        int messagesToSend = 1000;
        String message = "Hallo";
        int data = message.getBytes().length *messagesToSend;
        long timeStart = 0;
        long timeEnd = 0;

        try (Socket socket = new Socket(TARGETHOST, 4712)) {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                timeStart = System.currentTimeMillis();
                while (messagesToSend > 0){
                    if(messagesToSend % N == 0)
                        delay(k);

                    bw.write(message);
                    bw.flush();
                    messagesToSend--;
                }
                timeEnd = System.currentTimeMillis();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long sendingTime = timeEnd - timeStart;

        double goodPut = (double) data / sendingTime / 125;

        System.out.println("Size of message sent: " + data);
        System.out.println("The goodput was: " + goodPut +"kB/s");

        writeResults(goodPut, "TCP_Sender_Messung.txt");
    }


    private void delay(int delay){
        try{
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        double goodPut = (double) size / timeDiff / 1000;
        writeResults(goodPut, "UDP_Results");

        System.out.println("Size of message was: " + size + " bytes");
        System.out.println("Übertragungsdauer war: " + timeDiff + " Millisekunden");
        System.out.println("Durchsatz: " + goodPut + "kB");
    }

    private int sentMessage(int timesSent) {
        try{
            byte[] data = initData();

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getByName(TARGETHOST), 4711);
            socket.send(p);
            socket.close();
            timesSent++;
        } catch (IOException e){
            e.printStackTrace();
        }
        return timesSent;
    }

    private void writeResults(double goodPut, String filename){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename, true));
            writer.append(String.valueOf(goodPut)).append(",\n");
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

