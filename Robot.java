import panels.MyPanel;
public class Robot {
  private Manager manager;
  private int serialNumber;
  protected static int lastSerialNumber;
  private double[][] position = new double[3][2];
  private int[] dna;
  private double[] statistics = new double[8];
  
  public Robot(Manager pManager, int[] pDna, double[] pPosition) {
    manager = pManager;
    serialNumber = lastSerialNumber;
    lastSerialNumber++;
    dna = pDna;
    for (int i = 0; i < position.length; i++) {
      position[i] = pPosition;
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
}