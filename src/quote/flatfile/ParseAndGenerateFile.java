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
import java.util.List;
import java.util.Scanner;

import quote.parser.ParseQuotations;
import quote.parser.Quote;
import quote.parser.QuotePostScript;

/**
  Parse all quotations and generate a text file with 
  all quotes, one per line. 
  
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
  
  private static void log(String msg) {
    System.out.println(msg);
  }
}