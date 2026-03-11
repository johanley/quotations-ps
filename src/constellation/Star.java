package constellation;

/** Data-carrier for stars. */
final class Star {
  
  /** Index from the underlying star catalog. */
  Integer INDEX;

  /** Right ascension in radians. */
  Double RA; 
  
  /** Declination in radians. */
  Double DEC; 
  
  /** Visual magnitude. */
  Double MAG;
  
  /** X-coord after a projection is applied. */
  Double X;
  
  /** Y-coord after a projection is applied. */
  Double Y;

}