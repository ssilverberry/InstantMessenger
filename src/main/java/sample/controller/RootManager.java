package sample.controller;

/*
  This class manage windows startup.
  Shows authorisation, registration and main window
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Main;
import java.io.IOException;
import java.util.Objects;

public class RootManager {
    private static Stage primaryStage;
    private static final String authorisationView = "view/AuthorisationView.fxml";
    private static final String registrationView = "view/RegistrationView.fxml";
    private static final String mainView = "view/MainView.fxml";

    public RootManager() {
    }

    public static void initRootLayout(Stage primaStage) {
        primaryStage = primaStage;
        setAuthorisationView();
    }

    static void setAuthorisationView() {
        setSpecificScene(authorisationView);
        primaryStage.setTitle("Authorisation");
    }

    static void setRegistrationView() {
        setSpecificScene(registrationView);
        primaryStage.setTitle("Registration");
    }

    static void setMainView(){
        primaryStage.setTitle("Chat");
        setSpecificScene(mainView);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
    }

    private static void setSpecificScene(String scene){
        try {
            if (primaryStage.isShowing()){
                primaryStage.close();
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource(scene)));
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
