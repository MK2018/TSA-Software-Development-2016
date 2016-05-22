package com.tsasoftdev.fly;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

//import com.tsasoftdev.fly.Core.IdentOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.apache.commons.lang3.StringUtils;

public class UploadSceneController implements Initializable {
    
    private Core c;
    
    //private IdentOutput data;
    
    private FlyResult fr;
    
    private String origSubj;
    
    @FXML
    Label subjectLabel;
    
    @FXML
    AnchorPane loadingPane;
    @FXML
    Label messageLabel;
    
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
        //Core.loadFile(null);
        c.goToScene("MainScene");
    }
    
    @FXML
    public void finalUpload(ActionEvent event) throws IOException{      
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
            String uuid = UUID.randomUUID().toString();
            //Core.ref.child("users/"+Core.ref.getAuth().getUid()+"/uploads").child(subjectLabel.getText()).child(fileNameField.getText()).setValue(new FlyDocument(Core.getCurrentFileText(), subjectLabel.getText(), finalTags, fileNameField.getText(), uuid, Core.docExtension, Core.serializedDoc));
            Core.ref.child("users").child(Core.ref.getAuth().getUid()).child("uploads").child(subjectLabel.getText()).child(fileNameField.getText()).setValue(new FlyDocument(fr.getText(), subjectLabel.getText(), finalTags, fileNameField.getText(), uuid, fr.getExt(), DocumentSerializer.serialize(fr.getPath())));
            Core.getDocuments();
            if(!subjectLabel.getText().equals(origSubj)){
                c.teachAlgorithm(subjectLabel.getText(), finalTags);
            }
            Core.updateRecents(uuid);
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        messageLabel.setText("Success!");
                        loadingPane.setStyle("-fx-background-color: #00cc66;");
                        loadingPane.setVisible(true);
                        TimeUnit.MILLISECONDS.sleep(1750);
                        //Core.loadFile(null);
                        c.goToScene("MainScene");
                    } catch (InterruptedException | IOException ex) {
                        Logger.getLogger(UploadSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadingPane.toFront();
        c = new Core();
        subjectChoiceBox.setItems(FXCollections.observableArrayList("History", "English", "Science", "Math", "Computer Science"));
        subjectChoiceBox.getSelectionModel().selectedItemProperty().addListener(new
            ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                subjectLabel.setText(subjectChoiceBox.getValue().toString());
            }
        });
        
        /*Platform.runLater(new Runnable(){
            @Override
            public void run() {
                //System.out.println(AsyncParser.fetchText());
                
            }
        });
        */
        
        
        final List<TextField> tagFields = new ArrayList<TextField>();
        tagFields.addAll(Arrays.asList(tagBox1, tagBox2, tagBox3, tagBox4, tagBox5));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //try {
                    //data = c.initAnalysis();
                    //subjectLabel.setText(data.getSubject());
                    //origSubj = data.getSubject();
                    //subjectChoiceBox.setValue(data.getSubject());
                    
                fr = AsyncParser.fetchResult();
                
                origSubj = fr.getSubject();
                subjectLabel.setText(origSubj);                
                subjectChoiceBox.setValue(origSubj);
                
                
                for(int i = 0; i < tagFields.size(); i++){
                    tagFields.get(i).setText(fr.getTags().get(i));
                }
                loadingPane.setVisible(false);
                //} catch (IOException | InterruptedException ex) {
                //    System.out.println(Arrays.toString(ex.getStackTrace()));
                //}
            }
        });
    }    
    
}
