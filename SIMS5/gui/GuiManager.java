package SIMS5.gui;

import javax.swing.SwingUtilities;

import SIMS5.data.ProfileReader;
import SIMS5.data.ProfileWriter;
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
  private int entitysPerRound;
  private String profileName;

  //run time
  private long timeSave = System.currentTimeMillis()/1000;
  private int fpsCounter = 0;
  private int fps = 0;
  private int isGraphAlreadyBuffed = 0;
  private int shownEntity = 0;

  //---------------start----------------muss ausgeführt werden
  public static void main(String[] args) {
    SimManager simManager = new SimManager();
  //------------------------------------
    new GuiManager(simManager);
  }

  public GuiManager(SimManager simManager) {
    this.simManager = simManager;
    //----erstellen der graphic elemente----
    SwingUtilities.invokeLater(() -> {;                     
      simulationPanel = new MyPanelSimulation();
      dataPanel = new MyPanelData();
      graphPanel = new MyPanelGraphs();
      entityDataPanel = new MyPanelEntityData();
      inputPanel = new MyPanelInput(this);
      screen = new MyFrame(this, simulationPanel, dataPanel, graphPanel, entityDataPanel, inputPanel);
    });
    //-------------------------------------
  }

  public void runGui() {
    initialiseGraphpanel();
    while (true) {
      long startTime = System.currentTimeMillis(); 

      this.updateGui();

      if (((1000/sollFps) - (System.currentTimeMillis() - startTime)) > 0) {
        try {
          Thread.sleep((1000/sollFps) - (System.currentTimeMillis() - startTime));       
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void startSimulation() {
    setSollFps(20);

    //-----------dataLoading-----------muss ausgeführt werden

    //benutzer gibt eien namen ein
    setProfileName("fillOut");

    //systemCheck
    ProfileWriter.checkOrdner();
  
    //-> neues profil erstellen welches bereits die default daten hatt und laden
    ProfileWriter.createNewProfile(profileName, true);
    ProfileReader.loadProfile(profileName);

    //oder

    //-> nur bereits vorhandenes profil laden 
    //ProfileReader.loadProfile(profileName);

    // benutzer kann nun einstellungen der neuen/alten datei verändern 
    ProfileWriter.writeInProfile(profileName, "oneDayInSeconds", 5.0);

    //zum ende müssen die daten wieder neu geladen werden um aktuell zu sein wenn der benuzer etwas verändert hatt
    ProfileReader.loadProfile(profileName);

    //-----------------------------
    
    graphPanel.setDaysOnSlide(3);
    graphPanel.setRandgröße(25); //fix (only needed for the current gui graph)
    simulationPanel.setSimulationSize((int)ProfileReader.getDoubleSettings("simulationSize"));
    simulationPanel.setEntitysPerRound((int)ProfileReader.getDoubleSettings("entitysPerRound"));

    //-------------simulationStart--------------muss ausgeführt werden
    simManager.startSimulation();
    //------------------------------------------

    Thread guiThread = new Thread(() -> {
      runGui();
    });
    screen.guiModes(4);
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
    graphPanel.setGraphSizeY((int)simManager.getMaxLight());
    graphPanel.start(startLight);
  }

  //---------------set----------------

  public void setSeed(int pSeed) {  
    if (pSeed >= 1) {
      startSeed = pSeed;
    } else {
      startSeed = 54318;
    } 
  }

  public void setProfileName(String name) {
    profileName = name;
  }

  public void setSollFps(int pFps) {
    if (pFps > 0) {
      sollFps = pFps;    
    }
  }

  public void setEntitysPerRound(int value) {
    if (value > 0) {
      entitysPerRound = value;    
    }
  }

  //----------------------------------

  //---------------get----------------

  public int getSimulationSize() {
    if (screen != null) {
      return screen.getSimulationSize();      
    } 
    return -1;
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
        updateShownEntity();
        entityDataPanel.myUpdate(simManager.getEntitys().get(shownEntity));
      } else {
        simulationPanel.myUpdate(null);
      }
      fpsUpdate();
      screen.repaintScreen();
    }
  }

  public void entityShownShift(boolean right) {
    if (right) {
      if (shownEntity < entitysPerRound) {
        shownEntity++;
      }
    } else {
      if (shownEntity > 0) {
        shownEntity--;
      }
    }
  }

  private void updateShownEntity() {
    if (shownEntity > simManager.getEntitys().size()-1) {
      shownEntity = simManager.getEntitys().size()-1;
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
