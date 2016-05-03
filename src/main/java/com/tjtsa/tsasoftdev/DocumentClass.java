/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tjtsa.tsasoftdev;

import java.util.List;

public class DocumentClass{
    private String docText;
    private String docSubject;
    private String[] docTags;
    private String docName;
            
    public DocumentClass(){}
            
    public DocumentClass(String fullText, String subject, String[] tags, String name){
        this.docText = fullText;
        this.docSubject = subject;
        this.docTags = tags;
        this.docName = name;
    }
    
    public DocumentClass(String fullText, String subject, List<String> tags, String name){
        this.docText = fullText;
        this.docSubject = subject;
        this.docTags = tags.toArray(new String[tags.size()]);
        this.docName = name;
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
    
    public String toString(){
        return this.docSubject + " - " + this.docName;
    }
}