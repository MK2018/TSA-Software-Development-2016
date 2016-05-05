package com.tjtsa.tsasoftdev;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.*;
import org.apache.commons.codec.binary.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class DocumentSerializer {
   
   /*public static void main(String[] args) {
      String boy = "";
      JFileChooser fileChooser = new JFileChooser();
      int returnValue = fileChooser.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
         File selectedFile = fileChooser.getSelectedFile();
         boy = (selectedFile.getAbsolutePath());
      }
      String base64 = DocumentSerializer.serialize(boy);
      System.out.println(base64);
      DocumentSerializer.unserialize(base64, "Test.docx");
   }*/
   
   public static String serialize(String filePath) {
      File document = new File(filePath);
      byte[] data = null;
      try {
         Path path = document.toPath();
         data = Files.readAllBytes(path);
         System.out.println(filePath);
      } 
      catch (IOException e) {
         e.printStackTrace();
      }
      String base64Rep = Base64.encodeBase64String(data);
      return base64Rep;
   }
   
   public static String unserialize(String base64, String filename) {
      try {
         File file = new File(defaultDirectory() + "\\Fly");
         if (!file.exists()) {
            file.mkdir();
         }
         byte[] data = Base64.decodeBase64(base64);
         //System.out.println(defaultDirectory());
         //System.out.println("FILENAME: " +  filename);
         FileOutputStream fos = new FileOutputStream(defaultDirectory() + "\\Fly\\" + filename);
         //System.out.println(fos.toString());
         fos.write(data);
         fos.close();
         return fos.toString();
      } 
      catch (Exception e) {
         e.printStackTrace();
      }
      return "";
   } 
   
   public static String defaultDirectory()
   {
      String OS = System.getProperty("os.name").toUpperCase();
      //System.out.println(OS);
      if (OS.contains("WIN"))
         return System.getenv("APPDATA");
      else if (OS.contains("MAC"))
         return System.getProperty("user.home") + "/Library/Application "
                + "Support";
      else if (OS.contains("NUX"))
         return System.getProperty("user.home");
      return System.getProperty("user.dir");
   }

}