package com.company;

/**
 * Created by Zver on 05.03.2018.
 */
public class OutputClientMessage {

    private String mail, login, password, chatMessage;
    private Integer idChat,idUsers;

    public OutputClientMessage(String mail, String login, String password) {
        this.mail = mail;
        this.login = login;
        this.password = password;
    }

    public OutputClientMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public OutputClientMessage(String chatMessage, Integer idUsers, Integer idChat) {
        this.chatMessage = chatMessage;
        this.idUsers = idUsers;
        this.idChat = idChat;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void setIdCat(Integer idCat) {
        this.idChat = idCat;
    }

    public void setIdUsers(Integer idUsers) {
        this.idUsers = idUsers;
    }
}
