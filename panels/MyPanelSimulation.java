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
  private double[][] roboPos;
  private int[] energie;
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
    if (roboPos != null) {
      for (int i = 0; i < roboPos.length; i++) {
        if (energie != null) {
          int x = MyPanel.normaliseValue(roboPos[i][0], screenWidth, this.getWidth());
          int y = MyPanel.normaliseValue(roboPos[i][1], screenHeight, this.getHeight());
          g2D.drawString("" + energie[i], x, y);          
        } else {
          int x = MyPanel.normaliseValue(roboPos[i][0], screenWidth, this.getWidth());
          int y = MyPanel.normaliseValue(roboPos[i][1], screenHeight, this.getHeight());
          g2D.drawString("" + i, x, y);
        }
      }
    }
    //-------------------
  }
  
  public void myUpdate(int pz, int pg) {                 //aktualisiren der daten
    zahl = pz;
    zahl2 = pg;
    zahl3 = (int)((System.currentTimeMillis()-start)/1000);
  }

  public void robotest(double[][] pos, int[] pEnergie) {               //test
    roboPos = pos;
    energie = pEnergie;
  }
}