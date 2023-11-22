public class Robot {
  private int serialNumber;
  protected static int lastSerialNumber;
  private double[] position;
  private int[] dna;
  private int[] statistics = new int[7];
  
  public Robot(int[] pDna, double[] pPosition) {
    serialNumber = lastSerialNumber;
    lastSerialNumber++;
    dna = pDna;
    position = pPosition;

    //energie                       energie des robos welche für vortbewegung und attaken und alles weitere benötigt wird
    statistics[0] = 100;
    //schrott                       währung mit welcher teile und kinder "hergestellt" werden können
    statistics[1] = 100;
    //attack                        schaden welcher pro atacke zugerichtet wird
    statistics[2] = 0;
    //energie speicher              max energie die der robo haben kann
    statistics[3] = 100;
    //speed                         ...pixel pro sec werden max zurückgelegt     
    statistics[4] = 10;
    //defense                       wird von der gegnerischen attake abgezogen
    statistics[5] = 0; 
    //health                        anzahl der leben welche von ataken veringert werden kann und sinkt wenn energie 0 ist. bei 0 leben stirbt er
    statistics[6] = 5;       
  }
  
  //get methoden des roboters 
  
  public int[] getDna() {
    return dna;
  }
  
  public double[] getPosition() {
    return position;
  }
  
  public int[] getStatistics() {
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
    statistics[0]-=1;
  }
}