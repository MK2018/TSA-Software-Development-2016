package com.tjtsa.tsasoftdev;

import com.firebase.client.Firebase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import com.tjtsa.tsasoftdev.Core.IdentOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.apache.commons.lang3.StringUtils;

/**
 * FXML Controller class
 *
 * @author TSARegionals
 */
public class UploadSceneController implements Initializable {
    
    private Core c;
    
    private IdentOutput data;
    
    private String origSubj;
    
    @FXML
    Label subjectLabel;
    
    @FXML
    AnchorPane loadingPane;
    
    @FXML
    ChoiceBox subjectChoiceBox;
    
    @FXML
    Button cancelButton;
    @FXML
    Button finalUploadButton;
    
    @FXML
    private TextField tagBox1;
    @FXML
    private TextField tagBox2;
    @FXML
    private TextField tagBox3;
    @FXML
    private TextField tagBox4;
    @FXML
    private TextField tagBox5;
    
    @FXML
    private TextField fileNameField;
    
    @FXML
    private Label filenameHintText;
    
    
    @FXML
    public void cancelUpload(ActionEvent event) throws IOException{
        Core.loadFile(null);
        c.goToScene("MainScene");
    }
    
    @FXML
    public void finalUpload(ActionEvent event) throws IOException{
        
        class dummyClass{
            private String docText;
            private String docSubject;
            private String[] docTags;
            private String docName;
            
            public dummyClass(){}
            
            public dummyClass(String fullText, String subject, String[] tags, String name){
                this.docText = fullText;
                this.docSubject = subject;
                this.docTags = tags;
                this.docName = name;
            }
            
            public String getText(){
                return docText;
            }
            
            public String getSubject(){
                return docSubject;
            }
            
            public String[] getTags(){
                return docTags;
            }
            
            public String getName(){
                return docName;
            }
        }
        
        //GET THE USER ID THING WORKING
        
        Firebase uploader = new Firebase("https://tsaparser.firebaseio.com/users/testingUser/uploads");
        
        
        String[] finalTags = new String[5];
        finalTags[0] = tagBox1.getText();
        finalTags[1] = tagBox2.getText();
        finalTags[2] = tagBox3.getText();
        finalTags[3] = tagBox4.getText();
        finalTags[4] = tagBox5.getText();
        if(fileNameField.getText().trim().equals("")||StringUtils.containsAny(fileNameField.getText(), ".$#[]/")){
            fileNameField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            filenameHintText.setText("Document name must not be empty or have the following characters: . $ # [ ] /");
        }
        else{
            uploader.child(subjectLabel.getText()).child(fileNameField.getText()).setValue(new dummyClass(Core.getCurrentFileText(), subjectLabel.getText(), finalTags, fileNameField.getText()));
            //do other stuff here, possibly show loading screen.
            if(!subjectLabel.getText().equals(origSubj)){
                c.teachAlgorithm(subjectLabel.getText(), finalTags);
            }
            c.goToScene("MainScene");
        }        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Core();
        subjectChoiceBox.setItems(FXCollections.observableArrayList("History", "English", "Science", "Math", "Computer Science"));
        subjectChoiceBox.getSelectionModel().selectedItemProperty().addListener(new
            ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                subjectLabel.setText(subjectChoiceBox.getValue().toString());
            }
        });
        final List<TextField> tagFields = new ArrayList<TextField>();
        tagFields.addAll(Arrays.asList(tagBox1, tagBox2, tagBox3, tagBox4, tagBox5));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    data = c.initAnalysis();
                    subjectLabel.setText(data.getSubject());
                    origSubj = data.getSubject();
                    subjectChoiceBox.setValue(data.getSubject());
                    for(int i = 0; i < tagFields.size(); i++){
                        tagFields.get(i).setText(data.getTags().get(i));
                    }
                    //tagLabel.setText(data.getTags().toString().replace("[", "\u2022 ").replace("]", "").replace(", ", "\n\u2022 "));
                    //loadingPane.setVisible(false);
                } catch (IOException | InterruptedException ex) {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }
            }
        });
    }    
    
}
