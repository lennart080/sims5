import javax.swing.*;
import java.awt.*;
public class MyPanelSimulation extends JPanel {         //graphic classe der simulation
  private int zahl;
  public MyPanelSimulation() {
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
  }
  
  public void myUpdate(int pz) {                 //aktualisiren der daten
    zahl = pz;
  }
}