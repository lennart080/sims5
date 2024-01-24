package SIMS5.simulation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyEntity {
  //objekts
  private SimManager manager;
  //get set
  private double[] statistics;
  private double[] defaultStats;
  private double[] updateList = {0.06, 0.6, 6.0, 2.5}; //rost plus, rost loss, energy loss, health loss --per sec
  private double walkActivasion = 0.3;
  private int entitySize;
  private int simSize;
  private List<int[]> position = new ArrayList<>();
  private List<List<Double>> neurons; 
  private List<List<List<Double[]>>> weights;

  //run time
  private int[] endStats = new int[1];
  private boolean alive = true;
  private int serialNumber;
  public static int lastSerialNumber;
  private List<Boolean> positionSwitch = new ArrayList<>();
  private long[] calcTime = new long[5];
  private long calcTimeSave;
  private int day;
  private int lastMovment;
  
  public MyEntity(SimManager pManager, List<List<List<Double[]>>> pWeigths, List<List<Double>> pNeurons, int[] pPosition, double[] startStatistics, int pRoboSize, int pSimSize) {
    manager = pManager;
    neurons = new ArrayList<>(pNeurons);
    weights = new ArrayList<>(pWeigths);
    statistics = Arrays.copyOf(startStatistics, startStatistics.length);
    defaultStats = Arrays.copyOf(startStatistics, startStatistics.length);
    entitySize = pRoboSize;
    simSize = pSimSize;
    serialNumber = lastSerialNumber;
    lastSerialNumber++;

    for (int i = 0; i < 5; i++) {
      position.add(pPosition);
    }
    for (int i = 0; i < 5; i++) {
      positionSwitch.add(true);
    }
  }

  //-------------------------------------

  //--------------abfrage----------------

  public boolean alive() {
    return alive;
  }

  //-------------------------------------
  
  //----------------get------------------

  public List<List<Double>> getNeurons() {
    return neurons;
  }

  public List<List<List<Double[]>>> getWeights() {
    return weights;
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

  public int[] getEndStats() {
    return endStats;
  }

  //-------------------------------------

  //----------------set------------------

  public void setDay(int pDay) {
    day = pDay;
  }

  //--------------------------------------

  //-----------neural network-------------

  public void simulate(double lightIntensity) {
    //newSimulate(lightIntensity);
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
    int x = position.get(position.size()-1)[0]; 
    int y = position.get(position.size()-1)[1];
    if (x < (entitySize/2)) {
      x = (entitySize/2);
    }
    if (x > (simSize-(entitySize/2))) {
      x = (simSize-(entitySize/2));
    }
    if (y < (entitySize/2)) {
      y = (entitySize/2);
    }
    if (y > (simSize-(entitySize/2))) {
      y = (simSize-(entitySize/2));
    }
    if (position.get(position.size()-1)[0] != x) {
      position.get(position.size()-1)[0] = x;
      positionSwitch.set(positionSwitch.size()-1, false);
      lastMovment = 0;
    }
    if (position.get(position.size()-1)[1] != y) {
      position.get(position.size()-1)[1] = y;
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
        endStats[0] = manager.getUpdates();
        alive = false;
      }
    } 
  }

  private void setInputs() {
    neurons.get(0).set(0, roundToDecPlaces(statistics[0]/statistics[3], 4)); //prozent des "akkus" (0-1) (energie/speicher)
    neurons.get(0).set(1, statistics[2]/10); // atk/10
    neurons.get(0).set(2, statistics[4]/10); // speed/10
    neurons.get(0).set(3, statistics[5]/10); // defence/10
    neurons.get(0).set(4, roundToDecPlaces(statistics[6]/10, 4)); // health/10
    neurons.get(0).set(5, statistics[7]); // rust
    neurons.get(0).set(6, statistics[8]/10); // solar/10
    neurons.get(0).set(7, roundToDecPlaces((double)position.get(position.size()-1)[0]/(double)simSize, 4)); // x pos in prozent zu max (0 bis 1)
    neurons.get(0).set(8, roundToDecPlaces((double)position.get(position.size()-1)[1]/(double)simSize, 4)); // y pos in prozent zu max (0 bis 1)
    neurons.get(0).set(9, (double)lastMovment/4.0); //last movment (0 still, 0.25 up, 0.5 right, 0.75 down, 1.0 left)
  }


  private void calculate() {
    for (int i = 1; i < weights.size(); i++) {
      for (int j = 0; j < weights.get(i).size(); j++) {
        neurons.get(i).set(j, 0.0);
        for (int j2 = 0; j2 < weights.get(i).get(j).size(); j2++) {
          double cnv = neurons.get(weights.get(i).get(j).get(j2)[0].intValue()).get(weights.get(i).get(j).get(j2)[1].intValue());
          double w = weights.get(i).get(j).get(j2)[2];
          neurons.get(i).set(j, neurons.get(i).get(j)+(w*cnv));
        }
        if (i != neurons.size()-1) {  // LReLU funktion for hidden neurons
          neurons.get(i).set(j, roundToDecPlaces((neurons.get(i).get(j) > 0) ? neurons.get(i).get(j) : 0.01 * neurons.get(i).get(j), 4));
        }
      }
    }
  }

  private void formatOutputNeurons() {
    double sumExp = 0.0; // softmax funktion for the first 4 outputs (walking)
    for (int i = 0; i < 4; i++) {
      sumExp += Math.exp(neurons.get(neurons.size()-1).get(i));
    }
    for (int i = 0; i < 4; i++) {
      neurons.get(neurons.size()-1).set(i, roundToDecPlaces(Math.exp(neurons.get(neurons.size()-1).get(i)) / sumExp, 4));
    }
    double sumExp2 = 0.0; // softmax funktion for the outputs 5 to 9 (upgrades)
    for (int i = 4; i < 9; i++) {
      sumExp2 += Math.exp(neurons.get(neurons.size()-1).get(i));
    }
    for (int i = 4; i < 9; i++) {
      neurons.get(neurons.size()-1).set(i, roundToDecPlaces(Math.exp(neurons.get(neurons.size()-1).get(i)) / sumExp2, 4));
    }
  }

  private void setOutputs() {
    int outputNeuronPos = 0;
    int highestPosneuron = -1;
    double highestPos = 0;
    for (int i = 0; i < 4; i++) {
      if (neurons.get(neurons.size()-1).get(outputNeuronPos) > highestPos) {
        highestPos = neurons.get(neurons.size()-1).get(outputNeuronPos);
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
    statistics[2] = defaultStats[2] + Math.round(statistics[1]*neurons.get(neurons.size()-1).get(outputNeuronPos));
    statistics[3] = defaultStats[3] + (10*Math.round(statistics[1]*neurons.get(neurons.size()-1).get(outputNeuronPos+1)));
    statistics[4] = defaultStats[4] + Math.round(statistics[1]*neurons.get(neurons.size()-1).get(outputNeuronPos+2));
    statistics[5] = defaultStats[5] + Math.round(statistics[1]*neurons.get(neurons.size()-1).get(outputNeuronPos+3));
    statistics[8] = defaultStats[8] + Math.round(statistics[1]*neurons.get(neurons.size()-1).get(outputNeuronPos+4));
  }
}