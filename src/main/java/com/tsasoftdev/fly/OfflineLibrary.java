package com.tsasoftdev.fly;

//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.Path;
import java.io.*;
import java.util.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.*;

public class OfflineLibrary {

   static Library lib;
     
   public static Library generateLib() {
      return lib;
   }

   public static void addDocument(/*String docname, String serial, String sub, String[] tg, String plaintext*/ FlyDocument f) {
      lib = loadJSON();   
      lib.addDocument(f /*docname, serial, sub, tg, plaintext*/);
   }
   
   private static Library loadJSON() {
      Library l = null;
      
      try {
         String direct = defaultDirectory() + "\\Fly\\data.json";
         File ff = new File(direct);
         if(!ff.exists()) {
            ff.createNewFile();
            return new Library();   
         } 
         else if (ff.length() == 0) {
            ff.delete();
            ff.createNewFile();
            return new Library();
         }
         else {
            String text = new Scanner(new File(direct))
               .useDelimiter("\\A").next();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            l = gson.fromJson(text, Library.class);
         } 
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return l;
   }
   
   public static String getJSONRep() {
      lib = loadJSON();
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      String json = gson.toJson(lib);
      return json;
   }
   
   public static void commitToJSON() {
      try {
         Gson gson = new GsonBuilder().setPrettyPrinting().create();
         String json = gson.toJson(lib);
         File myFoo = new File(defaultDirectory() + "\\Fly\\data.json");
         FileOutputStream fooStream = new FileOutputStream(myFoo, false); 
         byte[] myBytes = json.getBytes(); 
         fooStream.write(myBytes);
         fooStream.close();
      } 
      catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public static void sync() {
      lib = loadJSON();
   //do stuff with lib to sync with Firebase
   }
   
   private static String defaultDirectory()
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

/*class Library {
   
   @SerializedName("documents")
   public List<FlyDocument> docs = new ArrayList<FlyDocument>();
   
   public void addDocument(/*String docN, String serial, String subName, String[] tg, String plaintxt*//* FlyDocument f) {
      /*Document doc = new Document();
      doc.docName = docN;
      doc.serializedDoc = serial;
      doc.subject = subName;
      for(String t : tg) {
         doc.addTag(t);
      }
      doc.docPlainText = plaintxt;
      List<Document> ll = new ArrayList<Document>(Arrays.asList(docs));
      ll.add(doc);
      docs = ll.toArray(new Document[ll.size()]);*/
        //FlyDocument doc = new FlyDocument(plaintxt, subName, tg, docN, null, ".txt", serial);
        //List<FlyDocument> ll = new ArrayList<FlyDocument>(Arrays.asList(docs));
       // docs.add(f);
        //docs = ll.toArray(new FlyDocument[ll.size()]);
  // } 
   
  // public List<FlyDocument> fecthDocs(){
  //     return docs;
  // }
//}

/*class Document {
   @SerializedName("name")
   public String docName;
   
   @SerializedName("serializedDoc")
   public String serializedDoc;
   
   @SerializedName("subject")
   public String subject;
   
   @SerializedName("tags")
   public String[] tags;
   
   @SerializedName("text")
   public String docPlainText;
   
   public Document() {
      tags = new String[0];
   }
   
   public void addTag(String t) {
      List<String> ll = new ArrayList<String>(Arrays.asList(tags));
      ll.add(t);
      tags = ll.toArray(new String[ll.size()]);
   }
}*/

