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
  private int robotsPerRound;
  private int simulationSize;
  private int dayLengthRealTimeInSec;  
  private int[] randomRobot;
  private int[] neuronLayers;
  private int robotSize;

  //run time
  private int round = 0;            
  private int updates = 0;        
  private int time = 0;           
  private int day = 0;               
  private int longestRobot = 0;
  private List<MyRobot> robots = new ArrayList<>();
  private List<double[][][]> bestPerformersWeights = new ArrayList<>();

  public SimManager() {
    simData = new LightData();
  }

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
          for (int i = 0; i < robots.size(); i++) {
            robots.get(i).setDay(day);
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

  public void startSimulation() {
    Thread simulationThread = new Thread(() -> {
      startRounds();
      startSim();
    });
    simulationThread.start();
  }

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

  public void setRobotsPerRound(int pRobotsPerRound) {
    if (pRobotsPerRound < 10) {
      robotsPerRound = 10;
    } else {
      robotsPerRound = 10 * (int)((double)pRobotsPerRound/10.0);
    }
  }

  public void setDayLengthRealTimeInSec(int sec) {
    if (sec < 1) {
      dayLengthRealTimeInSec = 1;
    } else {
      dayLengthRealTimeInSec = sec;
    }
  }

  public void setRobotSize(int pRoboSize) {
    robotSize = pRoboSize;
  }

  public void setNeuronLayers(int[] pNeuronLayers) {
    neuronLayers = Arrays.copyOf(pNeuronLayers, pNeuronLayers.length);
  }

  public void setRandomlyPickedOnes(int count) {
    randomRobot = new int[count];
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

  public int getLongestRobot() {
    return longestRobot;
  }

  public int[] getBasePrice() {
    return guiManager.getBasePrice();
  }

  public double getMaxLight() {
    return simData.getMaxLight();
  }

  public int getRobotsPerRound() {
    return robotsPerRound;
  }

  public List<MyRobot> getRobots() {
    return robots;
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
  
  //-----------------------------------

  private void startRounds() {
    randomRobot[0] = (int)(((double)robotsPerRound/100.0)*10)+Calculator.normaliseValue(newRandom(), 1, robotsPerRound-(int)(((double)robotsPerRound/100.0)*10));
    randomRobot[1] = (int)(((double)robotsPerRound/100.0)*10)+Calculator.normaliseValue(newRandom(), 1, robotsPerRound-(int)(((double)robotsPerRound/100.0)*10));
    int gridPosX = 0;
    int gridPosY = 0;
    if (round == 0) {
      for (int i = 0; i < robotsPerRound; i++) {
        newRobot(newRandomPos(gridPosX, gridPosY), -1, false);
        gridPosX++;
        if (gridPosX >= (int)Math.sqrt(robotsPerRound)) {
          gridPosX = 0;
          gridPosY++;
        }
      }
    } else {
      for (int i = 0; i < bestPerformersWeights.size(); i++) {
        for (int j = 0; j < robotsPerRound/bestPerformersWeights.size(); j++) {
          if (j == 0) {
            newRobot(newRandomPos(gridPosX, gridPosY), i, true);
          } else if (j == 1 && i == 0) {
            newRobot(newRandomPos(gridPosX, gridPosY), -1, false);
          } else {
            newRobot(newRandomPos(gridPosX, gridPosY), i, false);
          }
          gridPosX++;
          if (gridPosX >= (int)Math.sqrt(robotsPerRound)) {
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

  private int[] newRandomPos(int pGridPosX, int pGridPosY) {
    pGridPosX *= (int)(simulationSize/Math.sqrt(robotsPerRound));
    pGridPosY *= (int)(simulationSize/Math.sqrt(robotsPerRound));
    int[] pos = new int[2];
    pos[0] = pGridPosX + (robotSize/2) + Calculator.normaliseValue(newRandom(), 1, (int)(simulationSize/Math.sqrt(robotsPerRound))-robotSize);
    pos[1] = pGridPosY + (robotSize/2) + Calculator.normaliseValue(newRandom(), 1, (int)(simulationSize/Math.sqrt(robotsPerRound))-robotSize);
    return pos;  
  }

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
    robots.add(new MyRobot(this, weigths, neurons, pPos, guiManager.getStartStatistics(), guiManager.getBasePrice(), robotSize, simulationSize));
  }

  public void deleteRobo(MyRobot robo) {
    for (int i = 0; i < robots.size(); i++) {
      if (robots.get(i) == robo) {
        if (robots.size() <= ((int)(((double)robotsPerRound/100.0)*10))-2) {
          bestPerformersWeights.add(robots.get(i).getWeights());
        } else if (robots.size() == randomRobot[0] || robots.size() == randomRobot[1]) {
          bestPerformersWeights.add(robots.get(i).getWeights());
        }
        robots.remove(i);     
        if (robots.size() == 0) {
          longestRobot = updates;
        } 
      }   
    }
    if (robots.size() <= 0) {
      startRounds();
    }
  }

  private void simulateData() {         //methode für die simulations berechnungen
    if (robots.size() != 0) {
      for (int i = 0; i < robots.size(); i++) {
        robots.get(i).simulate(simData.getLightIntensityAtTime(updates));
      }    
    }
  }
}
