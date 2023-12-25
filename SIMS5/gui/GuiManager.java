package SIMS5.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import SIMS5.calculator.Calculator;
import SIMS5.simulation.SimManager;
public class GuiManager {
  private SimManager simManager;
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private MyPanelRobotData robotDataPanel;
  private MyPanelInput inputPanel;

  private int startSeed;
  private int basePrice;
  private double[] startStatistics = new double[9];

  private long timeSave = System.currentTimeMillis()/1000;
  private int fpsCounter = 0;
  private int sollFps;
  private int fps;

  private int graphCounter = 0;
  private List<Boolean> isGraphAlreadyBuffed = new ArrayList<>();

  public GuiManager() {
    //----erstellen der graphic elemente----
    SwingUtilities.invokeLater(() -> {;                     
      simulationPanel = new MyPanelSimulation();
      dataPanel = new MyPanelData();
      graphPanel = new MyPanelGraphs();
      robotDataPanel = new MyPanelRobotData();
      inputPanel = new MyPanelInput();
      screen = new MyFrame(this, simulationPanel, dataPanel, graphPanel, robotDataPanel, inputPanel);
    });
    //-------------------------------------

    //--------default data gets set--------
    //energie                       energie des robos welche für vortbewegung und attaken und alles weitere benötigt wird
    startStatistics[0] = 10000.0;
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
    //------------------------------------
  }

  public void runGui() {
    while (true) {
      long startTime, endTime, elapsedTime;
      startTime = System.nanoTime();

      this.updateGui();

      endTime = System.nanoTime();
      elapsedTime = (endTime - startTime);
      long remainingTime = (1000000/sollFps) - elapsedTime;
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
    setSeed(54674);
    setBasePrice(10);
    setSollFps(20);
    Calculator.setSeed(startSeed);
    simManager.setSeed(startSeed);
    graphPanel.start();
    graphPanel.updateGraphSize((int)simManager.getMaxLight());
    simManager.startSimulation();
    Thread guiThread = new Thread(() -> {
      runGui();
    });
    guiThread.start();
  }

  //---------------set----------------
  public void setSimManager(SimManager pSimManager) {
    simManager = pSimManager;
    simManager.setRobotsPerRound(2);
  }

  public void setSeed(int pSeed) {  
    if (pSeed >= 1) {
      startSeed = pSeed;
    } else {
      startSeed = 1;
    } 
  }

  public void setSollFps(int pFps) {
    if (pFps > 0) {
      sollFps = pFps;    
    }
  }

  public void setBasePrice(int pBasePrice) {
    if (pBasePrice > 0) {
      basePrice = pBasePrice;    
    }
  }
  //----------------------------------

  //---------------get----------------
  public int getSollFps() {
    return sollFps;
  }

  public int getSimulationSize() {
    if (screen != null) {
      return screen.getSimulationSize();      
    } 
    return -1;
  }

  public int getBasePrice() {
    return basePrice;
  }

  public double[] getStartStatistics() {
    return startStatistics;
  }

  public int getSeed() {
    return startSeed;
  }
  //----------------------------------

  private void fpsUpdate() {                                              //calkuliren der angezegten bilder pro secunde
    if ((timeSave+1) <= (System.currentTimeMillis()/1000)) {
      timeSave = System.currentTimeMillis()/1000;
      fps = fpsCounter;
      fpsCounter = 0;
    }
    fpsCounter++;
  }

  private void updateGui() {  
    if (simulationPanel != null && dataPanel != null && graphPanel != null && screen != null && inputPanel != null) {         
      simulationPanel.myUpdate(simManager.getUpdates(), simManager.getTime(), simManager.getDay());  
      dataPanel.myUpdate(fps, simManager.getRobotsPerRound(), simManager.getRobots().size());
      graphUpdate();
      inputPanel.myUpdate();
      if (simManager.getRobots().size() != 0) {
        simulationPanel.roboUpdate(simManager.getRobots()); 
        robotDataPanel.myUpdate(simManager.getRobots().get(0));
      } else {
        simulationPanel.roboUpdate(null);
      }
      fpsUpdate();
      screen.repaintScreen();
    }
  }

  private void graphUpdate() {
    if (isGraphAlreadyBuffed.size() == (int)((simManager.getUpdates()/graphPanel.getBuffedImageTime()))) {
      double[] light = new double[graphPanel.getBuffedImageTime()];
      for (int i = 0; i < light.length; i++) {
        light[i] = simManager.getLightIntensityAtTime(i+((isGraphAlreadyBuffed.size()*graphPanel.getBuffedImageTime())+(graphPanel.getBuffedImageTime()/2)));
      }
      graphPanel.myUpdate(light, (isGraphAlreadyBuffed.size()*graphPanel.getBuffedImageTime())+(graphPanel.getBuffedImageTime()/2));
      isGraphAlreadyBuffed.add(true);
      System.out.println("buffed");
    }   
    graphPanel.setTime(simManager.getUpdates());
  }
}
