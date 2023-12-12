package SIMS5.simulation;
import SIMS5.calculator.Calculator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyRobot {
  private SimManager manager;
  private int serialNumber;
  protected static int lastSerialNumber;
  private List<int[]> position = new ArrayList<>();
  private double[] statistics;

  private double[][] fieldInfos = new double[4][3];      //4 richtungen(0=oben,1=rechts,2=unten,3=links) 3 entfenrung(0=wandt,1=gegner,2=schrott)

  private double[][] neurons;   //[] reihe [][] neuron
  private double[][][] weigths;  //[] reihe [][] neuron [][][] verbindung(2tes neuron)
  
  public MyRobot(SimManager pManager, double[][][] pWeigths, double[][] pNeurons, int[] pPosition, double[] startStatistics) {
    manager = pManager;
    weigths = Arrays.copyOf(pWeigths, pWeigths.length);
    neurons = Arrays.copyOf(pNeurons, pNeurons.length);
    statistics = Arrays.copyOf(startStatistics, startStatistics.length);
    serialNumber = lastSerialNumber;
    lastSerialNumber++;

    for (int i = 0; i < 5; i++) {
      position.add(pPosition);
    }
  }
  
  //get methoden des roboters 
  
  public double[][][] getWeights() {
    return weigths;
  }

  public double[][] getNeurons() {
    return neurons;
  }
  
  public int[][] getPositions() { 
    int[][] pos = new int[position.size()][];
    for (int i = 0; i < position.size(); i++) {
      pos[i] = position.get(i);
    }
    return pos;
  }
  
  public double[] getStatistics() {
    return statistics;
  }
  
  public int getSerialNumber() {
    return serialNumber;
  }

  //hirn des robos

  public void simulate(double pLightIntensity){
    updateStatistics();
    setInputs();
    calculate();
    setOutputs();
  }

  private void updateStatistics() {
    int x = 0;
    boolean posWechsel = false;
    do {
      if (position.get(x) != position.get(x+1)) {
        posWechsel = true;
      }
      x++;
    } while (position.size() > x+2);
    if (posWechsel == false) {
      if (position.get(position.size()-1) == position.get(position.size()-2)) {
        statistics[7] = Calculator.roundToDecPlaces(statistics[7]+0.02, 2);
      }
    } else {
      if (statistics[7] > 0.0) {
        statistics[7] = Calculator.roundToDecPlaces(statistics[7]-0.02, 2);
      }
    }
    if (statistics[6] > 0.0) {
      if (statistics[0] > 0.0) {
        statistics[0] = Calculator.roundToDecPlaces(statistics[0]-(0.1+statistics[7]), 1);
        if (statistics[0] < 0.0) {
          statistics[0] = 0.0;
        }
      } else {
        statistics[6] = Calculator.roundToDecPlaces(statistics[6]-0.1, 1);
        if (statistics[6] < 0.0) {
          statistics[6] = 0.0;
        }
      }     
    } else {
      manager.deleteRobo(serialNumber);       
    }
  }

  private void setInputs() {
    int zähler = 0;
    for (int i = 0; i < statistics.length; i++) {
      neurons[0][i] = statistics[zähler];
      zähler++;
    }
    for (int i = 0; i < fieldInfos.length; i++) {
      for (int j = 0; j < fieldInfos[i].length; j++) {
        neurons[0][zähler] = fieldInfos[i][j];
        zähler++;
      }
    }
  }

  private void calculate() {
    for (int i = 1; i < neurons.length; i++) {
      for (int j = 0; j < neurons[i].length; j++) {
        neurons[i][j] = 0;
        for (int j2 = 0; j2 < neurons[i-1].length; j2++) {
          neurons[i][j] += neurons[i-1][j2] * weigths[i-1][j2][j];
        }
        neurons[i][j] = Calculator.sigmoid(neurons[i][j]);
      }
    }
  }

  private void setOutputs() {
    int outputNeuronPos = 0;
    int highestPosneuron = -1;
    double highestPos = 0;
    for (int i = 0; i < 3; i++) {
      if (neurons[neurons.length-1][outputNeuronPos] > highestPos) {
        highestPos = neurons[neurons.length-1][outputNeuronPos];
        if (highestPos > 0.5) {
          highestPosneuron = outputNeuronPos;        
        }
      }
      outputNeuronPos++;
    }
    int[] pos = new int[2];
    switch (highestPosneuron) {
      case 0:
        pos[0] = position.get(position.size()-1)[0];
        pos[1] = position.get(position.size()-1)[1]+1;
        position.add(pos);
        position.remove(0);       
        break;
      case 1:
        pos[0] = position.get(position.size()-1)[0]+1;
        pos[1] = position.get(position.size()-1)[1];
        position.add(pos);   
        position.remove(0);    
        break;
      case 2:
        pos[0] = position.get(position.size()-1)[0];
        pos[1] = position.get(position.size()-1)[1]-1;
        position.add(pos);      
        position.remove(0); 
        break;
      case 3:
        pos[0] = position.get(position.size()-1)[0]-1;
        pos[1] = position.get(position.size()-1)[1];
        position.add(pos);
        position.remove(0);        
        break;
      default:
        break;
    }  
    /* 
    for (int i = 0; i < 5; i++) {
      if (neurons[neurons.length-1][outputNeuronPos] > 0.4) {
        switch (i){
          case  0:
            if (manager.getBasePrice() + ((int)statistics[2]*2) < statistics[0]) {
              statistics[0]-= manager.getBasePrice() + ((int)statistics[2]*2);
              statistics[2] += 1;             
            }
            break;
          case  1:
            if (manager.getBasePrice() + ((int)statistics[3]*2) < statistics[0]) {
              statistics[0]-= manager.getBasePrice() + ((int)statistics[3]*2);
              statistics[3] += 1;             
            }
            break;
          case  2:
            if (manager.getBasePrice() + ((int)statistics[4]*2) < statistics[0]) {
              statistics[0]-= manager.getBasePrice() + ((int)statistics[4]*2);
              statistics[4] += 1;             
            }
            break;
          case  3:
            if (manager.getBasePrice() + ((int)statistics[5]*2) < statistics[0]) {
              statistics[0]-= manager.getBasePrice() + ((int)statistics[5]*2);
              statistics[5] += 1;             
            }
            break;
          case  4:
            if (manager.getBasePrice() + ((int)statistics[8]*2) < statistics[0]) {
              statistics[0]-= manager.getBasePrice() + ((int)statistics[8]*2);
              statistics[8] += 1;             
            }
            break;
          default:
          break;
        }
        statistics[0] = Calculator.roundToDecPlaces(statistics[0],2); 
      }
      outputNeuronPos++;
    }
    */
  }
}