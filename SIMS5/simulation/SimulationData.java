package SIMS5.simulation;

public class SimulationData {
  private PerlinNoise noise;
  private double lightTime;
  private double lightIntensity;
  private double noiseStrength;
  private double noiseSize;
  private int seed;

  public SimulationData() {        
    setNoiseStrength(0.02);
    setLightTime(40);
    setLightIntensity(80.0);
    noiseSize = 0.03;
  }

  public void setLightTime(int lightTimeOf60) {
    if (60 > lightTimeOf60 && lightTimeOf60 > 0) {
        lightTime = lightTimeOf60*60;
    } else {
      lightTime = 40*60;
    }
  }
  
  public void setLightIntensity(double intensity) {            //setzen der licht stärke in der simulation
    if (intensity >= 0) {
      lightIntensity = intensity;  
    } else {
      lightIntensity = 0;
    } 
  }
  
  public void newNoise(int pSeed) {
    setSeed(pSeed);
    noise = new PerlinNoise(seed);
  }

  private void setSeed(int pSeed) {              //setzen des seed welcher zb. für die zufällichkeit der simulation sorgt
    if (pSeed > 1) {
      seed = pSeed;
    } else {
      seed = 1;
    } 
  }

  public void setNoiseStrength(double weight) {
    if (weight > 0) {
      noiseStrength = weight;
    } else {
      noiseStrength = 0.0;
    }
  }

  public int getMaxPermute() {
    return noise.getMaxPermute();
  }
  
  //get methoden der simulation daten

  public double getLightIntensity() {
    return lightIntensity;
  }
  
  public int getSeed() {
    return seed;
  }

  public double getNoiseStrength() {
    return noiseStrength;
  }

  public int[] getPermut() {
    return noise.getpermut();
  }
  
  public double getLightIntensityAtTime(int pTime) {              //berechnung der licht intensivität zu einer bestimmten zeit
    double time = pTime;
    if ((time % 3600.0) <= lightTime) {
      double gx = noise.getPerlinNoise(time/(noiseSize*noise.getTableSize()))*noiseStrength;
      double fx = 0.0;
      fx = Math.sin(((time % 3600.0)*Math.PI)/lightTime);
      double noiseFade = (1 - Math.abs(((time % 3600.0)/lightTime)-0.5)*2)/2;
      return ((gx*noiseFade*2)+fx)*lightIntensity;
    } 
    return 0.0;
  }

  //klasse und methoden zur erstellung des 1d perlin noise
  
  private static class PerlinNoise {
    private static final int TABLE_SIZE = 2048;          //bestimmt die rauschgöße des perlin noise
    private static final int[] permutation = new int[TABLE_SIZE];
    private static final int[] permutationIndices = new int[TABLE_SIZE];
    private static final boolean[] used = new boolean[TABLE_SIZE];
    private int maxPermute = 0;
    private int seed;

    public PerlinNoise(int pSeed) {            //erstellen eines prelin musters am anfag der simulation 
      seed = pSeed;                             
      for (int i = 0; i < TABLE_SIZE; i++) {
        int index;
        do {
          index = (int) (XorShiftNext(seed) % TABLE_SIZE);
        } while (used.length < index || index < 0);
        permutationIndices[i] = index;
        used[index] = true;
      }
      for (int i = 0; i < TABLE_SIZE; i++) {
        permutation[i] = permutationIndices[i];
        if (maxPermute < permutation[i]) {
          maxPermute = permutation[i];
        } 
      }
    }

    private long XorShiftNext(int pSeed) {
        pSeed ^= (pSeed << 21);
        pSeed ^= (pSeed >>> 35);
        pSeed ^= (pSeed << 4);
        seed = pSeed;
        return pSeed;
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
      return lerp(u, grad(permutation[c], xf), grad(permutation[c + 1], xf - 1)) * 2; 
    }

    public int[] getpermut() {            //übergabe des niose musters zu testzwecken
      return permutation;
    }

    public int getMaxPermute() {
      return maxPermute;
    }

    public int getTableSize() {
      return TABLE_SIZE;
    }
  }
}