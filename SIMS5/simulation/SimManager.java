package SIMS5.simulation;

import java.util.ArrayList;
import java.util.List;

import SIMS5.calculator.Calculator;
import SIMS5.gui.GuiManager;

public class SimManager {
  private GuiManager guiManager;
  private SimulationData simData;

  private long extendetSeed;
  private int robotsPerRound;
  private int round = 0;
  private int simulationSize;
  private int updates = 0;        //anzahl der updates seit start des programms (60updates = 1zeiteinheit)
  private int time = 0;           //fictive zeiteinheit (60ze = 1tag)
  private int day = 0;               //in game tag (relativ zur runde)
  private int dayLengthRealTimeInSec = 60;  
  private int longestRobot;

  private List<MyRobot> robots = new ArrayList<>();
  private List<double[][][]> bestPerformersWeights = new ArrayList<>();
  private int[] neuronLayers = {12, 9, 11};
  private double[][] fieldInfos = new double[4][3];


  public SimManager() {
    simData = new SimulationData();
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
    simulationSize = guiManager.getSimulationSize(); 
    simData.setSimulationSize(simulationSize);
    Thread simulationThread = new Thread(() -> {
      startRounds();
      startSim();
    });
    simulationThread.start();
  }

  //---------------set------------------
  public void setRobotsPerRound(int pRobotsPerRound) {
    if (pRobotsPerRound < 10) {
      //robotsPerRound = 10;
      robotsPerRound = 2;
    } else {
      robotsPerRound = 10 * (int)((double)pRobotsPerRound/10.0);
    }
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

  public double getLightIntensityAtTime(int pTime) {
    if (simData != null) {
      return simData.getLightIntensityAtTime(pTime);   
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
    if (round == 0) {
      for (int i = 0; i < robotsPerRound; i++) {
        newRobot(newRandomPos(), -1);
      }
    } else {
      for (int i = 0; i < bestPerformersWeights.size(); i++) {
        for (int j = 0; j < robotsPerRound/bestPerformersWeights.size(); j++) {
          newRobot(newRandomPos(), i);
        }
      }
      bestPerformersWeights.clear();
    }
    updates = 0;
    time = 0;
    day = 0;
    round++;       
  }

  private int[] newRandomPos() {
    int[] pos = new int[2];
    pos[0] = Calculator.normaliseValue(newRandom(), 1, simulationSize);
    pos[1] = Calculator.normaliseValue(newRandom(), 1, simulationSize);  
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

  private void newRobot(int[] pPos, int bestPerformersPos) {  
    double[][] neurons = new double[2+neuronLayers.length][];   //[] reihe [][] neuron
    double[][][] weigths = new double[neurons.length-1][][];  //[] reihe [][] neuron [][][] verbindung(2tes neuron)
    neurons[0] = new double [guiManager.getStartStatistics().length+(fieldInfos[0].length*fieldInfos.length)];
    for (int i = 0; i < neuronLayers.length; i++) {
      neurons[1+i] = new double[neuronLayers[i]];
    }
    neurons[neurons.length-1] = new double[10];  // 0-3 inRichtungBewegen; 4-8 atk,enSp,sp,def,so upgade; 9 attack;
    for (int i = 0; i < weigths.length; i++) {
      weigths[i] = new double[neurons[i].length][neurons[i+1].length];
    }
    if (bestPerformersPos >= 0) {
      weigths = bestPerformersWeights.get(bestPerformersPos);
    }
    for (int i = 0; i < weigths.length; i++) {
      for (int j = 0; j < weigths[i].length; j++) {
        for (int j2 = 0; j2 < weigths[i][j].length; j2++) {
          weigths[i][j][j2]+= (newRandom()-0.5)/10;
        }
      }
    }
    robots.add(new MyRobot(this, weigths, neurons, pPos, guiManager.getStartStatistics(), guiManager.getBasePrice()));
  }

  public void deleteRobo(MyRobot robo) {
    for (int i = 0; i < robots.size(); i++) {
      if (robots.get(i) == robo) {
        if (robots.size() <= (int)(((double)robotsPerRound/100.0)*10)) {
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

  public void checkHitBoxes(int roboNumber) {
    for (int i = 0; i < robots.size(); i++) {
      if (robots.get(i).getSerialNumber() == roboNumber) {
        simData.checkHitBoxes(robots, i);
      }
    }  
  }
}
