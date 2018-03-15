package sample.controller;

/*
  Organize user registration
 */


import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.json.OutputClientMessage;
import sample.json.ProtocolClient;
import sample.model.ClientModel;
import sample.model.User;

import java.io.IOException;

import static sample.json.ProtocolClient.mesage;

public class RegistrationController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signUpButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label errorLabel;

    public RegistrationController() {
    }

    @FXML
    private void signUpListener(){
        errorLabel.setText("");
        if (!checkFieldsForEmpty()) {
            errorLabel.setPrefHeight(20);
            errorLabel.setText("");
            if (!checkEmailForValid()) {
                String email = emailField.getText();
                String login = userNameField.getText();
                String password = passwordField.getText();
                int userId = registrationRequest(email, login, password);
                User user = new User(email, userId, "userName");
                System.out.println(user);
                clearRegistrationFields();
                RootManager.setAuthorisationView();
            }
        }
    }

    private Integer registrationRequest(String email, String login, String password) {

        try {
            OutputClientMessage outputClientMessage = new OutputClientMessage(email, login, password);
            ProtocolClient.start(ProtocolClient.transform(outputClientMessage));
            if (mesage.getAccept().equals("ok")) {
                return mesage.getUserId();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } return -1;
    }

    private void clearRegistrationFields(){
        emailField.clear();
        userNameField.clear();
        passwordField.clear();
    }

    @FXML
    private void cancelListener(){
        RootManager.setAuthorisationView();
    }

    private boolean checkFieldsForEmpty(){
        boolean emptyFlag = false;
        if (emailField.getText().isEmpty()){
            emptyEmail();
            emptyFlag = true;
        }
        if (userNameField.getText().isEmpty()){
            emptyUserName();
            emptyFlag = true;
        }
        if (passwordField.getText().isEmpty()){
            emptyPassword();
            emptyFlag = true;
        }
        return emptyFlag;
    }

    private boolean checkEmailForValid(){
        boolean invalidFlag = false;
        String line = emailField.getText();
        if (!line.contains("@")){
            invalidEmail();
            invalidFlag = true;
        }
        if (!(line.indexOf("@") < line.indexOf(".")) || (line.indexOf(".") - line.indexOf("@") < 3)){
            invalidEmail();
            invalidFlag = true;
        } return invalidFlag;
    }

    private void invalidEmail(){
        emailField.setStyle("-fx-border-color: #d62f2f;");
        errorLabel.setText("! Invalid email");
        errorLabel.setPrefHeight(20);
    }

    private void emptyEmail(){
        emailField.setStyle("-fx-border-color: #d62f2f;");
        errorLabel.setText("! Email is required.");
        errorLabel.setPrefHeight(20);
    }

    private void emptyUserName(){
        userNameField.setStyle("-fx-border-color: #d62f2f;");
        if (errorLabel.getText().equals("! Email is required.")){
            errorLabel.setText(errorLabel.getText().concat("\n! Username is required."));
            errorLabel.setPrefHeight(33);
        } else {
            errorLabel.setText("! Username is required.");
            errorLabel.setPrefHeight(20);
        }
    }

    private void emptyPassword(){
        passwordField.setStyle("-fx-border-color: #d62f2f;");
        if (errorLabel.getText().equals("! Email is required.\n! Username is required.")) {
            errorLabel.setText(errorLabel.getText().concat("\n! Password is required."));
            errorLabel.setPrefHeight(50);
        } else if (errorLabel.getText().contains("Email")) {
            errorLabel.setText(errorLabel.getText().concat("\n! Password is required."));
            errorLabel.setPrefHeight(33);
        } else if (errorLabel.getText().contains("Username")){
            errorLabel.setText(errorLabel.getText().concat("\n! Password is required."));
            errorLabel.setPrefHeight(33);
        } else {
            errorLabel.setText("! Password is required.");
            errorLabel.setPrefHeight(20);
        }
    }
}
