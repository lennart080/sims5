public class SimulationData {
  private int[][] board; 
  private double lightIntensity;
  private double lightAmplitude;
  private int seed;
  public SimulationData() {
    //newBoard(100, 70);
  }
  
  //set methoden der simulation daten
  
  public void setBoardSize(int x, int y) {            //erstellen der größe der simulation (einzige einstellung die nur einmal gesetzt werden kann)
    if (board == null) {
      board = new int[x][y];
    }
  }
  
  public void setLightIntensity(int intensity) {            //setzen der licht stärke in der simulation
    if (intensity >= 0) {
      lightIntensity = intensity;  
    } else {
      lightIntensity = 0;
    } 
  }
  
  public void setSeed(int pSeed) {              //setzen des seed welcher zb. für die zufällichkeit der simulation sorgt
    if (pSeed >= 0) {
      seed = pSeed;
    } else {
      seed = 0;
    } 
  }
  
  public void setLightChangeStrength(double pAmplitude) {             //setzen der schwankungen mit der zeit der stärke des lichts 
    if (pAmplitude >= 0.0) {
      if (pAmplitude <= 2.0) {
        lightAmplitude = pAmplitude;
      } else {
        lightAmplitude = 2.0;
      }
    } else {
      lightAmplitude = 0.0;
    } 
  }
  
  //get methoden der simulation daten

  public double getLightIntensity() {
    return lightIntensity;
  }
  
  public double getLightChangeStrenght() {
    return lightAmplitude;
  }
  
  public int getSeed() {
    return seed;
  }
  
  //methoden zur erstellung des 1d perlin noise
  
  public double getLightIntensityAtTime(int time) {              //berechnung der licht intensivität zu einer bestimmten zeit
    double lightIntensityAtTime = (double)time;
    return lightIntensityAtTime;
  }
}