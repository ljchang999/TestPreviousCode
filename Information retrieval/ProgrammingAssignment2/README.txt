1. type: javac -classpath jsoup-1.10.3.jar FreqBased_IndexBuilder.java PositionBased_IndexBuilder.java Main.java	to compile
2. java -cp .:jsoup-1.10.3.jar Main	to run the program

Input: path to the file directory that contains webpages (relative to where the source code is)

This program creates inverted index on top of the output of the first project (500 web pages). It creates both frequency based index and position based index. This programs generates a 2 file of inverted index (frequency based and position based), and 2 files of fileName index.

The 2 files of fileName index is used for referencing the actual file name used in the inverted index. The file names used in the inverted index are web1, web2, web3…etc in order to save space for representation purpose.