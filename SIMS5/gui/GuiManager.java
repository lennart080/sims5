package SIMS5.gui;

import javax.swing.SwingUtilities;

import SIMS5.simulation.SimManager;
public class GuiManager {
  //objekts
  private SimManager simManager;
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private MyPanelEntityData entityDataPanel;
  private MyPanelInput inputPanel;

  //get set
  private int startSeed;
  private int sollFps;
  private double[] startStatistics = new double[9];

  //run time
  private long timeSave = System.currentTimeMillis()/1000;
  private int fpsCounter = 0;
  private int fps = 0;
  private int isGraphAlreadyBuffed = 0;

  public GuiManager() {
    //----erstellen der graphic elemente----
    SwingUtilities.invokeLater(() -> {;                     
      simulationPanel = new MyPanelSimulation();
      dataPanel = new MyPanelData();
      graphPanel = new MyPanelGraphs();
      entityDataPanel = new MyPanelEntityData();
      inputPanel = new MyPanelInput();
      screen = new MyFrame(this, simulationPanel, dataPanel, graphPanel, entityDataPanel, inputPanel);
    });
    //-------------------------------------

    //--------default data gets set--------
    //energie                       energie des robos welche für vortbewegung und attaken und alles weitere benötigt wird
    startStatistics[0] = 100.0;
    //schrott (int)                 währung mit welcher teile und kinder "hergestellt" werden können
    startStatistics[1] = 5.0;
    //attack                        schaden welcher pro atacke zugerichtet wird
    startStatistics[2] = 0.0;
    //energie speicher              max energie die der robo haben kann
    startStatistics[3] = 100.0;
    //speed                         ...pixel pro sec werden max zurückgelegt     
    startStatistics[4] = 1.0;
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
    //Set GuiManager
    setSeed(54378);
    setSollFps(20);
    //Set SimManager
    simManager.setNoiseStrength(0.02);
    simManager.setLightTime(40);
    simManager.setLightIntensity(1.0);
    simManager.setNoiseSize(0.03);
    simManager.setSeed(startSeed);
    simManager.setEntitysPerRound(100);
    simManager.setSimulationSize(getSimulationSize());
    simManager.setEntitySize(40);
    simManager.setDayLengthRealTimeInSec(20);
    simManager.setDayLengthVariation(1000);
    int[] n = {3, 2};
    simManager.setHiddenLayers(n);
    //Set GraphPanel
    graphPanel.setDaysOnSlide(3);
    graphPanel.setRandgröße(25);
    //Set SimPanel
    simulationPanel.setSimulationSize(getSimulationSize());
    simulationPanel.setEntitysPerRound(100);
    //start
    initialiseGraphpanel();
    simManager.startSimulation();
    Thread guiThread = new Thread(() -> {
      runGui();
    });
    guiThread.start();
  }

  private void initialiseGraphpanel() {
    double[] startLight = new double[3600*graphPanel.getDaysOnSlide()];
      for (int i = 0; i < graphPanel.getDaysOnSlide(); i++) {
        double[] oneDayLight = simManager.getLightOfDay(i-((double)graphPanel.getDaysOnSlide()-((double)graphPanel.getDaysOnSlide()/2)));
        for (int j = 0; j < oneDayLight.length; j++) {
          startLight[j+(3600*i)] = oneDayLight[j];
        }
      }
    graphPanel.start(startLight);
    graphPanel.setGraphSizeY((int)simManager.getMaxLight());
  }

  //---------------set----------------
  public void setSimManager(SimManager pSimManager) {
    simManager = pSimManager;
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
      dataPanel.myUpdate(fps, simManager.getEntitysPerRound(), simManager.getEntitys().size(), simManager.getUpdates(), simManager.getTime(), simManager.getDay(), simManager.getRound(), simManager.getLongestEntity());
      graphUpdate();
      if (simManager.getEntitys().size() != 0) {
        simulationPanel.myUpdate(simManager.getEntitys());  
        entityDataPanel.myUpdate(simManager.getEntitys().get(0));
      } else {
        simulationPanel.myUpdate(null);
      }
      fpsUpdate();
      screen.repaintScreen();
    }
  }

  private void graphUpdate() {
    if (isGraphAlreadyBuffed == (int)((simManager.getUpdates()/(graphPanel.getDaysOnSlide()*3600)))) {
      double[] light = new double[3600*graphPanel.getDaysOnSlide()];
      for (int i = 0; i < graphPanel.getDaysOnSlide(); i++) {
        double[] oneDayLight = simManager.getLightOfDay((isGraphAlreadyBuffed*graphPanel.getDaysOnSlide())+i+((double)graphPanel.getDaysOnSlide()/2));
        for (int j = 0; j < oneDayLight.length; j++) {
          light[j+(3600*i)] = oneDayLight[j];
        }
      }
      graphPanel.myUpdate(light);

      isGraphAlreadyBuffed++;
    }   
    graphPanel.setTime(simManager.getUpdates());
  }
}
