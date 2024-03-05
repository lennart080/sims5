package SIMS5.simulation;

public class LightData {
  //objekts
  private PerlinNoise noise;

  //get set
  private double lightTime;
  private double lightIntensity;
  private double noiseStrength;
  private double noiseSize;
  private double dayLengthVariation;

  public void newNoise(int pSeed) {
    noise = new PerlinNoise(pSeed);
  }

  private double calcLight(int pTime) {  //berechnung der licht intensivität zu einer bestimmten zeit
    double time = pTime;
    double dayVariation = noise.getPerlinNoise(time-(time % 3600)+Math.round(lightTime/2)/(0.2*noise.getTableSize()))*dayLengthVariation;
    if ((lightTime+dayVariation) > 3600) {
      dayVariation = 3600-lightTime;
    }
    if ((lightTime+dayVariation) < 0) {
      dayVariation = 0;
    }
    if ((time % 3600.0) <= (lightTime+dayVariation)) {
      double gx = noise.getPerlinNoise(time/(noiseSize*noise.getTableSize()))*noiseStrength;
      double fx = Math.sin(((time % 3600.0)*Math.PI)/(lightTime+dayVariation));
      double noiseFade = (1 - Math.abs(((time % 3600.0)/(lightTime+dayVariation))-0.5)*2)/2;
      return ((gx*noiseFade*2)+fx)*lightIntensity;
    } 
    return 0.0;
  }

  public double[] calcLightOfDay(double pDay) {
    double[] lightOfDay = new double[3600];
    for (int i = 0; i < lightOfDay.length; i++) {
      if (i+(int)(pDay*3600) < 0) {
        lightOfDay[i] = getLightIntensityAtTime(0);
      } else {
        lightOfDay[i] = getLightIntensityAtTime(i+(int)(pDay*3600));
      }
    }
    return lightOfDay;
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
  
  public void setLightIntensity(double intensity) {  // wie stark das licht scheint
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

  public void setDayLengthVariation(double variation) {  //tages längen unterschied
    if (variation > 0) {
      if (variation < 500) {
        dayLengthVariation = variation;
      } else {
        dayLengthVariation = 500;
      }
    } else {
      dayLengthVariation = 0;
    }
  }

  //-----------------------------
  
  //------------get--------------

  public class DataSettings {
    private double lightTimeSave;
    private double lightIntensitySave;
    private double noiseStrengthSave;
    private double noiseSizeSave;
    private double dayLengthVariationSave;

    public DataSettings() {
      lightTimeSave = lightTime; 
      lightIntensitySave = lightIntensity;
      noiseStrengthSave = noiseStrength;
      noiseSizeSave = noiseSize;
      dayLengthVariationSave = dayLengthVariation;
    }

    public double getLightIntensity() {
      return lightIntensitySave;
    }
  
    public double getLightTime() {
      return lightTimeSave;
    }
  
    public double getNoiseStrength() {
      return noiseStrengthSave;
    }
  
    public double getNoiseSize() {
      return noiseSizeSave;
    }
  
    public double getDayLengthVariation() {
      return dayLengthVariationSave;
    } 
  }

  public DataSettings getDataSettings() {
    return new DataSettings();
  }

  public double getMaxLight() {
    return (noiseStrength+2)*lightIntensity;
  }

  public double getLightIntensityAtTime(int pTime) {              
    return calcLight(pTime);
  }

  public double[] getLightOfDay(int pDay) {
    return calcLightOfDay(pDay);
  }

  //----------------------------------

  //-------------perlin---------------
  
  private static class PerlinNoise {
    private static final int TABLE_SIZE = 2048;      
    private static final int[] permutation = new int[TABLE_SIZE];
    private static final int[] permutationIndices = new int[TABLE_SIZE];
    private static final boolean[] used = new boolean[TABLE_SIZE];
    private static int maxPermute = 0;
    private static int seed;

    public PerlinNoise(int pSeed) {           
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

    //--------------------------

    //-----------get------------

    public double getPerlinNoise(double x) {                     
      int c = (int)x & (TABLE_SIZE - 1);
      double xf = x - (int)x;
      double u = fade(xf);
      return lerp(u, grad(permutation[c], xf), grad(permutation[c + 1], xf - 1)) * 2; 
    }

    public int getTableSize() {
      return TABLE_SIZE;
    }
  }
}