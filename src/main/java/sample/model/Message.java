package sample.model;

import java.util.Date;

public class Message {
    private Date messageId;
    private int userId;
    private int chatId;
    private String content;

    public Message(String messageContent) {
        this.content = messageContent;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getMessageContent() {
        return content;
    }

    public void setMessageContent(String messageContent) {
        this.content = messageContent;
    }
}
