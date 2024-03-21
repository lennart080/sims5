package SIMS5.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import SIMS5.sim.util.MathUtil;

public class MyEntity {
  private SimManager manager;
  private MathUtil math = new MathUtil();

  private double[] statistics;
  private double[] defaultStats;
  private double[] updateList;
  private double energieLossAjustment;
  private double walkActivasion;
  private List<int[]> positions = new ArrayList<>();
  private double[][][] neurons;
  private List<double[]> weights;
  private int serialNumber;
  public static int lastSerialNumber;
  private int day;
  private int lastMovment;
  
  public MyEntity(SimManager pManager, List<double[]> pWeigths, double[][][] pNeurons, int[] pPosition, double[] ss, double[] ul, double ela, double wa) {
    manager = pManager;
    neurons = Arrays.copyOf(pNeurons, pNeurons.length);
    weights = new ArrayList<>(pWeigths);
    Collections.sort(weights, Comparator.comparingDouble(arr -> arr[1]));
    statistics = Arrays.copyOf(ss, ss.length);
    defaultStats = Arrays.copyOf(ss, ss.length);
    updateList = Arrays.copyOf(ul, ul.length);
    energieLossAjustment = ela;
    walkActivasion = wa;
    serialNumber = lastSerialNumber;
    lastSerialNumber++;
    setStartPos(pPosition);
  }

  //-------------------------------------

  //-------------inicialise--------------
    
  private void setStartPos(int[] position) {
    int[] tempPosition = new int[position.length];
    tempPosition = Arrays.copyOf(position, 2);
    tempPosition[2] = 1;
    for (int i = 0; i < 5; i++) {
      positions.add(position);
    }
  }

  //-------------------------------------
  
  //----------------get------------------

  public double[][][] getNeurons() {
    return neurons;
  }

  public List<double[]> getWeights() {
    return weights;
  }
  
  public int[][] getPositions() { 
    int[][] pos = new int[positions.size()][2];
    for (int i = 0; i < positions.size(); i++) {
      pos[i][0] = positions.get(i)[0];
      pos[i][1] = positions.get(i)[1];
    }
    return pos;
  }
  
  public double[] getStatistics() {
    return statistics;
  }
  
  public int getSerialNumber() {
    return serialNumber;
  }

  public double getEnergieLoss() {
    return math.roundToDecPlaces(((0.1*(manager.getDay()+1))+statistics[7]), 2);
  }

  public double getEnergiePlus() {
    return math.roundToDecPlaces(((statistics[8]*manager.getLightIntensityAtTime())/60), 2);
  }

  //-------------------------------------

  //----------------set------------------

  public void setDay(int pDay) {
    day = pDay;
  }

  public void setPosition(int x, int y, boolean bewegt) {
    int[] tempPos = {x, y, 1};
    if (!bewegt) {
      tempPos[2] = 0;
    }
    positions.set(positions.size(), tempPos);
  }

  //--------------------------------------

  //-----------neural network-------------

  public void simulate(double lightIntensity) {
    updateStatistics(lightIntensity);
    setInputs();
    calculate();
    formatOutputNeurons();
    setOutputs();
  }

  private void updateStatistics(double lightIntensity) {
    boolean posWechsel = false;
    for (int i = 0; i < positions.size(); i++) {
      if (positions.get(i)[2] == 1) {
        posWechsel = true;
      }
    }
    if (posWechsel == false) {
        statistics[7] = math.roundToDecPlaces(statistics[7]+(updateList[0]/60), 3);
    } else {
      if (statistics[7] >= 0.01) {
        statistics[7] = math.roundToDecPlaces(statistics[7]-(updateList[1]/60), 3);
      }
    }
    if (statistics[0] < statistics[3]) { // wenn weniger energie als speicherplatz
      statistics[0] = math.roundToDecPlaces(statistics[0]+((statistics[8]*lightIntensity)/60), 3); // bekommt enerige
      if (statistics[0] > statistics[3]) { // wenn zu viel energie
        statistics[0] = statistics[3]; // bekommt max enegrie
      }
    }
    statistics[0] = math.roundToDecPlaces(statistics[0]-(((updateList[2]/60)*((day+1)*energieLossAjustment))+statistics[7]), 3); // energie verlust
    if (statistics[0] <= 0.0) { // wenn keine energie
      statistics[0] = 0.0; // bekommt min energie
      statistics[6] = math.roundToDecPlaces(statistics[6]-(updateList[3]/60), 3); // leben verlust
      if (statistics[6] <= 0.0) { // wenn keine enerige
        manager.deleteRobo(this); // stirbt
      }
    } 
  }

  private void setInputs() {
    neurons[0][0][0] = math.roundToDecPlaces(statistics[0]/statistics[3], 4); //prozent des "akkus" (0-1) (energie/speicher)
    neurons[0][1][0] = statistics[2]/10; // atk/10
    neurons[0][2][0] = statistics[4]/10; // speed/10
    neurons[0][3][0] = statistics[5]/10; // defence/10
    neurons[0][4][0] = math.roundToDecPlaces(statistics[6]/10, 4); // health/10
    neurons[0][5][0] = statistics[7]; // rust
    neurons[0][6][0] = statistics[8]/10; // solar/10
    neurons[0][7][0] = math.roundToDecPlaces((double)positions.get(positions.size()-1)[0]/(double)simSize, 4); // x pos in prozent zu max (0 bis 1)
    neurons[0][8][0] = math.roundToDecPlaces((double)positions.get(positions.size()-1)[1]/(double)simSize, 4); // y pos in prozent zu max (0 bis 1)
    neurons[0][9][0] = (double)lastMovment/4.0; //last movment (0 still, 0.25 up, 0.5 right, 0.75 down, 1.0 left)
  }


  private void calculate() {
    int neuronNumber = -1;
    for (int i = 0; i < weights.size(); i++) {
      boolean deleteValue = false;
      if (weights.get(i)[1] > neuronNumber) {
        deleteValue = true;
        neuronNumber = (int)weights.get(i)[1];
      }
      double neuronValue = 0;
      double weightValue = weights.get(i)[2];
      int nId1 = (int)weights.get(i)[0];
      int nId2 = (int)weights.get(i)[1];
      int nOutPosX = 0;
      int nOutPosY = 0;
      for (int j = 0; j < neurons.length; j++) {
        for (int j2 = 0; j2 < neurons[j].length; j2++) {
          if (neurons[j][j2][2] == nId1) {
            neuronValue = neurons[j][j2][0];
          } else if (neurons[j][j2][2] == nId2) {
            nOutPosX = j;
            nOutPosY = j2;
          }
        }
      }
      if (deleteValue == true) {
        neurons[nOutPosX][nOutPosY][0] = neurons[nOutPosX][nOutPosY][1];               
      }
      neurons[nOutPosX][nOutPosY][0] = math.roundToDecPlaces(neurons[nOutPosX][nOutPosY][0] + (weightValue*neuronValue), 4);
    }
  }

  private void formatOutputNeurons() {
    double sumExp = 0.0; // softmax funktion for the first 4 outputs (walking)
    for (int i = 0; i < 4; i++) {
      sumExp += Math.exp(neurons[neurons.length-1][i][0]);
    }
    for (int i = 0; i < 4; i++) {
      neurons[neurons.length-1][i][0] = math.roundToDecPlaces(Math.exp(neurons[neurons.length-1][i][0]) / sumExp, 4);
    }
    double sumExp2 = 0.0; // softmax funktion for the outputs 5 to 9 (upgrades)
    for (int i = 4; i < 9; i++) {
      sumExp2 += Math.exp(neurons[neurons.length-1][i][0]);
    }
    for (int i = 4; i < 9; i++) {
      neurons[neurons.length-1][i][0] = math.roundToDecPlaces(Math.exp(neurons[neurons.length-1][i][0]) / sumExp2, 4);
    }
  }

  private void setOutputs() {
    int outputNeuronPos = 0;
    int highestPosneuron = -1;
    double highestPos = 0;
    for (int i = 0; i < 4; i++) {
      if (neurons[neurons.length-1][outputNeuronPos][0] > highestPos) {
        highestPos = neurons[neurons.length-1][outputNeuronPos][0];
        if (highestPos >= walkActivasion) {
          highestPosneuron = outputNeuronPos;        
        }
      }
      outputNeuronPos++;
    }
    int[] pos = new int[2];
    if (0 <= highestPosneuron && highestPosneuron <= 3) {
      pos[2] = 1;
    } else {
      pos[2] = 0;      
    }
    switch (highestPosneuron) {
      case 0:
        pos[0] = positions.get(positions.size()-1)[0];
        pos[1] = positions.get(positions.size()-1)[1]+(int)statistics[4];
        positions.add(pos);
        positions.remove(0);   
        lastMovment = 1;  
        break;
      case 1:
        pos[0] = positions.get(positions.size()-1)[0]+(int)statistics[4];
        pos[1] = positions.get(positions.size()-1)[1];
        positions.add(pos);   
        positions.remove(0);   
        lastMovment = 2;
        break;
      case 2:
        pos[0] = positions.get(positions.size()-1)[0];
        pos[1] = positions.get(positions.size()-1)[1]-(int)statistics[4];
        positions.add(pos);      
        positions.remove(0); 
        lastMovment = 3;
        break;
      case 3:
        pos[0] = positions.get(positions.size()-1)[0]-(int)statistics[4];
        pos[1] = positions.get(positions.size()-1)[1];
        positions.add(pos);
        positions.remove(0);   
        lastMovment = 4;
        break;
    } 
    statistics[2] = defaultStats[2] + Math.round(statistics[1]*neurons[neurons.length-1][outputNeuronPos][0]);
    statistics[3] = defaultStats[3] + (10*Math.round(statistics[1]*neurons[neurons.length-1][outputNeuronPos+1][0]));
    statistics[4] = defaultStats[4] + Math.round(statistics[1]*neurons[neurons.length-1][outputNeuronPos+2][0]);
    statistics[5] = defaultStats[5] + Math.round(statistics[1]*neurons[neurons.length-1][outputNeuronPos+3][0]);
    statistics[8] = defaultStats[8] + Math.round(statistics[1]*neurons[neurons.length-1][outputNeuronPos+4][0]);
  }
}