package com.tsasoftdev.fly;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import static com.tsasoftdev.fly.Core.docExtension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class AsyncParser{
        
    //private File toProcess;
    
    //public AsyncParser(File filepath){
    //    toProcess = filepath;
    //    startProcessing();
    //}
    
    public static void parseFile(File f){
        
    }
    
    public class InternalFlyParser implements Runnable{

        public void run() {
            
        }
        
    }
    
    /*public static void loadFile(File f){
        File currentRawFile = f;
        String rawText = "";
        FileInputStream fis;
        if(currentRawFile != null){
            try {
                fis = new FileInputStream(currentRawFile);
                //Core.currentFileText = "";
                Core.serializedDoc = DocumentSerializer.serialize(f.getAbsolutePath());
                String ext = f.toString().substring(f.toString().lastIndexOf(".")+1);
                docExtension = ext;
                if(ext.equals("docx")){
                    XWPFDocument currentWordDocx = new XWPFDocument(fis);
                    XWPFWordExtractor extract = new XWPFWordExtractor(currentWordDocx);
                    currentFileText = extract.getText();
                    //Core.isDocx = true;
                }
                else if(ext.equals("doc")){
                    HWPFDocument currentWordDoc = new HWPFDocument(fis);
                    WordExtractor extract = new WordExtractor(currentWordDoc);
                    currentFileText = extract.getText();                            
                    //Core.isDocx = false;
                }
                else if(ext.equals("ppt")){
                    HSLFSlideShowImpl currentPpt = new HSLFSlideShowImpl(fis);
                    PowerPointExtractor extract = new PowerPointExtractor(currentPpt);
                    currentFileText = extract.getText();                            
                    /*for (HSLFSlide slide: currentPpt.getSlides()) {
                        List<HSLFShape> shapes = slide.getShapes();
                        for(HSLFShape shape: shapes) {
                            if (shape instanceof HSLFTextShape) {
                                HSLFTextShape textShape = (HSLFTextShape)shape;
                                currentFileText += " " + textShape.getText();
                            }
                        }
                    }
                    
                }
                else if(ext.equals("pptx")){
                    XMLSlideShow currentPptx = new XMLSlideShow(fis);
                    XSLFPowerPointExtractor extract = new XSLFPowerPointExtractor(currentPptx);
                    currentFileText = extract.getText(); 
                    /*for (XSLFSlide slide: currentPptx.getSlides()) {
                        List<XSLFShape> shapes = slide.getShapes();
                        for(XSLFShape shape: shapes) {
                            if (shape instanceof XSLFTextShape) {
                                XSLFTextShape textShape = (XSLFTextShape)shape;
                                currentFileText += " " + textShape.getText();
                            }
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            currentFileText = "";
        }
    }
    
    //OLD CODE FROM CORE PARSER
    
    public IdentOutput initAnalysis() throws IOException, InterruptedException{
        if(!isAnalyzing){
            isAnalyzing = true;
            
            String contents = currentFileText;
            TagIdentifier tg = new TagIdentifier(contents, 5);
            while(tg.listsLoaded != true){ 
                TimeUnit.MILLISECONDS.sleep(100);
            }
            tg.generateTags();
            
            latestOutput = new IdentOutput(tg.getTags(), tg.getSubject());
            isAnalyzing = false;
        }
        return latestOutput;
    }
    
    public void teachAlgorithm(String subject, String[] tags) throws IOException{
        if(subject.equals("Science")) {
            for(String tag: Arrays.asList(tags)){
                if(!science.contains(tag))
                    science.add(stem(tag));
            }
            Core.ref.child("sci_tags").setValue(science.toString().substring(1, science.toString().length()-1));
        }
        else if(subject.equals("History")) {
            for(String tag: Arrays.asList(tags)){
                if(!history.contains(tag))
                    history.add(stem(tag));
            }
            Core.ref.child("hist_tags").setValue(history.toString().substring(1, history.toString().length()-1));
        }
        else if(subject.equals("Math")) {
            for(String tag: Arrays.asList(tags)){
                if(!math.contains(tag))
                    math.add(stem(tag));
            }
            Core.ref.child("math_tags").setValue(math.toString().substring(1, math.toString().length()-1));
        }
        else if(subject.equals("English")) {
            for(String tag: Arrays.asList(tags)){
                if(!english.contains(tag))
                    english.add(stem(tag));
            }
            Core.ref.child("eng_tags").setValue(english.toString().substring(1, english.toString().length()-1));
        }
        else if(subject.equals("Computer Science")) {
            for(String tag: Arrays.asList(tags)){
                if(!cs.contains(tag))
                    cs.add(stem(tag));
            }
            Core.ref.child("cs_tags").setValue(cs.toString().substring(1, cs.toString().length()-1));
        }
    }
    
    
    class IdentOutput{
        private String[] tags;
        private String subject;
        
        public IdentOutput(String[] tags, String subject){
            this.tags = tags;
            this.subject = subject;
        }
        
        public void setSubject(String subject){
            this.subject = subject;
        }
        
        public void setTags(String[] tags){
            this.tags = tags;
        }
        
        public String getSubject(){
            return this.subject;
        }
        
        public List<String> getTags(){
            return Arrays.asList(this.tags);
        }
    }
    
    public String stem(String term) throws IOException {

           TokenStream tokenStream = null;
           try {

           // tokenize
              tokenStream = new ClassicTokenizer(Version.LUCENE_36, new StringReader(term));
           // stem
              tokenStream = new PorterStemFilter(tokenStream);

           // add each token in a set, so that duplicates are removed
              Set<String> stems = new HashSet<String>();
              CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
              while (tokenStream.incrementToken()) {
                 stems.add(token.toString());
              }

           // if no stem or 2+ stems have been found, return null
              if (stems.size() != 1) {
                 return null;
              }
              String stem = stems.iterator().next();
           // if the stem has non-alphanumerical chars, return null
              if (!stem.matches("[a-zA-Z0-9-]+")) {
                 return null;
              }

              return stem;

           } 
           finally {
              if (tokenStream != null) {
                 tokenStream.close();
              }
           }

        }
    
    class TagIdentifier {

        private String filePlainText;

        private String subject = "";

        private int KEYWORD_LIMIT = 0;
        
        private boolean listsLoaded = false;

        public TagIdentifier() {
           tags = new ArrayList<String>();
           history = new ArrayList<String>();
           english = new ArrayList<String>();
           math = new ArrayList<String>();
           cs = new ArrayList<String>();
           science = new ArrayList<String>();
           commonWords = new ArrayList<String>();
           loadResources();
        }

        public TagIdentifier(String plainText, int n) {
           KEYWORD_LIMIT = n;
           filePlainText = plainText;
           tags = new ArrayList<String>();
           history = new ArrayList<String>();
           english = new ArrayList<String>();
           math = new ArrayList<String>();
           cs = new ArrayList<String>();
           science = new ArrayList<String>();
           commonWords = new ArrayList<String>();
           loadResources();
        }

        private List<Keyword> guessFromString() throws IOException {

           String input = filePlainText;
           TokenStream tokenStream = null;
           try {

           // hack to keep dashed words (e.g. "non-specific" rather than "non" and "specific")
              input = input.replaceAll("-+", "-0");
           // replace any punctuation char but apostrophes and dashes by a space
              input = input.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
           // replace most common english contractions
              input = input.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

           // tokenize input
              tokenStream = new ClassicTokenizer(Version.LUCENE_36, new StringReader(input));
           // to lowercase
              tokenStream = new LowerCaseFilter(Version.LUCENE_36, tokenStream);
           // remove dots from acronyms (and "'s" but already done manually above)
              tokenStream = new ClassicFilter(tokenStream);
           // convert any char to ASCII
              tokenStream = new ASCIIFoldingFilter(tokenStream);
           // remove english stop words
              tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, EnglishAnalyzer.getDefaultStopSet());

              List<Keyword> keywords = new LinkedList<Keyword>();
              CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
              while (tokenStream.incrementToken()) {
                 String term = token.toString();
              // stem each term
                 String stem = stem(term);
                 if (stem != null) {
                 // create the keyword or get the existing one if any
                    Keyword keyword = find(keywords, new Keyword(stem.replaceAll("-0", "-")));
                 // add its corresponding initial token
                    keyword.add(term.replaceAll("-0", "-"));
                 }
              }

           // reverse sort by frequency
              Collections.sort(keywords);

              return keywords;

           } 
           finally {
              if (tokenStream != null) {
                 tokenStream.close();
              }
           }

        }

        private <T> T find(Collection<T> collection, T example) {
           for (T element : collection) {
              if (element.equals(example)) {
                 return element;
              }
           }
           collection.add(example);
           return example;
        }

        private void loadResources() {
            try {
                
                Core.ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        for (DataSnapshot tag: snap.getChildren()) {                         
                            if(null != tag.getKey())
                                switch (tag.getKey()) {
                                case "cs_tags":
                                    cs.addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                    break;
                                case "eng_tags":
                                    english.addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                    break;
                                case "hist_tags":
                                    history.addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                    break;
                                case "math_tags":
                                    math.addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                    break;
                                case "sci_tags":
                                    science.addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                    break;
                                case "common":
                                    commonWords.addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                    break;
                            }
                        }
                        listsLoaded = true;
                    }
                    @Override
                    public void onCancelled(FirebaseError fe) {
                        System.out.println("error.");
                    }
                });
                
              Iterator it = history.iterator();
              while(it.hasNext())
                 if((it.next()).toString().trim().equals(""))
                    it.remove();
              it = cs.iterator();
              while(it.hasNext())
                 if((it.next()).toString().trim().equals(""))
                    it.remove();
              it = english.iterator();
              while(it.hasNext())
                 if((it.next()).toString().trim().equals(""))
                    it.remove();
              it = math.iterator();
              while(it.hasNext())
                 if((it.next()).toString().trim().equals(""))
                    it.remove();
              it = science.iterator();
              while(it.hasNext())
                 if((it.next()).toString().trim().equals(""))
                    it.remove();
           } 
           catch (Exception e) {
              e.printStackTrace();
           }
        }

        private String getLikelySubject(ArrayList<String> list) {
           int countHistory = 0;
           for(String c : history) {
              if(list.contains(c))
                 countHistory++;
           }
           int countMath = 0;
           for(String c : math) {
              if(list.contains(c)) {
                 countMath++;
              }
           }
           int countEnglish = 0;
           for(String c : english) {
              if(list.contains(c))
                 countEnglish++;
           }
           int countCS = 0;
           for(String c : cs) {
              if(list.contains(c)) {
                 countCS++;
              }
           }
           int countScience = 0;
           for(String c : science) {
              if(list.contains(c)) {
                 countScience++;
              }
           }

           HashMap<String, Integer> subjects = new HashMap<String,Integer>();
           subjects.put("History", countHistory);
           subjects.put("Science", countScience);
           subjects.put("English", countEnglish);
           subjects.put("Math", countMath);
           subjects.put("Computer Science", countCS);
           subjects.put("None", 0);
           String biggestSubject = "None";
           for (String key : subjects.keySet()) {
              if (subjects.get(key) > subjects.get(biggestSubject))
                 biggestSubject = key;
           }
           return biggestSubject;
        }

        public boolean generateTags() {
           ArrayList<String> al = new ArrayList<String>();
           List<Keyword> ll = null;
           try {
           ll = guessFromString();
           } catch (IOException e) {
              e.printStackTrace();
           }
           List<Keyword> filtered = new LinkedList<Keyword>();
           for(Keyword e : ll)
              if(!commonWords.contains(e.getStem()) && e.getStem().length() > 2)
                 filtered.add(e);
           for(int i = 0; i < KEYWORD_LIMIT; i++)
              al.add(filtered.get(i).getTerms().iterator().next().toString());
           tags = al;
           ArrayList<String> d = new ArrayList<String>();
           for(Keyword e : filtered)
              d.add(e.getStem());
           subject = getLikelySubject(d);
           return true;
        }

        private int getOccurences(String o, String[] arr) {
           int ct = 0;
           //speeeeed
           LinkedList ll = new LinkedList(Arrays.asList(arr));
           Iterator it = ll.iterator();
           while(it.hasNext()) {
              if(it.next().equals(o))
                 ct++;
           }
           return ct;
        }

        public String[] getTags() {
           return tags.toArray(new String[0]);
        }

        public String getSubject() {
           return subject;
        }

        public String getPlainText() {
           return filePlainText;
        }
    }*/
}
