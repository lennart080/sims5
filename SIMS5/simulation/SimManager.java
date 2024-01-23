package SIMS5.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
  private int[] randomEntity;
  private int[] neuronLayers;
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
  private double[][] neuronModel;
  private List<MyEntity> entitys = new ArrayList<>();
  private List<double[][][]> bestPerformersWeights = new ArrayList<>();

  //---------Simulation Start-----------

  public SimManager() {
    simData = new LightData();
  }

  public void startSimulation() {
    Thread simulationThread = new Thread(() -> {
      inicialiseSim();
      startRounds();
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
      startRounds();
    }
  }

  //---------------------------------------

  //---round handler and round generator---

  private void startRounds() { //old
    randomEntity[0] = (int)(((double)entitysPerRound/100.0)*10)+Calculator.normaliseValue(newRandom(), 1, entitysPerRound-(int)(((double)entitysPerRound/100.0)*10));
    randomEntity[1] = (int)(((double)entitysPerRound/100.0)*10)+Calculator.normaliseValue(newRandom(), 1, entitysPerRound-(int)(((double)entitysPerRound/100.0)*10));
    int gridPosX = 0;
    int gridPosY = 0;
    if (round == 0) {
      for (int i = 0; i < entitysPerRound; i++) {
        newRobot(newRandomPos(gridPosX, gridPosY), -1, false);
        gridPosX++;
        if (gridPosX >= (int)Math.sqrt(entitysPerRound)) {
          gridPosX = 0;
          gridPosY++;
        }
      }
    } else {
      for (int i = 0; i < bestPerformersWeights.size(); i++) {
        for (int j = 0; j < entitysPerRound/bestPerformersWeights.size(); j++) {
          if (j == 0) {
            newRobot(newRandomPos(gridPosX, gridPosY), i, true);
          } else if (j == 1 && i == 0) {
            newRobot(newRandomPos(gridPosX, gridPosY), -1, false);
          } else {
            newRobot(newRandomPos(gridPosX, gridPosY), i, false);
          }
          gridPosX++;
          if (gridPosX >= (int)Math.sqrt(entitysPerRound)) {
            gridPosX = 0;
            gridPosY++;
          }
        }
      }
      bestPerformersWeights.clear();
    }
    updates = 0;
    time = 0;
    day = 0;
    round++;       
  }

  private void inicialiseSim() { //new
    //spawning grid
    xGridCount = (int)Math.round(Math.sqrt(entitysPerRound));
    yGridCount = (int)((double)entitysPerRound/(double)Math.round(Math.sqrt(entitysPerRound)));
    if (xGridCount*yGridCount < entitysPerRound) {
      xGridCount++; 
    }
    xGridSize = (int)roundToDecPlaces((double)simulationSize/(double)xGridCount, 0);
    yGridSize = (int)roundToDecPlaces((double)simulationSize/(double)yGridCount, 0);
    //neuron model
    neuronModel = new double[neuronLayers.length+2][];
    neuronModel[0] = new double [guiManager.getStartStatistics().length + 3];
    for (int i = 0; i < neuronLayers.length; i++) {
      neuronModel[i+1] = new double[neuronLayers[i]];
    }
    neuronModel[neuronModel.length-1] = new double[10];
  }

  private void nextRound() {  //new
    entitys = null; 
    entitys = getBestEntitys();
    for (int i = 0; i < entitysPerRound; i++) {
      //position
      int[] position = newPosition(i);
      //neurons
      double[][] neurons = Arrays.copyOf(neuronModel, neuronModel.length);
      //weights
      if (round == 0) {
      
      } else {
      
      }
      //new entity
      entitys.add(new MyEntity(this, null, neurons, position, guiManager.getStartStatistics(), entitySize, simulationSize));
    }

    updates = 0;
    time = 0;
    day = 0;
    round++;  
  }

  private List<MyEntity> getBestEntitys() {
    List<MyEntity> oldest = new ArrayList<>();
    
    return oldest;
  }

  private int[] newPosition(int x) {  //new
    int xGridPos = x % xGridCount;
    int yGridpos = x / xGridCount;
    int[] position = {xGridPos*xGridSize, yGridpos*yGridSize};
    position[0]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, xGridSize-entitySize);
    position[1]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, yGridSize-entitySize);
    return position;
  }

  private void newRobot(int[] pPos, int bestPerformersPos, boolean old) {  
    double[][] neurons = new double[2+neuronLayers.length][];   //[] reihe [][] neuron
    double[][][] weigths = new double[neurons.length-1][][];  //[] reihe [][] neuron [][][] verbindung(2tes neuron)
    neurons[0] = new double [guiManager.getStartStatistics().length + 3];
    for (int i = 0; i < neuronLayers.length; i++) {
      neurons[1+i] = new double[neuronLayers[i]];
    }
    neurons[neurons.length-1] = new double[10];  // 0-3 inRichtungBewegen; 4-8 atk,enSp,sp,def,so upgade; 9 attack;
    for (int i = 0; i < weigths.length; i++) {
      weigths[i] = new double[neurons[i].length][neurons[i+1].length];
    }
    if (bestPerformersPos >= 0) {
      weigths = bestPerformersWeights.get(bestPerformersPos);
      for (int i = 0; i < weigths.length; i++) {
        for (int j = 0; j < weigths[i].length; j++) {
          for (int j2 = 0; j2 < weigths[i][j].length; j2++) {
            if (newRandom() > 0.8 && old == false) {
              weigths[i][j][j2]+= (newRandom()-0.25)/50;
              weigths[i][j][j2] = roundToDecPlaces(weigths[i][j][j2], 5);
            }
         }
        }
      }
    } else {
      for (int i = 0; i < weigths.length; i++) {
        for (int j = 0; j < weigths[i].length; j++) {
          for (int j2 = 0; j2 < weigths[i][j].length; j2++) {
            weigths[i][j][j2]+= ((newRandom()*3.0)-1.0);
            weigths[i][j][j2] = roundToDecPlaces(weigths[i][j][j2], 5);
         }
        }
      } 
    }
    entitys.add(new MyEntity(this, weigths, neurons, pPos, guiManager.getStartStatistics(), entitySize, simulationSize));
  }

  public void deleteRobo(MyEntity robo) {
    for (int i = 0; i < entitys.size(); i++) {
      if (entitys.get(i) == robo) {
        if (entitys.size() <= ((int)(((double)entitysPerRound/100.0)*10))-2) {
          bestPerformersWeights.add(entitys.get(i).getWeights());
        } else if (entitys.size() == randomEntity[0] || entitys.size() == randomEntity[1]) {
          bestPerformersWeights.add(entitys.get(i).getWeights());
        }
        entitys.remove(i);     
        if (entitys.size() == 0) {
          longestEntity = updates;
        } 
      }   
    }
    if (entitys.size() <= 0) {
      startRounds();
    }
  }

  private int[] newRandomPos(int pGridPosX, int pGridPosY) {  //old
    pGridPosX *= (int)(simulationSize/Math.sqrt(entitysPerRound));
    pGridPosY *= (int)(simulationSize/Math.sqrt(entitysPerRound));
    int[] pos = new int[2];
    pos[0] = pGridPosX + (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, (int)(simulationSize/Math.sqrt(entitysPerRound))-entitySize);
    pos[1] = pGridPosY + (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, (int)(simulationSize/Math.sqrt(entitysPerRound))-entitySize);
    return pos;  
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

  public double roundToDecPlaces(double value, int decPlaces) {
    return Math.round(value * (Math.pow(10, decPlaces))) / (Math.pow(10, decPlaces));
  }

  public static void sortiere(List<MyEntity> liste) {
      for (int i = 0; i < liste.size() - 1; i++) {
          for (int j = i + 1; j < liste.size(); j++) {
              if (liste.get(i).getZahl() > liste.get(j).getZahl()) {
                  MyEntity temp = liste.get(i);
                  liste.set(i, liste.get(j));
                  liste.set(j, temp);
              }
          }
      }
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

  public void setNeuronLayers(int[] pNeuronLayers) {
    neuronLayers = Arrays.copyOf(pNeuronLayers, pNeuronLayers.length);
  }

  public void setRandomlyPickedOnes(int count) {
    randomEntity = new int[count];
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
