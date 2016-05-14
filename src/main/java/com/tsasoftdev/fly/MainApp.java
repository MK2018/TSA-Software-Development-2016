package com.tsasoftdev.fly;

import com.firebase.client.Firebase;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
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

        Parent authScreen = FXMLLoader.load(getClass().getResource("/fxml/AuthScene.fxml"));
        
        Scene authScene = new Scene(authScreen);
        authScene.getStylesheets().add("/styles/MainStyles.css");
        
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        if(screenBounds.getHeight() < 800)
            stage.setHeight(screenBounds.getHeight());
        
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

    public static void main(String[] args) {
        launch(args);
    }

}