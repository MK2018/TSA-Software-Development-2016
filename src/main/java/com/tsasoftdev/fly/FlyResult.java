package com.tsasoftdev.fly;

import java.util.List;

public class FlyResult {
    private List<String> tags;
    private String subject;
    private String fullText;
    private String ext;
    private String origPath;
     
    public FlyResult(List<String> tags, String subject, String fullText, String ext, String origPath){
        this.tags = tags;
        this.subject = subject;
        this.fullText = fullText;
        this.ext = ext;
        this.origPath = origPath;
    }
            
    public String getSubject(){
        return this.subject;
    }
    
    public String getText(){
        return this.fullText;
    }
    
    public String getExt(){
        return this.ext;
    }
    
    public String getPath(){
        return this.origPath;
    }
       
    public List<String> getTags(){
        return this.tags;
    } 
}
