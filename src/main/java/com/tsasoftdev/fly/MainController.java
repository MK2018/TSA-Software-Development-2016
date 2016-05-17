package com.tsasoftdev.fly;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {
    
    private Core c;
    
    private FileChooser uploader;
    
    private List<FlyDocument> res;
    private int sLoc;
    private int aLoc;
    
    private Map<AnchorPane, String> paneRefs;
    
    private String docPath;
    
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
    private AnchorPane srchResultsPane;
    @FXML
    private AnchorPane srchInitPane;
    @FXML
    private Button srchNextButton;
    @FXML
    private Button srchPrevButton;
    
    @FXML
    private Label srchCap1;
    @FXML
    private Label srchCap2;
    @FXML
    private Label srchCap3;
    @FXML
    private Label srchCap4;
    @FXML
    private Label srchCap5;
    
    @FXML
    private ImageView srchImg1;
    @FXML
    private ImageView srchImg2;
    @FXML
    private ImageView srchImg3;
    @FXML
    private ImageView srchImg4;
    @FXML
    private ImageView srchImg5;
    
    
    @FXML
    private Label allCap1;
    @FXML
    private Label allCap2;
    @FXML
    private Label allCap3;
    @FXML
    private Label allCap4;
    @FXML
    private Label allCap5;
    
    @FXML
    private ImageView allImg1;
    @FXML
    private ImageView allImg2;
    @FXML
    private ImageView allImg3;
    @FXML
    private ImageView allImg4;
    @FXML
    private ImageView allImg5;
    
    @FXML
    private Button allNextButton;
    @FXML
    private Button allPrevButton;
    
    @FXML
    private Button newSrchButton;
    
    
    @FXML
    private Tab docTab;
    @FXML
    private Label currentDocTitle;
    @FXML
    private Label docTagLabel;
   
    @FXML
    private Button openFileInDesktop;
    
    @FXML
    private TextField oldEmail;
    @FXML
    private TextField newEmail;
    @FXML
    private TextField passEmail;
    
    @FXML
    private PasswordField oldPass;
    @FXML
    private PasswordField newPass;
    @FXML
    private PasswordField emailPass;
    
    @FXML
    private Label errSettingLabel;
    
    @FXML
    private void emailUpdate(ActionEvent e){
        Core.ref.changeEmail(oldEmail.getText(), emailPass.getText(), newEmail.getText(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Platform.runLater(new Runnable(){

                    @Override
                    public void run() {
                        errSettingLabel.setText("Email successfully changed.");
                    }
                    
                });
                
            }
            @Override
            public void onError(final FirebaseError fe) {
                
                Platform.runLater(new Runnable(){

                    @Override
                    public void run() {
                        errSettingLabel.setText(fe.getMessage());
                    }
                    
                });
            }
        });
    }
    
    @FXML
    private void passwordUpdate(ActionEvent e){
        Core.ref.changePassword(passEmail.getText(), oldPass.getText(), newPass.getText(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Platform.runLater(new Runnable(){

                    @Override
                    public void run() {
                        errSettingLabel.setText("Password successfully changed.");
                    }
                    
                });
            }
            @Override
            public void onError(final FirebaseError fe) {
                Platform.runLater(new Runnable(){

                    @Override
                    public void run() {
                        errSettingLabel.setText(fe.getMessage());
                    }
                    
                });
            }
        });
    }
    
    
    @FXML
    private void openFileInDesktop(ActionEvent e) throws IOException{
        Desktop d = Desktop.getDesktop();
        
        d.open(new File(docPath));
    }
    
    @FXML
    private void openDocument(MouseEvent e){
        AnchorPane tmp = (AnchorPane) e.getSource();
        FlyDocument doc = Core.uuidToDoc.get(paneRefs.get(tmp));
        if(doc != null){
            currentDocTitle.setText(doc.getName() + " - " + doc.getSubject());
            List<String> tempTags = new ArrayList<String>();
            tempTags.addAll(Arrays.asList(doc.getTags()));
            ArrayList<String> bad = new ArrayList<String>();
            bad.add("");
            tempTags.removeAll(bad);
            String tagString = tempTags.toString().replace(", ", "\n\u2022 ");
            docTagLabel.setText("\u2022 " + tagString.substring(1, tagString.length()-1));          
            String tmpSerializedForm = doc.getSerializedForm();
            String tmpPath = doc.getUuid() + "." +  doc.getExtension();
            //if(doc.getDocState())
            //    tmpPath += ".docx";
            //else
            //    tmpPath += ".doc";
            docPath = DocumentSerializer.unserialize(tmpSerializedForm, tmpPath);
            docTab.setDisable(false);
            tabpane.getSelectionModel().select(docTab);
        }        
    }
    
    private void displayAllDocs(){
        final ArrayList<Label> allLabels = new ArrayList<>(Arrays.asList(allCap1, allCap2, allCap3, allCap4, allCap5));
        final ArrayList<ImageView> allPhotos = new ArrayList<>(Arrays.asList(allImg1, allImg2, allImg3, allImg4, allImg5));
        Platform.runLater(new Runnable(){

            @Override
            public void run() {
                while(!Core.allDocsLoaded){
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(aLoc == 0)
                    allPrevButton.setDisable(true);
                if(aLoc+5 < Core.allDocs.size())
                    allNextButton.setDisable(false);
                displayDocuments(allLabels, allPhotos, aLoc, Core.allDocs);
            }
            
        });
    }
    
    @FXML
    private void newSearchAction(ActionEvent event){
        searchBar.setText("");
        srchInitPane.setVisible(true);
        srchResultsPane.setVisible(false);
        final ArrayList<Label> srchLabels = new ArrayList<>(Arrays.asList(srchCap1, srchCap2, srchCap3, srchCap4, srchCap5));
        final ArrayList<ImageView> srchPhotos = new ArrayList<>(Arrays.asList(srchImg1, srchImg2, srchImg3, srchImg4, srchImg5));
        for(Label l: srchLabels)
            l.setText("Loading...");
        for(ImageView i: srchPhotos)
            i.setImage(new Image(getClass().getResource("/imgs/l.png").toString()));
    }
    
    @FXML
    private void allNextFive(ActionEvent event){
        aLoc += 5;
        if(aLoc != 0)
            allPrevButton.setDisable(false);
        if(aLoc+5 >= Core.allDocs.size())
            allNextButton.setDisable(true);
        displayDocuments(new ArrayList<>(Arrays.asList(allCap1, allCap2, allCap3, allCap4, allCap5)), new ArrayList<>(Arrays.asList(allImg1, allImg2, allImg3, allImg4, allImg5)), aLoc, Core.allDocs);
    }
    
    @FXML
    private void allPrevFive(ActionEvent event){
        aLoc -= 5;
        if(aLoc == 0)
            allPrevButton.setDisable(true);
        if(aLoc+5 < Core.allDocs.size())
            allNextButton.setDisable(false);
        displayDocuments(new ArrayList<>(Arrays.asList(allCap1, allCap2, allCap3, allCap4, allCap5)), new ArrayList<>(Arrays.asList(allImg1, allImg2, allImg3, allImg4, allImg5)), aLoc, Core.allDocs);
    }
    
    @FXML
    private void searchNextFive(ActionEvent event){
        sLoc += 5;
        if(sLoc != 0)
            srchPrevButton.setDisable(false);
        if(sLoc+5 >= res.size())
            srchNextButton.setDisable(true);
        displayDocuments(new ArrayList<>(Arrays.asList(srchCap1, srchCap2, srchCap3, srchCap4, srchCap5)), new ArrayList<>(Arrays.asList(srchImg1, srchImg2, srchImg3, srchImg4, srchImg5)), sLoc, res);
    }
    
    @FXML
    private void searchPrevFive(ActionEvent event){
        sLoc -= 5;
        if(sLoc == 0)
            srchPrevButton.setDisable(true);
        if(sLoc+5 < res.size())
            srchNextButton.setDisable(false);
        displayDocuments(new ArrayList<>(Arrays.asList(srchCap1, srchCap2, srchCap3, srchCap4, srchCap5)), new ArrayList<>(Arrays.asList(srchImg1, srchImg2, srchImg3, srchImg4, srchImg5)), sLoc, res);
    }
    
    private void displayDocuments(ArrayList<Label> labels, ArrayList<ImageView> imgs, int loc, List<FlyDocument> docs){
        ArrayList<Label> dispLabels = labels;
        ArrayList<ImageView> dispPhotos = imgs;
        List<FlyDocument> lst = docs;
        int count = 0;
        for(int i = loc; i < loc+5; i++){  
            if(i < lst.size()){
                dispLabels.get(count).setText(lst.get(i).getName());
                dispPhotos.get(count).setImage(new Image(getClass().getResource("/imgs/"+lst.get(i).getSubject().substring(0, 1).toLowerCase()+".png").toString()));
                paneRefs.put((AnchorPane)(dispLabels.get(count).getParent()), lst.get(i).getUuid());
            }
            else if(count < 5){
                dispLabels.get(count).setText("No more results!");
                dispPhotos.get(count).setImage(new Image(getClass().getResource("/imgs/n.png").toString()));
                paneRefs.put((AnchorPane)(dispLabels.get(count).getParent()), "NULL");
            }
            count++;
        }
    }
    
    @FXML
    private void searchAction(ActionEvent event) throws IOException{
        final ArrayList<Label> srchLabels = new ArrayList<>(Arrays.asList(srchCap1, srchCap2, srchCap3, srchCap4, srchCap5));
        final ArrayList<ImageView> srchPhotos = new ArrayList<>(Arrays.asList(srchImg1, srchImg2, srchImg3, srchImg4, srchImg5));
        res = new ArrayList<>();
        String searchText = searchBar.getText();
        if(searchText.trim().equals(""))
            return;
        String[] tokens = searchText.split(" ");
        for(String tok: tokens){
            for(String uuid: Core.searchKeys.keySet()){
                for(String key: Core.searchKeys.get(uuid)){
                    if(!c.stem(tok).equals("") && c.stem(tok).equalsIgnoreCase(c.stem(key))){
                        res.add(Core.uuidToDoc.get(uuid));
                    }
                }
                if(Core.uuidToDoc.get(uuid).getText().toLowerCase().contains(c.stem(tok).toLowerCase()) && !res.contains(Core.uuidToDoc.get(uuid)))
                    res.add(Core.uuidToDoc.get(uuid));
            }
        }
        
        srchInitPane.setVisible(false);
        srchResultsPane.setVisible(true);
        Platform.runLater(new Runnable(){

            @Override
            public void run() {
                sLoc = 0;
                if(sLoc == 0)
                    srchPrevButton.setDisable(true);
                if(sLoc+5 < res.size())
                    srchNextButton.setDisable(false);
                displayDocuments(new ArrayList<>(Arrays.asList(srchCap1, srchCap2, srchCap3, srchCap4, srchCap5)), new ArrayList<>(Arrays.asList(srchImg1, srchImg2, srchImg3, srchImg4, srchImg5)), sLoc, res);
            }
        });
    }
    
    @FXML
    private void uploadAction(ActionEvent event) throws IOException, InterruptedException{
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        File file = uploader.showOpenDialog(stage);
        if (file != null) {
            File toUpload = new File(file.toURI());
            Core.loadFile(toUpload);
            c.goToScene("UploadScene");
        }        
    }
    
    
    @FXML
    private void logOutButton(ActionEvent event) throws IOException {
        Core.ref.unauth();
        Core.loadFile(null);
        Core.allDocs = null;
        c.goToScene("AuthScene");
    }
    
    private void initRecents(){
        final ArrayList<Label> recLabels = new ArrayList<>(Arrays.asList(recCap1, recCap2, recCap3, recCap4, recCap5));
        final ArrayList<ImageView> recPhotos = new ArrayList<>(Arrays.asList(recImg1, recImg2, recImg3, recImg4, recImg5));
        for(Label l: recLabels){
            l.setText("Add documents to get started!");
        }
        for(ImageView i: recPhotos){
            i.setImage(new Image(getClass().getResource("/imgs/a.png").toString()));
            paneRefs.put((AnchorPane)(i.getParent()), "NULL");
        }
        Core.ref.child("users/"+Core.ref.getAuth().getUid()+"/recents").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(final DataSnapshot snap) {
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        if(!snap.getValue(String.class).equals("")){
                            List<String> recents = new ArrayList<String>();
                            recents.addAll(Arrays.asList(snap.getValue(String.class).split(",")));
                            List<FlyDocument> recDocs = new ArrayList<FlyDocument>();
                            for(String uuid: recents){
                                if(!uuid.trim().equals(""))
                                    recDocs.add(Core.uuidToDoc.get(uuid.trim()));
                            }
                            for(int i = 0; i < recDocs.size(); i++){
                                recLabels.get(i).setText(recDocs.get(i).getName());
                                recPhotos.get(i).setImage(new Image(getClass().getResource("/imgs/"+recDocs.get(i).getSubject().substring(0, 1).toLowerCase()+".png").toString()));
                                paneRefs.put((AnchorPane)(recPhotos.get(i).getParent()), recDocs.get(i).getUuid());
                            }
                        }
                    }
                });
                
            }

            @Override
            public void onCancelled(FirebaseError fe) {
                System.out.println(fe);
            }
            
        });
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Core();
        Core.getDocuments();
        res = new ArrayList<>();
        sLoc = 0;
        aLoc = 0;
        paneRefs = new HashMap<AnchorPane, String>();
        initRecents();
        srchPrevButton.setDisable(true);
        srchNextButton.setDisable(true);
        uploader = new FileChooser();
        uploader.setTitle("Choose file to upload...");
        Core.ref.child("users/"+Core.ref.getAuth().getUid()+"/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snap) {   
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        welcomeLabel.setText("Welcome to your Fly Dashboard, " + snap.getValue(String.class)+".");
                    }
                });
            }
            @Override
            public void onCancelled(FirebaseError fe) {
                System.out.println(fe);
            }
        });
        tabpane.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                    if(!newTab.equals(docTab)){
                        docTab.setDisable(true);
                        docPath = "";
                    }
                }
            }
        );
        displayAllDocs();
    }    
}
