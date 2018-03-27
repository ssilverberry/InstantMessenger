package com.InstantMessengerServer;

import com.InstantMessengerServer.controller.Server;
import com.InstantMessengerServer.controller.UsersDAOimpl;

public class App 
{
    public static void main( String[] args ) {
        Server server = new Server();
        server.start();
        UsersDAOimpl.getInstance().start();
    }
}
