package com.company.paul.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread {
    private int serverPort;

    private LinkedList<ServerWorker> workerList = new LinkedList<>();

    Server (int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerWorker> getWorkerList() {
        return workerList;
    }

    @Override
    public void run() {
        try {
            ServerSocket listener = new ServerSocket(serverPort);
            while (true) {
                System.out.println("Wait for client");
                final Socket socket = listener.accept();
                System.out.println("Connection is established ! " + socket);
                ServerWorker worker = new ServerWorker(this, socket);
                workerList.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            System.err.println("Connection is failed !");
        }
    }

    public void removeWorker(ServerWorker serverWorker) {
        workerList.remove(serverWorker);
    }
}
