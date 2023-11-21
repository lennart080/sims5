package panels;
import javax.swing.*;
import java.awt.*;
public class MyPanelSimulation extends JPanel {         //graphic classe der simulation
  private int zahl;
  private int zahl2;
  private int zahl3;
  private long start;
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
          g2D.drawString("" + energie[i],(int)roboPos[i][0], (int)roboPos[i][1]);          
        } else {
          g2D.drawString("" + i,(int)roboPos[i][0], (int)roboPos[i][1]);
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