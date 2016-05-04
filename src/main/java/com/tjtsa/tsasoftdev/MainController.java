package com.tjtsa.tsasoftdev;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    
    //Uploading Stuff
    @FXML
    private Label loadingLabel;
    @FXML
    private Button uploadButton;
    @FXML
    private Label messageLabel;
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private TextField searchBar;
    
    @FXML
    private void searchAction(ActionEvent event) throws IOException{
        String searchText = searchBar.getText();
        String[] tokens = searchText.split(" ");
        List<DocumentClass> res = new ArrayList<DocumentClass>();
        for(String tok: tokens){
            for(String uuid: Core.searchKeys.keySet()){
                for(String key: Core.searchKeys.get(uuid)){
                    if(!c.stem(tok).equals("") && c.stem(tok).equalsIgnoreCase(c.stem(key))){
                        res.add(Core.uuidToDoc.get(uuid));
                    }
                }
            }
        }
        System.out.println(res);
    }
    
    @FXML
    private void uploadAction(ActionEvent event) throws IOException, InterruptedException{
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        File file = uploader.showOpenDialog(stage);
        //System.out.println(file);
        if (file != null) {
            File toUpload = new File(file.toURI());
            Core.loadFile(toUpload);
            c.goToScene("UploadScene");
        }
        //loadingLabel.setVisible(true);
        //uploadButton.setVisible(false);
        //messageLabel.setVisible(false);
        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    c.initAnalysis();
                } catch (Exception ex) {
                }
            }
        });*/
        
    }
    
    
    @FXML
    private void logOutButton(ActionEvent event) throws IOException {
        //System.out.println("logging out...");
        Core.ref.unauth();
        Core.loadFile(null);
        Core.allDocs = null;
        c.goToScene("AuthScene");
    }
    
    /*private void updateSubjects(TitledPane newPane){
        subjects.getPanes().add(newPane);
        //System.out.println(subjects.getPanes());
    }*/
    
    /*@FXML
    private void addSubject(ActionEvent event){
        updateSubjects(new TitledPane(subjectName.getText(), new AnchorPane()));
        subjectName.setText("");
    }*/
    
    private void initRecents(){
        final ArrayList<Label> recLabels = new ArrayList<>(Arrays.asList(recCap1, recCap2, recCap3, recCap4, recCap5));
        final ArrayList<ImageView> recPhotos = new ArrayList<>(Arrays.asList(recImg1, recImg2, recImg3, recImg4, recImg5));
        for(Label l: recLabels){
            l.setText("Add documents to get started!");
        }
        for(ImageView i: recPhotos){
            i.setImage(new Image(getClass().getResource("/imgs/a.png").toString()));
        }
        Core.ref.child("users/"+Core.ref.getAuth().getUid()+"/recents").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot snap) {
                //System.out.println(snap.getValue(String.class));
                if(!snap.getValue(String.class).equals("")){
                    List<String> recents = new ArrayList<String>();
                    recents.addAll(Arrays.asList(snap.getValue(String.class).split(",")));
                    List<DocumentClass> recDocs = new ArrayList<DocumentClass>();
                    for(String uuid: recents){
                        recDocs.add(Core.uuidToDoc.get(uuid));
                    }
                    //System.out.println(recDocs.size());
                    for(int i = 0; i < recDocs.size(); i++){
                        recLabels.get(i).setText(recDocs.get(i).getName());
                        recPhotos.get(i).setImage(new Image(getClass().getResource("/imgs/"+recDocs.get(i).getSubject().substring(0, 1).toLowerCase()+".png").toString()));
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError fe) {
                
            }
            
        });
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Core();
        Core.getDocuments();
        initRecents();
        uploader = new FileChooser();
        uploader.setTitle("Choose file to upload...");
        Core.ref.child("users/"+Core.ref.getAuth().getUid()+"/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snap) {   
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //nameTagLabel.setText("Logged in as \n" + snap.getValue(String.class));
                        welcomeLabel.setText("Welcome to your Fly Dashboard, " + snap.getValue(String.class)+".");
                    }
                });
            }
            @Override
            public void onCancelled(FirebaseError fe) {
                System.out.println(fe);
            }
        });
    }    
}
