package quote.parser;

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
    res.append("/author " );
    res.append(psString(author));
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
}