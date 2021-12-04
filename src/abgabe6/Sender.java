package abgabe6;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Sender {

    public static void main(String[] args) {
        new Sender().sendTCPPacket();
    }

    private void sendTCPPacket() {
        try (Socket socket = new Socket("localhost", 4712)) {
            try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                 BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                //byte[] bytes = "Hallo".getBytes(StandardCharsets.UTF_8);
                String bytes = "Hallo";
                bw.write(bytes);
                bw.flush();

                System.out.println(br.readLine());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
