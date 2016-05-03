package com.tjtsa.tsasoftdev;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author TSARegionals
 */
public class AuthSceneController implements Initializable {

    private Core c;
    
    //private Firebase auth;
    
    @FXML
    Button backButton1;
    
    @FXML
    TextField signInEmail;
    @FXML
    TextField signUpEmail;
    @FXML
    TextField signUpName;
    @FXML
    PasswordField signInPass;
    @FXML
    PasswordField signUpPass;
    @FXML
    Label loginErrMessage;
    @FXML
    Label signupErrMessage;
    @FXML
    ProgressIndicator progress;
    
    @FXML
    private void signInButton(ActionEvent event) throws IOException {
        String email = signInEmail.getText();
        String pass = signInPass.getText();
        signInPass.getStyleClass().clear();
        signInEmail.getStyleClass().clear();
        signInPass.getStyleClass().addAll("text-input", "text-field", "password-field");
        signInEmail.getStyleClass().addAll("text-input", "text-field");
        loginErrMessage.setText("");
        progress.setVisible(true);
        Core.ref.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            c.goToScene("MainScene");
                        } catch (IOException ex) {
                            Logger.getLogger(UploadSceneController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
            @Override
            public void onAuthenticationError(final FirebaseError firebaseError) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisible(false);
                        loginErrMessage.setText(firebaseError.getMessage());
                        if(firebaseError.getMessage().equals("The specified password is incorrect.")){
                            signInPass.getStyleClass().add("incorrect-field");
                        }
                        else if(firebaseError.getMessage().equals("The specified user does not exist.")){
                            signInEmail.getStyleClass().add("incorrect-field");
                        }
                        else if(firebaseError.getMessage().equals("The specified email address is invalid.")){
                            signInEmail.getStyleClass().add("incorrect-field");
                        }
                    }
                });
            }
        });
    }
    
    @FXML
    private void signUpButton(ActionEvent event){
        final String email = signUpEmail.getText();
        final String pass = signUpPass.getText();
        final String name = signUpName.getText();
        progress.setVisible(true);
        Core.ref.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                //System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Core.ref.child("users").child((String) result.get("uid")).child("name").setValue(name);
                Core.ref.child("users").child((String) result.get("uid")).child("uploads").child("History").setValue("");
                Core.ref.child("users").child((String) result.get("uid")).child("uploads").child("Math").setValue("");
                Core.ref.child("users").child((String) result.get("uid")).child("uploads").child("Computer Science").setValue("");
                Core.ref.child("users").child((String) result.get("uid")).child("uploads").child("English").setValue("");
                Core.ref.child("users").child((String) result.get("uid")).child("uploads").child("Science").setValue("");
                Core.ref.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    c.goToScene("MainScene");
                                } catch (IOException ex) {
                                    Logger.getLogger(UploadSceneController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }
                    @Override
                    public void onAuthenticationError(final FirebaseError firebaseError) {
                        /*Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                loginErrMessage.setText(firebaseError.getMessage());
                                if(firebaseError.getMessage().equals("The specified password is incorrect.")){
                                    signInPass.getStyleClass().add("incorrect-field");
                                }
                                else if(firebaseError.getMessage().equals("The specified user does not exist.")){
                                    signInEmail.getStyleClass().add("incorrect-field");
                                }
                                else if(firebaseError.getMessage().equals("The specified email address is invalid.")){
                                    signInEmail.getStyleClass().add("incorrect-field");
                                }
                            }
                        });*/
                        progress.setVisible(false);
                        System.out.println(firebaseError);
                    }
                });
            }
            @Override
            public void onError(final FirebaseError firebaseError) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisible(false);
                        signupErrMessage.setText(firebaseError.getMessage());
                    }
                });
            }
        });
    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Core();
        //auth = new Firebase("https://tsaparser.firebaseio.com/");
    }    
    
}
