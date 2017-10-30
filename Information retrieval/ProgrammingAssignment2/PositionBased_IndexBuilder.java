import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class PositionBased_IndexBuilder {
    //  variables
    //  Every entry in a Set of each key in the outer hash-map is a unique string combination of "web-name + starting in index of the word (key of the outer hashmap)"
    private HashMap<String,HashSet<String>> invertedWordList;
    private HashMap<String,String> fileIndexList;
    private File [] files;
    private int totalIndexedWords;
    private int documentCounter = 1;    // for console/ IO output
    private String currentFile;          // for console/ IO output

    public PositionBased_IndexBuilder(File[] files) {
        invertedWordList = new HashMap<>();
        fileIndexList = new HashMap<>();
        this.files = files;
        totalIndexedWords = 0;
    }

    public void createIndex() throws IOException{
        for(File file : files){

            int positionCounter = 0;    // position counter for html text in a document
            currentFile = file.getName();
            if(file.isFile() && file.getName().endsWith(".html")) {
                System.out.println("Processing web #: " +documentCounter +", " + currentFile);
                String fileName = "web" + documentCounter;
                fileIndexList.put(fileName, file.getName());    // store file's original name (ex. web1, XXXX)
                Document doc = Jsoup.parse(file, "UTF-8");
                String htmlBody = doc.select("body").text().toLowerCase();

                int startIndex = 0;
                for(;positionCounter< htmlBody.length(); positionCounter++){
                    if(shouldBeAToken(htmlBody, positionCounter) == true){
                        String word = htmlBody.substring(startIndex,positionCounter).trim();
                        if((word.length() == 1 && isCertainSymbol(word.charAt(0))) || isNumeric(word) ){   // if the word isn't a symbol
                            // don't save any 1-character-symbol, or pure number
                        }else{
                            if (invertedWordList.get(word) == null){
                                invertedWordList.put(word,new HashSet<>());  // create a key based on current token if the word is not in the map yet
                                totalIndexedWords++;    //  if this if statement is trigger it means we encounter a new word, increment counter
                            }
                            invertedWordList.get(word).add(fileName + ":" + startIndex+1);
                        }
                        startIndex = positionCounter+1;
                    }
                }
                documentCounter++;
            }
        }
    }

    public boolean shouldBeAToken(String s, int wordEndIndex){
        if(s.charAt(wordEndIndex) == ' '){
            return true;
        }
        return false;
    }

    private boolean isCertainSymbol(char c){
        int asciiCode = (int)c;
        if(asciiCode == 39){
            return true;
        }
        if(asciiCode >=32 && asciiCode <=47){
            return true;
        }
        if(asciiCode >=58 && asciiCode <= 64){
            return true;
        }
        if(asciiCode >=91 && asciiCode <=96){
            return true;
        }
        if(asciiCode >=123 && asciiCode <=126){
            return true;
        }
        return false;
    }

    public HashMap<String, HashSet<String>> getInvertedWordList(){
        return invertedWordList;
    }
    public int getTotalIndexedWords(){
        return totalIndexedWords;
    }
    public HashMap<String, String> getFileIndexList(){
        return fileIndexList;
    }

    private boolean isNumeric(String s){
        try{
            Integer.parseInt(s);    //  if it can be parsed, then it's a number
            return true;
        }catch (Exception e){   // will throw exception if it's not a number
        }
        return false;
    }
}
