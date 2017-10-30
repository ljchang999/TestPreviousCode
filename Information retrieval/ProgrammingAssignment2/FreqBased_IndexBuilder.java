import java.io.*;
import java.util.*;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;


public class FreqBased_IndexBuilder {
    //  variables
    private HashMap<String,HashMap<String, Integer>> invertedWordList;
    private HashMap<String,String> fileIndexList;
    //private HashMap<String, int[]> invertedWordList1; // for int- array implementation
    private ArrayList<String> tokensList;
    private File [] files;
    private int totalIndexedWords;
    private int documentCounter1 = 1;    // for tokenizing process
    private int documentCounter2 = 1;    // for console/ IO output
    private String currentFile;          // for console/ IO output

    public FreqBased_IndexBuilder(File[] files){
        invertedWordList = new HashMap<>();
        //invertedWordList1 = new HashMap<>();  // for int- array implementation
        fileIndexList = new HashMap<>();
        this.files = files;
        totalIndexedWords = 0;
    }

    public void createIndex() throws IOException{
        for(File file : files){ // loop through every file
            currentFile = file.getName();
            tokensList = new ArrayList<>(); // instantiate a new tokenList in each iteration
            if(file.isFile() && file.getName().endsWith(".html")){
                String fileName = "web" + documentCounter2;
                fileIndexList.put(fileName, file.getName());    // store file's original name (ex. web1, XXXX)
                Document doc = Jsoup.parse(file,"UTF-8");
                String htmlBody = doc.select("body").text();

                createTokensList(htmlBody); // create a list of token for each file

                for(String token : tokensList){ // loop through each token in the file
                    if (invertedWordList.get(token) == null){   // if the word is not in the invertedList yet
                        invertedWordList.put(token,new HashMap<String,Integer>());  // create a key based on current token
                        invertedWordList.get(token).put(fileName,1);    // put current document name and 1 (since it just got created) in the list.
                        totalIndexedWords++;    //  encounter new word, increment counter
                    }else if(invertedWordList.get(token).get(fileName) == null){
                        invertedWordList.get(token).put(fileName,1);
                    }else{
                        int currentCount = invertedWordList.get(token).get(fileName);
                        invertedWordList.get(token).put(fileName,++currentCount);
                    }
                }
                //  for int - array implementation
//                for(String token : tokensList){
//                    try{
//                        if(invertedWordList1.get(token) == null){
//                            invertedWordList1.put(token,new int[500]);
//                        }
//                        int currentCount = invertedWordList1.get(token)[documentCounter1 -1];
//                        System.out.print("web" + documentCounter1 + ": " + token + "\t " + currentCount);
//                        invertedWordList1.get(token)[documentCounter1 -1] = ++currentCount;
//                        System.out.println(" -> " + invertedWordList1.get(token)[documentCounter1-1]);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
                documentCounter1++;
                documentCounter2++; // increment counter after current iteration ends (only increment for .html extension files)
            }
        }
    }

    public void createTokensList(String s){
        System.out.println("Processing web # " +  documentCounter2 + ", " + currentFile);
        String [] wordArr = s.toLowerCase().split(" ");

        for(String word : wordArr){
            if(word.trim().length()>0){
                if((word.length() == 1 && isCertainSymbol(word.charAt(0))) || isNumeric(word)  ){
                    // don't save any 1-character-symbol, or pure number
                }else{
                    tokensList.add(word);
                }
            }
        }
    }

    public HashMap<String, HashMap<String, Integer>> getInvertedWordList(){
        return invertedWordList;
    }

    /*  for int- array implementation
    public HashMap<String,int[]> getInvertedWordList(){
        return invertedWordList1;
    }
    */

    public HashMap<String, String> getFileIndexList(){
        return fileIndexList;
    }

    //  this method check where a string can be converted to a numeric value.
    private boolean isNumeric(String s){
        try{
            Integer.parseInt(s);    //  if it can be parsed, then it's a number
            return true;
        }catch (Exception e){   // will throw exception if it's not a number
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

    public int getTotalIndexedWords(){
        return totalIndexedWords;
    }
}
