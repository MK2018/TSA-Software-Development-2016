package com.tjtsa.tsasoftdev;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController implements Initializable {
    
    private Core c;
    
    private FileChooser uploader;
    
    private List<DocumentClass> res;
    private int sLoc;
    private int aLoc;
    
    private Map<AnchorPane, DocumentClass> paneRefs;
    
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
    private void openDocument(MouseEvent e){
        AnchorPane tmp = (AnchorPane) e.getSource();
        for(Node n: tmp.getChildren()){
            System.out.println(n);
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
                        System.out.println("waiting...");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //System.out.println(Core.allDocs);
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
        if(aLoc+5 > Core.allDocs.size())
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
        if(sLoc+5 > res.size())
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
    
    private void displayDocuments(ArrayList<Label> labels, ArrayList<ImageView> imgs, int loc, List<DocumentClass> docs){
        ArrayList<Label> dispLabels = labels;//new ArrayList<>(Arrays.asList(srchCap1, srchCap2, srchCap3, srchCap4, srchCap5));
        ArrayList<ImageView> dispPhotos = imgs;//new ArrayList<>(Arrays.asList(srchImg1, srchImg2, srchImg3, srchImg4, srchImg5));
        List<DocumentClass> lst = docs;
        int count = 0;
        for(int i = loc; i < loc+5; i++){  
            if(i < lst.size()){
                dispLabels.get(count).setText(lst.get(i).getName());
                dispPhotos.get(count).setImage(new Image(getClass().getResource("/imgs/"+lst.get(i).getSubject().substring(0, 1).toLowerCase()+".png").toString()));
            }
            else if(count < 5){
                dispLabels.get(count).setText("No more results!");
                dispPhotos.get(count).setImage(new Image(getClass().getResource("/imgs/n.png").toString()));
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
        //System.out.println("logging out...");
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
        }
        Core.ref.child("users/"+Core.ref.getAuth().getUid()+"/recents").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(final DataSnapshot snap) {
                //System.out.println(snap.getValue(String.class));
                Platform.runLater(new Runnable(){

                    @Override
                    public void run() {
                        if(!snap.getValue(String.class).equals("")){
                            List<String> recents = new ArrayList<String>();
                            recents.addAll(Arrays.asList(snap.getValue(String.class).split(",")));
                            List<DocumentClass> recDocs = new ArrayList<DocumentClass>();
                            for(String uuid: recents){
                                if(!uuid.trim().equals(""))
                                    recDocs.add(Core.uuidToDoc.get(uuid.trim()));
                            }
                            //System.out.println(recDocs);
                            for(int i = 0; i < recDocs.size(); i++){
                                recLabels.get(i).setText(recDocs.get(i).getName());
                                recPhotos.get(i).setImage(new Image(getClass().getResource("/imgs/"+recDocs.get(i).getSubject().substring(0, 1).toLowerCase()+".png").toString()));
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
        paneRefs = new HashMap<AnchorPane, DocumentClass>();
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
        displayAllDocs();
    }    
}
