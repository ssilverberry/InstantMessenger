package com.group42.client.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.text.Text;

public class Model {

    /**
     * instance of current user.
     */
    private User user;

    /**
     * list of online chat users.
     */
    private ObservableList<String> generalUserList;

    /**
     * storage for group chat history
     */
    private ObservableList<Text> groupChatHistoryList;

    /**
     * Map with key by chat and values by list of text messages
     */
    private ObservableMap<Chat, ObservableList<Text>> chatListMap;

    private static final Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }

    private Model(){
        generalUserList = FXCollections.observableArrayList();
        groupChatHistoryList = FXCollections.observableArrayList();
        chatListMap = FXCollections.observableHashMap();
        chatListMap.put(new Chat("General chat"), groupChatHistoryList);
    }

    public void setUser(String login) {
        user = new User(login);
    }

    public User getUser() {
        return user;
    }

    public ObservableList<String> getGeneralUserList() {
        return generalUserList;
    }

    public void setGeneralUserList(ObservableList<String> generalUserList) {
        this.generalUserList = generalUserList;
    }

    public ObservableMap<Chat, ObservableList<Text>> getChatListMap() {
        return chatListMap;
    }
}
