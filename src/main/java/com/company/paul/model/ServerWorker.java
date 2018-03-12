package com.company.paul.model;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerWorker extends Thread {

    private final Server server;
    private final Socket socket;
    private String login = null;

    private HashSet<String> topicSet = new HashSet<>();
    private OutputStream outputStream;
    private static final Logger LOGGER = LogManager.getLogger();

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
            LOGGER.error("Connection was closed unexpectedly !");
        }
    }

    private void handleClientSocket () throws IOException, InterruptedException {
        InputStream inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<ServerWorker> workersList = server.getWorkerList();

        while ( (line = reader.readLine()) != null) {
            //LOGGER.info(line);
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
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

            }
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
            LOGGER.error("CLinet socket was closed");
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
                LOGGER.info("User logged in successfully: " + login);

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
                LOGGER.warn("Login is failed !" + login);
            }
        }
    }

    private void send(String message) {
        try {
            if (login != null)
                outputStream.write(message.getBytes());
        } catch (IOException e) {
            LOGGER.warn("Oops, something wrong in " + this.getClass() + " \"send()\" method");
        }
    }
}
