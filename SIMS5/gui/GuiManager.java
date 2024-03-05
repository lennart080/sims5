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

  public static void main(String[] args) {
    SimManager simManager = new SimManager();
    GuiManager guiM = new GuiManager(simManager);
    simManager.setGuiManager(guiM);
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
    setProfileName("fillOut");
  
    // create profile
    ProfileWriter.checkOrdner();
    ProfileWriter.createNewProfile(profileName, true);

    // start values for every entity
    ProfileWriter.writeInProfile(profileName, "entityStartEnergie", 100.0); // should never be zero
    ProfileWriter.writeInProfile(profileName, "entityStartSchrott", 5.0); 
    ProfileWriter.writeInProfile(profileName, "entityStartAttack", 0.0); // should always be lower than StartHealth
    ProfileWriter.writeInProfile(profileName, "entityStartEnergieCapacity", 100.0); // should never be under StartEnergie value
    ProfileWriter.writeInProfile(profileName, "entityStartSpeed", 1.0); // should never be zero if StartSchrott is zero
    ProfileWriter.writeInProfile(profileName, "entityStartDefense", 0.0); // should never be higher than StartAttack
    ProfileWriter.writeInProfile(profileName, "entityStartHealth", 5.0); // should never be zero
    ProfileWriter.writeInProfile(profileName, "entityStartRust", 0.0); //should always be zero
    ProfileWriter.writeInProfile(profileName, "entityStartSolar", 0.0); // should never be zero if StartSchrott is zero

    // calculation values for every entity in runtime
    ProfileWriter.writeInProfile(profileName, "entityWalkActivation", 0.5); // (0.01 - 0.99)
    ProfileWriter.writeInProfile(profileName, "entityEnergylossAjustmentPerDay", 0.2); // how mutch more energieLoss is applied per day
    // per time (60 updates)
    ProfileWriter.writeInProfile(profileName, "entityRustPlus", 0.01); //only if standing still
    ProfileWriter.writeInProfile(profileName, "entityRustLoss", 1.0); //only when walking
    ProfileWriter.writeInProfile(profileName, "entityEnergyLoss", 1.0); // always
    ProfileWriter.writeInProfile(profileName, "entityHealthLoss", 0.1); // only if energy is 0 

    // light settings 
    ProfileWriter.writeInProfile(profileName, "noiseStrength", 0.02); // wie starke schwankungen das Noise haben soll (0 - 0.1)
    ProfileWriter.writeInProfile(profileName, "lightTime", 40.0); // wie lange die sonne pro tag scheint (0 - 60)
    ProfileWriter.writeInProfile(profileName, "lightIntensity", 0.6); // wie stark das licht scheint (0 - 10)
    ProfileWriter.writeInProfile(profileName, "noiseSize", 0.03); //je kleiner deszo schneller werden die schwingungen des Noise (0.0 - 0.5)
    ProfileWriter.writeInProfile(profileName, "dayLengthVariation", 300); //tages längen unterschied (0 - 500) 

    //other
    ProfileWriter.writeInProfile(profileName, "seed", 54318); // must be positive
    ProfileWriter.writeInProfile(profileName, "entitysPerRound", 100); // should never be zero 
    ProfileWriter.writeInProfile(profileName, "simulationSize", getSimulationSize()); // kommt immer aus dieser funtion 
    ProfileWriter.writeInProfile(profileName, "entitySize", 40); // should never be under 2 | sloud be the same as graphics size
    ProfileWriter.writeInProfile(profileName, "oneDayInSeconds", 5); // simulation speed (1 - 3600) 

    //gui
    ProfileWriter.writeInProfile(profileName, "fps", 20);
    ProfileWriter.writeInProfile(profileName, "DaysShownInGraph", 3);
    
    // network settings
    // n[row] = number in that row
    double[] n = {3, 2}; 
    ProfileWriter.writeInProfile(profileName, "networkStartHiddenLayers", n);

    ProfileWriter.writeInProfile(profileName, "doSpawnedNeuronshaveABias", false); 

    // v[0] = start probability | v[1] = end probability | v[2] = end probability round  
    // v[0] should never be above 1 | v[1] should always be lower than v[0] 
    double[] v = new double[3];
    v[0] = 0.1; v[1] = 0.001; v[2] = 500; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityWeightDying", v); //per weight 
    v[0] = 0.2; v[1] = 0.001; v[2] = 400; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityNewWeight", v); //per neuron
    v[0] = 0.0; v[1] = 0.001; v[2] = 1000; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityWeightAjustment", v); //per weight
    v[0] = 1.0; v[1] = 0.01; v[2] = 1000; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityWeightAjustmentValue", v); //per weight
    v[0] = .7; v[1] = 0.01; v[2] = 500; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityBiasAjustment", v); //per neuron
    v[0] = 1.0; v[1] = 0.01; v[2] = 1000; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityBiasAjustmentValue", v); //per neuron
    v[0] = 0.1; v[1] = 0.005; v[2] = 100; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityNewNeuronRow", v); //per newNeuron
    v[0] = 0.2; v[1] = 0.01; v[2] = 150; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityNewNeuron", v); //per network
    v[0] = 0.15; v[1] = 0.01; v[2] = 150; 
    ProfileWriter.writeInProfile(profileName, "mutationProbabilityNeuronDying", v); //per network

    // all under should always atleast be one less or lower than entitysPerRound 
    ProfileWriter.writeInProfile(profileName, "entitySelectionValueRandom", 2); 
    ProfileWriter.writeInProfile(profileName, "entitySelectionValueMostDifferent", 0); 
    ProfileWriter.writeInProfile(profileName, "entitySelectionValueNew", 3); 

    //load profile
    ProfileReader.loadProfile(profileName);  //must be done bevore reading of profile (only once)
    
    //set GuiManager
    setSollFps((int)ProfileReader.getDoubleSettings("fps"));
    //Set GraphPanel
    graphPanel.setDaysOnSlide((int)ProfileReader.getDoubleSettings("DaysShownInGraph"));
    graphPanel.setRandgröße(25); //fix (only needed for the current gui graph)
    //Set SimPanel
    simulationPanel.setSimulationSize((int)ProfileReader.getDoubleSettings("simulationSize"));
    simulationPanel.setEntitysPerRound((int)ProfileReader.getDoubleSettings("entitysPerRound"));

    simManager.setStartStatistics(startStatistics);
    simManager.setNoiseStrength(0.02);
    simManager.setLightTime(40);
    simManager.setLightIntensity(0.8);
    simManager.setNoiseSize(0.03);
    simManager.setSeed(startSeed);
    simManager.setEntitysPerRound(entitysPerRound);
    simManager.setSimulationSize(getSimulationSize());
    simManager.setEntitySize(40);
    simManager.setDayLengthRealTimeInSec(5);
    simManager.setDayLengthVariation(600);
    int[] x = {3, 2};
    simManager.setHiddenLayers(x);
    
    //start
    initialiseGraphpanel();
    simManager.startSimulation();
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
