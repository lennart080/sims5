package SIMS5.simulation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyRobot {
  private SimManager manager;
  private int serialNumber;
  protected static int lastSerialNumber;
  private List<int[]> position = new ArrayList<>();
  private List<Boolean> positionSwitch = new ArrayList<>();
  private double[] statistics;
  private int day;
  private int[] priceList;

  //private double[][] fieldInfos = new double[4][3];      //4 richtungen(0=oben,1=rechts,2=unten,3=links) 3 entfenrung(0=wandt,1=gegner,2=schrott)

  private double[][] neurons;   //[] reihe [][] neuron
  private double[][][] weigths;  //[] reihe [][] neuron [][][] verbindung(2tes neuron)

  private long[] calcTime = new long[4];
  private long calcTimeSave;
  
  public MyRobot(SimManager pManager, double[][][] pWeigths, double[][] pNeurons, int[] pPosition, double[] startStatistics, int[] pPriceList) {
    manager = pManager;
    weigths = Arrays.copyOf(pWeigths, pWeigths.length);
    neurons = Arrays.copyOf(pNeurons, pNeurons.length);
    statistics = Arrays.copyOf(startStatistics, startStatistics.length);
    serialNumber = lastSerialNumber;
    lastSerialNumber++;

    for (int i = 0; i < 5; i++) {
      position.add(pPosition);
    }
    for (int i = 0; i < 5; i++) {
      positionSwitch.add(true);
    }
    priceList = pPriceList;
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

  public double getEnergieLoss() {
    return roundToDecPlaces(((0.1*(manager.getDay()+1))+statistics[7]), 2);
  }

  public double getEnergiePlus() {
    return roundToDecPlaces(((statistics[8]*manager.getLightIntensityAtTime())/60), 2);
  }

  public long[] getCalcTime() {
    return calcTime;
  }

  public void moveRobot(int x, int y) {
    //position.get(position.size()-1)[0]+=x;
    //position.get(position.size()-1)[1]+=y;
    //System.out.println("x: " + x);
    //System.out.println("y: " + y);
  }

  public void setDay(int pDay) {
    day = pDay;
  }

  //hirn des robos

  public void simulate(double lightIntensity) {
    calcTimeSave = System.nanoTime();
    updateStatistics(lightIntensity);
    calcTime[0] = System.nanoTime() - calcTimeSave;
    calcTimeSave = System.nanoTime();
    setInputs();
    calcTime[1] = System.nanoTime() - calcTimeSave;
    calcTimeSave = System.nanoTime();
    calculate();
    calcTime[2] = System.nanoTime() - calcTimeSave;
    calcTimeSave = System.nanoTime();
    setOutputs();
    calcTime[3] = System.nanoTime() - calcTimeSave;
  }

  public double roundToDecPlaces(double value, int decPlaces) {
    return Math.round(value * (Math.pow(10, decPlaces))) / (Math.pow(10, decPlaces));
  }

  private void updateStatistics(double lightIntensity) {
    boolean posWechsel = false;
    for (int i = 0; i < positionSwitch.size(); i++) {
      if (positionSwitch.get(i) == true) {
        posWechsel = true;
      }
    }
    if (posWechsel == false) {
        statistics[7] = roundToDecPlaces(statistics[7]+0.01, 2);
    } else {
      if (statistics[7] >= 0.01) {
        statistics[7] = roundToDecPlaces(statistics[7]-0.01, 2);
      }
    }
    if (statistics[6] > 0.0) {
      statistics[0] = roundToDecPlaces(statistics[0]-((0.1*(day+1))+statistics[7]), 2);
      if (statistics[0] < statistics[3]) {
        statistics[0] = roundToDecPlaces(statistics[0]+((statistics[8]*lightIntensity)/60), 2); 
        if (statistics[0] > statistics[3]) {
          statistics[0] = statistics[3];
        }
      }
      if (statistics[0] <= 0.0) {
        statistics[6] = roundToDecPlaces(statistics[6]-0.1, 1);
        if (statistics[6] < 0.0) {
          statistics[6] = 0.0;
        }
      }    
    } else {
      manager.deleteRobo(this);     
    }
  }

  private void setInputs() {
    System.arraycopy(statistics, 0, neurons[0], 0, statistics.length);
    //System.arraycopy(fieldInfos, 0, neurons[0], statistics.length, fieldInfo.length);
  }

  private void calculate() {
    for (int i = 1; i < neurons.length; i++) {
      for (int j = 0; j < neurons[i].length; j++) {
        neurons[i][j] = 0;
        for (int j2 = 0; j2 < neurons[i-1].length; j2++) {
          neurons[i][j] += neurons[i-1][j2] * weigths[i-1][j2][j];
        }
        neurons[i][j] = roundToDecPlaces(1 / (1 + Math.pow(2.71, -(neurons[i][j]))), 3);
      }
    }
  }

  private void setOutputs() {
    int outputNeuronPos = 0;
    int highestPosneuron = -1;
    double highestPos = 0;
    for (int i = 0; i < 4; i++) {
      if (neurons[neurons.length-1][outputNeuronPos] > highestPos) {
        highestPos = neurons[neurons.length-1][outputNeuronPos];
        if (highestPos > 0.5) {
          highestPosneuron = outputNeuronPos;        
        }
      }
      outputNeuronPos++;
    }
    int[] pos = new int[2];
    if (0 <= highestPosneuron && highestPosneuron <= 3) {
      positionSwitch.add(true);
      positionSwitch.remove(0);
    } else {
      positionSwitch.add(false);
      positionSwitch.remove(0);      
    }
    switch (highestPosneuron) {
      case 0:
        pos[0] = position.get(position.size()-1)[0];
        pos[1] = position.get(position.size()-1)[1]+(int)statistics[4];
        position.add(pos);
        position.remove(0);   
        manager.checkHitBoxes(serialNumber);    
        break;
      case 1:
        pos[0] = position.get(position.size()-1)[0]+(int)statistics[4];
        pos[1] = position.get(position.size()-1)[1];
        position.add(pos);   
        position.remove(0);   
        manager.checkHitBoxes(serialNumber); 
        break;
      case 2:
        pos[0] = position.get(position.size()-1)[0];
        pos[1] = position.get(position.size()-1)[1]-(int)statistics[4];
        position.add(pos);      
        position.remove(0); 
        manager.checkHitBoxes(serialNumber);
        break;
      case 3:
        pos[0] = position.get(position.size()-1)[0]-(int)statistics[4];
        pos[1] = position.get(position.size()-1)[1];
        position.add(pos);
        position.remove(0);   
        manager.checkHitBoxes(serialNumber);     
        break;
    }   
    for (int i = 0; i < 5; i++) {
      if (neurons[neurons.length-1][outputNeuronPos] >= 0.5) {
        switch (i){
          case  0:     //atk
            if (priceList[0] + (statistics[2]*2) < statistics[1]) {
              statistics[1]-= priceList[0] + (statistics[2]*2);
              statistics[2] += 1;             
            }
            break;
          case  1:     //energie speicher
            if (priceList[1] + (statistics[3]*2) < statistics[1]) {
              statistics[1]-= priceList[1] + (statistics[3]*2);
              statistics[3] += 10;             
            }
            break;
          case  2:     //speed
            if (priceList[2] + (statistics[4]*2) < statistics[1]) {
              statistics[1]-= priceList[2] + (statistics[4]*2);
              statistics[4] += 1;             
            }
            break;
          case  3:     //defence
            if (priceList[3] + (statistics[5]*2) < statistics[1]) {
              statistics[1]-= priceList[3] + (statistics[5]*2);
              statistics[5] += 1;             
            }
            break;
          case  4:     //solar
            if (priceList[4] + (statistics[8]*2) < statistics[1]) {
              statistics[1]-= priceList[4] + (statistics[8]*2);
              statistics[8] += 1;             
            }
            break;
        }
      }
      outputNeuronPos++;
    }
  }
}