package com.tsasoftdev.fly;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
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
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class AsyncParser{
    
    private static Future<FlyResult> flyResult;
    
    private static HashMap<String, ArrayList<String>> keywords;
    
    public static void parseFile(File f){
        ExecutorService exec = Executors.newSingleThreadExecutor();
        flyResult = exec.submit(new InternalFlyParser(f));
    }
    
    public static void teachAlgorithm(String subject, String[] tags){
        try {
            InternalFlyParser.teachAlgorithm(subject, tags);
        } catch (IOException ex) {
            Logger.getLogger(AsyncParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static FlyResult fetchResult(){
        try {
            return flyResult.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(AsyncParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void loadResources(){
        keywords = new HashMap<String, ArrayList<String>>();
        keywords.put("cs", new ArrayList<String>());
        keywords.put("eng", new ArrayList<String>());
        keywords.put("hist", new ArrayList<String>());
        keywords.put("math", new ArrayList<String>());
        keywords.put("sci", new ArrayList<String>());
        keywords.put("common", new ArrayList<String>());
        
        class isEmpty<t> implements Predicate<t>{   
            @Override
            public boolean test(t tag){  
                return(tag.toString().trim().equals(""));
            }  
        }
        
        //Firebase ref = new Firebase("https://tsaparser.firebaseio.com/");
        
        Core.ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                for (DataSnapshot tag: snap.getChildren()) {                         
                    if(null != tag.getKey())
                        switch (tag.getKey()) {
                            case "cs_tags":
                                keywords.get("cs").addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                break;
                            case "eng_tags":
                                keywords.get("eng").addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                break;
                            case "hist_tags":
                                keywords.get("hist").addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                break;
                            case "math_tags":
                                keywords.get("math").addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                break;
                            case "sci_tags":
                                keywords.get("sci").addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                break;
                            case "common":
                                keywords.get("common").addAll(Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*")));
                                break;
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError fe) {
                System.out.println(fe.getMessage());
            }
        });  
        for(String tag : keywords.keySet())
            keywords.get(tag).removeIf(new isEmpty<String>());
    }
    
    public static class InternalFlyParser implements Callable{
        
        private File f;
        
        public InternalFlyParser(File f){
            this.f = f;
        }
        
        @Override
        public FlyResult call() {
            String rawtext = getRawFileText(this.f);
            TagIdentifier tg = new TagIdentifier(rawtext, 5);
            tg.process();
            return new FlyResult(tg.getTags(), tg.getSubject(), rawtext, this.f.toString().substring(this.f.toString().lastIndexOf(".")+1), this.f.toString());
        }
        
        public static void teachAlgorithm(String subject, String[] tags) throws IOException{
            if(subject.equals("Science")) {
                for(String tag: Arrays.asList(tags)){
                    if(!keywords.get("sci").contains(tag))
                        keywords.get("sci").add(stem(tag));
                }
                Core.ref.child("sci_tags").setValue(keywords.get("sci").toString().substring(1, keywords.get("sci").toString().length()-1));
            }
            else if(subject.equals("History")) {
                for(String tag: Arrays.asList(tags)){
                    if(!keywords.get("hist").contains(tag))
                        keywords.get("hist").add(stem(tag));
                }
                Core.ref.child("hist_tags").setValue(keywords.get("hist").toString().substring(1, keywords.get("hist").toString().length()-1));
            }
            else if(subject.equals("Math")) {
                for(String tag: Arrays.asList(tags)){
                    if(!keywords.get("math").contains(tag))
                        keywords.get("math").add(stem(tag));
                }
                Core.ref.child("math_tags").setValue(keywords.get("math").toString().substring(1, keywords.get("math").toString().length()-1));
            }
            else if(subject.equals("English")) {
                for(String tag: Arrays.asList(tags)){
                    if(!keywords.get("eng").contains(tag))
                        keywords.get("eng").add(stem(tag));
                }
                Core.ref.child("eng_tags").setValue(keywords.get("eng").toString().substring(1, keywords.get("eng").toString().length()-1));
            }
            else if(subject.equals("Computer Science")) {
                for(String tag: Arrays.asList(tags)){
                    if(!keywords.get("cs").contains(tag))
                        keywords.get("cs").add(stem(tag));
                }
                Core.ref.child("cs_tags").setValue(keywords.get("cs").toString().substring(1, keywords.get("cs").toString().length()-1));
            }
        }
        
        private String getRawFileText(File f){
            try {
                FileInputStream fis = new FileInputStream(f);
                String ext = f.toString().substring(f.toString().lastIndexOf(".")+1);
                if(ext.equals("docx")){
                    XWPFDocument currentWordDocx = new XWPFDocument(fis);
                    XWPFWordExtractor extract = new XWPFWordExtractor(currentWordDocx);
                    return(extract.getText());
                }
                else if(ext.equals("doc")){
                    HWPFDocument currentWordDoc = new HWPFDocument(fis);
                    WordExtractor extract = new WordExtractor(currentWordDoc);
                    return(extract.getText());                           
                }
                else if(ext.equals("ppt")){
                    HSLFSlideShowImpl currentPpt = new HSLFSlideShowImpl(fis);
                    PowerPointExtractor extract = new PowerPointExtractor(currentPpt);
                    return(extract.getText());                                                
                }
                else if(ext.equals("pptx")){
                    XMLSlideShow currentPptx = new XMLSlideShow(fis);
                    XSLFPowerPointExtractor extract = new XSLFPowerPointExtractor(currentPptx);
                    return(extract.getText());  
                }
                else if(ext.equals("pdf")){
                    PDFParser parser = null;
                    PDDocument pdDoc = null;
                    COSDocument cosDoc = null;
                    PDFTextStripper pdfStripper;

                    String parsedText = "";
                    try {
                        parser = new PDFParser(new RandomAccessBufferedFileInputStream(f));
                        parser.parse();
                        cosDoc = parser.getDocument();
                        pdfStripper = new PDFTextStripper();
                        pdDoc = new PDDocument(cosDoc);
                        parsedText = pdfStripper.getText(pdDoc).replaceAll("[^A-Za-z0-9. ]+", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (cosDoc != null)
                            cosDoc.close();
                        if (pdDoc != null)
                            pdDoc.close();
                    }
                    return parsedText;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "";
        }
        public static String stem(String term) throws IOException {

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

            private ArrayList<String> tags;

            private int KEYWORD_LIMIT = 0;

            private boolean listsLoaded = false;

            public TagIdentifier() {
               KEYWORD_LIMIT = 5;
               filePlainText = "";
               tags = new ArrayList<String>();
            }

            public TagIdentifier(String plainText, int n) {
               KEYWORD_LIMIT = n;
               filePlainText = plainText;
               tags = new ArrayList<String>();
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

            private String getLikelySubject(ArrayList<String> list) {
               int countHistory = 0;
               for(String c : keywords.get("hist")) {
                  if(list.contains(c))
                     countHistory++;
               }
               int countMath = 0;
               for(String c : keywords.get("math")) {
                  if(list.contains(c)) {
                     countMath++;
                  }
               }
               int countEnglish = 0;
               for(String c : keywords.get("eng")) {
                  if(list.contains(c))
                     countEnglish++;
               }
               int countCS = 0;
               for(String c : keywords.get("cs")) {
                  if(list.contains(c)) {
                     countCS++;
                  }
               }
               int countScience = 0;
               for(String c : keywords.get("sci")) {
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

            public boolean process() {
               ArrayList<String> al = new ArrayList<String>();
               List<Keyword> ll = null;
               try {
               ll = guessFromString();
               } catch (IOException e) {
                  e.printStackTrace();
               }
               List<Keyword> filtered = new LinkedList<Keyword>();
               for(Keyword e : ll)
                  if(!keywords.get("common").contains(e.getStem()) && e.getStem().length() > 2)
                     filtered.add(e);
               for(int i = 0; i < KEYWORD_LIMIT; i++)
                  al.add(filtered.get(i).getTerms().iterator().next());
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

            public List<String> getTags() {
               return tags;
            }

            public String getSubject() {
               return subject;
            }

            public String getPlainText() {
               return filePlainText;
            }
        }
    }
}