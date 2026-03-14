package quote.flatfile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import quote.parser.IndexPostScript;
import quote.parser.ParseQuotations;
import quote.parser.Quote;
import quote.parser.QuotePostScript;

/**
  Parse all quotations and generate a text file with all quotes, one per line. 
  
  The order of the quotes: 
    the author's last name, then the title (if present), then the quote (as it appears in the source text file).
  Unknown authors use a magic string for the author-name, which places 
  them at the end of the output file.
*/
public final class ParseAndGenerateFile {

  /**
   The structured input file. 8859-1 encoding.
   See the comments in the file itself for a description of its syntax. 
   */
  public static final String INPUT_FILE = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-8859-1\\quotes.txt";
  public static final String OUTPUT_FILE_PS = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-8859-1\\quotes_flat_file_ps.txt";
  public static final String OUTPUT_INDEX_FILE_PS = "C:\\johanley\\ProjectsPhoton\\quotations-ps\\quotes-data-8859-1\\index_file_ps.txt";
  final static Charset ENCODING = StandardCharsets.ISO_8859_1;  
  
  public static void main(String... args) throws IOException {
    log("Reading in " + INPUT_FILE);
    List<String> lines = lines(INPUT_FILE);
    
    log("Parsing...");
    ParseQuotations parser = new ParseQuotations();
    List<Quote> quotes = parser.parse(lines);
    Collections.sort(quotes);
    
    log("Outputting to " + OUTPUT_FILE_PS);
    outputToPostScriptFile(quotes, OUTPUT_FILE_PS);
    
    log("Outputting to " + OUTPUT_INDEX_FILE_PS);
    outputToPostScriptFile(indexFrom(quotes), OUTPUT_INDEX_FILE_PS);
    log("Done.");
  }
  
  private static List<String> lines(String fileName) throws IOException {
    List<String> result = new ArrayList<String>();
    Path path = Paths.get(fileName);
    try (Scanner scanner =  new Scanner(path, ENCODING.name())){
      while (scanner.hasNextLine()){
        result.add(scanner.nextLine());
      }      
    }
    return result;
  }
  
  /** N lines, 1 per quote, using QuotePostScript.toString() as the content of the line. */
  private static void outputToPostScriptFile(List<Quote> quotes, String fileName) throws IOException {
    Path path = Paths.get(fileName);
    try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
      for(Quote quote : quotes){
        QuotePostScript quotePS = new QuotePostScript(quote);
        writer.write(quotePS.toString());
        writer.newLine();
      }
      //the idea is to have all quotes terminate with a newline; never with end-of-file.
      writer.write("# This line is ignored. The file terminates exactly here ->.");
    }
  }
  
  private static Map<String /*author*/, Set<String/*title*/>> indexFrom(List<Quote> quotes){
    Map<String, Set<String>> res = new LinkedHashMap<>();
    for(Quote quote : quotes) {
      if (!res.containsKey(quote.getRawAuthor())) {
        res.put(quote.getRawAuthor(), new LinkedHashSet<>());
      }
      res.get(quote.getRawAuthor()).add(quote.getRawTitle()); //can be null!
    }
    //if an author has > 1 title, then remove any null title from the set
    for(String author : res.keySet()) {
      Set<String> titles = res.get(author);
      if (titles.size() > 1 && titles.contains(null)) {
        titles.remove(null);
      }
    }
    return res;
  }
  
  /** N lines, 1 per title. */
  private static void outputToPostScriptFile(Map<String, Set<String>> indexLines, String fileName) throws IOException {
    Path path = Paths.get(fileName);
    try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
      for(String author : indexLines.keySet()) {
        for(String title : indexLines.get(author)) {
          IndexPostScript indexPS = new IndexPostScript(author, title);
          writer.write(indexPS.toString());
          writer.newLine();
        }
      }
      //the idea is to have all lines terminate with a newline; never with end-of-file.
      writer.write("# This line is ignored. The file terminates exactly here ->.");
    }
  }
  
  /** Replace '(' and ')' with '\(' and '\)'. */
  private String escapeParens(String text) {
    return text.replace("(", "\\(").replace(")", "\\)");
  }
  
  
  private static void log(String msg) {
    System.out.println(msg);
  }
}