package com.tjtsa.tsasoftdev;

import java.io.File;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {
    
    private Core c;
    
    private FileChooser uploader;
    
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
    private void uploadAction(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        File file = uploader.showOpenDialog(stage);
        if (file != null) {
            System.out.println(file);
        }
    }
    
    
    @FXML
    private void logOutButton(ActionEvent event) throws IOException {
        System.out.println("logging out...");
        c.goToScene("AuthScene",(Stage)((Node) event.getSource()).getScene().getWindow());
    }
    
    private void updateSubjects(TitledPane newPane){
        subjects.getPanes().add(newPane);
        //System.out.println(subjects.getPanes());
    }
    
    @FXML
    private void addSubject(ActionEvent event){
        updateSubjects(new TitledPane(subjectName.getText(), new AnchorPane()));
        subjectName.setText("");
    }
    
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
        c = new Core();
        initRecents();
        uploader = new FileChooser();
        uploader.setTitle("Choose file to upload...");
        //LOAD ALL RELEVANT INFO UPON ENTERING.
        
    }    
}
