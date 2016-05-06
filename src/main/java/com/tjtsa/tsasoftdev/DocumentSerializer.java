package com.tjtsa.tsasoftdev;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
import org.apache.commons.codec.binary.*;
import java.io.File;

public class DocumentSerializer {
   
  
   public static String serialize(String filePath) {
      File document = new File(filePath);
      byte[] data = null;
      try {
         Path path = document.toPath();
         data = Files.readAllBytes(path);
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
         FileOutputStream fos = new FileOutputStream(defaultDirectory() + "\\Fly\\" + filename);
         fos.write(data);
         fos.close();
         return (defaultDirectory() + "\\Fly\\" + filename);
      } 
      catch (Exception e) {
         e.printStackTrace();
      }
      return "";
   } 
   
   public static String defaultDirectory()
   {
      String OS = System.getProperty("os.name").toUpperCase();
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