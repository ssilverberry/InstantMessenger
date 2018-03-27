package com.group42.client.controllers;

/**
 * Class for send request from client.
 */

import com.group42.client.network.NetworkController;
import com.group42.client.network.protocol.OutputClientMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestController {

    /**
     * Instance of RequestController class
     */
    private static final RequestController INSTANCE = new RequestController();

    private final Logger logger = LogManager.getLogger(RequestController.class);

    private RequestController() {
    }

    public static RequestController getInstance() {
        return INSTANCE;
    }

    public void authorizationRequest(String login, String password){
        OutputClientMessage outputClientMessage = new OutputClientMessage(1, login, password);
        NetworkController.getInstance().send(outputClientMessage);
        logger.info("SEND AUTHORIZATION REQUEST TO SERVER: " + login + " " + password);
    }

    /**
     *  Send request for registration.
     *
     */
    public void registrationRequest(String email, String login, String password) {
        OutputClientMessage outputClientMessage = new OutputClientMessage(2, email, login, password);
        NetworkController.getInstance().send(outputClientMessage);
        logger.info("SEND REGISTRATION REQUEST TO SERVER: " + email + " " + login + " " + password);
    }

    /**
     * send request for get online users.
     */
    public void getOnlineUsersRequest() {
        OutputClientMessage outputMessage = new OutputClientMessage(31);
        NetworkController.getInstance().send(outputMessage);
        logger.info("SEND GET ONLINE USERS REQUEST TO SERVER ");
    }

    /**
     * send message to general chat request.
     */
    public void messageToGeneralChat(String fromUser, String message){
        OutputClientMessage outputMessage = new OutputClientMessage(fromUser, 32, message);
        NetworkController.getInstance().send(outputMessage);
        logger.info("SEND MESSAGE TO GENERAL CHAT REQUEST TO SERVER: " + fromUser + " " + message);
    }

    /**
     * send log uot request.
     */
    public void logOutRequest(String fromUser) {
        OutputClientMessage outputMessage = new OutputClientMessage(35, fromUser);
        NetworkController.getInstance().send(outputMessage);
        logger.info("SEND LOG OUT REQUEST TO SERVER: " + fromUser);
    }
}
