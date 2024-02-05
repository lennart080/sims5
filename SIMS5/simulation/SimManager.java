package SIMS5.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import SIMS5.calculator.Calculator;
import SIMS5.gui.GuiManager;

public class SimManager {
  //objekts
  private GuiManager guiManager;
  private LightData simData;

  //get set
  private long extendetSeed;
  private int entitysPerRound;
  private int simulationSize;
  private int dayLengthRealTimeInSec;  
  private int[] hiddenLayers;
  private int randomSelected = 2;
  private int diffNetworkSelected = 0;
  private int newNetworks = 3;
  private int entitySize;
  private double[] weightDying = {0.1, 0.001, 500}; // 0 = start probability; 1 = value after...; 2 = when1; 
  private double[] newWeight = {0.2, 0.001 ,400};
  private double[] weightAjustment = {0.2, 0.01, 1000}; // 0 = start probability; 1 = value after...; 2 = when1; 
  private double[] weightAjustmentValue = {1.0, 0.01, 1000}; // 0 = start probability; 1 = value after...; 2 = when1; 
  private double[] biasAjustment = {0.7, 0.01, 500};
  private double[] biasAjustmentValue = {1.0, 0.01, 1000};

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
      inicialiseSim();
      startSim();
    });
    simulationThread.start();
  }

  //------------------------------------

  //-------simulation and timing--------

  public void startSim() {     
    while (true) {
      long startTime = System.nanoTime();

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

      long remainingTime = (long)((((double)dayLengthRealTimeInSec*1000000000.0)/60.0)/60.0);
      if ((System.nanoTime() - startTime) > 1000000) {
        remainingTime-= (System.nanoTime() - startTime);
        if (remainingTime <= 0) {
          remainingTime = 1;
        }
      }
      try {
        Thread.sleep(remainingTime / 1000000);       
      } catch (Exception e) {
        e.printStackTrace();
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
      entitys.add(new MyEntity(this, nr.getWeights(), nr.getNeurons(), position, guiManager.getStartStatistics(), entitySize, simulationSize));
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
        if (hasInWeight == false && j != neuronLayers.length-1) {
          int x = j + 1 + normaliseValue(newRandom(), 1, neuronLayers.length-2-j);
          int y = normaliseValue(newRandom(), 1, neuronLayers[x]);
          double nId2 = pNeurons[x][y][2]; 
          double[] weight = {pNeurons[j][j2][2], nId2, newRandom()*2};
          pWeights.add(weight);
        }
        if (hasOutWeight == false && j != 0) {
          int x = normaliseValue(newRandom(), 1, j-1);
          int y = normaliseValue(newRandom(), 1, neuronLayers[x]);
          double nId2 = pNeurons[x][y][2]; 
          double[] weight = {nId2, pNeurons[j][j2][2], newRandom()*2};
          pWeights.add(weight);
        }
      }
    }
    return pWeights;
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
      if (newRandom() < getCDBAV(1)) {  //random weight ajustment
        double[] ajustedWeight = {pWeights.get(i)[0], pWeights.get(i)[1], pWeights.get(i)[2] + (((newRandom()-0.5)*2)*getCDBAV(2))};
        pWeights.set(i, ajustedWeight);
      }
      if (newRandom() < getCDBAV(0)) {  //delete weight
        pWeights.remove(i);
        pWeights = weightFixer(pNeurons, pWeights);
      } 
    }
    for (int i = 0; i < pNeurons.length; i++) {
      for (int j = 0; j < pNeurons[i].length; j++) {
        if (newRandom() < getCDBAV(3) && i != 0 && i != pNeurons.length-1) {  //random bias ajustment 
          pNeurons[i][j][1] += ((newRandom()-0.5)*2)*getCDBAV(4);  
          pNeurons[i][j][1] = roundToDecPlaces(pNeurons[i][j][1], 4);
        }
        if (newRandom() < getCDBAV(5) && i != pNeurons.length-1) {  //random new weight
          int x = i + 1 + normaliseValue(newRandom(), 1, neuronLayers.length-2-i);
          int y = normaliseValue(newRandom(), 1, neuronLayers[x]);
          double nId2 = pNeurons[x][y][2]; 
          double[] w = {pNeurons[i][j][2], nId2, (newRandom()-0.5)*2};
          pWeights.add(w);
        }
      }
    }
    return new NeuronReturner(pNeurons, pWeights);
  }

  private void addEntity(double[][][] pNeurons, List<double[]> pWeights, int pos) {
    int[] position = newPosition(pos);
    entitys.add(new MyEntity(this, pWeights, pNeurons, position, guiManager.getStartStatistics(), entitySize, simulationSize));
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

    public NeuronReturner(double[][][] neurons, List<double[]> weights) {
      this.neurons = neurons;
      this.weights = weights;
    }

    public double[][][] getNeurons() {
        return neurons;
    }

    public List<double[]> getWeights() {
        return weights;
    }
  }

  private double getCDBAV(int ajustment) {  //get Current Declining Ajustment value
    double decay;
    switch (ajustment) {
      case 0: // dying weight probability 
        decay = Math.log(weightDying[1]/weightDying[0])/weightDying[2];
        return weightDying[0]*Math.pow(Math.E, (decay*round)); 
      
      case 1: // weight ajustment probability 
        decay = Math.log(weightAjustment[1]/weightAjustment[0])/weightAjustment[2];
        return weightAjustment[0]*Math.pow(Math.E, (decay*round));

      case 2: // weight value ajustment prbability
        decay = Math.log(weightAjustmentValue[1]/weightAjustmentValue[0])/weightAjustmentValue[2];
        return weightAjustmentValue[0]*Math.pow(Math.E, (decay*round));
      case 3: 
        decay = Math.log(biasAjustment[1]/biasAjustment[0])/biasAjustment[2];
        return biasAjustment[0]*Math.pow(Math.E, (decay*round));
      case 4:
        decay = Math.log(biasAjustmentValue[1]/biasAjustmentValue[0])/biasAjustmentValue[2];
        return biasAjustmentValue[0]*Math.pow(Math.E, (decay*round));
      case 5: 
        decay = Math.log(newWeight[1]/newWeight[0])/newWeight[2];
        return newWeight[0]*Math.pow(Math.E, (decay*round));
      default:
        return 0;
    }
  }

  //------------------------------------

  //---------------set------------------

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

  public void setGuiManager(GuiManager gm) {
    guiManager = gm;
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

  public int getDayLengthRealTimeInSec() {
    return dayLengthRealTimeInSec;
  }

  public double getLightIntensityAtTime() {
    if (simData != null) {
      return simData.getLightIntensityAtTime(updates);   
    }
    return 0.0;
  }

  public double[] getLightOfDay(double pDay) {
    double[] lightOfDay = new double[3600];
    for (int i = 0; i < lightOfDay.length; i++) {
      if (i+(int)(pDay*3600) < 0) {
        lightOfDay[i] = simData.getLightIntensityAtTime(0);
      } else {
        lightOfDay[i] = simData.getLightIntensityAtTime(i+(int)(pDay*3600));
      }
    }
    return lightOfDay;
  }
}
