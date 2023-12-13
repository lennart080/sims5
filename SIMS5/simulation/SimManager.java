package SIMS5.simulation;

import java.util.ArrayList;
import java.util.List;

import SIMS5.Manager;
import SIMS5.calculator.Calculator;
import SIMS5.gui.GuiManager;

public class SimManager {
  private Manager manager;
  private GuiManager guiManager;
  private SimulationData simData;

  private int robotsPerRound;
  private int round = 0;
  private int simulationSize;

  private List<MyRobot> robots = new ArrayList<>();
  private List<double[][][]> bestPerformersWeights = new ArrayList<>();
  private int[] neuronLayers = {10, 10, 10};
  private double[][] fieldInfos = new double[4][3];

  public SimManager(Manager pManager, GuiManager pGuiManager, SimulationData sd) {
    manager = pManager;
    guiManager = pGuiManager;
    simData = sd;
    simulationSize = guiManager.getSimulationSize();
  }

  public void setRobotsPerRound(int pRobotsPerRound) {
    if (pRobotsPerRound < 10) {
      robotsPerRound = 10;
    } else {
      robotsPerRound = 10 * (int)((double)pRobotsPerRound/10.0);
    }
  }

  public int getRobotsPerRound() {
    return robotsPerRound;
  }

  public int getRound() {
    return round;
  }

  public double getLightIntensityAtTime() {
    return simData.getLightIntensityAtTime(manager.getTime());
  }

  public void startRound() {
    if (round == 0) {
      for (int i = 0; i < robotsPerRound; i++) {
        newRobot(newRandomPos(), -1);
      }
    } else {
      for (int i = 0; i < bestPerformersWeights.size(); i++) {
        System.out.println(i);
        for (int j = 0; j < robotsPerRound/bestPerformersWeights.size(); j++) {
          newRobot(newRandomPos(), i);
        }
      }
    }
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
      startRound();
    }
  }

  public void simulateData(int timeInMin) {         //methode für die simulations berechnungen
    if (robots.size() != 0) {
      double lightIntensity = simData.getLightIntensityAtTime(timeInMin);
      for (int i = 0; i < robots.size(); i++) {
        robots.get(i).simulate(lightIntensity);
      }    
    }
  }

  public List<MyRobot> getRobots() {
    return robots;
  }
}
