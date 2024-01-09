package SIMS5.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

import javax.swing.JPanel;
public class MyPanelInput extends JPanel {

  public MyPanelInput() {
    this.setBackground(Color.MAGENTA);
    /*File pathtoFile = new File("Grafik/black-screen.png"); //HAB AUFGEBEN BRAUCHE HILFE
    try {                                                    //HAB AUFGEBEN BRAUCHE HILFE
      Image backround = ImageIO.read(pathtoFile);            //HAB AUFGEBEN BRAUCHE HILFE
    } catch (IOException e) {                                //HAB AUFGEBEN BRAUCHE HILFE
      e.printStackTrace();
    }*/
    repaint(); 
  }

  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.WHITE);
    g2D.drawString("Input-Panel", 20, 40);
   // g2D.drawImage(backround, 0, 0, null);
  }

  public void myUpdate() {                 //aktualisiren der daten
    
  }
}