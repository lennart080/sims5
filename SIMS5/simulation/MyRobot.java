package SIMS5.simulation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyRobot {
  //objekts
  private SimManager manager;
  //get set
  private double[] statistics;
  private int[] priceList;
  private double[] updateList = {0.06, 0.6, 6.0, 2.5}; //rost plus, rost loss, energy loss, health loss --per sec
  private double walkActivasion = 0.6;
  private double upgradActivasion = 0.9;
  private int roboSize;
  private int simSize;
  private double[][] neurons;   //[] reihe [][] neuron
  private double[][][] weigths;  //[] reihe [][] neuron [][][] verbindung(2tes neuron)
  private List<int[]> position = new ArrayList<>();
  //run time
  private int serialNumber;
  public static int lastSerialNumber;
  private List<Boolean> positionSwitch = new ArrayList<>();
  private long[] calcTime = new long[5];
  private long calcTimeSave;
  private int day;
  private int lastMovment;
  
  public MyRobot(SimManager pManager, double[][][] pWeigths, double[][] pNeurons, int[] pPosition, double[] startStatistics, int[] pPriceList, int pRoboSize, int pSimSize) {
    manager = pManager;
    weigths = Arrays.copyOf(pWeigths, pWeigths.length);
    neurons = Arrays.copyOf(pNeurons, pNeurons.length);
    statistics = Arrays.copyOf(startStatistics, startStatistics.length);
    roboSize = pRoboSize;
    simSize = pSimSize;
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
    formatOutputNeurons();
    calcTime[3] = System.nanoTime() - calcTimeSave;
    calcTimeSave = System.nanoTime();
    setOutputs();
    calcTime[4] = System.nanoTime() - calcTimeSave;
  }

  public double roundToDecPlaces(double value, int decPlaces) {
    return Math.round(value * (Math.pow(10, decPlaces))) / (Math.pow(10, decPlaces));
  }

  public static int normaliseValue(double value, int oldMax, int newMax) {     
    int originalMax = oldMax;
    double originalRange = (double) (originalMax);
    double newRange = (double) (newMax);
    double scaledValue = (value * newRange) / originalRange;
    return (int) Math.min(Math.max(scaledValue, 0), newMax);
  }

  public void checkBounds() {
    int robotX = position.get(position.size()-1)[0]; 
    int robotY = position.get(position.size()-1)[1];
    if (robotX < (roboSize/2)) {
      robotX = (roboSize/2);
    }
    if (robotX > (simSize-(roboSize/2))) {
      robotX = (simSize-(roboSize/2));
    }
    if (robotY < (roboSize/2)) {
      robotY = (roboSize/2);
    }
    if (robotY > (simSize-(roboSize/2))) {
      robotY = (simSize-(roboSize/2));
    }
    if (position.get(position.size()-1)[0] != robotX) {
      position.get(position.size()-1)[0] = robotX;
      positionSwitch.set(positionSwitch.size()-1, false);
      lastMovment = 0;
    }
    if (position.get(position.size()-1)[1] != robotY) {
      position.get(position.size()-1)[1] = robotY;
      positionSwitch.set(positionSwitch.size()-1, false);
      lastMovment = 0;
    }
  }

  private void updateStatistics(double lightIntensity) {
    boolean posWechsel = false;
    for (int i = 0; i < positionSwitch.size(); i++) {
      if (positionSwitch.get(i) == true) {
        posWechsel = true;
      }
    }
    if (posWechsel == false) {
        statistics[7] = roundToDecPlaces(statistics[7]+(updateList[0]/60), 3);
    } else {
      if (statistics[7] >= 0.01) {
        statistics[7] = roundToDecPlaces(statistics[7]-(updateList[1]/60), 3);
      }
    }
    if (statistics[0] < statistics[3]) { // wenn weniger energie als speicherplatz
      statistics[0] = roundToDecPlaces(statistics[0]+((statistics[8]*lightIntensity)/60), 3); // bekommt enerige
      if (statistics[0] > statistics[3]) { // wenn zu viel energie
        statistics[0] = statistics[3]; // bekommt max enegrie
      }
    }
    statistics[0] = roundToDecPlaces(statistics[0]-(((updateList[2]/60)*(day+1))+statistics[7]), 3); // energie verlust
    if (statistics[0] <= 0.0) { // wenn keine energie
      statistics[0] = 0.0; // bekommt min energie
      statistics[6] = roundToDecPlaces(statistics[6]-(updateList[3]/60), 2); // leben verlust
      if (statistics[6] <= 0.0) { // wenn keine enerige
        manager.deleteRobo(this); // stirbt
      }
    } 
  }

  private void setInputs() {
    neurons[0][0] =  roundToDecPlaces(statistics[0]/statistics[3], 4); 
    neurons[0][1] = statistics[2]/10; // atk/10
    neurons[0][2] = statistics[4]/10; // speed/10
    neurons[0][3] = statistics[5]/10; // defence/10
    neurons[0][4] = roundToDecPlaces(statistics[6]/10, 4); // health/10
    neurons[0][5] = statistics[7]; // rust
    neurons[0][6] = statistics[8]/10; // solar/10
    neurons[0][7] = roundToDecPlaces((double)position.get(position.size()-1)[0]/(double)simSize, 4); // x pos in prozent zu max (0 bis 1)
    neurons[0][8] = roundToDecPlaces((double)position.get(position.size()-1)[1]/(double)simSize, 4); // y pos in prozent zu max (0 bis 1)
    neurons[0][9] = (double)lastMovment/4.0; //last movment (0 still, 0.25 up, 0.5 right, 0.75 down, 1.0 left)
  }

  private void calculate() {
    for (int i = 1; i < neurons.length; i++) {
      for (int j = 0; j < neurons[i].length; j++) {
        neurons[i][j] = 0;
        for (int j2 = 0; j2 < neurons[i-1].length; j2++) {
          neurons[i][j] += neurons[i-1][j2] * weigths[i-1][j2][j];
        }
        if (i != neurons.length-1) { // LReLU funktion for hidden neurons
          neurons[i][j] = roundToDecPlaces((neurons[i][j] > 0) ? neurons[i][j] : 0.01 * neurons[i][j], 4);
        } 
      }
    }
  }

  private void formatOutputNeurons() {
    double sumExp = 0.0; // softmax funktion for the first 4 outputs (walking)
    for (int i = 0; i < 4; i++) {
      sumExp += Math.exp(neurons[neurons.length-1][i]);
    }
    for (int i = 0; i < 4; i++) {
      neurons[neurons.length-1][i] = roundToDecPlaces(Math.exp(neurons[neurons.length-1][i]) / sumExp, 4);
    }
    double sumExp2 = 0.0; // softmax funktion for the outputs 5 to 9 (walking)
    for (int i = 4; i < 9; i++) {
      sumExp2 += Math.exp(neurons[neurons.length-1][i]);
    }
    for (int i = 4; i < 9; i++) {
      neurons[neurons.length-1][i] = roundToDecPlaces(Math.exp(neurons[neurons.length-1][i]) / sumExp2, 4);
    }
  }

  private void setOutputs() {
    int outputNeuronPos = 0;
    int highestPosneuron = -1;
    double highestPos = 0;
    for (int i = 0; i < 4; i++) {
      if (neurons[neurons.length-1][outputNeuronPos] > highestPos) {
        highestPos = neurons[neurons.length-1][outputNeuronPos];
        if (highestPos >= walkActivasion) {
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
        lastMovment = 1;
        checkBounds();    
        break;
      case 1:
        pos[0] = position.get(position.size()-1)[0]+(int)statistics[4];
        pos[1] = position.get(position.size()-1)[1];
        position.add(pos);   
        position.remove(0);   
        lastMovment = 2;
        checkBounds(); 
        break;
      case 2:
        pos[0] = position.get(position.size()-1)[0];
        pos[1] = position.get(position.size()-1)[1]-(int)statistics[4];
        position.add(pos);      
        position.remove(0); 
        lastMovment = 3;
        checkBounds(); 
        break;
      case 3:
        pos[0] = position.get(position.size()-1)[0]-(int)statistics[4];
        pos[1] = position.get(position.size()-1)[1];
        position.add(pos);
        position.remove(0);   
        lastMovment = 4;
        checkBounds();     
        break;
    } 
    int relativeNeuronPos = outputNeuronPos;
    highestPosneuron = -1;
    highestPos = 0;
    for (int i = 0; i < 5; i++) {
      if (neurons[neurons.length-1][outputNeuronPos] > highestPos) {
        highestPos = neurons[neurons.length-1][outputNeuronPos];
        if (highestPos >= upgradActivasion) {
          highestPosneuron = outputNeuronPos;        
        }
      }
      outputNeuronPos++;
    }
    switch (outputNeuronPos-relativeNeuronPos){
      case  0:     //atk
        if (priceList[0] + (statistics[2]*2) < statistics[1]) {
          statistics[1]-= priceList[0] + (statistics[2]*2);
          statistics[2] += 1;             
        }
        break;
      case 1: // energie speicher
        if (priceList[1] + (statistics[3] * 2) < statistics[1]) {
          statistics[1] -= priceList[1] + (statistics[3] * 2);
          statistics[3] += 10;
        }
        break;
      case 2: // speed
        if (priceList[2] + (statistics[4] * 2) < statistics[1]) {
          statistics[1] -= priceList[2] + (statistics[4] * 2);
          statistics[4] += 1;
        }
        break;
      case 3: // defence
        if (priceList[3] + (statistics[5] * 2) < statistics[1]) {
          statistics[1] -= priceList[3] + (statistics[5] * 2);
          statistics[5] += 1;
        }
        break;
      case 4: // solar
        if (priceList[4] + (statistics[8] * 2) < statistics[1]) {
          statistics[1] -= priceList[4] + (statistics[8] * 2);
          statistics[8] += 1;
        }
        break;
    }
  }
}