import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.jsoup.Jsoup;

public class Crawler implements Runnable {


    public static void craw(String StringURL){
        Document HTTPdoc = null;
        //StringBuilder sb = new StringBuilder();
            try{
                if(!Main.pageVisited.keySet().contains(StringURL) ) {
                    HTTPdoc = httpConnect(StringURL);   // connect to web page (get) --> get html source code
                    if(!Main.pageVisited1.keySet().contains(HTTPdoc.title())) {
                        if (checkRelated(HTTPdoc)) {  // 2+ related terms
                            LinkedList<String> pageLinks = getURLs(HTTPdoc);   //  getting links on the current pages
                            for (String s : pageLinks) {  // push all discovered links to the queue
                                Main.pageQueueToVisit.add("https://en.wikipedia.org" + s);
                            }
                            //sb.append(HTTPdoc.html());
                            storeFile(HTTPdoc);  // store current page as a html file on local disk
                        }
                    }
                }else{  // the url is already visited
                    System.out.println(StringURL + " is visited already!");
                }

            }catch(IllegalArgumentException iae){
                System.out.println("No url in the queue");
                iae.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }finally{

            }
    }

    public static Document httpConnect(String url){ // http get request
        Document HTTPdoc = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            HTTPdoc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            String timeStamp2 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            System.out.println("Got http response from: " + url + "\nTime spent: " + timeStamp + " ~ " + timeStamp2);
        }catch(IOException ioe){
            System.out.println("URL: " + url + " has error");
            ioe.printStackTrace();
        }
        return HTTPdoc;
    }

    public static LinkedList<String> getURLs(Document HTTPdoc){    // get all URLs in the current page and save it to a list
        LinkedList newLinks = new LinkedList();
        Elements linksInThePage = HTTPdoc.select("a[href]");

        // regex
        String pattern = "^\\/wiki\\/.*";
        String pattern1 = "^\\/wiki\\/[A-Z][a-z]+[:][\\w\\d]";
        Pattern p = Pattern.compile(pattern); // /wiki/...
        Pattern p1 = Pattern.compile(pattern1); // /wiki/File: .... or /wiki/Special::...  etc
        Matcher m = null;

        for (Element link : linksInThePage) {
            String linkStr = link.attr("href");
            m = p.matcher(linkStr);
            if(!Main.pageVisited.keySet().contains("https://en.wikipedia.org"+linkStr)) { // check if the link is already visited
                if (m.find()) { // url start with: /wiki/ ...
                    m = p1.matcher(linkStr);
                    if (!m.find()) {  // url start with: /wiki/... but not /wiki/File:...
                        newLinks.add(linkStr);
                    }
                }
            }
        }
        return newLinks;
    }

    public static boolean checkRelated(Document HTTPdoc){ // check if the web page is related to the topic
        String webContent = HTTPdoc.body().text().toLowerCase();
        int relatedWordCount = 0;
        String pattern1 = "(\\s|\\.|\\!|\\-|\\:|\\)|\\&)*";
        String pattern2 = "(\\s|\\.|\\!|\\-|\\:|\\)|\\&)*";
        Matcher m  = null;
        for(String eachTerm : Main.relatedTerms){
            Pattern p = Pattern.compile(pattern1 + eachTerm + pattern2);
            m = p.matcher(webContent);
            if(m.find()){
                relatedWordCount++;
            }
            /*
            if(webContent.contains(" "+ eachTerm+ " ")){
                relatedWordCount++;
            }else{
            }
            */
            if(relatedWordCount>=2){
                Main.pageVisited.put(HTTPdoc.baseUri(),true);
                Main.pageVisited1.put(HTTPdoc.title(), HTTPdoc.html());
                System.out.println("Website: " + HTTPdoc.baseUri() + " is related");
                return true;
            }
        }
        Main.pageVisited.put(HTTPdoc.baseUri(),false);
        Main.pageVisited1.put(HTTPdoc.title(), HTTPdoc.html());
        System.out.println("Website: " + HTTPdoc.baseUri() + " is NOT related");
        return false;
    }

    public static void storeFile(Document doc){
        new File(Main.path + "/../webs").mkdirs();
        FileOutputStream fos = null;
        //PrintWriter pw = null;
        try{
            int i = Main.pageSaved.get();
            File f = new File(Main.path + "/../webs/" + doc.title().toString() +"(" + ++i + ")" +  ".html");
            fos = new FileOutputStream(f);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fos, "UTF-8" );
            BufferedWriter bufferedWriter = new BufferedWriter( outputStreamWriter );
            bufferedWriter.write( doc.html() );
            bufferedWriter.close();
            //pw = new PrintWriter(Main.path + "/../webs/" + doc.title().toString() +"(" + ++i + ")" +  ".html");
            //pw.write(doc.html().toString());
            Main.pageSaved.incrementAndGet();
            System.out.println("Page saved: " + Main.pageSaved.get());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally {
            //pw.close();
        }
    }

    //  for multi threading only
    public void run(){
        craw(Main.pageQueueToVisit.poll());
    }

}
