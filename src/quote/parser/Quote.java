package quote.parser;

import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;
import static java.util.Comparator.naturalOrder;

/** 
 Model object. Simple struct.
*/
public final class Quote implements Comparable<Quote>{
  
  public Quote(String author, String title, String text){
    this.author = author;
    this.title = title;
    this.text = text;
  }

  /** No new lines. */
  String author;
  
  /** Optional/nullable. No new lines. */
  String title;

  /** May contain new lines. */
  String text;
  
  public String getRawAuthor() {
    return author;
  }
  
  public String getRawTitle() {
    return title;
  }
  
  public String getRawText() {
    return text;
  }
  
  /** 
   Intended for output to a file. 
   If the name has a comma in it, the parts are reversed; 'Smith, John' becomes 'John Smith'.
  */
  @Override public String toString() {
    return getAuthorFirstThenLast() + SEP + getTitle() + SEP + text;
  }
  
  public String getTitle() {
    return (title == null || title.trim().length() == 0) ? NULL : title; 
  }
      
  public String getAuthorFirstThenLast() {
    String result = author;
    if (result.contains(NAME_SEPARATOR)) {
      String[] parts = author.split(Pattern.quote(NAME_SEPARATOR));
      if (parts.length == 2) {
        result = parts[1].trim() + " " + parts[0].trim();
      }
    }
    return result;
  }

  /**
   Sort by name, then title (if present).
   In the data, the name is 'Smith, John', so it can be used directly, without parsing into first name and last name.
  */
  @Override public int compareTo(Quote that) {
    return COMPARATOR.compare(this, that);
  }    
  
  @Override public boolean equals(Object aThat) {
    //unusual: multiple return statements
    if (this == aThat) return true;
    if (!(aThat instanceof Quote)) return false;
    Quote that = (Quote)aThat;
    for(int i = 0; i < this.getSigFields().length; ++i){
      if (!Objects.equals(this.getSigFields()[i], that.getSigFields()[i])){
        return false; 
      }
    }
    return true;
  }
  
  @Override public int hashCode() {
    return Objects.hash(getSigFields());
  }
  
  // PRIVATE 
  
  /** Separates the parts when outputting to a file. */
  private static final String SEP = "|";
  
  /** Magic string for missing titles. */
  private static final String NULL = "NULL";
  
  /** In the name, it separates last name from first. */
  private static final String NAME_SEPARATOR = ",";
  
  /** 
    The equals, hashCode and compareTo methods will usually reference
    the same fields, in the same order.
   */
   private Object[] getSigFields() {
     return new Object[] {
       //start with the data that's most likely to differ  
       author, title
     };
   }

   /** 
    To avoid creating an object each time a comparison is done, you should 
    usually store it in a static private field.
   */
   private static Comparator<Quote> COMPARATOR = getComparator();
   /** Be consistent with equals: use the same fields as getSigFields().*/
   private static Comparator<Quote> getComparator(){
     Comparator<Quote> result = 
       comparing(Quote::getRawAuthor)
       .thenComparing(Quote::getRawTitle, nullsLast(naturalOrder()))
     ;
     return result;
   }
}
