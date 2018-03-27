package com.InstantMessengerServer.model;

import java.util.List;

/******************************************************************************************
 +      Authorisation
 +            response: 11, "login"                       - authorisation successful
 +            response: 12                                - authorisation failed
 +      Registration
 +            response: 21                                - registration successful
 +            response: 22                                - registration failed
 +      Main chat:
 +            response: 311  List<String> onlineUsers     - returns list of users
 +            response: 312                               - no online users
 +            response: 32  "fromUser", "message"         - message to all
 +            response: 33  "fromUser", "message"         - message to private
 +            response: 34  "toUser"                      - create private chat
 +
 *******************************************************************************************/


public class InputMessage {
    /*****************
     * New Version **
     ****************/
    private Integer actionId;
    private String login;
    private String fromUser;
    private String toUser;
    private String password, email;
    private String msgBody;
    private List<String> onlineUsers;

    public InputMessage(Integer actionId, String login, String password) {
        this.actionId = actionId;
        this.login = login;
        this.password = password;
    }

    public InputMessage(Integer actionId) {
        this.actionId = actionId;
    }

    public InputMessage(Integer actionId, String email, String login, String password) {
        this.actionId = actionId;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public InputMessage(String fromUser, String toUser, Integer actionId) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.actionId = actionId;
    }

    public InputMessage(String fromUser, Integer actionId, String msgBody) {
        this.fromUser = fromUser;
        this.actionId = actionId;
        this.msgBody = msgBody;
    }

    public InputMessage(String fromUser, String toUser, String msgBody, Integer actionId) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.msgBody = msgBody;
        this.actionId = actionId;
    }

    public InputMessage(Integer actionId, String fromUser) {
        this.actionId = actionId;
        this.fromUser = fromUser;
    }


    public Integer getActionId() {
        return actionId;
    }

    public String getLogin() {
        return login;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public List<String> getOnlineUsers() {
        return onlineUsers;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    /***************
     * Old version**
     ***************/
    /*private String mail, login, password, chatMessage, str;
    private int idCat,idUsers;
    private Integer privateChat, responseId;*/

    /*public InputMessage (String mail, String login, String password) {
        this.mail = mail;
        this.login = login;
        this.password = password;
    }

    public InputMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public InputMessage (String chatMessage, int idUsers) {
        this.chatMessage = chatMessage;
        this.idUsers = idUsers;
    }
    public InputMessage (Integer privateChat, String login, String chatMessage) {
        this.privateChat = privateChat;
        this.login = login;
        this.chatMessage = chatMessage;
    }
    public InputMessage (String str) {
        this.str = str;
    }
    public InputMessage (Integer responseId) {
        this.responseId = responseId;
    }*/
    /*public String getMail() {
        return mail;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public int getIdCat() {
        return idCat;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public Integer getPrivateChat() {
        return privateChat;
    }

    public String getStr() {
        return str;
    }
    public Integer getResponseId() {
        return responseId;
    }*/
}


