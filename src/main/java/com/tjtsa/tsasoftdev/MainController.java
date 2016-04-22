package com.tjtsa.tsasoftdev;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        System.out.println("You clicked me!");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Parent AssignmentParent = FXMLLoader.load(getClass().getResource("/fxml/AssignmentScene.fxml"));
        Scene AssignmentScene = new Scene(AssignmentParent);
        //stage.hide(); //optional
        stage.setScene(AssignmentScene);
        stage.show(); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
