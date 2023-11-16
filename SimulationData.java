public class SimulationData {
  private PerlinNoise noise;
  private int[][] board; 
  private double dayStart;
  private double dayEnd;
  private double lightIntensity;
  private double noiseAmplitude;
  private double noiseStrength;
  private int seed;

  public SimulationData() {              //generirien und laden des simulations umfelds
    setSeed(2334535);
    setNoiseStrength(0.4);
    setDayStart(7.0);
    setDayEnd(20.0);
    setNoiseAmplitude(10.0);
    setLightIntensity(10.0);
    noise = new PerlinNoise(seed);
    //newBoard(100, 70);
  }
  
  //set methoden der simulation daten

  public void setDayStart(double pDayStart) {
    dayStart = 7.0;
    if (23.99 > pDayStart && pDayStart >= 0.0) {
      if (dayEnd > pDayStart || dayEnd == 0.0) {
        dayStart = pDayStart;
      } 
    } 
  }

  public void setDayEnd(double pDayEnd) {
    dayEnd = 20.0;
    if (24.0 >= pDayEnd && pDayEnd > 0.1) {
      if (dayStart < pDayEnd) {
        dayEnd = pDayEnd;     
      } 
    }
  }
  
  public void setBoardSize(int x, int y) {            //erstellen der größe der simulation (einzige einstellung die nur einmal gesetzt werden kann)
    if (board == null) {
      board = new int[x][y];
    }
  }
  
  public void setLightIntensity(double intensity) {            //setzen der licht stärke in der simulation
    if (intensity >= 0) {
      lightIntensity = intensity;  
    } else {
      lightIntensity = 0;
    } 
  }
  
  public void setSeed(int pSeed) {              //setzen des seed welcher zb. für die zufällichkeit der simulation sorgt
    if (pSeed >= 1) {
      seed = pSeed;
    } else {
      seed = 1;
    } 
  }

  public void setNoiseStrength(double waight) {
    if (waight > 0 && waight < 1) {
      noiseStrength = waight;
    }
  }
  
  public void setNoiseAmplitude(double pNoiseAmplitude) {             //setzen der schwankungen mit der zeit der stärke des lichts 
    if (pNoiseAmplitude > 0.0) {
      noiseAmplitude = pNoiseAmplitude;
    } else {
      noiseAmplitude = 0.1;
    } 
  }
  
  //get methoden der simulation daten

  public double getLightIntensity() {
    return lightIntensity;
  }
  
  public double getNoiseAmplitude() {
    return noiseAmplitude;
  }
  
  public int getSeed() {
    return seed;
  }

  public double getNoiseStrength() {
    return noiseStrength;
  }
  
  public double getLightIntensityAtTime(int pTime) {              //berechnung der licht intensivität zu einer bestimmten zeit
    double perlinTime = ((double)pTime / 86400.0);
    double time;
    if (perlinTime == (int)perlinTime && perlinTime != 0.0) {
      time = 1.0;
    } else {
      time = perlinTime - (int)perlinTime;
    }
    if ((time) > (dayStart/24.0) && (time) < (dayEnd/24.0)) {
      double w = this.getNoiseStrength();
      double fx = Math.cos(time*Math.PI*(24.0/(dayEnd-dayStart)))*(-1);
      double gx = noise.getPerlinNoise((perlinTime)*noiseAmplitude);
      if (((gx*w)+(fx*(1-w))*lightIntensity) > 0.0) {
      return (gx*w)+(fx*(1-w))*lightIntensity;
      }
    }
    return 0.0;
  }

  public void Write() {                //comadline augabe zu testzweken der überprüfung von generirten funktionen
    for (int x = 0; x < noise.getpermut().length; x++) {
      System.out.println(x + ": " + noise.getpermut()[x]);
    }
  }

  //klasse und methoden zur erstellung des 1d perlin noise
  
  private static class PerlinNoise {
    private static final int TABLE_SIZE = 2048;          //bestimmt die rauschgöße des perlin noise
    private static final int[] permutation = new int[TABLE_SIZE];
    private static final int[] permutationIndices = new int[TABLE_SIZE];
    private static final boolean[] used = new boolean[TABLE_SIZE];
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
  }
}