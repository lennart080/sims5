package simulation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import simulation.calculator.Calculator;
import simulation.panels.MyPanelData;
import simulation.panels.MyPanelGraphs;
import simulation.panels.MyPanelRobotData;
import simulation.panels.MyPanelSimulation;
public class Manager {                        //Manager zuständig für timings und updates
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private MyPanelRobotData robotDataPanel;
  private SimulationData simulationData;

  private List<MyRobot> robots = new ArrayList<>();
  private int robotsPerRound = 10;
  private List<double[][][]> bestPerformersWeights = new ArrayList<>();
  private int permutPos = 0;

  private int[] neuronLayers = {10, 10, 10};
  private double[] startStatistics = new double[9];
  private double[][] fieldInfos = new double[4][3];
  private int startSeed = 54674;

  private Timer simulationTimer;
  private Timer guiTimer;

  private int basePrice = 10;
  private int round = 0;
  private int updates = 0;        //anzahl der updates seit start des programms  
  private int time = 0;           //simulations zeit in sec
  private int programmSpeed = 10;   //um ... schneller als echtzeit (0-100)
  private int simulationUpdatesPerSec = 1;
  private int fps;
  private int sollFps = 100;

  private int fpsCounter = 0;
  private long timeSave = System.currentTimeMillis()/1000;

  public Manager() {
    SwingUtilities.invokeLater(() -> {;                     //erstellen der graphic elemente
      simulationPanel = new MyPanelSimulation();
      dataPanel = new MyPanelData();
      graphPanel = new MyPanelGraphs();
      robotDataPanel = new MyPanelRobotData();
      screen = new MyFrame(this, simulationPanel, dataPanel, graphPanel, robotDataPanel);
    });
    
    simulationData = new SimulationData(startSeed);
    Calculator.setSeed(startSeed);

    //energie                       energie des robos welche für vortbewegung und attaken und alles weitere benötigt wird
    startStatistics[0] = 100.0;
    //schrott (int)                 währung mit welcher teile und kinder "hergestellt" werden können
    startStatistics[1] = 100.0;
    //attack                        schaden welcher pro atacke zugerichtet wird
    startStatistics[2] = 0.0;
    //energie speicher              max energie die der robo haben kann
    startStatistics[3] = 100.0;
    //speed                         ...pixel pro sec werden max zurückgelegt     
    startStatistics[4] = 10.0;
    //defense                       wird von der gegnerischen attake abgezogen
    startStatistics[5] = 0.0; 
    //health                        anzahl der leben welche von ataken veringert werden kann und sinkt wenn energie 0 ist. bei 0 leben stirbt er
    startStatistics[6] = 5.0;       
    //rust                          rost bildet sich wenn der robo länger auf der stelle steht, je mehr rost deszo mehr energie verbrauch 
    startStatistics[7] = 0.0; 
    //solar                         solar panele welche energie gewinnen
    startStatistics[8] = 1.0; 
  
    ActionListener taskPerformerSimulation = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        updates++;
        simulateData(); 
        if (updates == (int)((double)updates/10.0)*10) {
          time++;       
        } 
      }
    };
    simulationTimer = new Timer(1000/(simulationUpdatesPerSec*programmSpeed), taskPerformerSimulation);
    simulationTimer.start(); 

    /* 
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        // Hier wird die Aufgabe definiert, die alle 100 Mikrosekunden ausgeführt wird
    Runnable task = () -> {
        // Deine Aufgabenlogik hier einfügen
      System.out.println("Aufgabe ausgeführt!");
    };

        // Zeitintervall in Mikrosekunden (hier 100 Mikrosekunden = 0.1 Millisekunden)
    long intervalMicroseconds = 100;

        // Umrechnung des Intervalls in Nanosekunden für die Planung des Tasks
    long intervalNanos = TimeUnit.MICROSECONDS.toNanos(intervalMicroseconds);

        // Starte die Aufgabe mit dem angegebenen Intervall
    executor.scheduleAtFixedRate(task, 0, intervalNanos, TimeUnit.NANOSECONDS);
    */

    ActionListener taskPerformerGui = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        fpsUpdate();
        updateGraphicData();
        updateScreen();
      }
    };
    guiTimer = new Timer(1000/sollFps, taskPerformerGui);
    guiTimer.start();
  }  
  
  private void fpsUpdate() {                                              //calkuliren der angezegten bilder pro secunde
    if ((timeSave+1) <= (System.currentTimeMillis()/1000)) {
      timeSave = System.currentTimeMillis()/1000;
      fps = fpsCounter;
      fpsCounter = 0;
    }
    fpsCounter++;
  }

  public void startRound() {
    int maxInt = simulationData.getMaxPermute();
    if (round == 0) {
      for (int i = 0; i < robotsPerRound; i++) {
        int[] pos = new int[2];
        pos[0] = Calculator.normaliseValue((double)simulationData.getPermut()[permutPos*2], maxInt, screen.getScreenWidth());
        pos[1] = Calculator.normaliseValue((double)simulationData.getPermut()[(permutPos*2)+1], maxInt, screen.getScreenHeight());  
        permutPos+= 2;        
        newRobot(pos, -1);
      }
    } else {
      System.out.println(round);
      for (int i = 0; i < bestPerformersWeights.size(); i++) {
        for (int j = 0; j < robotsPerRound/bestPerformersWeights.size(); j++) {
          int[] pos = new int[2];
          pos[0] = Calculator.normaliseValue((double)simulationData.getPermut()[permutPos*2], maxInt, screen.getScreenWidth());
          pos[1] = Calculator.normaliseValue((double)simulationData.getPermut()[(permutPos*2)+1], maxInt, screen.getScreenHeight());    
          permutPos+= 2;       
          newRobot(pos, i);  
        }
      }
    }
    round++;       
  }

  private void newRobot(int[] pPos, int bestPerformersPos) {  
    double[][] neurons = new double[2+neuronLayers.length][];   //[] reihe [][] neuron
    double[][][] weigths = new double[neurons.length-1][][];  //[] reihe [][] neuron [][][] verbindung(2tes neuron)
    neurons[0] = new double [startStatistics.length+(fieldInfos[0].length*fieldInfos.length)];
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
    robots.add(new MyRobot(this, weigths, neurons, pPos, startStatistics));
  }

  public void simulateData() {         //methode für die simulations berechnungen
    if (robots.size() != 0) {
      double LightIntensity = simulationData.getLightIntensityAtTime(time);
      for (int i = 0; i < robots.size(); i++) {
        robots.get(i).simulate(LightIntensity);
      }    
    }
  }
  
  private void updateGraphicData() {               //methode für die daten updates die graphic panele
    simulationPanel.myUpdate(updates, time);  
    dataPanel.myUpdate(fps);
    graphPanel.myUpdate(simulationData.getLightIntensityAtTime(time));
    //test
    if (robots.size() != 0) {
      int[] roboNumber = new int[robots.size()];
      for (int i = 0; i < roboNumber.length; i++) {
        roboNumber[i] = robots.get(i).getSerialNumber();
      }
      int[][] roboPos = new int[robots.size()][2];
      for (int i = 0; i < roboPos.length; i++) {
        roboPos[i] = robots.get(i).getPositions()[robots.get(i).getPositions().length-1];
      }
      double[][] roboStats = new double[robots.size()][7];
      for (int i = 0; i < roboStats.length; i++) {
        roboStats[i] = robots.get(i).getStatistics();
      } 
      simulationPanel.roboUpdate(roboNumber, roboPos, roboStats); 
      robotDataPanel.myUpdate(robots.get(0));
    } else {
      simulationPanel.roboUpdate(null, null, null);
    }
  }
  
  private void updateScreen() {           //methode für neuzeichnen des bildschirms
    screen.repaintScreen();
  }

  public void deleteRobo(int roboNumber) {
    for (int i = 0; i < robots.size(); i++) {
      if (robots.get(i).getSerialNumber() == roboNumber) {
        if (robots.size() <= Calculator.prozentage(robotsPerRound, 5)) {
          bestPerformersWeights.add(robots.get(i).getWeights());
        }
        robots.remove(i);       
      }   
    }
    if (robots.size() <= 0) {
      startRound();
    }
  }

  public int getBasePrice() {
    return basePrice;
  }
}