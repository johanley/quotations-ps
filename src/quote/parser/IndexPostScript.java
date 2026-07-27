package quote.parser;

import java.util.regex.Pattern;

/** 
 Represent an index entry as a dictionary in PostScript.
 The output is immediately usable in a PostScript program.
*/
public final class IndexPostScript {

  /** The title can be null. */
  public IndexPostScript(String author, String title) {
    this.author = author;
    this.title = title;
  }
  
  /** 
   Returns a dict describing the index entry for PostScript consumption.
   The dict has keys for /author and (optionally) /title.
  */
  @Override public String toString() {
    StringBuilder res = new StringBuilder();
    res.append("/author-last " );
    res.append(psString(getAuthorLast()));

    res.append(" /author-first " );
    res.append(psString(getAuthorFirst()));

    res.append(" /title " );
    res.append(psString(title == null ? "" : title));
    
    return psDict(res.toString());
  }

  private String author, title;
  
  /** Replace '(' and ')' with '\(' and '\)'. */
  private String escapeParens(String text) {
    return text.replace("(", "\\(").replace(")", "\\)");
  }

  private String psString(String s) {
    return "(" + escapeParens(s) + ")";
  }
  
  private String psDict(String s) {
    return "<<" + s + ">>";
  }

  /** In the name, it separates last name from first. */
  private static final String NAME_SEPARATOR = ",";

  /** If there's no separator char (eg 'Aristotle'), then the whole name is taken to be the last name. */
  private String getAuthorLast() {
    String result = author;
    if (result.contains(NAME_SEPARATOR)) {
      String[] parts = author.split(Pattern.quote(NAME_SEPARATOR));
      if (parts.length == 2) {
        result = parts[0].trim();
      }
    }
    return result;
  }
  
  /** Empty string if there's no separator character. */
  private String getAuthorFirst() {
    String result = "";
    if (author.contains(NAME_SEPARATOR)) {
      String[] parts = author.split(Pattern.quote(NAME_SEPARATOR));
      if (parts.length == 2) {
        result = parts[1].trim();
      }
    }
    return result;
  }
}