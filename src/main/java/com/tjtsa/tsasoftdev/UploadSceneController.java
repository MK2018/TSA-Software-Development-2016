package com.tjtsa.tsasoftdev;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import com.tjtsa.tsasoftdev.Core.IdentOutput;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author TSARegionals
 */
public class UploadSceneController implements Initializable {
    
    private Core c;
    
    private IdentOutput data;
    
    @FXML
    Label subjectLabel;
    @FXML
    Label tagLabel;
    
    @FXML
    AnchorPane loadingPane;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Core();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    data = c.initAnalysis();
                    subjectLabel.setText(data.getSubject());
                    tagLabel.setText(data.getTags().toString());
                    loadingPane.setVisible(false);
                } catch (Exception ex) {
                }
            }
        });
    }    
    
}
