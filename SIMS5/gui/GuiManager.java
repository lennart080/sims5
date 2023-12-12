package SIMS5.gui;

import SIMS5.simulation.SimManager;

public class GuiManager {
  private SimManager simManager;
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private MyPanelRobotData robotDataPanel;

  private int startSeed;
  private int basePrice;
  private double[] startStatistics = new double[9];

  private long timeSave = System.currentTimeMillis()/1000;
  private int fpsCounter = 0;
  private int sollFps;
  private int fps;

  public GuiManager() {
    setSeed(54674);
    setBasePrice(10);
    setSollFps(20);
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
  }

  public void setSimManager(SimManager pSimManager) {
    simManager = pSimManager;
    simManager.setRobotsPerRound(20);
  }

  public int getSollFps() {
    return sollFps;
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

  public int getSimulationSize() {
    return screen.getSimulationSize();
  }

  public void setGraphicObjekts(MyFrame mf, MyPanelSimulation sp, MyPanelData dp, MyPanelGraphs gp, MyPanelRobotData rdp) {
    screen = mf;
    simulationPanel = sp;
    dataPanel = dp;
    graphPanel = gp;
    robotDataPanel = rdp;
  }

  private void fpsUpdate() {                                              //calkuliren der angezegten bilder pro secunde
    if ((timeSave+1) <= (System.currentTimeMillis()/1000)) {
      timeSave = System.currentTimeMillis()/1000;
      fps = fpsCounter;
      fpsCounter = 0;
    }
    fpsCounter++;
  }

  public void updateScreen() {           //methode für neuzeichnen des bildschirms
    screen.repaintScreen();
  }

  public void updateGui(int updates, int timeInMin) {           
    simulationPanel.myUpdate(updates, timeInMin);  
    dataPanel.myUpdate(fps, simManager.getRobotsPerRound(), simManager.getRobots().size());
    graphPanel.myUpdate(simManager.getLightIntensityAtTime());
    if (simManager.getRobots().size() != 0) {
      simulationPanel.roboUpdate(simManager.getRobots()); 
      robotDataPanel.myUpdate(simManager.getRobots().get(0));
    } else {
      simulationPanel.roboUpdate(null);
    }
    fpsUpdate();
    updateScreen();
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

  public void startSim() {
    if (simManager.getRound() == 0) {
      simManager.startRound(); 
    }
  }
}
