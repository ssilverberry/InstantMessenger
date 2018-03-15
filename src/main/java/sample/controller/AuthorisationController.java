package sample.controller;

/*
  Organize user authorisation
 */

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import sample.json.OutputClientMessage;
import sample.json.ProtocolClient;
import java.io.*;

import static sample.json.ProtocolClient.mesage;

public class AuthorisationController {
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink registerNow;
    @FXML
    private CheckBox rememberMeBox;
    @FXML
    private Button signInButton;
    @FXML
    private Label errorLabel;

    private final File file = new File("User.txt").getAbsoluteFile();

    public AuthorisationController() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void signInListener(){
        if (!checkFieldsForEmpty()) {
            errorLabel.setText("");
            String login = userNameField.getText();
            String password = passwordField.getText();
            try {
                OutputClientMessage outputClientMessage = new OutputClientMessage(login, password);
                ProtocolClient.start(ProtocolClient.transform(outputClientMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String response = mesage.getAccept();
            switch (response) {
                case "ok":
                    RootManager.setMainView();
                    break;
                case "regUser":
                    RootManager.setAuthorisationView();
                    break;
                case "noRegUser":
                    invalidUser();
                    break;
                default:
                    invalidUser();
                    break;
            }
        }
    }

    @FXML
    private void registerNowListener(){
        RootManager.setRegistrationView();
    }

    // checks write/read name to/from file. /////////////////////////////////////////////////
    private void writeUserToFile(String userName){
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))){
            pw.print("Username: " + userName + ".\n");
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readUserFromFile(){
        String line;
        String userName = "";
        try (BufferedReader buffer = new BufferedReader(new FileReader(file))){
            while ((line = buffer.readLine()) != null) {
                int start = line.indexOf(":");
                int end = line.lastIndexOf(".");
                userName = line.substring(start + 2, end);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } return userName;
    }
    /////////////////////////////////////////////////////////////////////////////////////
    private boolean checkFieldsForEmpty(){
        boolean emptyFlag = false;
        if (userNameField.getText().isEmpty()){
            emptyUserName();
            emptyFlag = true;
        }
        if (passwordField.getText().isEmpty()){
            emptyPassword();
            emptyFlag = true;
        } return emptyFlag;
    }

    private void emptyUserName(){
        userNameField.setStyle("-fx-border-color: #d62f2f;");
        errorLabel.setText("! Username is required.");
        errorLabel.setPrefHeight(15);
    }

    private void emptyPassword(){
        passwordField.setStyle("-fx-border-color: #d62f2f;");
        if (errorLabel.getText().equals("! Username is required.")) {
            errorLabel.setText(errorLabel.getText().concat("\n! Password is required."));
            errorLabel.setPrefHeight(35);
        } else {
            errorLabel.setText("! Password is required.");
            errorLabel.setPrefHeight(15);
        }
    }

    private void invalidUser(){
        errorLabel.setText("! Username or password do not match or are invalid!");
        errorLabel.setPrefHeight(35);
    }

    @FXML
    private void unlitUserNameField(){
        userNameField.setStyle("-fx-border-color: inherit");
    }


}
