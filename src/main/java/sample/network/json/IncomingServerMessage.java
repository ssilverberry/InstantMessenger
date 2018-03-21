package sample.network.json;

/**
 * Created by Zver on 05.03.2018.
 */
public class IncomingServerMessage {
   private String accept, responseId, login;
   private Integer userId, chatId;
   private String chatMessage;


   public IncomingServerMessage() {
   }

   public IncomingServerMessage(String accept, Integer userId, String login) {
      this.accept = accept;
      this.userId = userId;
      this.login = login;
   }

   public IncomingServerMessage(String responseId, String fromUser, String chatMessage) {
      this.responseId = responseId;
      this.login = fromUser;
      this.chatMessage = chatMessage;
   }

   public String getLogin() {
      return login;
   }

   public void setLogin(String login) {
      this.login = login;
   }

   public String getResponseId() {
      return responseId;
   }

   public void setResponseId(String responseId) {
      this.responseId = responseId;
   }

   public String getChatMessage() {
      return chatMessage;
   }

   public void setChatMessage(String chatMessage) {
      this.chatMessage = chatMessage;
   }

   public String getAccept() {
      return accept;
   }

   public Integer getUserId() {
      return userId;
   }

   public Integer getChatId() {
      return chatId;
   }

   public void setAccept(String accept) {
      this.accept = accept;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public void setChatId(Integer chatId) {
      this.chatId = chatId;
   }

}
