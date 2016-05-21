package com.tsasoftdev.fly;

import java.util.List;

public class FlyResult {
    private List<String> tags;
    private String subject;
     
    public FlyResult(List<String> tags, String subject){
        this.tags = tags;
        this.subject = subject;
    }
            
    public String getSubject(){
        return this.subject;
    }
       
    public List<String> getTags(){
        return this.tags;
    } 
}
