package constellation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

final class StarCatalog {
  
  StarCatalog(){
    parseInputFile();
  }
  
  Star lookup(Integer id){ return catalog.get(id); }
  
  private Map<Integer, Star> catalog = new LinkedHashMap<>();
  private static final String CATALOG_NAME = "stars.8859-1";
  
  private void parseInputFile() {
    DataFileReader reader = new DataFileReader();
    List<String> lines = reader.readFile(this.getClass(), CATALOG_NAME);
    for (String line : lines) {
      processLine(line.trim());
    }
  }
  
  /**
  Source data example:
      index,constellation,right_ascension,declination,magnitude,designation,proper_name,pm_ra,pm_dec,parallax,radial_velocity,hd
      88,Phe,0.010263089045270951,-0.8494646876675593,5.71,τ Phe,,-0.018359999999999998,-0.0058200000000000005,0.0055,8.0,224834
  I need fields 1, 3, 4, 5.
  I can simply split on the ',' character.
 */
 private void processLine(String line) {
   if (line != null && line.length() > 0 && !line.startsWith("index") ) {
     String[] parts = line.split(Pattern.quote(","));
     Star star = new Star();
     star.INDEX = Integer.valueOf(parts[0]);
     star.RA = Double.valueOf(parts[2]);
     star.DEC = Double.valueOf(parts[3]);
     star.MAG = Double.valueOf(parts[4]);
     catalog.put(star.INDEX, star);
   }
 }  
}