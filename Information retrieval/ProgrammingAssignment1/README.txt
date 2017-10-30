1. type: javac -classpath jsoup-1.10.3.jar Crawler.java Main.java	to compile
2. type: java -cp .:jsoup-1.10.3.jar Main	to run the program

This program crawls 500 unique websites that are related “basketball”. It used 10 different related-terms to determine whether a page is related to the topic. This program also generates a detailed report about which websites are visited/crawled/related.