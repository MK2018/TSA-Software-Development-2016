/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tjtsa.tsasoftdev;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author TJ TSA
 */
public class Core {
    
    public void goToScene(String toSceneName, Stage ctx) throws IOException{
        Parent toParent = FXMLLoader.load(getClass().getResource("/fxml/"+toSceneName+".fxml"));
        Scene toScene = new Scene(toParent);
        toScene.getStylesheets().add("/styles/MainStyles.css");
        ctx.setScene(toScene);
        ctx.show();
    }
    
}
