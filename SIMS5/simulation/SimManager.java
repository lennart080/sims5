package SIMS5.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import SIMS5.calculator.Calculator;
import SIMS5.gui.GuiManager;

public class SimManager {
  //objekts
  private GuiManager guiManager;
  private LightData simData;

  //get set
  private long extendetSeed;
  private int entitysPerRound;
  private int simulationSize;
  private int dayLengthRealTimeInSec;  
  private int[] hiddenLayers;
  private int randomSelected = 2;
  private int diffNetworkSelected = 0;
  private int entitySize;

  //run time
  private int round;            
  private int updates;        
  private int time;           
  private int day;               
  private int longestEntity;
  private int xGridCount;
  private int yGridCount;
  private int xGridSize;
  private int yGridSize;
  private int[] neuronLayers;
  private List<MyEntity> entitys = new ArrayList<>();
  private List<MyEntity> ageSortedEntities = new ArrayList<>();

  //---------Simulation Start-----------

  public SimManager() {
    simData = new LightData();
  }

  public void startSimulation() {
    Thread simulationThread = new Thread(() -> {
      inicialiseSim();
      nextRound();
      startSim();
    });
    simulationThread.start();
  }

  //------------------------------------

  //-------simulation and timing--------

  public void startSim() {     
    while (true) {
      long startTime, elapsedTime;
      startTime = System.nanoTime();

      this.simulateData();
      updates++;
      if (updates % 60 == 0) {
        time++;
        if (time >= 60) {
          time = 0;
        }
        if (time % 60 == 0) {
          day++;        
          for (int i = 0; i < entitys.size(); i++) {
            entitys.get(i).setDay(day);
          }
        }
      }

      elapsedTime = (System.nanoTime() - startTime)/100;
      long remainingTime = (long)Math.round((((double)dayLengthRealTimeInSec/60)*1000000)/60) - elapsedTime;
      if (remainingTime > 0) {
        try {
          Thread.sleep(remainingTime / 1000, (int) (remainingTime % 1000));       
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void simulateData() { 
    int x = 0; 
    for (int i = 0; i < entitys.size(); i++) {
      if (entitys.get(i).alive()) {
        entitys.get(i).simulate(simData.getLightIntensityAtTime(updates));
        x++;
      }
    }  
    if (x == 0) {
      longestEntity = updates;
      nextRound();
    }
  }

  //---------------------------------------

  //---round handler and round generator---

  private void inicialiseSim() { //new
    //spawning grid
    xGridCount = (int)Math.round(Math.sqrt(entitysPerRound));
    yGridCount = (int)((double)entitysPerRound/(double)Math.round(Math.sqrt(entitysPerRound)));
    if (xGridCount*yGridCount < entitysPerRound) {
      xGridCount++; 
    }
    xGridSize = (int)roundToDecPlaces((double)simulationSize/(double)xGridCount, 0);
    yGridSize = (int)roundToDecPlaces((double)simulationSize/(double)yGridCount, 0);
    //neuronLayer
    neuronLayers = new int[2+hiddenLayers.length];
    neuronLayers[0] = 12;
    for (int j = 1; j < hiddenLayers.length+1; j++) {
      neuronLayers[j] = hiddenLayers[j-1];
    }
    neuronLayers[neuronLayers.length-1] = 10;
  }

  private void nextRound() {  //new
    if (round == 0) {
      for (int i = 0; i < entitysPerRound; i++) {
        //position
        int[] position = newPosition(i);
        //neurons and weights
        List<List<Double>> neurons; 
        List<List<List<Double[]>>> weights;
        //new network
        neurons = new ArrayList<>(); 
        weights = new ArrayList<>();
        boolean[][] finised = new boolean[neuronLayers.length][];  
        for (int j = 0; j < neuronLayers.length; j++) {
          finised[j] = new boolean[neuronLayers[j]];
        }
        for (int j = 0; j < neuronLayers.length; j++) {
          neurons.add(new ArrayList<>());
          weights.add(new ArrayList<>());
          for (int l = 0; l < neuronLayers[j]; l++) {
            neurons.get(j).add(0.0); 
            if (j != 0) {
              weights.get(j).add(new ArrayList<>());
              int x2 = normaliseValue(newRandom(), 1, j-1);
              int y2 = normaliseValue(newRandom(), 1, neuronLayers[x2]);
              Double[] x = {(double)x2, (double)y2, newRandom()*2};
              weights.get(j).get(l).add(x);
              finised[x2][y2] = true;
            }
          }
        }
        for (int j = 0; j < neurons.size()-1; j++) {
          for (int l = 0; l < neurons.get(j).size(); l++) {
            if (!finised[j][l]) {
              int x2 = j + 1 + normaliseValue(newRandom(), 1, neuronLayers.length-2-j);
              int y2 = normaliseValue(newRandom(), 1, neuronLayers[x2]);
              Double[] x = {(double)j, (double)l, newRandom()*2};
              weights.get(x2).get(y2).add(x);
            }
          }
        }
        //new entity
        entitys.add(new MyEntity(this, weights, neurons, position, guiManager.getStartStatistics(), entitySize, simulationSize));
      }
    } else {
      int pos = 0;
      for (int i = 0; i < ((entitysPerRound/5)-randomSelected-diffNetworkSelected); i++) {
        for (int j = 0; j < 5; j++) {
          pos++;
          List<List<Double>> neurons; 
          List<List<List<Double[]>>> weights;
          neurons = new ArrayList<>(ageSortedEntities.get(ageSortedEntities.size()-1).getNeurons());
          weights = new ArrayList<>(ageSortedEntities.get(ageSortedEntities.size()-1).getWeights());
          ageSortedEntities.remove(ageSortedEntities.size()-1);
          mutatedAndAdd(neurons, weights, pos);
        }
      }
      for (int i = 0; i < randomSelected; i++) {
        for (int j = 0; j < 5; j++) {
          pos++;
          List<List<Double>> neurons; 
          List<List<List<Double[]>>> weights;
          int randomPos = normaliseValue(newRandom(), 1, ageSortedEntities.size()-1);
          neurons = new ArrayList<>(ageSortedEntities.get(randomPos).getNeurons());
          weights = new ArrayList<>(ageSortedEntities.get(randomPos).getWeights());
          ageSortedEntities.remove(ageSortedEntities.size()-1);
          mutatedAndAdd(neurons, weights, pos);
        }
      }
    }
    ageSortedEntities.clear();
    updates = 0;
    time = 0;
    day = 0;
    round++;  
  }

  private void mutatedAndAdd(List<List<Double>> neurons, List<List<List<Double[]>>> weights, int pos) {
    int[] position = newPosition(pos);
    for (int k = 1; k < neuronLayers.length; k++) {
      for (int j2 = 0; j2 < neuronLayers[k]; j2++) {
        if (newRandom() > 0.8) {  //random weights
          int third = normaliseValue(newRandom(), 1, weights.get(k).get(j2).size());
          Double[] x = weights.get(k).get(j2).get(third);
          x[2]+= (newRandom() * (0.9*Math.pow(Math.E, (-0.01*round))));
          weights.get(k).get(j2).set(third, x);
        }
      }
    }
    //new entity
    entitys.add(new MyEntity(this, weights, neurons, position, guiManager.getStartStatistics(), entitySize, simulationSize));
  }

  private int[] newPosition(int x) {  //new
    int xGridPos = x % xGridCount;
    int yGridpos = x / xGridCount;
    int[] position = {xGridPos*xGridSize, yGridpos*yGridSize};
    position[0]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, xGridSize-entitySize);
    position[1]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, yGridSize-entitySize);
    return position;
  }

  public void deleteRobo(MyEntity robo) {  //new
    if (entitys.size() > 0) {
      for (int i = 0; i < entitys.size(); i++) {
        if (entitys.get(i) == robo) {
          ageSortedEntities.add(entitys.get(i));
          entitys.remove(i);     
        }
      }
    }
  }

  //------------------------------------

  //------------- helper----------------

  private double newRandom() {   
    long a = 1103515245; // multiplier
    long c = 12345; // increment
    long m = (long) Math.pow(2, 31); // modulus
    do {
      extendetSeed = (a * extendetSeed + c) % m;     
    } while (((double) extendetSeed / m) < 0 && ((double) extendetSeed / m) > 1);     
    return (double) extendetSeed / m;  
  }

  public static double roundToDecPlaces(double value, int decPlaces) {
    return Math.round(value * (Math.pow(10, decPlaces))) / (Math.pow(10, decPlaces));
  }

  public static int normaliseValue(double value, int oldMax, int newMax) {     
    int originalMax = oldMax;
    double originalRange = (double) (originalMax);
    double newRange = (double) (newMax);
    double scaledValue = (value * newRange) / originalRange;
    return (int) Math.min(Math.max(scaledValue, 0), newMax);
  }

  //------------------------------------

  //---------------set------------------

  public void setSimulationSize(int pSimSize) {
    simulationSize = pSimSize;
  }

  public void setNoiseStrength(double noiseStrength) {
    simData.setNoiseStrength(noiseStrength);
  }

  public void setLightTime(int lightTime) {
    simData.setLightTime(lightTime);
  }

  public void setLightIntensity(double lightIntensity) {
    simData.setLightIntensity(lightIntensity);
  }

  public void setNoiseSize(double noiseSize) {
    simData.setNoiseSize(noiseSize);
  }

  public void setDayLengthVariation(double variation) {
    simData.setDayLengthVariation(variation);
  }

  public void setEntitysPerRound(int pEntitysPerRound) {
    if (pEntitysPerRound < 10) {
      entitysPerRound = 10;
    } else {
      entitysPerRound = 10 * (int)((double)pEntitysPerRound/10.0);
    }
  }

  public void setDayLengthRealTimeInSec(int sec) {
    if (sec < 1) {
      dayLengthRealTimeInSec = 1;
    } else {
      dayLengthRealTimeInSec = sec;
    }
  }

  public void setEntitySize(int pEntitySize) {
    entitySize = pEntitySize;
  }

  public void setHiddenLayers(int[] pNeuronLayers) {
    hiddenLayers = Arrays.copyOf(pNeuronLayers, pNeuronLayers.length);
  }

  public void setGuiManager(GuiManager gm) {
    guiManager = gm;
  }

  public void setSeed(int pSeed) {  
    if (pSeed >= 1) {
      simData.newNoise(pSeed);
      extendetSeed = pSeed;
    } else {
      simData.newNoise(1);
      extendetSeed = 1;
    } 
  }
  //------------------------------------

  //---------------get------------------

  public int getLongestEntity() {
    return longestEntity;
  }

  public double getMaxLight() {
    return simData.getMaxLight();
  }

  public int getEntitysPerRound() {
    return entitysPerRound;
  }

  public List<MyEntity> getEntitys() {
    return entitys;
  }

  public int getRound() {
    return round;
  }

  public int getTime() {
    return time;
  }

  public int getDay() {
    return day;
  }

  public int getUpdates() {
    return updates;
  }

  public int getDayLengthRealTimeInSec() {
    return dayLengthRealTimeInSec;
  }

  public double getLightIntensityAtTime() {
    if (simData != null) {
      return simData.getLightIntensityAtTime(updates);   
    }
    return 0.0;
  }

  public double[] getLightOfDay(double pDay) {
    double[] lightOfDay = new double[3600];
    for (int i = 0; i < lightOfDay.length; i++) {
      if (i+(int)(pDay*3600) < 0) {
        lightOfDay[i] = simData.getLightIntensityAtTime(0);
      } else {
        lightOfDay[i] = simData.getLightIntensityAtTime(i+(int)(pDay*3600));
      }
    }
    return lightOfDay;
  }
}
