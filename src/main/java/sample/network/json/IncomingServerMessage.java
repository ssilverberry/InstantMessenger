package sample.network.json;

import java.util.List;

/**
 * Created by Zver on 05.03.2018.
 */
public class IncomingServerMessage {

   private Integer actionId;
   private String login, fromUser;
   private String msgBody;
   private List<String> onlineUsers;

   /*
      Authorisation
            response: 11, "login"                       - authorisation successful
            response: 12                                - authorisation failed

      Registration
            response: 21                                - registration successful
            response: 22                                - registration failed

      Main chat:
            response: 311  List<String> onlineUsers     - returns list of users
            response: 312                               - no online users
            response: 32  "fromUser", "message"         - message to all
            response: 33  "fromUser", "message"         - message to private
            response: 34  "toUser"                      - create private chat
    */

   public IncomingServerMessage() {
   }

   /**
    * Successful registration response
    */
   public IncomingServerMessage(Integer actionId, String login) {
      this.actionId = actionId;
      this.login = login;
   }

   /**
    * Receive next responses: id = 12, id = 21, id = 22, id = 312.
    *
    * @param actionId - id operation
    */
   public IncomingServerMessage(Integer actionId) {
      this.actionId = actionId;
   }

   /**
    * Receive list of online users
    *
    * @param actionId - 311
    * @param onlineUsers - list if user names
    */
   public IncomingServerMessage(Integer actionId, List<String> onlineUsers) {
      this.actionId = actionId;
      this.onlineUsers = onlineUsers;
   }

   /**
    * Receive message from general chat
    *
    * @param actionId - 32
    * @param fromUser - user name of writer
    * @param msgBody - content of message
    */
   public IncomingServerMessage(Integer actionId, String fromUser, String msgBody){
      this.actionId = actionId;
      this.fromUser = fromUser;
      this.msgBody = msgBody;
   }

   /**
    * Receive message from private chat
    *
    * @param fromUser - user name of writer
    * @param msgBody - content of message
    * @param actionId - 33
    */
   public IncomingServerMessage(String fromUser, String msgBody, Integer actionId){
      this.fromUser = fromUser;
      this.msgBody = msgBody;
      this.actionId = actionId;
   }

   public String getLogin() {
      return login;
   }

   public String getFromUser() {
      return fromUser;
   }

   public String getMsgBody() {
      return msgBody;
   }

   public List<String> getOnlineUsers() {
      return onlineUsers;
   }

   public Integer getActionId() {
      return actionId;
   }
}
