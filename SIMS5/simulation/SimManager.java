package SIMS5.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import SIMS5.calculator.Calculator;
import SIMS5.data.ProfileReader;
import SIMS5.simulation.LightData.DataSettings;

public class SimManager {
  //objekts
  private LightData simData;

  //get set
  private int startSeed;
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
      setNoiseStrength(ProfileReader.getDoubleSettings("noiseStrength"));
      setLightTime((int)ProfileReader.getDoubleSettings("lightTime"));
      setLightIntensity(ProfileReader.getDoubleSettings("lightIntensity"));
      setNoiseSize(ProfileReader.getDoubleSettings("noiseSize"));
      setSeed((int)ProfileReader.getDoubleSettings("seed"));
      setEntitysPerRound((int)ProfileReader.getDoubleSettings("entitysPerRound"));
      setSimulationSize((int)ProfileReader.getDoubleSettings("simulationSize"));
      setEntitySize((int)ProfileReader.getDoubleSettings("entitySize"));
      setDayLengthRealTimeInSec((int)ProfileReader.getDoubleSettings("oneDayInSeconds"));
      System.out.println(dayLengthRealTimeInSec);
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

  public void startSim() {     
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
    xGridCount = (int)Math.round(Math.sqrt(entitysPerRound));
    yGridCount = (int)((double)entitysPerRound/(double)Math.round(Math.sqrt(entitysPerRound)));
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
      entitys.add(new MyEntity(this, nr.getWeights(), nr.getNeurons(), position, startStatistics, entitySize, simulationSize));
    }
  }

  private NeuronReturner newNetwork() {
    double[][][] neurons = new double[neuronLayers.length][][]; 
    List<double[]> weights = new ArrayList<>();
    int nId = 0;
    for (int j = 0; j < neurons.length; j++) {
      neurons[j] = new double[neuronLayers[j]][3];
      for (int j2 = 0; j2 < neurons[j].length; j2++) {
        neurons[j][j2][2] = nId;
        if (j != neurons.length-1 && j != 0) {
          neurons[j][j2][1] = roundToDecPlaces((newRandom()-0.5)*2, 4);
        } else {
          neurons[j][j2][1] = 0;
        }
        nId++;
      }
    }
    weights = weightFixer(neurons, weights);
    return new NeuronReturner(neurons, weights);
  } 

  private List<double[]> weightFixer(double[][][] pNeurons, List<double[]> pWeights) {
    for (int i = 0; i < pWeights.size(); i++) {
      boolean id1 = false;
      boolean id2 = false;
      for (int j = 0; j < pNeurons.length; j++) {
        for (int j2 = 0; j2 < pNeurons[j].length; j2++) {
          if (pNeurons[j][j2][2] == pWeights.get(i)[0]) {
            id1 = true;
          }
          if (pNeurons[j][j2][2] == pWeights.get(i)[1]) {
            id2 = true;
          }
        }
      }
      if (id1 != true || id2 != true) {
        pWeights.remove(i);
      }
    }
    for (int j = 0; j < pNeurons.length; j++) {
      for (int j2 = 0; j2 < pNeurons[j].length; j2++) {
        boolean hasOutWeight = false;
        boolean hasInWeight = false;
        for (int i = 0; i < pWeights.size(); i++) {
          if (pNeurons[j][j2][2] == pWeights.get(i)[0]) {
            hasInWeight = true;
          }     
          if (pNeurons[j][j2][2] == pWeights.get(i)[1]) {
            hasOutWeight = true;
          }
        }
        if (hasInWeight == false && j != pNeurons.length-1) {
          int x = j + 1 + normaliseValue(newRandom(), 1, pNeurons.length-2-j);
          int y = normaliseValue(newRandom(), 1, pNeurons[x].length);
          double nId2 = pNeurons[x][y][2]; 
          double[] weight = {pNeurons[j][j2][2], nId2, newRandom()*2};
          pWeights.add(weight);
        }
        if (hasOutWeight == false && j != 0) {
          int x = normaliseValue(newRandom(), 1, j-1);
          int y = normaliseValue(newRandom(), 1, pNeurons[x].length);
          double nId2 = pNeurons[x][y][2]; 
          double[] weight = {nId2, pNeurons[j][j2][2], newRandom()*2};
          pWeights.add(weight);
        }
      }
    }
    return pWeights;
  }

  private double[][][] neuronFixer(double[][][] pNeurons) {
    for (int i = 0; i < pNeurons.length; i++) {
      if (pNeurons[i].length == 0) {
        List<List<double[]>> listNeurons = convertInToList(pNeurons);
        listNeurons.remove(i);
        pNeurons = covertInToArray(listNeurons);
      }
    }
    return pNeurons;
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
      if (newRandom() < getCDBAV(2)) {  //random weight ajustment
        double[] ajustedWeight = {pWeights.get(i)[0], pWeights.get(i)[1], pWeights.get(i)[2] + (((newRandom()-0.5)*2)*getCDBAV(3))};
        pWeights.set(i, ajustedWeight);
      }
      if (newRandom() < getCDBAV(0)) {  //delete weight
        pWeights.remove(i);
        pWeights = weightFixer(pNeurons, pWeights);
      } 
    }
    for (int i = 0; i < pNeurons.length; i++) {
      for (int j = 0; j < pNeurons[i].length; j++) {
        if (newRandom() < getCDBAV(4) && i != 0 && i != pNeurons.length-1) {  //random bias ajustment 
          pNeurons[i][j][1] += ((newRandom()-0.5)*2)*getCDBAV(5);  
          pNeurons[i][j][1] = roundToDecPlaces(pNeurons[i][j][1], 4);
        }
        if (newRandom() < getCDBAV(1) && i != pNeurons.length-1) {  //random new weight
          int x = i + 1 + normaliseValue(newRandom(), 1, pNeurons.length-2-i);
          int y = normaliseValue(newRandom(), 1, pNeurons[x].length);
          double nId2 = pNeurons[x][y][2]; 
          double[] w = {pNeurons[i][j][2], nId2, (newRandom()-0.5)*2};
          pWeights.add(w);
        }
      }
    }
    if (newRandom() < getCDBAV(7)) { //new neuron    
      if (newRandom() < getCDBAV(6)) { //new neuron row
        
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
    if (newRandom() < getCDBAV(8)) { //delete neuron
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
    entitys.add(new MyEntity(this, pWeights, pNeurons, position, startStatistics, entitySize, simulationSize));

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

  private int[] newPosition(int x) {  
    int xGridPos = x % xGridCount;
    int yGridpos = x / xGridCount;
    int[] position = {xGridPos*xGridSize, yGridpos*yGridSize};
    position[0]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, xGridSize-entitySize);
    position[1]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, yGridSize-entitySize);
    return position;
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

  private double newRandom() {   
    long a = 1103515245; // multiplier
    long c = 12345; // increment
    long m = (long) Math.pow(2, 31); // modulus
    do {
      extendetSeed = (a * extendetSeed + c) % m;     
    } while (((double) extendetSeed / m) < 0 && ((double) extendetSeed / m) > 1);     
    return (double) extendetSeed / m;  
  }

  public static double roundToDecPlaces(double value, int decPlaces) {
    return Math.round(value * (Math.pow(10, decPlaces))) / (Math.pow(10, decPlaces));
  }

  public static int normaliseValue(double value, int oldMax, int newMax) {     
    int originalMax = oldMax;
    double originalRange = (double) (originalMax);
    double newRange = (double) (newMax);
    double scaledValue = (value * newRange) / originalRange;
    return (int) Math.min(Math.max(scaledValue, 0), newMax);
  }

  public class NeuronReturner {
    private double[][][] neurons;
    private List<double[]> weights;

    public NeuronReturner(double[][][] pNeurons, List<double[]> pWeights) {
      this.neurons = Arrays.copyOf(pNeurons, pNeurons.length);
      this.weights = new ArrayList<>(pWeights);
    }

    public double[][][] getNeurons() {
        return neurons;
    }

    public List<double[]> getWeights() {
        return weights;
    }
  }

  private double getCDBAV(int ajustment) {  //get Current Declining Ajustment value
    double decay = Math.log(prbabilityValues[ajustment][1]/prbabilityValues[0][0])/prbabilityValues[0][2];
    return prbabilityValues[0][0]*Math.pow(Math.E, (decay*round));
  }

  private double[][][] covertInToArray(List<List<double[]>> pNeurons) {
    double[][][] arrayNeurons = new double[pNeurons.size()][][];
    for (int i = 0; i < arrayNeurons.length; i++) {
      arrayNeurons[i] = new double[pNeurons.get(i).size()][];
      for (int j = 0; j < arrayNeurons[i].length; j++) {
        arrayNeurons[i][j] = pNeurons.get(i).get(j);
      }
    }
    return arrayNeurons;
  }

  private List<List<double[]>> convertInToList(double[][][] pNeurons) {
    List<List<double[]>> listNeurons = new ArrayList<>(pNeurons.length);
    for (int i = 0; i < pNeurons.length; i++) {
      listNeurons.add(new ArrayList<>(pNeurons[i].length));
      for (int j = 0; j < pNeurons[i].length; j++) {
        listNeurons.get(i).add(pNeurons[i][j]);
      }
    }
    return listNeurons;
  }

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
      startSeed = pSeed;
      extendetSeed = pSeed;
    } else {
      simData.newNoise(1);
      extendetSeed = 1;
      startSeed = 1;
    } 
  }
  //------------------------------------

  //---------------get------------------

  public int getLongestEntity() {
    return longestEntity;
  }

  public double getMaxLight() {
    return simData.getMaxLight();
  }

  public int getEntitysPerRound() {
    return entitysPerRound;
  }

  public List<MyEntity> getEntitys() {
    return entitys;
  }

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

  public double getLightIntensityAtTime() {
      return simData.getLightIntensityAtTime(updates);   
  }

  public class DataGeneral {
    private int round;            
    private int updates;        
    private int time;           
    private int day;    
    private int startSeed;
    private int entitysPerRound;
    private int simulationSize;
    private int entitySize;
    private int dayLengthRealTimeInSec;  
    private boolean spawnedNeuronsHaveBias;
    private double[] startStatistics;
    private double[][] prbabilityValues;

    public DataGeneral() {
      
    }
  }


  //------------------------------

  //------data gui conection------


  public DataSettings getLightSettings() {
    return simData.getDataSettings();
  }

  public double[] getLightOfDay(double pDay) {
    return simData.getLightOfDay(day);
  }

  //public DataEntitys getDataEntitys() {
  //  return new DataEntitys();
  //}

  public DataGeneral getDataGeneral() {
    return new DataGeneral();
  }
}
