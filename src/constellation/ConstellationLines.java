package constellation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Stick figures for outlining the constellations.  */
final class ConstellationLines {

  /** 
   The ctor reads in the data file. 
   The data file exists in the same directory as this class. 
  */
  ConstellationLines(){
     parseInputFile();
  }
  
  /** The polylines for the given constellation. */
  List<List<Integer>> polylinesFor(String constellationAbbr){
    return lines.get(constellationAbbr);
  }

  // PRIVATE 
  
  private Map<String/*Ari*/ , List<List<Integer>> /*1..N polylines*/> lines = new LinkedHashMap<>();
  private static final String CATALOG_NAME = "constellation-lines-hip.8859-1";
  
  private void parseInputFile() {
    DataFileReader reader = new DataFileReader();
    List<String> lines = reader.readFile(this.getClass(), CATALOG_NAME);
    for (String line : lines) {
      processLine(line.trim());
    }
  }
  
  /**
   Source data example (came from another project, in javascript-world):
     Cnc = [43103, 42806, 42911, 44066];[40526, 42911]
   Each line is a single constellation, and almost every constellation is present. 
   Some constellations are faint, and have no stars to join (in this implementation).
  */
  private void processLine(String line) {
    int equals = line.indexOf("=");
    String constellationAbbr = line.substring(0, equals).trim();
    List<List<Integer>> polylinesIds = new ArrayList<>();
    
    String polylines = line.substring(equals+1).trim(); // [43103, 42806, 42911, 44066];[40526, 42911]
    //use regexes to grab each single-line
    String START = Pattern.quote("[");
    String END = Pattern.quote("]");
    String COMMA = Pattern.quote(",");
    Pattern singleLine = Pattern.compile(START + "(.*?)" + END); //1 matching group: '43103, 42806, 42911, 44066' Reluctant qualifier!
    
    //split the matching group around the comma
    Matcher matcher = singleLine.matcher(polylines);
    while (matcher.find()) {
      String oneLine = matcher.group(1);
      String[] parts = oneLine.split(COMMA);
      List<Integer> polylineIds = new ArrayList<>();
      for(String part : parts) {
        Integer id = Integer.valueOf(part.trim());
        polylineIds.add(id);
      }
      polylinesIds.add(polylineIds);
    }
    lines.put(constellationAbbr, polylinesIds);
  }
}