public class Robot {
  private int serialNumber;
  protected static int lastSerialNumber;
  private int[] position;
  private int[] dna;
  private int[] statistics;
  
  public Robot(int[] pDna, int[] pPosition) {
    serialNumber = lastSerialNumber;
    lastSerialNumber++;
    dna = pDna;
    position = pPosition;
  }
  
  //get methoden des roboters 
  
  public int[] getDna() {
    return dna;
  }
  
  public int[] getPosition() {
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
    dna[0] = 5; 
    position[2] = 5;
    }
}