package simulation.panels;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class MyPanelGraphs extends JPanel {         //graphic classe der simulation
  private List<Double> loadedLight = new ArrayList<>();
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
    for (int i = 0; i<loadedLight.size(); i++) {
      g2D.drawLine(1600-(i+10), (int)(100.0-loadedLight.get(i)), 1600-(i+10), (int)(100.0-loadedLight.get(i)));
    }
  }
  ;
  
  public void myUpdate(double light) {                 //aktualisiren der daten
    //----testdaten----
    loadedLight.add(light);
    if (loadedLight.size() >= 1560) {
      loadedLight.remove(0);
    }
    //-----------------                               
  }
}