package SIMS5.simulation;

import java.util.List;

public class SimulationData {
  private PerlinNoise noise;
  private IntersectionChecker checker;
  private int[][] simulationBordes = new int[4][2];
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
    checker = new IntersectionChecker(40);
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

  public void setSimulationSize(int simSize) {
    simulationBordes[0][0] = 0;
    simulationBordes[0][1] = 0;

    simulationBordes[1][0] = simSize;
    simulationBordes[1][1] = 0;

    simulationBordes[2][0] = simSize;
    simulationBordes[2][1] = simSize;

    simulationBordes[3][0] = 0;
    simulationBordes[3][1] = simSize;
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

  public double[] rayCastCheck(int l1x1, int l1y1, int l1x2, int l1y2, int l2x1, int l2y1, int l2x2, int l2y2) {
    return checker.rayCastCheck(l1x1, l1y1, l1x2, l1y2, l2x1, l2y1, l2x2, l2y2);
  }

  public void checkHitBoxes(List<MyRobot> robots, int robotNumber) {
    int robotX = robots.get(robotNumber).getPositions()[robots.get(robotNumber).getPositions().length-1][0]; 
    int robotY = robots.get(robotNumber).getPositions()[robots.get(robotNumber).getPositions().length-1][1];
    for (int i = 0; i < robots.size(); i++) {
      int x = robots.get(i).getPositions()[robots.get(i).getPositions().length-1][0]; 
      int y = robots.get(i).getPositions()[robots.get(i).getPositions().length-1][1];
      int[] move = checker.coliding(robotX, robotY, x, y); 
      robots.get(robotNumber).moveRobot(move[0], move[1]);
    }
  }

  //--------intesecitonChecker--------

  private static class IntersectionChecker {
    int durchmesser;
    public IntersectionChecker(int pDurchmesser) {
      durchmesser = pDurchmesser;
    }

    private double[] ccdts(int l1x1, int l1y1, int l1x2, int l1y2, int l2x1, int l2y1, int l2x2, int l2y2) {
      double[] diffenences = {l1x1-l1x2, l2x1-l2x2, l1y1-l1y2, l2y1, l2y2};
      double[] constants = {(l1x1*l1y2)-(l1y1*l1x2), (l2x1*l2y2)-(l2y1*l2x2)};
      double denominator = (diffenences[0]*diffenences[3])-(diffenences[2]*diffenences[1]);
      double t = (((l1x1-l2x1)*diffenences[3])-((l1y1-l2y1)*diffenences[1]))/denominator;
      double s = (((l1x1-l1x2)*diffenences[2])-((l1y1-l1y2)*diffenences[0]))/denominator;
      double[] ccdts = {constants[0], constants[1], denominator, t, s};
      return ccdts;
    }

    public double[] rayCastCheck(int l1x1, int l1y1, int l1x2, int l1y2, int l2x1, int l2y1, int l2x2, int l2y2) {
      double[] ccdts = ccdts(l1x1, l1y1, l1x2, l1y2, l2x1, l2y1, l2x2, l2y2);
      double[] xAndY = new double[2];
      if (ccdts[2] != 0 && ccdts[3] >= 0 && ccdts[3] <= 1 && ccdts[4] >= 0 && ccdts[4] <= 1) {
        xAndY[0] = l1x1+(ccdts[3]*(l1x2-l1x1));
        xAndY[1] = l1y1+(ccdts[3]*(l1y2-l1y1));
        return xAndY;
      } else if (ccdts[2] == 0 && ((ccdts[0] == (l2x1 *l1y2)-(l2y1*l1x2)) || ccdts[1] == (l1x1*l2y2)-(l1y1*l2x2))) {
        return xAndY;
      } else {
        return xAndY;
      }
    }

    public int[] coliding(int x1, int y1, int x2, int y2) {
      int[] ins = new int[2]; 
      int diffX = x1-x2;
      int diffY = y1-y2;
      int posDiffX = diffX;
      int posDiffY = diffY;
      if (diffX<0) {
        posDiffX = (-1)*diffX;
      }
      if (diffY<0) {
        posDiffY = (-1)*diffY;
      }
      if (posDiffX <= durchmesser && posDiffY <= durchmesser) {
        ins[0] = durchmesser-diffX;
        ins[1] = durchmesser-diffY;
      } else {
        ins[0] = 0;
        ins[1] = 0;
      }
      return ins;
    } 
  }

  //----------------------------------

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