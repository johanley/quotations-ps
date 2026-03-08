package quote.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 Represent a quotation as a dictionary in PostScript.
 The output is immediately usable in a PostScript program.
*/
public final class QuotePostScript {
  
  public QuotePostScript(Quote quote) {
    this.quote = quote;
  }
  
  /** 
   Returns a dict describing the quote for PostScript consumption.
   The dict has keys for /author, /title, and /body.
   The author and title may have empty-string values. 
  */
  @Override public String toString() {
    StringBuilder res = new StringBuilder();
    res.append("/author " );
    res.append(psString(anonymousAuthor(quote.getAuthorFirstThenLast())));
    res.append(" /title " );
    res.append(psString(nullTitle(quote.getTitle()))); //uses the 'NULL' magic value
    res.append(" /body " );
    res.append(poetryOrProse(quote.text));
    return psDict(res.toString());
  }

  private Quote quote;
  /** Magic */
  private static final String POETRY_NEWLINE = "^NL^";
  
  /** Replace '(' and ')' with '\(' and '\)'. */
  private String escapeParens(String text) {
    return text.replace("(", "\\(").replace(")", "\\)");
  }
  
  /** 
   Replace a magic string for an unknown author with an empty string.
   The magic string is used for sorting, to place anonymous items at the end.
   ASSUMPTION: such sorting has already taken place in a list of Quote objects. 
  */
  private String anonymousAuthor(String author) {
    String res = author;
    if ("ZzzUnknown".equals(author)) {
      res = "";
    }
    return res;
  }

  /** 
   Needed for poetry, having explicit line breaks. 
   Use \n, the PostScript literal for a new-line. 
  */
  private String newLines(String text) {
    return text.replace(POETRY_NEWLINE, "\\n");
  }
  
  /** Title of the work is missing for the quotation. */
  private String nullTitle(String title) {
    return title.replace("NULL", "");
  }
  
  private boolean isPoetry(String text) {
    return text.contains(POETRY_NEWLINE);
  }
 
  /** String if poetry; array if prose. */
  private String poetryOrProse(String text) {
    String res = "";
    if (isPoetry(text)) {
      res = psString(newLines(text));
      //ASSUMPTION: no em(...) in the poetry
    }
    else {
      res = psArray(controlCodesAndStrings(quote.text));
    }
    return res;
  }
  
  private String psString(String s) {
    return "(" + escapeParens(s) + ")";
  }
  
  private String psArray(String s) {
    return "[" + s + "]";
  }
  
  private String psDict(String s) {
    return "<<" + s + ">>";
  }

  /** 
   This is the tricky bit.
   The source data has the form  '......em(....)...em(...)...'.
   The italic parts are explicit, but the regular parts are implicit. 
   The ( ) signs have cross-talk with both normal parens and escaping for PostScript strings.
   To remove the cross-talk, temporarily change em(...) to em[...] instead.
   The algo then assumes the most common case, that em[...] parts are in the middle, not at the start or end.
   Then replace those parts with control codes for the most common case.
   Finally, special handling for the other cases, when em(...) is at the start or end of the text.
   ASSUMPTION: em(...) has no parens in it.
  */
  private String controlCodesAndStrings(String text) {
    //1. avoid cross-talk: change em(...) to em[...]
    //group 1 is the text in the parens
    String SP = "(?: )?"; //possible single space at the start or end
    String regex = SP + "em\\(([^()]+?)\\)" + SP; //group-1 is the text to be italicized 
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    String res = matcher.replaceAll(" em[$1] ");
    
    //2.
    res = escapeParens(res);
    
    //3. insert control codes etc. for the most common case:  em[..] in the middle of the text
    regex = SP + "em\\[([^()]+?)\\]" + SP; //group-1 is the text to be italicized 
    pattern = Pattern.compile(regex);
    matcher = pattern.matcher(res);
    res = matcher.replaceAll(") /I($1) /R(");
    
    //4. 'clean up' the cases where em(..) may have been at the start or end
    if (res.startsWith(") /I") ) {
      res = res.substring(2); // to start with '/I' instead
    }
    else {
      res = "/R(" + res;
    }
    if (res.endsWith(") /R(")) {
      res = res.substring(0, res.length() - ") /R(".length() + 1);
    }
    else {
      res = res + ")";
    }
    return res;
  }
  
  public static void main(String... args) {
    //Quote quote = new Quote("Marcel Proust", "Le blah de blah", "This (is) an em(italicized so-called) test.");
    //Quote quote = new Quote("Marcel Proust", "Le blah de blah", "em(This is an italicized part) at the start.");
    //Quote quote = new Quote("Marcel Proust", "Le blah de blah", "This has an em(italicized part at the end.)");
    //Quote quote = new Quote("Marcel Proust", "Le blah de blah", "This (is) an em(italicized so-called) and em(lovely) test.");
    Quote quote = new Quote("Marcel Proust", "Le blah de blah", "One of the truly universal concepts that runs through almost every phase of mathematics is that of a em(function), a em(mapping) from one set to another. One could safely say that there is no part of mathematics where the notion does not arise or play a central role."); 
    QuotePostScript qps = new QuotePostScript(quote);
    String qpstest = qps.poetryOrProse(quote.text);
    System.out.println(qpstest);
  }
}