package serverSide;

/**
 * Created by Zver on 05.03.2018.
 */
public class InputChatMessage {


    private String mail, login, password, chatMessage;
    private int idCat,idUsers;

    public InputChatMessage (String mail, String login, String password) {
        this.mail = mail;
        this.login = login;
        this.password = password;
    }

    public InputChatMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public InputChatMessage (String chatMessage, int idUsers) {
        this.chatMessage = chatMessage;
        this.idUsers = idUsers;
    }

    public String getMail() {
        return mail;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public int getIdCat() {
        return idCat;
    }

    public int getIdUsers() {
        return idUsers;
    }
}


