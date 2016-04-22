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
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController implements Initializable {
        
    @FXML
    private TabPane tabpane;
    @FXML
    private Tab recentTab;
    @FXML
    private Tab uploadTab;
    @FXML
    private Accordion subjects;
    @FXML
    private Button addNewSubject;
    @FXML
    private TextField subjectName;
 
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        System.out.println("logging out...");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Parent AssignmentParent = FXMLLoader.load(getClass().getResource("/fxml/AuthScene.fxml"));
        Scene AssignmentScene = new Scene(AssignmentParent);
        //stage.hide(); //optional
        stage.setScene(AssignmentScene);
        stage.show(); 
    }
    
    private void updateSubjects(TitledPane newPane){
        subjects.getPanes().add(newPane);
        System.out.println(subjects.getPanes());
    }
    
    @FXML
    private void addSubject(ActionEvent event){
        updateSubjects(new TitledPane(subjectName.getText(), new AnchorPane()));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //LOAD ALL RELEVANT INFO UPON ENTERING.
        
    }    
}
