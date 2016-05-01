package com.tjtsa.tsasoftdev;

import com.firebase.client.Firebase;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(
            getClass().getResource("/fonts/rcb.ttf").toExternalForm(), 
            10
        );
        Font.loadFont(
            getClass().getResource("/fonts/rcr.ttf").toExternalForm(), 
            10
        );
        Font.loadFont(
            getClass().getResource("/fonts/rcl.ttf").toExternalForm(), 
            10
        );

        //Parent mainScreen = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
        Parent authScreen = FXMLLoader.load(getClass().getResource("/fxml/AuthScene.fxml"));
        
        //Scene mainScene = new Scene(mainScreen);
        //mainScene.getStylesheets().add("/styles/MainStyles.css");
        
        Scene authScene = new Scene(authScreen);
        authScene.getStylesheets().add("/styles/MainStyles.css");
        
        stage.setTitle("TSA Software Development");
        stage.setScene(authScene);
        stage.show();
        Core.setUpStage(stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){

            @Override
            public void handle(WindowEvent event) {
                Firebase.goOffline();
            }
            
        });
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
