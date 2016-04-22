package com.tjtsa.tsasoftdev;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainScreen = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
        Parent assignmentScreen = FXMLLoader.load(getClass().getResource("/fxml/AssignmentScene.fxml"));
        
        Scene mainScene = new Scene(mainScreen);
        mainScene.getStylesheets().add("/styles/MainStyles.css");
        
        Scene assignmentScene = new Scene(assignmentScreen);
        mainScene.getStylesheets().add("/styles/AssignmentStyles.css");
        
        stage.setTitle("TSA Software Development");
        stage.setScene(mainScene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
