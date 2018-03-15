package sample.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ProtocolClient {
    public static Gson gson = new Gson();
    public static IncomingServerMesage mesage;

    /* constructor for connection with an ip server and port*/
    public ProtocolClient(String ipServ, int port) {

    }
    /*method convert to json*/
    public static String transform (Object OutputClientMessage){
        Gson out = new GsonBuilder().create();
        String json = out.toJson(OutputClientMessage);
        return json;
    }

    public static void start(String test) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        Socket s1=null;
        String line = null;
        BufferedReader br=null;
        BufferedReader is=null;
        PrintWriter os=null;

        try {
            s1=new Socket("10.112.32.9", 3000); // You can use static final constant PORT_NUM
            br= new BufferedReader(new InputStreamReader(System.in));
            is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os= new PrintWriter(s1.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : "+ address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        String response = null;
        try {
            line=test;
            //while(line.compareTo("QUIT")!=0){
            os.println(line);
            os.flush();
            response=is.readLine();
            mesage = gson.fromJson(response, IncomingServerMesage.class);
            //}
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Socket read Error");
        }
        finally{

            is.close();os.close();br.close();s1.close();
            System.out.println("Connection Closed");

        }
    }

}