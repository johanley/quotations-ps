package constellation;

/** 
 An equirectangular projection centered on a constellation.
 
 <P>WARNING: This can't be used near the celestial poles.
 
 <P>The scale R of the projection is left as 1. 
 The PostScript caller will almost always need to adjust the scale, so it seems redundant to 
 include a scale here. The caller is left to 'discover' a pleasing scale by trial and error.

 https://en.wikipedia.org/wiki/Equirectangular_projection
*/
final class Projection {

  /**
   Ctor.
   It's okay for the center of projection to be near 0h.
   It's not okay for it to be near a celestial pole.
   @param ctrRaHours in hours 
   @param ctrRaMinutes in minutes 
   @param ctrDec in degrees, -80..+80
  */
  Projection(double ctrRaHours, double ctrRaMinutes, double ctrDec){
    if (Math.abs(ctrDec) > 80) {
      throw new IllegalArgumentException("Too near a celestial pole: " + ctrDec);
    }
    this.ctrRa = rads(timeToDegs(ctrRaHours, ctrRaMinutes));
    this.ctrDec = rads(ctrDec);
  }

  /**
   Assigns values to the X and Y fields of the given star. 
   Thus, this method has a side-effect of changing the state of the star object.
   The nominal unit is degrees, not radians.
  */
  void calcXY(Star star) {
    //the minus sign is because of PS conventions: 
    double dX = - continuous(star.RA - ctrRa) * Math.cos(ctrDec); 
    double dY = star.DEC - ctrDec;
    star.X = round(degs(dX), NUM_DECIMAL_DEGREES);
    star.Y = round(degs(dY), NUM_DECIMAL_DEGREES);
  }
  
  private double ctrRa;
  private double ctrDec;
  
  private static final Double DEG_TO_RADS = 2 * Math.PI/360.0;
  private static final int NUM_DECIMAL_DEGREES = 5;
  
  private double rads(double deg) {
    return deg * DEG_TO_RADS;   
  }
  
  private double degs(double rads) {
    return rads / DEG_TO_RADS;   
  }
  
  private double timeToDegs(double ctrRaHours, double ctrRaMinutes) {
    return 15.0 * (ctrRaHours + (ctrRaMinutes/60.0));
  }

  /**
   Keep the delta-RA from the center of projection within -pi..+pi. 
   The discontinuity is an issue only near 0h in right ascension. 
  */
  private double continuous(double rawDeltaRa) {
    int adjustment = 0;
    if (rawDeltaRa > Math.PI) {
      adjustment = -1;
    }
    else if (rawDeltaRa < -1 * Math.PI) {
      adjustment = +1;
    }
    return rawDeltaRa + adjustment * 2 * Math.PI;
  }
  
  /** Round the given value to the given number of decimals. */
  private double round(double val, int numDecimals) {
    //start with 5.236, and round it to two decimals
    Double factor = Math.pow(10, numDecimals); //100
    Double temp = val * factor; //523.6
    Long result = Math.round(temp); //524
    return result.doubleValue() / factor; //5.24, this avoids integer division 
  }
}
