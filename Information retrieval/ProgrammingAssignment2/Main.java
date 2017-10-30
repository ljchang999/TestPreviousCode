import java.io.*;
import java.util.*;

public class Main {
    static String path;
    public static void main(String [] args){
        System.out.println("Please enter a path (relative to where the source code is) of a directory that contains web files:");
        Scanner sc = new Scanner(System.in);
        path = sc.next();
        try {
            System.out.println("Start making position based inverted index: ");
            PositionBased_IndexBuilder pib = new PositionBased_IndexBuilder(getFiles(path));
            pib.createIndex();
            HashMap<String, HashSet<String>> pib_invertedWordList = pib.getInvertedWordList();
            savePositionBasedIndex(pib_invertedWordList);
            System.out.println("Finish making position based inverted index.");
            System.out.println("Total index words: " + pib.getTotalIndexedWords());

            System.out.println("\n\n");
            System.out.println("Start making frequency based inverted index: ");
            FreqBased_IndexBuilder fib = new FreqBased_IndexBuilder(getFiles(path));
            fib.createIndex();
            //HashMap<String, int[]> invertedWordList = fib.getInvertedWordList(); // using int array for inner dic of each token
            HashMap<String, HashMap<String, Integer>> fib_invertedWordList = fib.getInvertedWordList();
            SaveFrequencyBasedIndex(fib_invertedWordList);
            saveFileIndex(fib.getFileIndexList(), pib.getFileIndexList());
            System.out.println("Finish making frequency based inverted index.");
            System.out.println("Total index words: " + fib.getTotalIndexedWords());

            for(Object key : pib_invertedWordList.keySet()){
                if(fib_invertedWordList.get(key)==null){
                    System.out.println(key.toString() + ", in pib not in fib");
                }
            }

            for(Object key : fib_invertedWordList.keySet()){
                if(pib_invertedWordList.get(key) ==null){
                    System.out.println(key.toString() + ", in fib not in pib");
                }
            }

        }catch(NullPointerException npe){
            System.out.println("No files in the current directory");
            npe.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static File[] getFiles(String path) throws IOException{
        File folder = new File(path);
        File [] files = folder.listFiles();
        return files;
    }

    public static void SaveFrequencyBasedIndex(HashMap<String, HashMap<String,Integer>> invertedIndex){
        PrintWriter pw;
        try{
            pw = new PrintWriter("FrequencyBased_InvertedIndex.txt");
            for(Object key : invertedIndex.keySet()){   // loop through each key in the outer hash-map
                if(key instanceof String){  // double check if the key is a String (vacab name)
                    pw.print(key.toString() + " : \n\t");
                    StringBuilder sb = new StringBuilder();
                    int i = 0;  //  counter for output file. change to next line in every 10 entries

                    //  loop through each document (in each vocab) --- doc name is the key of the inner hash-map
                    for(Object doc : invertedIndex.get(key.toString()).keySet()){
                        sb.append(doc.toString() + ":");
                        sb.append(invertedIndex.get(key).get(doc));
                        sb.append(" ");
                        if(++i%10 == 0){
                            sb.append("\n\t");
                        }
                    }
                    pw.print(sb.toString());
                    pw.println("\n");
                }
            }
            pw.close();
        }catch (IOException ioe){
            System.out.println("Error occurs when saving FrequencyBased_InvertedIndex");
        }
    }

    public static void savePositionBasedIndex(HashMap<String, HashSet<String>> invertedIndex){
        PrintWriter pw;
        try{
            pw = new PrintWriter("positionBased_InvertedIndex.txt");
            for(Object key : invertedIndex.keySet()){   // loop through each key in the outer hash-map
                if(key instanceof String){  // double check if the key is a String (vacab name)
                    pw.print(key.toString() + " : \n\t");
                    StringBuilder sb = new StringBuilder();
                    int i = 0;  //  counter for output file. change to next line in every 10 entries
                    //  loop through each document (in each vocab)
                    for(Object doc : invertedIndex.get(key.toString())){
                        sb.append(doc.toString());
                        sb.append(" ");
                        if(++i%10 == 0){
                            sb.append("\n\t");
                        }
                    }
                    pw.print(sb.toString());
                    pw.println("\n");
                }
            }
            pw.close();
        }catch (IOException ioe){
            System.out.println("Error occurs when saving PositionBased_InvertedIndex");
        }
    }

    public static void saveFileIndex(HashMap<String, String> fibFileIndexList, HashMap<String, String> pibFileIndexList){
        PrintWriter pw;
        try{
            pw = new PrintWriter("FreqBased_FileNameIndex.txt");

            for(Object key : fibFileIndexList.keySet()){
                pw.println(key + ":\t" + fibFileIndexList.get(key));
            }

            pw.close();
        }catch(IOException ioe){
            System.out.println("Error occurs when saving FrequencyBased_FileNameIndex");
        }
        try{
            pw = new PrintWriter("PositionBased_FileNameIndex.txt");

            for(Object key : pibFileIndexList.keySet()){
                pw.println(key + ":\t" + pibFileIndexList.get(key));
            }

            pw.close();
        }catch(IOException ioe){
            System.out.println("Error occurs when saving PositionBased_FileNameIndex");
        }
    }

    //  same method for int-array implementation for inner map
    //  array index serves as identifier of each web document, array's value = each word's count in each document
    /*
    public static void SaveFrequencyBasedIndex(HashMap<String, int[]> invertedIndex){
        PrintWriter pw;
        try{
            pw = new PrintWriter("index.txt");
            for(Object key : invertedIndex.keySet()){
                if(key instanceof String){
                    pw.print(key.toString() + " : \n\t");
                    StringBuilder sb = new StringBuilder();
                    int i = 0;

                    for(int index = 0; index < (invertedIndex.get(key)).length; index++){
                        if(invertedIndex.get(key)[index]!=0){
                            int documentNumber = index +1;
                            sb.append("web" + documentNumber + ":");
                            sb.append((invertedIndex.get(key))[index]);
                            sb.append(" ");
                            if(++i %10 == 0){
                                sb.append("\n\t");
                            }
                        }
                    }
                    pw.print(sb.toString());
                    pw.println("\n");
                }
            }
            pw.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    */
}
