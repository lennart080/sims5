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
    setLightIntensity(1.0);
    setNoiseSize(0.03);
  }

  public void newNoise(int pSeed) {  //noise objekt wird erstellt
    setSeed(pSeed);
    noise = new PerlinNoise(seed);
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

  //--------------set---------------

  public void setNoiseSize(double size) {  //je kleiner deszo schneller werden die schwingungen des Noise
    if (size > 0) {
      if (size < 0.5) {
        noiseSize = size;
      } else {
        noiseSize = 0.5;
      }
    } else {
      noiseSize = 0.01;
    }
  }

  public void setLightTime(int lightTimeOf60) {  // wie lange die sonne pro tag scheint 
    if (lightTimeOf60 > 0) {
        if (lightTimeOf60 < 60) {
          lightTime = lightTimeOf60*60;          
        } else {
          lightTime = 59*60;
        }
    } else {
      lightTime = 1*60;
    }
  }
  
  public void setLightIntensity(double intensity) {            // wie stark das licht scheint
    if (intensity > 0) {
      if (intensity < 10) {
        lightIntensity = intensity;        
      } else {
        lightIntensity = 10; 
      }
    } else {
      lightIntensity = 1;
    } 
  }

  private void setSeed(int pSeed) {    // setzt den seed
    if (pSeed > 0) {
      seed = pSeed;
    } else {
      seed = 1;
    } 
  }

  public void setNoiseStrength(double weight) {  // wie starke schwankungen das Noise haben soll
    if (weight > 0) {
      if (weight < 0.1) {
        noiseStrength = weight;      
      } else {
        noiseStrength = 0.1;
      }
    } else {
      noiseStrength = 0.0;
    }
  }

  //-----------------------------
  
  //------------get--------------

  public double getMaxLight() {
    return (noiseStrength+2)*lightIntensity;
  }

  public int getMaxPermute() {
    return noise.getMaxPermute();
  }

  public double getLightIntensity() {
    return lightIntensity;
  }
  
  public int getSeed() {
    return seed;
  }

  public double getNoiseSize() {
    return noiseSize;
  }

  public double getNoiseStrength() {
    return noiseStrength;
  }

  public int[] getPermut() {
    return noise.getpermut();
  }

  //-------------perlin---------------
  
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

    //-----------get------------

    public int[] getpermut() {          
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