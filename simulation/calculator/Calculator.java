package simulation.calculator;

public class Calculator{  
  static long seed;

  public static void setSeed(int pSeed) {              //setzen des seed welcher zb. für die zufällichkeit der simulation sorgt
    if (pSeed >= 1) {
      seed = pSeed;
    } else {
      seed = 1;
    } 
  }

  public static int normaliseValue(double value, int oldMax, int newMax) {       //linear mapping
    int originalMax = oldMax;
    double originalRange = (double) (originalMax);
    double newRange = (double) (newMax);
    double scaledValue = (value * newRange) / originalRange;
    // Ensure the scaled value fits within the new range
    return (int) Math.min(Math.max(scaledValue, 0), newMax);
  }

  public static double roundToDecPlaces(double value, int decPlaces) {
    return Math.round(value * (Math.pow(10, decPlaces))) / (Math.pow(10, decPlaces));
  }

  public static double sigmoid(double value) {
    return roundToDecPlaces(1 / (1 + Math.pow(2.71, -value)), 3); 
  }

  public static int prozentage(int value, int prozentage) {
    return (int)(((double)value/100.0)*prozentage);
  }

  public static double newRandom() {   
    long a = 1103515245; // multiplier
    long c = 12345; // increment
    long m = (long) Math.pow(2, 31); // modulus
    do {
      seed = (a * seed + c) % m;     
    } while (((double) seed / m) < 0 && ((double) seed / m) > 1);        
    return (double) seed / m;  
  }
}
