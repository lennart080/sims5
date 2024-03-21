package SIMS5.gui;

import javax.swing.SwingUtilities;

import SIMS5.data.FileHandling.FileClass;
import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.Manager;

public class GuiManager {
  //objekts
  private Manager simManager;
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private MyPanelEntityData entityDataPanel;
  private MyPanelInput inputPanel;

  //get set
  private int sollFps;
  private int entitysPerRound;

  //run time
  private long timeSave = System.currentTimeMillis()/1000;
  private int fpsCounter = 0;
  private int fps = 0;
  private int isGraphAlreadyBuffed = 0;
  private int shownEntity = 0;

  //---------------start----------------muss ausgeführt werden
  public static void main(String[] args) {
    Manager simManager = new Manager();
  //------------------------------------
    new GuiManager(simManager);
  }

  public GuiManager(Manager simManager) {
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

    //-----------profiling-----------

    //benutzer gibt simulations namen ein
    String profileName = "beispielSim";

    //neues Profile Objekt erstellen 
    //wenn das profile bereits exestirt wird es automatisch in das objekt geladen
    //wenn es noch nicht exestirt also neu ist wird es automatisch erstellt mit default daten beschriben und in das objekt geladen
    Profile profile = new Profile(profileName);

    //benutzer kann nun einstellungen das profiles verändern 
    profile.set("oneDayInSeconds", 2.0); //diese veränderung wird automatisch geladen und gespeichert 

    //beispiel zum auslesen von daten des profiles
    int beispiel = profile.getIntager("oneDayInSeconds");

    //alle atribute sind mit variable typ in der datei "SIMS5/data/profiles/profilePropertys/atributesDoku.txt" aufgelistet 

    //-----------------------------
    
    graphPanel.setDaysOnSlide(3);
    setSollFps(20);

    graphPanel.setRandgröße(25); //fix (only needed for the current gui graph)
    simulationPanel.setSimulationSize(profile.getIntager("simulationSize"));
    simulationPanel.setEntitysPerRound(profile.getIntager("entitysPerRound"));

    //-------------simulationStart--------------muss ausgeführt werden
    simManager.startSimulation(profileName);
    //------------------------------------------

    Thread guiThread = new Thread(() -> {
      //runGui();
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
      dataPanel.myUpdate(fps, (int)ProfileReader.getDoubleSettings("entitysPerRound"), simManager.getEntitys().size(), simManager.getUpdates(), simManager.getTime(), simManager.getDay(), simManager.getRound(), simManager.getLongestEntity());
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
