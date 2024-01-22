package SIMS5.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class MyPanelInput extends JPanel {

  private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private int screenWidth = (int)screenSize.getWidth();
  private int screenHeight = (int)screenSize.getHeight();

  public MyPanelInput() {
    //this.addMouseListener((MouseListener) this);
    repaint(); 
  }

  /* 
  private class MyMouseAdapter extends MouseAdapter{
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse " + e.getClickCount() + " times clicked at "
                + e.getPoint());
    }
  }*/

  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.WHITE);
    g2D.drawString("Input-Panel", 20, 40);
  }

  public void sendData() {  

  }
}