package com.tsasoftdev.fly;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Library {
   @SerializedName("documents")
   public List<FlyDocument> docs = new ArrayList<FlyDocument>();
   
   public void addDocument(FlyDocument f) {
        docs.add(f);
   } 
   
   public List<FlyDocument> fetchDocs(){
       return docs;
   }
   
   public String toString(){
       return docs.toString();
   }
}
