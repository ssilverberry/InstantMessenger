package com.company.paul.model;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ChatClient {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;

    private LinkedList<UserStatusListener> userStatusListeners = new LinkedList<>();
    private LinkedList<MessageListener> messageListeners = new LinkedList<>();
    private static final Logger LOGGER = LogManager.getLogger();

    public ChatClient (String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main (String[] args) {
        ChatClient client = new ChatClient("localhost", 1234);
        client.addUserStatusListener(new UserStatusListener() {
            @Override
            public void online(String login) {
                LOGGER.info("ONLINE: " + login);
            }

            @Override
            public void offline(String login) {
                LOGGER.info("OFFLINE: " + login);
            }
        });

        client.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                LOGGER.info("You got a message " + fromLogin + " > " + msgBody);
            }
        });
        if (!client.connect())
            LOGGER.error("Connection failed !");
        else {
            LOGGER.info("Connection succeed !");
            if (client.login("guest", "guest")) {
                LOGGER.info("Login successful");

                client.msg("pasha", "Hello there");
            }
            else
                LOGGER.error("Login failed");

            //client.logoff();
        }
    }

    public void msg(String sendTo, String msgBody) {
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        try {
            serverOut.write(cmd.getBytes());
        } catch (IOException e) {
            LOGGER.warn("Cmd was not sent");
        }
    }

    public void logoff() {
        String cmd = "logoff\n";
        try {
            serverOut.write(cmd.getBytes());
        } catch (IOException ex) {
            LOGGER.error("logoff failed: " + ex.getCause());
        }

    }

    public boolean login(String login, String password) {
        String cmd = "login " + login + " " + password + "\n";
        try {
            serverOut.write(cmd.getBytes());
            String response = bufferedIn.readLine();
            LOGGER.info("Response line: " + response);

            if ("ok login".equalsIgnoreCase(response)) {
                startMessageReader();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            LOGGER.warn("Client login is failed");
        }
        return false;
    }

    private void startMessageReader() {
        Thread t = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t.start();
    }

    private void readMessageLoop() {
        try {
            String line;
            while ((line = bufferedIn.readLine()) != null) {
                String[] tokens = StringUtils.split(line);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)) {
                        handleOnline(tokens);
                    } else if ("offline".equalsIgnoreCase(cmd)) {
                        handleOffline(tokens);
                    } else if ("msg".equalsIgnoreCase(cmd)) {
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        handleMessage(tokensMsg);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ChatClient error, line was not read !");
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Socket was not found !");
            }
        }
    }

    private void handleMessage(String[] tokensMsg) {
        String login = tokensMsg[1];
        String msgBody = tokensMsg[2];

        for (MessageListener listener : messageListeners) {
            listener.onMessage(login, msgBody);
        }
    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for (UserStatusListener listener : userStatusListeners) {
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for (UserStatusListener listener : userStatusListeners) {
            listener.online(login);
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUserStatusListener (UserStatusListener listener) {
        userStatusListeners.add(listener);
    }
    public void removeStatusListener (UserStatusListener listener) {
        userStatusListeners.remove(listener);
    }

    public void addMessageListener (MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener (MessageListener listener) {
        messageListeners.remove(listener);
    }
}
