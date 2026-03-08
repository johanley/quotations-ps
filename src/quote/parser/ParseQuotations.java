package quote.parser;

import java.util.ArrayList;
import java.util.List;

/**
 Transform raw input files into a flat form. 
 The flat form has 3 parts: quote, author, and title.
 The title is optional. 
 The author can take a magic value, representing an unknown author; in that case, the title must be present.
 
 The idea here is that the raw input form is structured, to eliminate repetition 
 of the author's name and the title. Here, we want to actually generate a format in 
 which the repetition is present, in which the full quotation always has the 3 parts.
 
 There are also special symbols in the input: 
 pre(...) - preserve white space; this is intended for poetry, where the newline is critical, and can't be ignored
 em(...) - emphasis/italics; this can't be present in poetry snippets.
 
 New lines in the raw input are always removed! 
 
  The output is ordered by: 
      - author's last name, title, order of appearance of the quote in the file
  
  First name/last name:
  In the source file, the name is : 'Smith, John'.
  In the output, the name is changed to 'John Smith'.
*/
public final class ParseQuotations {

  /** Parse through my structured text file, remembering the parse-state 'manually' as you go from line to line. */
  public List<Quote> parse(List<String> lines){
    List<Quote> result = new ArrayList<>();
    ScanState state = new ScanState();
    for(String l : lines) {
        String line = l.trim();
        if (isIgnorable(line, state)) {
          //go to the next line
        }
        else if (startBrace(line)) {
          doStartBrace(state, line);
        }
        else if (endBrace(line)) {
          doEndBrace(result, state, line);
        }
        else if (line.length() > 0) {
          doTextLine(state, line);
        }
        else if (state.hasText() && line.length() == 0){
          doEmptyTextLine(result, state);
        }
    }
    
    return result;
  }

  // PRIVATE 
  
  private static final String START_SOMETHING = "{";
  private static final String END_SOMETHING = "}";
  private static final String SPACE = " ";
  private static final String COMMENT = "#";
  
  /** Let output keep items always on a single line, by using a magic placeholder for new lines. */
  private static final String MAGIC_NL = "^NL^";
  
  /** Syntax for preserving whitespace. */
  private static final String PRE = "pre(";
  private static final String PRE_END = ")";
  
  private void doStartBrace(ScanState state, String line) {
    if (state.isNone()) {
      state.author = startOf(line); 
    }
    else if (state.hasAuthor()) {
      state.title = startOf(line);
    }
    else {
      error("Unexpected state! (start of something)", line);
    }
  }
  
  private void doEndBrace(List<Quote> result, ScanState state, String line) {
    if (state.hasText()) {
      addQuoteTo(result, state);
    }
    
    if (state.hasTitle()) {
      state.title = null;
    }
    else if (state.hasAuthor()) {
      state.author = null;
    }
    else {
      error("Unexpected state! (end of something)", line);
    }
  }

  /** Add text to the quote. */
  private void doTextLine(ScanState state, String line) {
    if (state.text == null) { //first line
      state.text = line;
      if (isStartingPre(line)) {
        state.isPreservingNewLines = true;
      }
    }
    else {
      state.text = state.text + applyNewLinePolicy(state) + line; 
      if (isEndingPre(line)) {
        state.text = trimPre(state.text);
        state.isPreservingNewLines = false;
      }
    }
  }

  private void doEmptyTextLine(List<Quote> result, ScanState state) {
    if (state.isPreservingNewLines) {
      state.text = state.text + MAGIC_NL;
    }
    else {
      //single empty line between (and only between) quotes; this delimits a quote
      addQuoteTo(result, state);
    }
  }

  private boolean startBrace(String line) {
    return line.endsWith(START_SOMETHING);
  }
  
  private String startOf(String line) {
    return line.trim().replace(START_SOMETHING, "").trim();
  }
  
  private boolean endBrace(String line) {
    return line.startsWith(END_SOMETHING);
  }
  
  /** This changes the state, by setting its text to null, since it's been consumed! */
  private void addQuoteTo(List<Quote> result, ScanState state) {
    Quote quote = new Quote(state.author, state.title /*nullable*/, state.text);
    result.add(quote);
    state.text = null;
    state.isPreservingNewLines = false;
  }
  
  private void error(String msg, String line) {
    log(msg);
    log(line);
  }
  
  private boolean isIgnorable(String line, ScanState state) {
    return isComment(line) || isEmptyAndIgnorable(line, state);
  }

  private boolean isComment(String line) {
    return line.startsWith(COMMENT);
  }
  
  private boolean isEmptyAndIgnorable(String line, ScanState state) {
    boolean isEmpty = line.length() == 0;
    boolean isIgnorable = !state.hasText();
    return isEmpty && isIgnorable;
  }

  private boolean isStartingPre(String line) {
    return line.startsWith(PRE);
  }
  
  private boolean isEndingPre(String line) {
    return line.endsWith(PRE_END);
  }
  
  private String applyNewLinePolicy(ScanState state) {
    return state.isPreservingNewLines ? MAGIC_NL : SPACE;
  }

  /** Chop off 'pre(' and ')'. */
  private String trimPre(String text) {
    String result = text;
    if (text.startsWith(PRE)){
      int start = PRE.length();
      int end = text.length() - 1;
      result = text.substring(start, end);
    }
    return result;
  }

  private static void log(String msg) {
    System.out.println(msg);
  }
}
