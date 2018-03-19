
package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.sound.sampled.Port;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ProtocolClient {

    Socket s1 = null;
    String line = null;
    BufferedReader br = null;
    BufferedReader is = null;
    PrintWriter os = null;
    IncomingServerMesage mesage;

    public static Gson gson = new Gson();

    public static String transform (Object OutputClientMessage){
        Gson out = new GsonBuilder().create();
        String json = out.toJson(OutputClientMessage);
        return json;
    }

    public void start() throws IOException {
        InetAddress address = InetAddress.getByName("10.112.32.9");
        int PORT = 3000;

        try {
            s1 = new Socket(address, PORT); // You can use static final constant PORT_NUM
            br = new BufferedReader(new InputStreamReader(System.in));
            is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os = new PrintWriter(s1.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }
        System.out.println("Client Address : " + address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");
    }




    public void inputOutput(String test) throws IOException {
        String response = null;
        boolean a = false;
        try {
            line = test;
            while (line.compareTo("QUIT") != 0) {
                os.println(line);
                response = is.readLine();
                mesage = gson.fromJson(line, IncomingServerMesage.class);
                    os.println(line);

                System.out.println(mesage.getChatId() + " plus " + mesage.getUserId() + " plus " + mesage.getAccept());
                System.out.println("Server Response : " + response);
                if(mesage.getMessageAll() != null){
                    a = true;
                }
                line = br.readLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        } finally {

            is.close();
            os.close();
            br.close();
            s1.close();
            System.out.println("Connection Closed");

        }
    }
}

