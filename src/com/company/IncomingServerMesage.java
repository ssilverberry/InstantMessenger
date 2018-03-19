package com.company;

/**
 * Created by Zver on 05.03.2018.
 */
public class IncomingServerMesage {
   private String accept, denay, all, messageAll;
   private Integer userId, chatId;

   public String getMessageAll() {
      return messageAll;
   }

   public void setMessageAll(String messageAll) {
      this.messageAll = messageAll;
   }

   public String getAll() {
      return all;
   }

   public void setAll(String all) {
      this.all = all;
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
