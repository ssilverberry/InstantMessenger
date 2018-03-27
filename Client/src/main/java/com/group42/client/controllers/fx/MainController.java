package com.group42.client.controllers.fx;

import com.group42.client.model.factory.ChatListCellFactory;
import com.group42.client.model.factory.UserListCellFactory;
import com.group42.client.network.protocol.IncomingServerMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.group42.client.controllers.RequestController;
import com.group42.client.model.Chat;
import com.group42.client.model.Model;

public class MainController extends Controller {

    /**
     * instance of model
     */
    private Model model;

    /**
     * Logging for exception trace
     */
    private final Logger logger = LogManager.getLogger(MainController.class);

    /**
     * bindings to fxml elements
     */
    @FXML
    private Label chatName;
    @FXML
    private HBox writeBox;
    @FXML
    private TextArea messageField;
    @FXML
    private ImageView sendButton;
    @FXML
    private VBox sendBox;
    @FXML
    private AnchorPane centerPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button logOutBtn;

    /**
     * container for chat list
     */
    @FXML
    private ListView<Chat> chatListView;

    /**
     * list of chats, which contains in container
     */
    private ObservableList<Chat> chatList;

    /**
     * Container for chat history
     */
    @FXML
    private TextFlow chatHistoryView;

    /**
     * container for list of online chat users
     */
    @FXML
    private ListView<String> generalUserListView;

    public MainController() {
    }

    /**
     * Process response from server.
     */
    @Override
    public void processResponse(IncomingServerMessage message) {
        Platform.runLater(() -> {
            switch (message.getActionId()){
                case 311:
                    model.getGeneralUserList().setAll(message.getOnlineUsers());
                    logger.info("Receive list of online users" + message.getOnlineUsers());
                    break;
                case 312:
                    model.setGeneralUserList(null);
                    break;
                case 32:
                    addMessageToGeneralChat(message);
                    logger.info("add message to group chat: " + message.getFromUser() + ": " + message.getMsgBody());
                    break;
            }
        });
    }

    /**
     * init function load after scene setup.
     */
    @FXML
    void initialize() {
        model = Model.getInstance();
        chatList = FXCollections.observableArrayList();
        chatList.setAll(model.getChatListMap().keySet());
        initializeFactories();
        generalUserListView.setVisible(false);
        centerPane.setVisible(false);
        RequestController.getInstance().getOnlineUsersRequest();
        setLogOutListener();
    }

    /**
     * Cell factories add listeners to lists of data
     */
    private void initializeFactories() {
        chatListView.setCellFactory(new ChatListCellFactory());
        generalUserListView.setCellFactory(new UserListCellFactory());
        chatListView.setItems(chatList);
        generalUserListView.setItems(model.getGeneralUserList());
    }

    /**
     * add message to general chat.
     */
    private void addMessageToGeneralChat(IncomingServerMessage message) {
        receiveMessageToChat("General chat", message);
    }

    /**
     * parse message to show it in chat
     */
    private Text parseMessage(IncomingServerMessage message) {
        Text text = new Text(message.getFromUser() + " > " + message.getMsgBody() + "\n");
        text.setFont(new Font(14));
        chatHistoryView.getChildren().add(text);
        scrollPane.setVvalue(1.0);
        return text;
    }

    /**
     * receive message in chat history for chat in <tt>chatType</tt>
     */
    private void receiveMessageToChat(String chatType, IncomingServerMessage message) {
        for (ObservableMap.Entry<Chat, ObservableList<Text>> chatList: model.getChatListMap().entrySet()) {
            Chat chat = chatList.getKey();
            if (chat.getChatName().equals(chatType)){
                model.getChatListMap().get(chat).add(parseMessage(message));
                logger.info("receive message to chat list");
            }
        }
    }

    /**
     * listen to get username from user list and insert into message field.
     */
    @FXML
    private void generalListListener() {
        String user = generalUserListView.getSelectionModel().getSelectedItem();
        if (user != null) {
            messageField.setText(messageField.getText() + " " + user + ", ");
            messageField.positionCaret(messageField.getText().length());
        }
    }

    /**
     * listen to select chat and set chat history for current chat
     */
    @FXML
    private void chatListListener() {
        if (chatListView.getSelectionModel().getSelectedItem() != null){
            Chat chat = chatListView.getSelectionModel().getSelectedItem();
            chatHistoryView.getChildren().clear();
            if (model.getChatListMap().get(chat) != null) {
                chatHistoryView.getChildren().setAll(model.getChatListMap().get(chat));
            } else chatHistoryView.getChildren().setAll(FXCollections.observableArrayList());
            sendButton.setOnMouseClicked(event -> {
                sendMessageToChat(chat);
            });
            messageField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    messageField.setCursor(Cursor.DEFAULT);
                    sendMessageToChat(chat);
                }
            });
            chatName.setText(chat.getChatName());
        }
        chatName.setVisible(true);
        generalUserListView.setVisible(true);
        centerPane.setVisible(true);
    }

    /**
     * send request with message to specific chat.
     */
    private void sendMessageToChat(Chat chatType) {
        String message = messageField.getText();
        if (!message.isEmpty()){
            Text text = new Text(message + "\n");
            text.setFont(new Font(14));
            scrollPane.setVvalue(1.0);
            if (chatType.getChatName().equals("General chat")){
                RequestController.getInstance().messageToGeneralChat(model.getUser().getUserName(), message);
            } reduceToDefaultSize();
        }
    }

    /**
     * Listening typing into message field.
     */
    @FXML
    private void messageFieldListener() {
        if (messageField.getText().contains("\n") && messageField.getText().length() == 1)
            messageField.clear();
        if (messageField.getScrollTop() > 0.0) {
            if (writeBox.getMinHeight() + 10 < writeBox.getMaxHeight()) {
                messageField.setScrollTop(0.0);
                increaseMessageField();
            }
        }
    }

    /**
     * Automatically increases  message field, if it necessary.
     */
    private void increaseMessageField(){
        writeBox.setMinHeight(writeBox.getMinHeight() + 20);
        messageField.setMinHeight(messageField.getMinHeight() + 20);
        sendBox.setPadding(new Insets(sendBox.getPadding().getTop() + 20, sendBox.getPadding().getRight(),
                sendBox.getPadding().getBottom(), sendBox.getPadding().getLeft()));
    }

    /**
     * Reduced message field to default size
     */
    private void reduceToDefaultSize(){
        messageField.clear();
        writeBox.setMinHeight(40);
        messageField.setMinHeight(30);
        sendBox.setPadding(new Insets(0, sendBox.getPadding().getRight(),
                sendBox.getPadding().getBottom(), sendBox.getPadding().getLeft()));
    }

    /**
     * log out request.
     */
    private void setLogOutListener() {
        logOutBtn.setOnAction(event -> {
            RequestController.getInstance().logOutRequest(model.getUser().getUserName());
            RootManager.getInstance().setAuthorisationScene();
        });
        RootManager.getInstance().getPrimaryStage().setOnCloseRequest(event -> {
            RequestController.getInstance().logOutRequest(model.getUser().getUserName());
        });
    }

    /**
     * illuminate and unlit next buttons.
     */

    @FXML
    private void illuminateSendButton(){
        sendButton.setOpacity(1.0);
    }

    @FXML
    private void unlitSendButton(){
        sendButton.setOpacity(0.7);
    }
}
