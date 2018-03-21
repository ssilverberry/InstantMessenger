package sample.network;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.controllers.Controller;
import sample.network.json.IncomingServerMessage;
import sample.network.json.ProtocolClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkController {

    private static final NetworkController INSTANCE = new NetworkController();
    private final Logger logger = LogManager.getLogger(NetworkController.class);
    private ConnectionThread connectionThread = new ConnectionThread();
    private ProtocolClient protocol;

    public static NetworkController getInstance() {
        return INSTANCE;
    }

    private NetworkController() {
    }

    public void openConnection() {
        protocol = new ProtocolClient();
        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    public void send(Object request) {
        connectionThread.outputStream.println(protocol.transform(request));
    }

    public void closeConnection() throws IOException {
        connectionThread.socket.close();
    }

    private class ConnectionThread extends Thread {
        private Socket socket;
        private BufferedReader inputStream;
        private PrintWriter outputStream;
        private final int SocketPort = 3000;

        @Override
        public void run() {
            try(Socket socket = new Socket(InetAddress.getLocalHost(), SocketPort);
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);) {
                this.socket = socket;
                this.inputStream = inputStream;
                this.outputStream = outputStream;
                socket.setTcpNoDelay(true);
                logger.info("Connection success!");
                while (!Thread.currentThread().isInterrupted())
                try {
                    String response = this.inputStream.readLine();
                    if (!response.equals("")) {
                        logger.info("GET SERVER RESPONSE: " + response);
                        IncomingServerMessage inData = protocol.transformOut(response);
                        Controller.onReceiveCallback.accept(inData);
                    }
                } catch (IOException e) {
                    logger.error("Failed to process connection: {}", e);
                    Thread.currentThread().interrupt();
                }
            } catch (IOException e) {
                logger.error("Connection failed!");
            }
        }
    }
}
