package com.group42.server.model;

import java.util.ArrayList;

/***********************************************************************************
+    private Integer id
+
+    1 - Authorisation
+        request: 1,  "login", "password"
+
+    2 - Registration
+        request: 2, "email", "login", "password"
+
+    3 - Main chat:
+        request: 31                                     - get online users
+        request: 32  "fromUser", "message"              - message to all
+        request: 33  "fromUser", "toUser", "message"    - message to private
+        request: 34  "fromUser", "toUser"               - create private chat
+        request: 35  "fromUser"                         - log out
+
+     *******************************************************************************/
public class OutMessage {
    /****************
     **New Version**
     **************/
    private Integer actionId;
    private String email, login, password;
    private String fromUser, toUser, msgBody;
    private ArrayList<String> onlineUsers;

    public OutMessage(Integer actionId) {
        this.actionId = actionId;
    }

    public OutMessage(Integer actionId, String login) {
        this.actionId = actionId;
        this.login = login;
    }
    public OutMessage(String toUser, Integer actionId) {
        this.actionId = actionId;
        this.toUser = toUser;
    }
    public OutMessage (Integer actionId, ArrayList<String> onlineUsers) {
        this.actionId = actionId;
        this.onlineUsers = onlineUsers;
    }

    public OutMessage(Integer actionId, String fromUser, String msgBody) {
        this.actionId = actionId;
        this.fromUser = fromUser;
        this.msgBody = msgBody;
    }

    public OutMessage(String fromUser, String msgBody, Integer actionId){
        this.fromUser = fromUser;
        this.msgBody = msgBody;
        this.actionId = actionId;
    }
    public OutMessage(String fromUser, Integer actionId, String msgBody){
        this.fromUser = fromUser;
        this.msgBody = msgBody;
        this.actionId = actionId;
    }

    /*****************
    ** Old version***
    ****************
    private String accept, denay, chatMessage, login;
    private Integer userId, chatId;
    private Integer privateChat, responseId ;

    private List<String> activeList;

    public model.OutMessage(Integer userId, Integer chatId, String accept) {
        this.accept = accept;
        this.userId = userId;
        this.chatId = chatId;

    }

    public model.OutMessage(String accept, String login) {
        this.accept = accept;
        this.login = login;

    }

    public model.OutMessage(String accept, Integer userId) {
        this.accept = accept;
        this.userId = userId;
    }

    public model.OutMessage(String accept, Integer userId, String login) {
        this.accept = accept;
        this.userId = userId;
        this.login = login;
    }

    public model.OutMessage(String accept, String login, String chatMessage) {
        this.accept = accept;
        this.login = login;
        this.chatMessage = chatMessage;
    }

    public model.OutMessage(Integer privateChat, String login, String chatMessage) {
        this.privateChat = privateChat;
        this.login = login;
        this.chatMessage = chatMessage;
    }

    public model.OutMessage (Integer responseId, List<String> list) {
        this.responseId = responseId;
        activeList = list;
    }

    public model.OutMessage(String accept) {
        this.accept = accept;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }*/
}

