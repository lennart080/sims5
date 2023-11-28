import panels.MyPanel;
public class Robot {
  private Manager manager;
  private int serialNumber;
  protected static int lastSerialNumber;
  private double[][] position = new double[3][2];
  private int[] dna;
  private double[] statistics = new double[8];

  private double[][] fieldInfos = new double[4][3];      //4 richtungen(0=forne,1=rechts,2=hinten,3=links) 3 entfenrung(0=wandt,1=gegner,2=schrott)

  private double[][] neurons = new double[5][];   //[] reihe [][] neuron   //55555555555555555555555 in first
  private double[][][] weigths = new double[neurons.length-1][][];  //[] reihe [][] neuron [][][] verbindung(2tes neuron)
  
  public Robot(Manager pManager, int[] pDna, double[] pPosition) {
    manager = pManager;
    serialNumber = lastSerialNumber;
    lastSerialNumber++;
    dna = pDna;
    for (int i = 0; i < position.length; i++) {
      position[i] = pPosition;
    }
    
    neurons[0] = new double [statistics.length+(fieldInfos[0].length*fieldInfos.length)];
    neurons[1] = new double[10];
    neurons[2] = new double[10];
    neurons[3] = new double[10];
    neurons[4] = new double[10];   // 0-3 inRichtungBewegen; 4-7 atk,enSp,sp,def upgade; 8 attack; 9 kind
    
    for (int i = 0; i < weigths.length; i++) {
      weigths[i] = new double[neurons[i].length][neurons[i+1].length];
    }
    
    //energie                       energie des robos welche für vortbewegung und attaken und alles weitere benötigt wird
    statistics[0] = 10.0;
    //schrott (int)                      währung mit welcher teile und kinder "hergestellt" werden können
    statistics[1] = 100.0;
    //attack                        schaden welcher pro atacke zugerichtet wird
    statistics[2] = 0.0;
    //energie speicher              max energie die der robo haben kann
    statistics[3] = 100.0;
    //speed                         ...pixel pro sec werden max zurückgelegt     
    statistics[4] = 10.0;
    //defense                       wird von der gegnerischen attake abgezogen
    statistics[5] = 0.0; 
    //health                        anzahl der leben welche von ataken veringert werden kann und sinkt wenn energie 0 ist. bei 0 leben stirbt er
    statistics[6] = 5.0;       
    //rust                          rost bildet sich wenn der robo länger auf der stelle steht, je mehr rost deszo mehr energie verbrauch 
    statistics[7] = 0.0; 
  }
  
  //get methoden des roboters 
  
  public int[] getDna() {
    return dna;
  }
  
  public double[] getPosition() {
    return position[0];
  }
  
  public double[] getStatistics() {
    return statistics;
  }
  
  public int getSerialNumber() {
    return serialNumber;
  }
  
  //set methoden des Roboters 
  
  public void setStatistic(String statistic, int value) {
    
  }

  //hirn des robos

  public void simulate(double pLightIntensity){
    updateStatistics();
    setInputs();
    calculate();
  }

  private void updateStatistics() {
    if (position[0] == position[1]) {
      statistics[7] = MyPanel.roundToDecPlaces(statistics[7]+0.02, 2);;
    } else {
      if (statistics[7] > 0.0) {
        statistics[7] = MyPanel.roundToDecPlaces(statistics[7]-0.02, 2);;
      }
    }
    if (statistics[6] > 0.0) {
      if (statistics[0] > 0.0) {
        statistics[0] = MyPanel.roundToDecPlaces(statistics[0]-(0.1+statistics[7]), 1);
        if (statistics[0] < 0.0) {
          statistics[0] = 0.0;
        }
      } else {
        statistics[6] = MyPanel.roundToDecPlaces(statistics[6]-0.1, 1);
        if (statistics[6] < 0.0) {
          statistics[6] = 0.0;
        }
      }     
    } else {
      manager.deleteRobo(serialNumber);       
    }
  }

  private void setInputs() {
    for (int i = 0; i < statistics.length; i++) {
      neurons[0][i] = statistics[i];
    }
    int zähler = 0;
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
          neurons[i][j] += MyPanel.sigmoid(neurons[i-1][j2] * weigths[i-1][j2][j]);
        }
      }
    }
    for (int i = 0; i < neurons[neurons.length].length; i++) {
      System.out.println(i + ": " + neurons[neurons.length][i]); 
    }
  }
}