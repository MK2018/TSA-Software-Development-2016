package com.tjtsa.tsasoftdev;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import com.tjtsa.tsasoftdev.Core.IdentOutput;
import java.io.IOException;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    
    @FXML
    ChoiceBox subjectChoiceBox;
    
    @FXML
    Button cancelButton;
    @FXML
    Button finalUploadButton;
    
    @FXML
    public void cancelUpload(ActionEvent event) throws IOException{
        Core.loadFile(null);
        c.goToScene("MainScene");
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Core();
        subjectChoiceBox.setItems(FXCollections.observableArrayList("History", "English", "Science", "Math", "Computer Science"));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    data = c.initAnalysis();
                    subjectLabel.setText(data.getSubject());
                    subjectChoiceBox.setValue(data.getSubject());
                    tagLabel.setText(data.getTags().toString().replace("[", "\u2022 ").replace("]", "").replace(", ", "\n\u2022 "));
                    //loadingPane.setVisible(false);
                } catch (IOException | InterruptedException ex) {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }
            }
        });
    }    
    
}
