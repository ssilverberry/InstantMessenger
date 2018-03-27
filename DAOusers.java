package com.InstantMessengerServer.controller;

import com.InstantMessengerServer.model.*;

import java.util.List;

public interface DAOusers {
    void init();
    boolean connect();
    void start();
    void disconnect();
    List<User> getUsers();
}
