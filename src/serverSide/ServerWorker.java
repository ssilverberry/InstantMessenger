package serverSide;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
/*import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;*/

public class ServerWorker extends Thread {

    private final Server server;
    private final Socket socket;
    private String login = null;

    private HashSet<String> topicSet = new HashSet<>();
    private OutputStream outputStream;
    private PrintWriter output;
    public static InputChatMessage mesage;
    public static Gson gson = new Gson();
  //  private static final Logger LOGGER = LogManager.getLogger();

    ServerWorker(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException | InterruptedException e) {
         //   LOGGER.error("Connection was closed unexpectedly !");
        }
    }


    public static String transformOut (Object OutMessage){
        Gson out = new GsonBuilder().create();
        String json = out.toJson(OutMessage);
        return json;
    }
    private void handleClientSocket () throws IOException, InterruptedException {
        InputStream inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        output = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<ServerWorker> workersList = server.getWorkerList();

        while ( (line = reader.readLine()) != null) {
            //LOGGER.info(line);
            mesage = gson.fromJson(line, InputChatMessage.class);
            System.out.println("Login" + "  " + mesage.getLogin() + "  nfooo   " + mesage.getChatMessage());

            HandleLogin.start(mesage);
            String incom = HandleLogin.getResponse();
            String[] tokens = StringUtils.split(incom);

            switch (tokens[0]) { //getResponce();
                //ответ на доступ
                case "access":
                    OutMessage access = new OutMessage("ok", Integer.parseInt(tokens[1])); //StrungUtils.Split int 1
                    line = transformOut(access);
                    output.println(line);
                    break;

                case "noRegUser":
                    OutMessage noRegUser = new OutMessage("noRegUser");
                    line = transformOut(noRegUser);
                    output.println(line);
                    break;

                // ответ на регистрацию
                case "regUser":
                    OutMessage regUser = new OutMessage("RegUser");
                    line = transformOut(regUser);
                    output.println(line);
                    break;
                case "messageAll":
                    OutMessage messageAll = new OutMessage(mesage.getIdUsers(), mesage.getChatMessage());
                    List<ServerWorker> mylist = server.getWorkerList();
                    for(ServerWorker i : mylist){
                        System.out.println(i);
                        line = transformOut(messageAll);
                        output.println(line);
                        i.send(line);
                    }
                 /*  HashSet<User> users = HandleLogin.getUserList();
                   for (User usr : users){
                       line = transformOut(messageAll);
                       output.println(line);
                   }*/
                   // System.out.println(mesage.getChatMessage() + " 00000000 " +  mesage.getIdUsers());
                    break;


            }

            /*if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(cmd) || "logoff".equalsIgnoreCase(cmd)) {
                    handleLogOff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                } else if ("join".equalsIgnoreCase(cmd)) {
                    handleJoin(tokens);
                } else if ("leave".equalsIgnoreCase(cmd)) {
                    handleLeave(tokens);
                } else {
                    String msg = "Unknown user: " + cmd + "\n";
                    for (ServerWorker worker : workersList) {
                        worker.send(msg);
                    }
                }

            }*/
        }
        socket.close();
    }

    private void handleLeave(String[] tokens) {
        if (tokens.length > 1) {
            String topic = tokens[1];
            topicSet.remove(topic);
        }
    }

    private void handleJoin(String[] tokens) {
        if (tokens.length > 1) {
            String topic = tokens[1];
            topicSet.add(topic);
        }
    }

    public boolean isMemberOfTopic (String topic) {
        return topicSet.contains(topic);
    }

    private void handleMessage(String[] tokens) {
        String sendTo = tokens[1];
        String body = tokens[2];

        boolean isTopic = sendTo.charAt(0) == '#';

        List<ServerWorker> workerList = server.getWorkerList();
        for (ServerWorker worker : workerList) {
            if (isTopic) {
                if (worker.isMemberOfTopic(sendTo)) {
                    String outMsg = "msg from " + sendTo + ": @" + login + " > " + body + "\n";
                    worker.send(outMsg);
                }
            } else {
                if (sendTo.equalsIgnoreCase(worker.getLogin())) {
                    String outMsg = "msg from: @" + login + " > " + body + "\n";
                    worker.send(outMsg);
                }
            }
        }
    }

    private void handleLogOff() {
        server.removeWorker(this);
        List<ServerWorker> workersList = server.getWorkerList();
        String onlineMsg = "offline " + login + "\n";
        for (ServerWorker worker : workersList) {
            if (!login.equals(worker.getLogin())) {
                worker.send(onlineMsg);
            }

        }
        try {
            socket.close();
        } catch (IOException e) {
           // LOGGER.error("CLinet socket was closed");
        }
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

            if ((login.equalsIgnoreCase("guest") && password.equals("guest")) ||
                    (login.equalsIgnoreCase("pasha") && password.equals("pasha"))) {
                String msg = "ok login\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                //LOGGER.info("User logged in successfully: " + login);

                List<ServerWorker> workersList = server.getWorkerList();
                for (ServerWorker worker: workersList) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin())) {
                            String msg2 = "online " + worker.getLogin() + "\n";
                            send(msg2);
                        }
                    }
                }

                String onlineMsg = "online " + login + "\n";
                for (ServerWorker worker : workersList) {
                    if (!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }
            } else {
                String msg = "error login\n";
                outputStream.write(msg.getBytes());
                //LOGGER.warn("Login is failed !" + login);
            }
        }
    }

    private void send(String message) {
        //if (HandleLogin.isMemberOfChat(HandleLogin.getUser()))
            output.println(message);
        //LOGGER.warn("Oops, something wrong in " + this.getClass() + " \"send()\" method");
    }
}
