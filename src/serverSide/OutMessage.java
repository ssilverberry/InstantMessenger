package serverSide;

public class OutMessage {
    private String accept, denay, chatMessage;
    private Integer userId, chatId;

    public OutMessage(Integer userId, Integer chatId, String accept ) {
        this.accept = accept;
        this.userId = userId;
        this.chatId = chatId;

    }
    public OutMessage(Integer userId,String chatMessage ){
        this.userId = userId;
        this.chatMessage = chatMessage;

    }

    public OutMessage(String accept, Integer userId) {
        this.accept = accept;
        this.userId = userId;
    }
    public OutMessage(String accept) {
        this.accept = accept;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}

