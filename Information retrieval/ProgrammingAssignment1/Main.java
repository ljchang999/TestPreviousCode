import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.*;

public class Main {
    public static String path = System.getProperty("user.dir");
    public static ArrayList<String> relatedTerms;
    public static Queue<String> pageQueueToVisit;
    public static Map<String,Boolean> pageVisited;  //no duplicate pages(URL) allowed. // Key: url, Value: true/false (related or not)
    public static Map<String,String> pageVisited1;  // key Title, Value: content
    public static AtomicInteger  pageSaved = null;
    public static int maxURL; //max pages to be searched

    public static void main(String[] args) {
        //variables
        pageQueueToVisit = new LinkedList<>();
        maxURL = 500;
        pageSaved = new AtomicInteger();
        pageVisited = new HashMap<>();
        pageVisited1 = new HashMap<>();
        relatedTerms = new ArrayList<>();


        //seed urls
        String [] initSeeds = {"https://en.wikipedia.org/wiki/Basketball","https://en.wikipedia.org/wiki/FIBA"};

        //related terms
        relatedTerms.add("basketball");
        relatedTerms.add("fiba");
        relatedTerms.add("nba");
        relatedTerms.add("ncaa");
        relatedTerms.add("dunk");
        relatedTerms.add("shoot guard");
        relatedTerms.add("point guard");
        relatedTerms.add("small forward");
        relatedTerms.add("power forward");
        relatedTerms.add("field goal");
        relatedTerms.add("rim");
        relatedTerms.add("hoop");

        Crawler.craw(initSeeds[0]);
        Crawler.craw(initSeeds[1]);

        int i = 1; // to keep track of how many iterations it runs to save 500 pages
        while(pageQueueToVisit.size()>0 && pageSaved.get()<maxURL){
            String StringURL = pageQueueToVisit.poll();
            System.out.println("\nIteration: " + i++);
            Crawler.craw(StringURL);
            /*  // for multi-threading
            Thread t = new Thread(new Crawler());
            t.start();
            */
        }
        storeReportFile();
    }

    public static void storeReportFile(){
        //new File(path + "/WebCrawler/reports").mkdirs();
        StringBuilder sb = new StringBuilder(); // for crawled websites
        StringBuilder sb1 = new StringBuilder();    // detailed info -- all visited websites
        sb.append("===============  Stored websites    ===============\n");
        sb.append("Count:\tWebs\n");

        sb1.append("\n\n\n===============  Detailed report for all visited websites    ===============\n");
        sb1.append("Count:\tRelevant?\tWebs\n");
        int totalWebsCount = 0;
        int crawledWebCount = 0;
        for(Map.Entry<String, Boolean> webURL : pageVisited.entrySet()){
            totalWebsCount++;
            sb1.append(totalWebsCount + "\t" + webURL.getValue() + "\t" +  webURL.getKey() + "\n");
            if(webURL.getValue() == true){
                crawledWebCount++;
                sb.append(crawledWebCount + "\t" + webURL.getKey() + "\n");
            }
        }

        try(PrintWriter pw = new PrintWriter("report.txt")){
            pw.write(sb.toString());
            pw.write(sb1.toString());
        }catch (IOException ioe){
            System.out.println("Error when saving report.");
        }
    }

}
