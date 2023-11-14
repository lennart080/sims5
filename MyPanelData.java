import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
public class MyPanelData extends JPanel {          //graphic classe der daten
  private int fps;
  public MyPanelData() {
    this.setBackground(Color.BLUE);
    repaint(); 
  }
  
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.RED);
    g2D.drawString("Data-Panel", 20, 40);
    g2D.drawString("" + fps, 20, 60);
  }
  
  public void myUpdate(int pFps) {                 //aktualisiren der daten
    fps = pFps;
  }
}