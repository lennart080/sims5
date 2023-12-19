package SIMS5.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
public class MyPanelInput extends JPanel {

  public MyPanelInput() {
    this.setBackground(Color.MAGENTA);
    repaint(); 
  }

  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.WHITE);
    g2D.drawString("Input-Panel", 20, 40);
  }

  public void myUpdate() {                 //aktualisiren der daten
    
  }
}