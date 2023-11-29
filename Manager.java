import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import panels.MyPanelData;
import panels.MyPanelGraphs;
import panels.MyPanelRobotData;
import panels.MyPanelSimulation;
import panels.MyPanel;
public class Manager {                        //Manager zuständig für timings und updates
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private MyPanelRobotData robotDataPanel;
  private SimulationData simulationData;

  private List<Robot> robots = new ArrayList<>();
  private int robotsPerRound = 1;
  private List<double[][][]> bestPerformersWeights = new ArrayList<>();
  private int permutPos = 0;

  private Timer simulationTimer;
  private Timer guiTimer;

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
    
    simulationData = new SimulationData();
    
  
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
        pos[0] = MyPanel.normaliseValue((double)simulationData.getPermut()[permutPos*2], maxInt, screen.getScreenWidth());
        pos[1] = MyPanel.normaliseValue((double)simulationData.getPermut()[(permutPos*2)+1], maxInt, screen.getScreenHeight());         
        robots.add(new Robot(this, null, pos));
        permutPos+= 2; 
      }
    } else {
      for (int i = permutPos; i < permutPos+bestPerformersWeights.size(); i++) {
        for (int j = 0; j < robotsPerRound/bestPerformersWeights.size(); j++) {
          int[] pos = new int[2];
          pos[0] = MyPanel.normaliseValue((double)simulationData.getPermut()[permutPos*2], maxInt, screen.getScreenWidth());
          pos[1] = MyPanel.normaliseValue((double)simulationData.getPermut()[(permutPos*2)+1], maxInt, screen.getScreenHeight());         
          robots.add(new Robot(this, null, pos));
          permutPos+= 2;       
        }
      }
    }
    round++;       
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

      //int[] help = {(int)robo[0][0], (int)robo[0][1]};
      //screen.robotDataMode(help);
      //robotDataPanel.myUpdate(0, null);
    } else {
      simulationPanel.roboUpdate(null, null, null);
    }
  }
  
  private void updateScreen() {           //methode für neuzeichnen des bildschirms
    simulationPanel.repaint();
    dataPanel.repaint();
    graphPanel.repaint();
    //robotDataPanel.repaint();
  }

  public void deleteRobo(int roboNumber) {
    for (int i = 0; i < robots.size(); i++) {
      if (robots.get(i).getSerialNumber() == roboNumber) {
        if (robots.size() <= MyPanel.prozentage(robotsPerRound, 5)) {
          bestPerformersWeights.add(robots.get(i).getWeights());
        }
        robots.remove(i);       
      }   
    }
    if (robots.size() == 0) {
      startRound();
    }
  }
}