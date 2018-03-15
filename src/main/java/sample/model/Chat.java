package sample.model;

import java.util.Map;
import java.util.TreeMap;

public class Chat {
    private Map<Integer, String> chats = new TreeMap<>();
    private int chatId;
    private String chatName;
    private boolean banFlag;
    private ChatUsers [] chatUsers;
    private Message[] messageSet;

    public Chat(int chatId, String chatName) {
        this.chatId = chatId;
        this.chatName = chatName;
        chats.put(chatId, chatName);
    }

    public Map<Integer, String> getChats() {
        return chats;
    }

    public void setChats(Map<Integer, String> chats) {
        this.chats = chats;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public boolean isBanFlag() {
        return banFlag;
    }

    public void setBanFlag(boolean banFlag) {
        this.banFlag = banFlag;
    }

    public ChatUsers[] getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ChatUsers[] chatUsers) {
        this.chatUsers = chatUsers;
    }

    public Message[] getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(Message[] messageSet) {
        this.messageSet = messageSet;
    }
}
