package clientSide;

import java.net.InetAddress;

/**
 * Created by o.spichak on 20.03.2018.
 */
public class ViewClient {

     void closed(){
        System.out.println("Connection Closed");
    }

     void address(InetAddress address){
        System.out.println("Client Address : "  + address);
    }

     void serverResp(String response){
        System.out.println("Server Response : " + response);
    }

}
