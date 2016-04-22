package com.tjtsa.tsasoftdev;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
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
    
    
    //RECENT BAR CAPTIONS
    @FXML
    private Label recCap1;
    @FXML
    private Label recCap2;
    @FXML
    private Label recCap3;
    @FXML
    private Label recCap4;
    @FXML
    private Label recCap5;
    
    //RECENT BAR IMGS
    @FXML
    private ImageView recImg1;
    @FXML
    private ImageView recImg2;
    @FXML
    private ImageView recImg3;
    @FXML
    private ImageView recImg4;
    @FXML
    private ImageView recImg5;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        System.out.println("logging out...");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Parent AuthParent = FXMLLoader.load(getClass().getResource("/fxml/AuthScene.fxml"));
        Scene authScene = new Scene(AuthParent);
        authScene.getStylesheets().add("/styles/MainStyles.css");
        //stage.hide(); //optional
        stage.setScene(authScene);
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
    
    @SuppressWarnings("empty-statement")
    private void initRecents(){
        ArrayList<Label> recLabels = new ArrayList<>(Arrays.asList(recCap1, recCap2, recCap3, recCap4, recCap5));
        ArrayList<ImageView> recPhotos = new ArrayList<>(Arrays.asList(recImg1, recImg2, recImg3, recImg4, recImg5));
        for(Label l: recLabels){
            //SOMEHOW FETCH FIVE MOST RECENT TITLES OF DOCUMENTS FROM SERVER
            //SET THOSE TO THE LABELS HERE
        }
        for(ImageView i: recPhotos){
            //SOMEHOW FETCH FIVE MOST RECENT SUBJECTS OF DOCUMENTS FROM SERVER
            //SET THOSE TO THE IMAGES HERE
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initRecents();
        //LOAD ALL RELEVANT INFO UPON ENTERING.
        
    }    
}
