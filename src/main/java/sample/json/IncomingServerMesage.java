package sample.json;

/**
 * Created by Zver on 05.03.2018.
 */
public class IncomingServerMesage {
   private String accept, denay;
   private Integer userId, chatId;

   public IncomingServerMesage(String accept, Integer userId) {
      this.accept = accept;
      this.userId = userId;
   }

   public String getAccept() {
      return accept;
   }

   public String getDenay() {
      return denay;
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

   public void setDenay(String denay) {
      this.denay = denay;
   }

   public void setUserId(Integer userId) {
      this.userId = userId;
   }

   public void setChatId(Integer chatId) {
      this.chatId = chatId;
   }
}
