package quote.parser;

/**
 Holds the current data.
 Highly mutable struct! 
 Used to make N full Quotes, when the end of the Quote is detected (the tricky part).
 Quotes end either with an empty line, or at the end of a title, or at the end of an author.
 
 Usually, the author and the title can stay the same, and the text varies for 
 each specific quote.
 
 Nulls are used for missing values, and they indicate the state.
*/
final class ScanState {
  
  String author;
  String title;
  
  /** This part is the hardest. N lines, with pre(...) and em(...). */
  String text;
  Boolean isPreservingNewLines = false;
  
  boolean isNone() {
    return author == null && title == null && text == null;
  }
  
  boolean hasAuthor() {
    return author != null && title == null && text == null;
  }
  
  boolean hasTitle() {
    return title != null && text == null;
  }
  
  boolean hasText() {
    return text != null;
  }
}
