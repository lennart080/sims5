public class SimulationData {
  private PerlinNoise noise;
  private int[][] board; 
  private double lightIntensity;
  private double lightAmplitude;
  private int seed;
  public SimulationData() {
    noise = new PerlinNoise(seed);
    //newBoard(100, 70);
  }
  
  //set methoden der simulation daten
  
  public void setBoardSize(int x, int y) {            //erstellen der größe der simulation (einzige einstellung die nur einmal gesetzt werden kann)
    if (board == null) {
      board = new int[x][y];
    }
  }
  
  public void setLightIntensity(int intensity) {            //setzen der licht stärke in der simulation
    if (intensity >= 0) {
      lightIntensity = intensity;  
    } else {
      lightIntensity = 0;
    } 
  }
  
  public void setSeed(int pSeed) {              //setzen des seed welcher zb. für die zufällichkeit der simulation sorgt
    if (pSeed >= 0) {
      seed = pSeed;
    } else {
      seed = 0;
    } 
  }
  
  public void setLightChangeStrength(double pAmplitude) {             //setzen der schwankungen mit der zeit der stärke des lichts 
    if (pAmplitude >= 0.0) {
      if (pAmplitude <= 2.0) {
        lightAmplitude = pAmplitude;
      } else {
        lightAmplitude = 2.0;
      }
    } else {
      lightAmplitude = 0.0;
    } 
  }
  
  //get methoden der simulation daten

  public double getLightIntensity() {
    return lightIntensity;
  }
  
  public double getLightChangeStrenght() {
    return lightAmplitude;
  }
  
  public int getSeed() {
    return seed;
  }
  
  public double getLightIntensityAtTime(int time) {              //berechnung der licht intensivität zu einer bestimmten zeit
    double lightIntensityAtTime = noise.getPerlinNoise((double)time/10.0);                  //nicht final
    return lightIntensityAtTime;
  }

  //klasse und methoden zur erstellung des 1d perlin noise
  
  private static class PerlinNoise {
    private static final int TABLE_SIZE = 256;
    private static final int[] permutation = new int[2*TABLE_SIZE];

    public PerlinNoise(int pSeed) {                                 //erstellen eines prelin musters am anfag der simulation 
      for (int i = 0; i < TABLE_SIZE; i++) {
        permutation[i] = permutation[i + TABLE_SIZE] = (i + pSeed) & (TABLE_SIZE - 1); 
      }
    }

    private double fade(double t) {
      return t * t * t * (t * (t* 6 -15) +10);
    }

    private double lerp(double t, double a, double b) {
      return a + t * (b - a);
    }

    private double grad(int hash, double x) {
      int h = hash & 15;
      double grad = 1.0 + (h & 7);
      if ((h & 8) != 0) grad = -grad;
      return (grad * x);
      
    }

    public double getPerlinNoise(double x) {                         // methode welche die y pos des noise musters bei angabe des x ausgibt
      int c = (int)x & (TABLE_SIZE - 1);
      double xf = x - (int)x;
      double u = fade(xf);
      System.out.println("c :" + c + " xf: " + xf + " u: "+ u);
      System.out.println("lerp: " + (lerp(u, grad(permutation[c], xf), grad(permutation[c + 1], xf - 1)) * 2));
      return lerp(u, grad(permutation[c], xf), grad(permutation[c + 1], xf - 1)) * 2; 
    }
  }
}