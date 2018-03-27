package com.group42.client.controllers.fx;

/*
  This class manage windows startup.
  Shows authorisation, registration and main window
 */

import com.group42.client.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Objects;

public class RootManager {

    private static final RootManager INSTANCE = new RootManager();

    /**
     * Logging for exception trace
     */
    private final Logger logger = LogManager.getLogger(RootManager.class);

    /**
     * Instance of primary stage
     */
    private Stage primaryStage;

    /**
     * Paths to javafx view forms. load authorisation, registration and main scene
     */
    private final String authorisationView = "view/AuthorisationView.fxml";
    private final String registrationView = "view/RegistrationView.fxml";
    private final String mainView = "view/MainView.fxml";

    private RootManager() {
    }

    public static RootManager getInstance() {
        return INSTANCE;
    }

    /**
     * Init first primary stage.
     */
    public void initRootLayout(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setAuthorisationScene();
    }

    /**
     * set authorization scene
     */
    public void setAuthorisationScene() {
        setSpecificScene(authorisationView);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Authorisation");
    }

    /**
     * set registration scene
     */
    public void setRegistrationScene() {
        setSpecificScene(registrationView);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Registration");
    }

    /**
     * set main scene
     */
    public void setMainScene(){
        primaryStage.setTitle("Chat");
        setSpecificScene(mainView);
        primaryStage.sizeToScene();
        primaryStage.setResizable(true);
    }

    /**
     * set define scene
     *
     */
    private void setSpecificScene(String scene){
        try {
            if (primaryStage.isShowing()){
                primaryStage.close();
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource(scene)));
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            logger.error("Primary stage didn't initialize!", e);
        }
    }

    public void connectionFailed(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Server not response!");
            alert.showAndWait();
            RootManager.getInstance().getPrimaryStage().close();
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
