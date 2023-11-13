import javax.swing.*;
import java.awt.*;
public class MyPanelGraphs extends JPanel {         //graphic classe der simulation
  private int h;
  private double[] graph = new double[20000];
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

    //zur test veranschaulichung der graphen funktion (nicht final!!!)
    for (int i = 0; i<h; i++) {
      g2D.drawLine((i+50)/10, (int)graph[i]+100, (i+50)/10, (int)graph[i]+100);
    }
  }
  
  public void myUpdate(int x, int g) {                 //aktualisiren der daten
    graph[g] = x;                           //falsche daten schnitstelle (nur testweiße)
    h = g;                                    //...
  }
}