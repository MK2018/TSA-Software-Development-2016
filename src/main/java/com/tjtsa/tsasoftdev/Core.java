/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tjtsa.tsasoftdev;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;
import java.io.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.util.*;
import org.apache.lucene.analysis.en.*;

/**
 *
 * @author TJ TSA
 */
public class Core {
    
    private boolean isAnalyzing = false;
    
    public void goToScene(String toSceneName, Stage ctx) throws IOException{
        Parent toParent = FXMLLoader.load(getClass().getResource("/fxml/"+toSceneName+".fxml"));
        Scene toScene = new Scene(toParent);
        toScene.getStylesheets().add("/styles/MainStyles.css");
        ctx.setScene(toScene);
        ctx.show();
    }
    
    public void initAnalysis(File f) throws IOException{
        if(!isAnalyzing){
            isAnalyzing = true;
            FileInputStream fis = new FileInputStream(f);
            byte[] data = new byte[(int) f.length()];
            fis.read(data);
            fis.close();
            String contents = new String(data, "UTF-8");
            TagIdentifier tg = new TagIdentifier(contents, 5);
            tg.generateTags();
            System.out.println(Arrays.toString(tg.getTags()));

            System.out.println(tg.getSubject());
            
            
            
            //System.out.println(contents);
            isAnalyzing = false;
        }
    }
    
    class TagIdentifier {

        private String filePlainText;
        private ArrayList<String> commonWords;
        private ArrayList<String> tags;
        private ArrayList<String> history;
        private ArrayList<String> english;
        private ArrayList<String> math;
        private ArrayList<String> cs;
        private ArrayList<String> science;

        private String subject = "";

        private int KEYWORD_LIMIT = 0;

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

        public void teachAlgo(String subject) {
           ArrayList<String> wanted = null;
            File ff = null;
           if(subject.equals("Science")) {
              wanted = science;
              ff = new File("sci_tags.txt");
           }
           if(subject.equals("History")) {
              wanted = history;
              ff = new File("hist_tags.txt");
           }
           if(subject.equals("Math")) {
              wanted = math;
              ff = new File("math_tags.txt");
           }
           if(subject.equals("English")) {
              wanted = english;
              ff = new File("eng_tags.txt");
           }
           if(subject.equals("Computer Science")) {
              wanted = cs;
              ff = new File("cs_tags.txt");
           }
           PrintWriter out = null;
           try {
              out = new PrintWriter(new BufferedWriter(new FileWriter(ff.getName(), true)));
              out.println();
              for(int i = 0; i < KEYWORD_LIMIT; i++) {
                 String st = stem(tags.get(i));
                 if(!wanted.contains(st)) {
                    out.println(st);
                 }
              }
           }
           catch (IOException e) {
              System.err.println(e);
           }
           finally{
              if(out != null){
                 out.close();
              }
           }    
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


        private String stem(String term) throws IOException {

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

        private void loadResources() {
            try {
                /*BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/tag_docs/hist_tags.txt")));
                String line;
                while ((line = in.readLine()) != null){
                    history.add(line);
                }
              //File file = new File("hist_tags.txt");
              //Scanner scan = new Scanner(file);
              //while(scan.hasNext()) {
              //   history.add(scan.nextLine());
              //}
                in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/tag_docs/eng_tags.txt")));
                line = "";
                while ((line = in.readLine()) != null){
                    english.add(line);
                }
                in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/tag_docs/cs_tags.txt")));
                line = "";
                while ((line = in.readLine()) != null){
                    cs.add(line);
                }
                in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/tag_docs/math_tags.txt")));
                line = "";
                while ((line = in.readLine()) != null){
                    math.add(line);
                }
                in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/tag_docs/sci_tags.txt")));
                line = "";
                while ((line = in.readLine()) != null){
                    science.add(line);
                }*/
                
                final Firebase tagfiles = new Firebase("https://tsaparser.firebaseio.com/");
                
                //Map<String, String> nameMap = new HashMap<String, String>();
                //nameMap.put("cs_tags", "cs");
                
                tagfiles.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot tag: snapshot.getChildren()) {
                            System.out.println(tag.getKey());
                            List<String> items = Arrays.asList(tag.getValue(String.class).split("\\s*,\\s*"));
                          }
                        tagfiles.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(FirebaseError fe) {
                        System.out.println("The read failed: " + fe.getMessage());
                    }
                });
                
                /*tagfiles.child("cs_tags").setValue(cs.toString().substring(1, cs.toString().length()-1));
                tagfiles.child("eng_tags").setValue(english.toString().substring(1, english.toString().length()-1));
                tagfiles.child("hist_tags").setValue(history.toString().substring(1, history.toString().length()-1));
                tagfiles.child("math_tags").setValue(math.toString().substring(1, math.toString().length()-1));
                tagfiles.child("sci_tags").setValue(science.toString().substring(1, science.toString().length()-1));*/
              /*file = new File("eng_tags.txt");
              scan = new Scanner(file);
              while(scan.hasNext()) {
                 english.add(scan.nextLine());
              }
              file = new File("cs_tag.txts");
              scan = new Scanner(file);
              while(scan.hasNext()) {
                 cs.add(scan.nextLine());
              } 
              file = new File("math_tags.txt");
              scan = new Scanner(file);
              while(scan.hasNext()) {
                 math.add(scan.nextLine());
              }
              file = new File("sci_tags.txt");
              scan = new Scanner(file);
              while(scan.hasNext()) {
                 science.add(scan.nextLine());
              }*/
                //in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/tag_docs/common.txt")));
                //line = "";
                //while ((line = in.readLine()) != null){
                //    commonWords.add(stem(line));
                //}
              /*file = new File("common.txt");
              scan = new Scanner(file);
              while(scan.hasNext()) {
                 commonWords.add(stem(scan.nextLine()));
              }
*/
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

           // System.out.println(countHistory);
           // System.out.println(countEnglish);
           // System.out.println(countScience);
           // System.out.println(countMath);
           // System.out.println(countCS);

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
    }

    
}
