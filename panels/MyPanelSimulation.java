package panels;
import javax.swing.*;
import java.awt.*;
public class MyPanelSimulation extends JPanel {         //graphic classe der simulation
  private int zahl;
  private int zahl2;
  private int zahl3;
  private long start;

  private int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
  private int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
  private int[] roboNumber;
  private int[][] roboPos;
  private double[][] roboStats;
  public MyPanelSimulation() {
    start = System.currentTimeMillis();
    this.setBackground(Color.RED);
    repaint(); 
  }
  
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der simulation)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.BLUE);
    g2D.drawString("Simulation-Panel", 20, 40);
    g2D.drawString("" + zahl, 20, 60);
    g2D.drawString("" + zahl2, 20, 80);
    g2D.drawString("" + zahl3, 20, 100);
    //-------test--------
    if (roboNumber != null) {
      for (int i = 0; i < roboNumber.length; i++) {
        int x = MyPanel.normaliseValue(roboPos[i][0], screenWidth, this.getWidth());
        int y = MyPanel.normaliseValue(roboPos[i][1], screenHeight, this.getHeight());
        g2D.drawString("ROBO_" + roboNumber[i], x, y);
        g2D.drawString("ENERGIE_" + roboStats[i][0], x, y+20);
        g2D.drawString("SCHROTT_" + roboStats[i][1], x, y+40);
        g2D.drawString("ATTAK_" + roboStats[i][2], x, y+60);
        g2D.drawString("ENERGIE-SPEICHER_" + roboStats[i][3], x, y+80);
        g2D.drawString("SPEED_" + roboStats[i][4], x, y+100);
        g2D.drawString("DEFENCE_" + roboStats[i][5], x, y+120);
        g2D.drawString("HEALTH_" + roboStats[i][6], x, y+140);
        g2D.drawString("RUST_" + roboStats[i][7], x, y+160);
        g2D.drawString("SOLAR_" + roboStats[i][8], x, y+180);
      }
    }
    //-------------------
  }
  
  public void myUpdate(int pz, int pg) {                 //aktualisiren der daten
    zahl = pz;
    zahl2 = pg;
    zahl3 = (int)((System.currentTimeMillis()-start)/1000);
  }

  public void roboUpdate(int[] pNumber, int[][] pPosition, double[][] pStatistics) {               //test
    roboNumber = pNumber;
    roboPos = pPosition;
    roboStats = pStatistics;
  }
}