package quote.flatfile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public final class MakeIndex {
  
  public static void main(String[] args) throws IOException {
    String input = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-1252\\quotes_flat_file_ps.txt";
    String output = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-1252\\index_file_ps.txt";
    MakeIndex makeIndex = new MakeIndex();
    List<String> lines = makeIndex.sortedQuotes(input);
    makeIndex.makeIndex(lines, output);
    log("Done");
  }
  
  /**
   Sort the source quotes alphabetically, and remove '#' comments.
   This a simple line-by-line 'lexicographic' sort of the line-strings.
   Outputs a new file, with 'sorted' appended to the given input file name.
  */ 
  List<String> sortedQuotes(String input) throws IOException {
    log("Source file: " + input);
    List<String> lines = read(input);
    log("Num lines in source file: " + lines.size());
    lines.removeIf(line -> line.trim().startsWith(COMMENT));
    log("Num lines in source file, with comments removed: " + lines.size());
    String sortedInput = input + ".sorted";
    log("Sorting and writing to " + sortedInput);
    Collections.sort(lines);
    write(lines, sortedInput);
    return lines;
  }
  
  /** 
   Make an 'index' out of the source data.
   The source data is here assumed to be sorted already, and has comments removed.
   
   <P>The resulting index has no page numbers. 
   It lists the authors alphabetically (last name, first name, title, body), along with the titles of their works.
    
   <P>WARNING: overwrites the output file. 
  */
  void makeIndex(List<String> sortedLines, String output) throws IOException {
    log("Making index file.");
    Set<String> sortedIndexLines = new LinkedHashSet<>();
    for(String line : sortedLines) {
      sortedIndexLines.add(removeBodyEntry(line));
    }
    List<String> sortedList = new ArrayList<String>(sortedIndexLines);
    log("Num lines in index: " + sortedList.size());
    log("Writing index file: " + output);
    write(sortedList, output);
  }

  private final static Charset ENCODING = Charset.forName("windows-1252");  
  private final static String COMMENT = "#";
  

  private String removeBodyEntry(String dict) {
    int body = dict.indexOf("/body");
    return dict.substring(0, body - 1) + ">>";
  }
  
  private List<String> read(String fileName) throws IOException {
    List<String> result = new ArrayList<String>();
    Path path = Paths.get(fileName);
    try (Scanner scanner =  new Scanner(path, ENCODING.name())){
      while (scanner.hasNextLine()){
        result.add(scanner.nextLine());
      }      
    }
    return result;
  }
  
  private void write(List<String> lines, String fileName) throws IOException {
    Path path = Paths.get(fileName);
    int count = 0;
    try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
      for(String line : lines){
        ++count;
        writer.write(line);
        if (count < lines.size()) writer.newLine();
      }
    }
  }
  
  private static void log(String msg) {
    System.out.println(msg);
  }
}
