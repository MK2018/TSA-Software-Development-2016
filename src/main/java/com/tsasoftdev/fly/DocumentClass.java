package com.tsasoftdev.fly;

import java.util.List;

public class DocumentClass{
    private String docText;
    private String docSubject;
    private String[] docTags;
    private String docName;
    private String uuid;
    private boolean isDocx;
    private String srzDoc; //serialized Word Document
            
    public DocumentClass(){}
            
    public DocumentClass(String fullText, String subject, String[] tags, String name, String uuid, boolean isDocx, String srzDoc){
        this.docText = fullText;
        this.docSubject = subject;
        this.docTags = tags;
        this.docName = name;
        this.uuid = uuid;
        this.isDocx = isDocx;
        this.srzDoc = srzDoc;
    }
    
    public DocumentClass(String fullText, String subject, List<String> tags, String name, String uuid,  boolean isDocx, String srzDoc){
        this.docText = fullText;
        this.docSubject = subject;
        this.docTags = tags.toArray(new String[tags.size()]);
        this.docName = name;
        this.uuid = uuid;
        this.isDocx = isDocx;
        this.srzDoc = srzDoc;
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
    
    public String getUuid(){
        return uuid;
    }
    
    public boolean getDocState(){
        return isDocx;
    }
    
    public String getSerializedDoc(){
        return srzDoc;
    }
    
    public String toString(){
        return this.docSubject + " - " + this.docName;
    }
}