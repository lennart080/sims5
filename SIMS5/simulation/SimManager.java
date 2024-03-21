package SIMS5.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import SIMS5.data.FileHandling.profileFiles.ProfileReader;
import SIMS5.gui.Calculator;
import SIMS5.sim.enviroment.LightData;

public class SimManager {
  //objekts
  private LightData simData;

  //get set
  private long extendetSeed;
  private int entitysPerRound;
  private int simulationSize;
  private int dayLengthRealTimeInSec;  
  private int[] hiddenLayers;
  private int randomSelected;
  private int diffNetworkSelected;
  private int newNetworks;
  private int entitySize;
  private boolean spawnedNeuronsHaveBias;
  private double[] startStatistics = new double[9];
  private double[][] prbabilityValues;
  private double[] updateList = new double[4];
  private double energylossAjustment;
  private double walkActivation;

  //run time
  private int round;            
  private int updates;        
  private int time;           
  private int day;               
  private int longestEntity;
  private int xGridCount;
  private int yGridCount;
  private int xGridSize;
  private int yGridSize;
  private int[] neuronLayers;
  private List<MyEntity> entitys = new ArrayList<>();
  private List<MyEntity> ageSortedEntities = new ArrayList<>();

  //---------Simulation Start-----------

  public SimManager() {
    simData = new LightData();
  }

  public void startSimulation() {
    Thread simulationThread = new Thread(() -> {
      startStatistics[0] = ProfileReader.getDoubleSettings("entityStartEnergie");
      startStatistics[1] = ProfileReader.getDoubleSettings("entityStartSchrott");
      startStatistics[2] = ProfileReader.getDoubleSettings("entityStartAttack");
      startStatistics[3] = ProfileReader.getDoubleSettings("entityStartEnergieCapacity");
      startStatistics[4] = ProfileReader.getDoubleSettings("entityStartSpeed");
      startStatistics[5] = ProfileReader.getDoubleSettings("entityStartDefense");
      startStatistics[6] = ProfileReader.getDoubleSettings("entityStartHealth");
      startStatistics[7] = ProfileReader.getDoubleSettings("entityStartRust");
      startStatistics[8] = ProfileReader.getDoubleSettings("entityStartSolar");
      updateList[0] = ProfileReader.getDoubleSettings("entityRustPlus");
      updateList[1] = ProfileReader.getDoubleSettings("entityRustLoss");
      updateList[2] = ProfileReader.getDoubleSettings("entityEnergyLoss");
      updateList[3] = ProfileReader.getDoubleSettings("entityHealthLoss");
      energylossAjustment = ProfileReader.getDoubleSettings("entityEnergylossAjustmentPerDay");
      walkActivation = ProfileReader.getDoubleSettings("entityWalkActivation");
      setNoiseStrength(ProfileReader.getDoubleSettings("noiseStrength"));
      setLightTime((int)ProfileReader.getDoubleSettings("lightTime"));
      setLightIntensity(ProfileReader.getDoubleSettings("lightIntensity"));
      setNoiseSize(ProfileReader.getDoubleSettings("noiseSize"));
      setSeed((int)ProfileReader.getDoubleSettings("seed"));
      setEntitysPerRound((int)ProfileReader.getDoubleSettings("entitysPerRound"));
      setSimulationSize((int)ProfileReader.getDoubleSettings("simulationSize"));
      setEntitySize((int)ProfileReader.getDoubleSettings("entitySize"));
      setDayLengthRealTimeInSec((int)ProfileReader.getDoubleSettings("oneDayInSeconds"));
      setDayLengthVariation(ProfileReader.getDoubleSettings("dayLengthVariation"));
      double[] d = ProfileReader.getArraySettings("networkStartHiddenLayers");
      int[] h = new int[d.length];
      for (int i = 0; i < h.length; i++) {
        h[i] = (int)d[i];
      }
      setHiddenLayers(h);
      inicialiseSim();
      startSim();
    });
    simulationThread.start();
  }

  //------------------------------------

  //-------simulation and timing--------

  private void startSim() {     
    while (true) {
      long startTime = System.currentTimeMillis();

      this.simulateData();
      updates++;
      if (updates % 60 == 0) {
        time++;
        if (time >= 60) {
          time = 0;
        }
        if (time % 60 == 0) {
          day++;        
          for (int i = 0; i < entitys.size(); i++) {
            entitys.get(i).setDay(day);
          }
        }
      }

      if (((long)(((double)dayLengthRealTimeInSec/3600.0)*1000) - (System.currentTimeMillis() - startTime)) > 0) {
        try {
          Thread.sleep((long)(((double)dayLengthRealTimeInSec/3600.0)*1000) - (System.currentTimeMillis() - startTime));       
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void simulateData() { 
    if (entitys.size() > 0) {
      for (int i = 0; i < entitys.size(); i++) {
        entitys.get(i).simulate(simData.getLightIntensityAtTime(updates));
      } 
    } else {
      longestEntity = updates;
      nextRound();
    }
  }

  //---------------------------------------

  //------------round handler--------------

  private void inicialiseSim() { 
    //spawning grid
    
    
    if (xGridCount*yGridCount < entitysPerRound) {
      xGridCount++; 
    }
    xGridSize = (int)roundToDecPlaces((double)simulationSize/(double)xGridCount, 0);
    yGridSize = (int)roundToDecPlaces((double)simulationSize/(double)yGridCount, 0);
    //neuronLayer
    neuronLayers = new int[2+hiddenLayers.length];
    neuronLayers[0] = 12;
    for (int j = 1; j < hiddenLayers.length+1; j++) {
      neuronLayers[j] = hiddenLayers[j-1];
    }
    neuronLayers[neuronLayers.length-1] = 10;
    //first round
    for (int i = 0; i < entitysPerRound; i++) {
      //position
      int[] position = newPosition(i);
      //new entity
      NeuronReturner nr = newNetwork();
      entitys.add(new MyEntity(this, nr.getWeights(), nr.getNeurons(), position, startStatistics, updateList, energylossAjustment, walkActivation));
    }
  }

  private void nextRound() {  
    int pos = 0;
    for (int i = 0; i < ((entitysPerRound / 5) - randomSelected - diffNetworkSelected - newNetworks); i++) {
      for (int j = 0; j < 5; j++) {
        pos++;
        double[][][] neurons;
        List<double[]> weights;
        neurons = Arrays.copyOf(ageSortedEntities.get(ageSortedEntities.size() - 1).getNeurons(), ageSortedEntities.get(ageSortedEntities.size() - 1).getNeurons().length);
        weights = new ArrayList<>(ageSortedEntities.get(ageSortedEntities.size() - 1).getWeights());
        NeuronReturner nr = mutate(neurons, weights);
        addEntity(nr.getNeurons(), nr.getWeights(), pos);
      }
      ageSortedEntities.remove(ageSortedEntities.size() - 1);
    }
    for (int i = 0; i < randomSelected; i++) {
      int randomPos = normaliseValue(newRandom(), 1, ageSortedEntities.size() - 1);
      for (int j = 0; j < 5; j++) {
        pos++;
        double[][][] neurons;
        List<double[]> weights;
        neurons = Arrays.copyOf(ageSortedEntities.get(randomPos).getNeurons(), ageSortedEntities.get(randomPos).getNeurons().length);
        weights = new ArrayList<>(ageSortedEntities.get(randomPos).getWeights());
        NeuronReturner nr = mutate(neurons, weights);
        addEntity(nr.getNeurons(), nr.getWeights(), pos);
      }
      ageSortedEntities.remove(randomPos);
    }
    for (int i = 0; i < newNetworks; i++) {
      for (int j = 0; j < 5; j++) {
        pos++;
        NeuronReturner nr = newNetwork();
        addEntity(nr.getNeurons(), nr.getWeights(), pos);
      }
    }
    ageSortedEntities.clear();
    updates = 0;
    time = 0;
    day = 0;
    round++;  
  }

  private NeuronReturner mutate(double[][][] pNeurons, List<double[]> pWeights) {
    for (int i = 0; i < pWeights.size(); i++) {
      if (newRandom() < getCDAV(2)) {  //random weight ajustment
        double[] ajustedWeight = {pWeights.get(i)[0], pWeights.get(i)[1], pWeights.get(i)[2] + (((newRandom()-0.5)*2)*getCDAV(3))};
        pWeights.set(i, ajustedWeight);
      }
      if (newRandom() < getCDAV(0)) {  //delete weight
        pWeights.remove(i);
        pWeights = weightFixer(pNeurons, pWeights);
      } 
    }
    for (int i = 0; i < pNeurons.length; i++) {
      for (int j = 0; j < pNeurons[i].length; j++) {
        if (newRandom() < getCDAV(4) && i != 0 && i != pNeurons.length-1) {  //random bias ajustment 
          pNeurons[i][j][1] += ((newRandom()-0.5)*2)*getCDAV(5);  
          pNeurons[i][j][1] = roundToDecPlaces(pNeurons[i][j][1], 4);
        }
        if (newRandom() < getCDAV(1) && i != pNeurons.length-1) {  //random new weight
          int x = i + 1 + normaliseValue(newRandom(), 1, pNeurons.length-2-i);
          int y = normaliseValue(newRandom(), 1, pNeurons[x].length);
          double nId2 = pNeurons[x][y][2]; 
          double[] w = {pNeurons[i][j][2], nId2, (newRandom()-0.5)*2};
          pWeights.add(w);
        }
      }
    }
    if (newRandom() < getCDAV(7)) { //new neuron    
      if (newRandom() < getCDAV(6)) { //new neuron row
        
      } else {
        if (pNeurons.length > 2) {
          int row;
          if (pNeurons.length == 3) {
            row = 1;
          } else {
            row = 1 + normaliseValue(newRandom(), 1, pNeurons.length-3);
          } 
          List<List<double[]>> listNeurons = convertInToList(pNeurons);
          double[] neuron = {0.0, 0.0, getFreeNId(pNeurons)};
          if (spawnedNeuronsHaveBias) {
            neuron[1] = (newRandom()-0.5)*2;
          } 
          listNeurons.get(row).add(neuron);
          pNeurons = covertInToArray(listNeurons);
          pWeights = weightFixer(pNeurons, pWeights);
        }
      }
    }
    if (newRandom() < getCDAV(8)) { //delete neuron
      int row;
      if (pNeurons.length == 3) {
        row = 1;
      } else {
        row = 1 + normaliseValue(newRandom(), 1, pNeurons.length-3);
      } 
      int y = normaliseValue(newRandom(), 1, pNeurons[row].length-1);
      List<List<double[]>> listNeurons = convertInToList(pNeurons);
      listNeurons.get(row).remove(y);
      pNeurons = covertInToArray(listNeurons);
      pNeurons = neuronFixer(pNeurons);
      pWeights = weightFixer(pNeurons, pWeights);
    }
    return new NeuronReturner(pNeurons, pWeights);
  }

  private void addEntity(double[][][] pNeurons, List<double[]> pWeights, int pos) {
    int[] position = newPosition(pos);
    entitys.add(new MyEntity(this, pWeights, pNeurons, position, startStatistics, updateList, energylossAjustment, walkActivation));

    System.out.println("network " + entitys.get(entitys.size()-1).getSerialNumber());
    for (int k = 0; k < pNeurons.length; k++) {
      for (int k2 = 0; k2 < pNeurons[k].length; k2++) {
        System.out.print("  N: " + pNeurons[k][k2][2]);
      }
    }
    System.out.println("");
    for (int k = 0; k < pWeights.size(); k++) {
      System.out.print("  W: " + entitys.get(entitys.size()-1).getWeights().get(k)[0] + "/" + entitys.get(entitys.size()-1).getWeights().get(k)[1]);
    }
    System.out.println("");
    System.out.println("");
  }

  

  public void deleteRobo(MyEntity robo) {  
    if (entitys.size() > 0) {
      for (int i = 0; i < entitys.size(); i++) {
        if (entitys.get(i) == robo) {
          ageSortedEntities.add(entitys.get(i));
          entitys.remove(i);     
        }
      }
    }
  }

  //------------------------------------

  //------------- helper----------------


  private int getFreeNId(double[][][] pNeurons) {
    int freeNId = 0;
    for (int i = 0; i < pNeurons.length; i++) {
      for (int j = 0; j < pNeurons[i].length; j++) {
        if (freeNId < pNeurons[i][j][2]) {
          freeNId = (int)pNeurons[i][j][2];
        }
      }
    }
    return freeNId+1;
  }

  //------------------------------------

  //---------------set------------------

  public void setStartStatistics(double[] stats) {
    startStatistics = new double[9];
    startStatistics = Arrays.copyOf(stats, 9);
  }

  public void setSimulationSize(int pSimSize) {
    simulationSize = pSimSize;
  }

  public void setNoiseStrength(double noiseStrength) {
    simData.setNoiseStrength(noiseStrength);
  }

  public void setLightTime(int lightTime) {
    simData.setLightTime(lightTime);
  }

  public void setLightIntensity(double lightIntensity) {
    simData.setLightIntensity(lightIntensity);
  }

  public void setNoiseSize(double noiseSize) {
    simData.setNoiseSize(noiseSize);
  }

  public void setDayLengthVariation(double variation) {
    simData.setDayLengthVariation(variation);
  }

  public void setEntitysPerRound(int pEntitysPerRound) {
    if (pEntitysPerRound < 10) {
      entitysPerRound = 10;
    } else {
      entitysPerRound = 10 * (int)((double)pEntitysPerRound/10.0);
    }
  }

  public void setDayLengthRealTimeInSec(int sec) {
    if (sec < 1) {
      dayLengthRealTimeInSec = 1;
    } else {
      dayLengthRealTimeInSec = sec;
    }
  }

  public void setEntitySize(int pEntitySize) {
    entitySize = pEntitySize;
  }

  public void setHiddenLayers(int[] pNeuronLayers) {
    hiddenLayers = Arrays.copyOf(pNeuronLayers, pNeuronLayers.length);
  }

  public void setSeed(int pSeed) {  
    if (pSeed >= 1) {
      simData.newNoise(pSeed);
      extendetSeed = pSeed;
    } else {
      simData.newNoise(1);
      extendetSeed = 1;
    } 
  }
  //------------------------------------

  //---------------get------------------

  public int getLongestEntity() {  //nur für debug
    return longestEntity;
  }

  public double getMaxLight() {  //only for current gui
    return simData.getMaxLight();
  }

  public List<MyEntity> getEntitys() {
    return entitys;
  }

  //------------------------------

  //--------gui conection---------

  //--light--
  public double[] getLightOfDay(double pDay) {
    return simData.getLightOfDay(day);
  }

  public double getLightIntensityAtTime() {
    return simData.getLightIntensityAtTime(updates);   
  }

  public double getLightIntensityAtTime(int pUpdates) {
    return simData.getLightIntensityAtTime(pUpdates);   
  }
  //--------

  //---sim---
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
  //---------
  
}
