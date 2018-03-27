package com.group42.client.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Chat {
    private IntegerProperty chatId;
    private StringProperty chatName;
    private boolean banFlag;

    public Chat(String chatName) {
        this.chatName = new SimpleStringProperty(chatName);
    }

    public int getChatId() {
        return chatId.get();
    }

    public IntegerProperty chatIdProperty() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId.set(chatId);
    }

    public String getChatName() {
        return chatName.get();
    }

    public StringProperty chatNameProperty() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName.set(chatName);
    }

    public boolean isBanFlag() {
        return banFlag;
    }

    public void setBanFlag(boolean banFlag) {
        this.banFlag = banFlag;
    }
}
