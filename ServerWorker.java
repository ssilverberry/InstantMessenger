package com.InstantMessengerServer.controller;

import com.InstantMessengerServer.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerWorker extends Thread {

    private final Server server;
    private final Socket socket;
    private String login = null;

    private static StringCrypter crypter = new StringCrypter(new byte[]{1,4,5,6,8,9,7,8});
    private OutputStream outputStream;
    private PrintWriter output;
    public static InputMessage mesage;
    public static Gson gson = new Gson();
    private static ArrayList<String> onlineUsers = new ArrayList<>();
    private HashMap<Thread, ArrayList<String>> privateRoom;
    ArrayList<String> chatRoomlist = new ArrayList<>();
    HashMap<String, String> hashList = new HashMap<>();

  //  private static final Logger LOGGER = LogManager.getLogger();

    public ServerWorker(Server server, Socket socket) {
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
        return crypter.encrypt(out.toJson(OutMessage));
    }

    public static InputMessage transform(String response) {
        return gson.fromJson(crypter.decrypt(response), InputMessage.class);
    }

    private void handleClientSocket () throws IOException, InterruptedException {
        this.outputStream = socket.getOutputStream();
        output = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        String line;
        List<ServerWorker> workersList = server.getWorkerList();
        onlineUsers = server.getOnlineUsers();
        while ( (line = reader.readLine()) != null) {
            //LOGGER.info(line);
            mesage = transform(line);
            System.out.println("Login" + "  " + mesage.getLogin() + " " + mesage.getMsgBody());

            Handler.start(mesage);
            String incom = Handler.getResponse();
            System.out.println(incom);
            String[] tokens = StringUtils.split(incom);

            switch (tokens[0]) { //getResponce();
                case "access":
                    OutMessage access;
                    List<User> userList = UsersDAOimpl.getInstance().getUsers();

                    if (userList.size() != 0) {
                        for (User index : userList) {
                            if (index.getName().equals(mesage.getLogin()) & index.getUser_status() == 0) {
                                UsersDAOimpl.getInstance().updateUsrStatus(index.getId(), 1);
                                onlineUsers.add(mesage.getLogin());
                                hashList.put(this.getName(), mesage.getLogin());
                                System.out.println("threadName : " + this.getName() + "clientName : " + mesage.getLogin());
                                sendList(onlineUsers);
                                access = new OutMessage(11, index.getName());
                                line = transformOut(access);
                                output.println(line);
                            } else if (index.getName().equals(mesage.getLogin()) & index.getUser_status() == 1) {
                                access = new OutMessage(12);
                                line = transformOut(access);
                                output.println(line);
                            }
                        }
                    }
                    break;
                case "noRegUser":
                    OutMessage noRegUser = new OutMessage(12);
                    line = transformOut(noRegUser);
                    output.println(line);
                    break;
                case "regUser":
                    OutMessage regUser = new OutMessage(21, tokens[1]);
                    line = transformOut(regUser);
                    output.println(line);
                    break;
                case "messageAll":
                    System.out.println("messageAll ->");
                    OutMessage messageAll = new OutMessage(mesage.getFromUser(), 32,  mesage.getMsgBody());
                    List<ServerWorker> mylist = server.getWorkerList();
                    String str;
                    int lol = 0;
                    str = transformOut(messageAll);
                    for (ServerWorker i : mylist){
                        i.send(str);
                    }
                    System.out.println("<- messageAll");
                    break;
                case "messagePrivate":
                    sendMessageToPrivateRoom(mesage.getFromUser(), mesage.getToUser(), mesage.getMsgBody());
                    break;
                case "activeList":
                    ArrayList<String> firstLocalList = getOnlineList();
                    sendList(firstLocalList);
                    System.out.println("----------------------");
                    for (String i : firstLocalList) {
                        System.out.println(i);
                    }
                    System.out.println(firstLocalList.getClass().getName());
                    System.out.println("----------------------");
                    break;
                case "createPrivateChatRoom":
                    System.out.println("chatRoom created");
                    createPrivateChatRoom(mesage.getFromUser(), mesage.getToUser(), mesage.getMsgBody());
                    System.out.println("-------------- " + mesage.getFromUser() + "aaaaaaaa" + mesage.getToUser());
                    OutMessage msg = new OutMessage(mesage.getToUser(), 34);
                    String newLine = transformOut(msg);
                    output.println(newLine);
                    break;
                case "logOut":
                    // if user logs out
                    System.out.println("///////////////////");
                    System.out.println(mesage.getFromUser() + "----" + mesage.getLogin());
                    System.out.println("///////////////////");
                    ArrayList<String> localList = getOnlineList();
                    String removeUser = " ";
                    List<User> usrList = UsersDAOimpl.getInstance().getUsers();
                    if (usrList.size() != 0 ) {
                        for (String index : localList) {
                            System.out.println(index);
                            for (User user : usrList) {
                                //System.out.println(user);
                                System.out.println(user.getName() + " user.getName " + mesage.getLogin() + " nameFromUser");
                                if (user.getName().equals(mesage.getFromUser())) {

                                    removeUser = user.getName();
                                    UsersDAOimpl.getInstance().updateUsrStatus(user.getId(), 0);
                                }
                            }
                        }

                    }

                    onlineUsers.remove(removeUser);
                    System.out.println(removeUser + ": removed user");
                    sendList(localList);
                    handleLogOff();
                    break;
            }
        }
        socket.close();
    }

    private void createPrivateChatRoom(String fromUser, String toUser, String msgBody) {
        chatRoomlist.add(fromUser);
        chatRoomlist.add(toUser);
        Thread thread = new Thread();
        List<ServerWorker> list = server.getWorkerList();
        for (ServerWorker worker : list) {
            privateRoom.put(worker, chatRoomlist);
            return;
        }
    }

    private void sendMessageToPrivateRoom (String fromUser, String toUser, String msgBody) {
        List<ServerWorker> newServerWorkerList = server.getWorkerList();

        /*for (HashMap.Entry<String, ArrayList<String>> entry : hashList.entrySet()) {
            if (entry.getKey().equals(toUser)) {
                OutMessage access = new OutMessage(33, fromUser, msgBody);
                String line = transformOut(access);
                output.println(line);
                this.send(line);
            }
        }*/
        for (HashMap.Entry<Thread, ArrayList<String>> entry : privateRoom.entrySet()) {
            for (ServerWorker worker : newServerWorkerList) {
                if (entry.getKey().equals(worker)) System.out.println("PotokiSovpali");
            }
        }


            /*for (HashMap.Entry<String, String> entry : hashList.entrySet()) {
                System.out.println("Thread Name: " + this.getName() + " Worker Name: " + worker.getName()
                        + "User Name: " + entry.getValue());
                if (entry.getKey().equals(worker.getName())) {
                    OutMessage out = new OutMessage(fromUser, msgBody, 33);
                    String line = transformOut(out);
                    worker.send(line);
                }
            }*/

        OutMessage out = new OutMessage(fromUser, msgBody, 33);
        String line = transformOut(out);
        output.println(line);
    }
    /*private void createPrivatechatRoom(String fromUser, String toUser, String msgBody) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                //super.run();
            }
        };
        thread.start();
    }*/
    private void handleLogOff() {
        server.removeWorker(this);
    }
    private void send(String message) {
        output.println(message);
        //LOGGER.warn("Oops, something wrong in " + this.getClass() + " \"send()\" method");
    }

    private ArrayList<String> sendList (ArrayList<String> arrayList) {
        String line;
        OutMessage message = new OutMessage(311, arrayList);
        line = transformOut(message);
        List<ServerWorker> mylist3 = server.getWorkerList();
        for (ServerWorker i : mylist3) {
            i.send(line);
        }
        //onlineUsers = newOnlineList;
        return arrayList;
    }

    private ArrayList<String> getOnlineList () {
        return onlineUsers;
    }

    /*public String getThreadName() {
        return threadName;
    }

    public void setThreadName (String name) {
        threadName = name;
    }*/
}
