package SIMS5.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

import javax.swing.JPanel;
public class MyPanelInput extends JPanel {

  private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private int screenWidth = (int)screenSize.getWidth();
  private int screenHeight = (int)screenSize.getHeight();
  private BufferedImage bufImg = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_RGB);

  public MyPanelInput() {
   // this.setBackground(Color.MAGENTA);
   try {
    bufImg = ImageIO.read(new File("Grafik/blackscreen.png"));
    }
    catch (IOException ex) {
    ex.printStackTrace();
    }
   
    repaint(); 
  }

  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.WHITE);
    g2D.drawString("Input-Panel", 20, 40);
    g2D.drawImage(bufImg, 0, 0, null);
  }

  public void myUpdate() {                 //aktualisiren der daten
    
  }
}