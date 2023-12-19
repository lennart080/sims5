package SIMS5.simulation;

import java.util.ArrayList;
import java.util.List;

import SIMS5.calculator.Calculator;
import SIMS5.gui.GuiManager;

public class SimManager {
  private GuiManager guiManager;
  private SimulationData simData;


  private int robotsPerRound;
  private int round = 0;
  private int simulationSize;
  private int updates = 0;        //anzahl der updates seit start des programms //um programmSpeed pro sec 
  private int time = 0;           //fictive zeiteinheit 60ze = 1tag
  private int day = 0;               //in game tag (relativ zur runde)
  private int dayLengthRealTimeInSec = 60;  

  private List<MyRobot> robots = new ArrayList<>();
  private List<double[][][]> bestPerformersWeights = new ArrayList<>();
  private int[] neuronLayers = {10, 10, 10};
  private double[][] fieldInfos = new double[4][3];

  public SimManager() {
    simData = new SimulationData();
  }

  public void startSim() {     
    while (true) {
      long startTime, elapsedTime;
      startTime = System.nanoTime();

      this.simulateData(time);
      updates++;
      if (updates % 60 == 0) {
        time++;
        if (time % 60 == 0) {
          day++;        
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
    Thread simulationThread = new Thread(() -> {
      startRounds();
      startSim();
    });
    simulationThread.start();
  }

  //---------------set------------------
  public void setRobotsPerRound(int pRobotsPerRound) {
    if (pRobotsPerRound < 10) {
      robotsPerRound = 10;
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
    } else {
      simData.newNoise(1);
    } 
  }
  //------------------------------------

  //---------------get------------------
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
      return simData.getLightIntensityAtTime(time);   
    }
    return 0.0;
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
    }
    updates = 0;
    time = 0;
    day = 0;
    round++;       
  }

  private int[] newRandomPos() {
    int[] pos = new int[2];
    pos[0] = Calculator.normaliseValue(Calculator.newRandom(), 1, simulationSize);
    pos[1] = Calculator.normaliseValue(Calculator.newRandom(), 1, simulationSize);  
    return pos;  
  }

  private void newRobot(int[] pPos, int bestPerformersPos) {  
    double[][] neurons = new double[2+neuronLayers.length][];   //[] reihe [][] neuron
    double[][][] weigths = new double[neurons.length-1][][];  //[] reihe [][] neuron [][][] verbindung(2tes neuron)
    neurons[0] = new double [guiManager.getStartStatistics().length+(fieldInfos[0].length*fieldInfos.length)];
    for (int i = 0; i < neuronLayers.length; i++) {
      neurons[1+i] = new double[neuronLayers[i]];
    }
    neurons[neurons.length-1] = new double[10];  // 0-3 inRichtungBewegen; 4-7 atk,enSp,sp,def upgade; 8 attack; 9 kind
    for (int i = 0; i < weigths.length; i++) {
      weigths[i] = new double[neurons[i].length][neurons[i+1].length];
    }
    if (bestPerformersPos >= 0) {
      weigths = bestPerformersWeights.get(bestPerformersPos);
    }
    for (int i = 0; i < weigths.length; i++) {
      for (int j = 0; j < weigths[i].length; j++) {
        for (int j2 = 0; j2 < weigths[i][j].length; j2++) {
          weigths[i][j][j2]+= (Calculator.newRandom()-0.5)/10;
        }
      }
    }
    robots.add(new MyRobot(this, weigths, neurons, pPos, guiManager.getStartStatistics()));
  }

  public void deleteRobo(int roboNumber) {
    for (int i = 0; i < robots.size(); i++) {
      if (robots.get(i).getSerialNumber() == roboNumber) {
        if (robots.size() <= Calculator.prozentage(robotsPerRound, 10)) {
          bestPerformersWeights.add(robots.get(i).getWeights());
        }   
        robots.remove(i);       
      }   
    }
    if (robots.size() <= 0) {
      startRounds();
    }
  }

  private void simulateData(int timeInMin) {         //methode für die simulations berechnungen
    if (robots.size() != 0) {
      double lightIntensity = simData.getLightIntensityAtTime(timeInMin);
      for (int i = 0; i < robots.size(); i++) {
        robots.get(i).simulate(lightIntensity);
      }    
    }
  }
}
