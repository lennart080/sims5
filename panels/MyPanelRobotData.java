package panels;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
public class MyPanelRobotData extends JPanel {          //graphic classe der daten
  private int serialnumber;
  private int[] position;
  public MyPanelRobotData() {
    this.setBackground(Color.BLACK);
    repaint(); 
  }
  
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.RED);
    g2D.drawString("RobotData-Panel", 20, 40);
    g2D.drawString("" + serialnumber, 20, 60);
  }
  
  public void myUpdate(int number, int[] pos) {                 //aktualisiren der daten
    serialnumber = number;
    position = pos;
  }
}
