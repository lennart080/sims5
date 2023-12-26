package SIMS5.calculator;

public class Calculator{  
  public static int normaliseValue(double value, int oldMax, int newMax) {       //linear mapping
    int originalMax = oldMax;
    double originalRange = (double) (originalMax);
    double newRange = (double) (newMax);
    double scaledValue = (value * newRange) / originalRange;
    // Ensure the scaled value fits within the new range
    return (int) Math.min(Math.max(scaledValue, 0), newMax);
  }
}
