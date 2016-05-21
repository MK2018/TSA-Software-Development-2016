package com.tsasoftdev.fly;

import java.util.List;

public class FlyDocument{
    private String docText;
    private String docSubject;
    private String[] docTags;
    private String docName;
    private String uuid;
    private String extension;
    private String serial; //serialized Document
            
    public FlyDocument(){}
            
    public FlyDocument(String fullText, String subject, String[] tags, String name, String uuid, String extension, String srzDoc){
        this.docText = fullText;
        this.docSubject = subject;
        this.docTags = tags;
        this.docName = name;
        this.uuid = uuid;
        this.extension = extension;
        this.serial = srzDoc;
    }
    
    public FlyDocument(String fullText, String subject, List<String> tags, String name, String uuid,  String extension, String srzDoc){
        this.docText = fullText;
        this.docSubject = subject;
        this.docTags = tags.toArray(new String[tags.size()]);
        this.docName = name;
        this.uuid = uuid;
        this.extension = extension;
        this.serial = srzDoc;
    }
           
    public String getText(){
        return this.docText;
    }
           
    public String getSubject(){
        return this.docSubject;
    }
           
    public String[] getTags(){
        return this.docTags;
    }
            
    public String getName(){
        return this.docName;
    }
    
    public String getUuid(){
        return this.uuid;
    }
    
    public String getExtension(){
        return this.extension;
    }
    
    public String getSerializedForm(){
        return this.serial;
    }
    
    public String toString(){
        return this.docSubject + " - " + this.docName;
    }
}