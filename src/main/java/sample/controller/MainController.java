package sample.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Main;
import sample.model.Emoji;
import sample.model.Message;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;


public class MainController {
    @FXML
    private ImageView menuButton;
    @FXML
    private TextField searchField;
    @FXML
    private Label chatName;
    @FXML
    private ListView<ObservableList> chatListView;
    @FXML
    private HBox writeBox;
    @FXML
    private TextArea messageField;
    @FXML
    private ImageView sendButton;
    @FXML
    private VBox sendBox;

    private ContextMenu contextMenu;
    private MenuItem newGroup;
    private MenuItem newPrivate;

    ///////// for chat history ///////////////////
    @FXML
    private TableView<Message> chatHistoryView;
    @FXML
    private TableColumn<Message, String> chatHistory;
    private ObservableList<Message> chatMessages;
    private Message message;
    //////////////////////////////////////////////

    private ObservableList<String> chatList;
    @FXML
    private ListView userList;

    public MainController() {

        chatMessages = FXCollections.observableArrayList();
        chatList = FXCollections.observableArrayList();
        chatList.add("General Chat");
    }

    @FXML
    private void sendListener(){
        if(!messageField.getText().isEmpty()) {
            message = new Message(messageField.getText());
            reduceToDefaultSize();
            chatMessages.add(message);
            chatHistory.setCellValueFactory(new PropertyValueFactory<>("content"));
            chatHistoryView.setItems(chatMessages);
            chatHistory.setStyle("-fx-font-size: 14");
        }
    }

    @FXML
    private void initOnKeySendListener () {
        chatListView.getItems().add(chatList);
        messageField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    sendListener();
                }
            }
        });
    }

    @FXML
    private void userListListener(){
        userList.getItems().add("user1");
    }

    /////////////////////////////////////////////   MESSAGE_FIELD   ///////////////////////////////////////////////////
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

    private void increaseMessageField(){
        writeBox.setMinHeight(writeBox.getMinHeight() + 20);
        messageField.setMinHeight(messageField.getMinHeight() + 20);
        sendBox.setPadding(new Insets(sendBox.getPadding().getTop() + 20, sendBox.getPadding().getRight(),
                sendBox.getPadding().getBottom(), sendBox.getPadding().getLeft()));
    }

    private void reduceToDefaultSize(){
        messageField.clear();
        writeBox.setMinHeight(40);
        messageField.setMinHeight(30);
        sendBox.setPadding(new Insets(0, sendBox.getPadding().getRight(),
                sendBox.getPadding().getBottom(), sendBox.getPadding().getLeft()));
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private void menuListener(){
        menuButton.setOpacity(1.0);
        menuButton.setOnMouseClicked(event -> {
            contextMenu = new ContextMenu();
            newGroup = new MenuItem("New Group");
            addNewGroupListener(newGroup);
            newPrivate = new MenuItem("New Private");
            addNewPrivateListener(newPrivate);
            contextMenu.getItems().addAll(newGroup, newPrivate);
            contextMenu.show(menuButton, event.getScreenX(), event.getScreenY());
        });
    }

    ////////////////////////////////////////////////    CREATE GROUP    ///////////////////////////////////////////////
    private void addNewGroupListener(MenuItem menuItem){
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog textInputDialog = new TextInputDialog();
                textInputDialog.setTitle("New group");
                textInputDialog.setHeaderText("Group name:");
                textInputDialog.setGraphic(null);
                textInputDialog.setContentText(null);
                if(!checkGroupNameForEmpty(textInputDialog)){
                    String result = textInputDialog.getResult();

                }
            }
        });
    }

    private boolean checkGroupNameForEmpty(TextInputDialog textInputDialog){
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()){
            if (result.get().equals("")){
                textInputDialog.getEditor().setStyle("-fx-border-color: #d62f2f;");
                return checkGroupNameForEmpty(textInputDialog);
            }
        } return false;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////   CREATE PRIVATE  ///////////////////////////////////////////////
    private void addNewPrivateListener(MenuItem menuItem){
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newPrivateDialog();
            }
        });
    }

    private void newPrivateDialog(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("user1");
        list.add("user2");
        list.add("user3");
        ChoiceDialog choiceDialog = new ChoiceDialog(list.get(0), list);
        choiceDialog.setHeaderText("Add member");
        choiceDialog.setGraphic(null);
        choiceDialog.setContentText(null);
        Optional<String> result = choiceDialog.showAndWait();
        if (result.isPresent()){
            chatList.add(result.get());
            //chatListView.getItems().add(result);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private void searchListener() {
        if (!searchField.getText().isEmpty()){
            String search = searchField.getText();
            new Alert(Alert.AlertType.INFORMATION, search).showAndWait();
        }
    }

    @FXML
    private void testingEmoji() {
        chatName.setStyle("-fx-font-size: 18px");
        chatName.setText(Arrays.toString(Emoji.values()));
    }

    @FXML
    private void illuminateSendButton(){
        sendButton.setOpacity(1.0);
    }

    @FXML
    private void unlitMenuButton(){
        menuButton.setOpacity(0.7);
    }

    @FXML
    private void unlitSendButton(){
        sendButton.setOpacity(0.7);
    }


}
