import javax.swing.*;
import java.awt.*;
public class MyPanelGraphs extends JPanel {         //graphic classe der simulation
  public MyPanelGraphs() {
    this.setBackground(Color.GREEN);
    repaint(); 
  }
  
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der simulation)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.BLUE);
    g2D.drawString("Graph-Panel", 20, 40);
  }
  
  public void myUpdate() {                 //aktualisiren der daten
  
  }
}