package com.tjtsa.tsasoftdev;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author TSARegionals
 */
public class AssignmentSceneController implements Initializable {

    
    @FXML
    Button backButton1;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        System.out.println("heading back");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Parent AssignmentParent = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
        Scene AssignmentScene = new Scene(AssignmentParent);
        //stage.hide(); //optional
        stage.setScene(AssignmentScene);
        stage.show(); 
        //label.setText("It is indeed lit.");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
