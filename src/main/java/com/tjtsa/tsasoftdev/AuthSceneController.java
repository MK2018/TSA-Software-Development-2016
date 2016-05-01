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
public class AuthSceneController implements Initializable {

    private Core c;
    
    @FXML
    Button backButton1;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        //System.out.println("starting...");
        c.goToScene("MainScene");
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Core();
    }    
    
}
