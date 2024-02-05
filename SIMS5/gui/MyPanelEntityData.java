package SIMS5.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import SIMS5.simulation.MyEntity;
public class MyPanelEntityData extends JPanel {          //graphic classe der daten
  private MyEntity robot;
  public MyPanelEntityData() {
    this.setBackground(Color.BLACK);
    repaint(); 
  }
  
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.RED);
    g2D.drawString("EntityData-Panel", 20, 40);
    /*if (robot != null) {
      g2D.drawString("" + robot.getSerialNumber(), 120, 40);
      g2D.drawString("" + robot.getPositions()[0][0] + "  " + robot.getPositions()[0][1], 20, 80);
      g2D.drawString("Loss: " + robot.getEnergieLoss(), 20, 100);
      g2D.drawString("Plus: " + robot.getEnergiePlus(), 100, 100);
      for (int i = 0; i < robot.getCalcTime().length; i++) {
        g2D.drawString("CT: " + robot.getCalcTime()[i], 20, (i*18)+120);
      }
      for (int i = 0; i < robot.getNeurons().length; i++) {
        for (int j = 0; j < robot.getNeurons()[i].length; j++) {
          g2D.drawString("" + robot.getNeurons()[i][j], (i*50)+20, (j*18)+210);
        }
      }
    }*/
    if (robot != null) {
      g2D.drawString("" + robot.getSerialNumber(), 120, 40);
      for (int i = 0; i < robot.getNeurons().length; i++) {
        for (int j = 0; j < robot.getNeurons()[i].length; j++) {
          g2D.setColor(Color.RED);
          g2D.drawString(""+robot.getNeurons()[i][j][0], (i*100)+20, (j*40)+50);
          g2D.setColor(Color.CYAN);
          g2D.drawString(""+robot.getNeurons()[i][j][1], (i*100)+20, (j*40)+63);
        }
      }
      for (int i = 0; i < robot.getWeights().size(); i++) {
        int nId1 = (int)robot.getWeights().get(i)[0];
        int nId2 = (int)robot.getWeights().get(i)[1];
        double weightValue = robot.getWeights().get(i)[2];
        int nPosX1 = 5;
        int nPosY1 = 0;
        int nPosX2 = 6;
        int nPosY2 = 0;
        for (int j = 0; j < robot.getNeurons().length; j++) {
          for (int j2 = 0; j2 < robot.getNeurons()[j].length; j2++) {
            if (robot.getNeurons()[j][j2][2] == nId1) {
              nPosX1 = j;
              nPosY1 = j2;
            }
            if (robot.getNeurons()[j][j2][2] == nId2) {
              nPosX2 = j;
              nPosY2 = j2;
            }
          }
        }
        nPosX1 = (nPosX1*100)+20;
        nPosX2 = (nPosX2*100)+20;
        nPosY1 = (nPosY1*40)+50;
        nPosY2 = (nPosY2*40)+50;
        g2D.setColor(Color.MAGENTA);
        g2D.drawLine(nPosX1, nPosY1, nPosX2, nPosY2);
        g2D.setColor(Color.GREEN);
        g2D.drawString("" + roundToDecPlaces(weightValue, 3), nPosX1+((nPosX2-nPosX1)/2), nPosY1+((nPosY2-nPosY1)/2));
      }
    }
  }

  public double roundToDecPlaces(double value, int decPlaces) {
    return Math.round(value * (Math.pow(10, decPlaces))) / (Math.pow(10, decPlaces));
  }
  
  public void myUpdate(MyEntity robot2) {                 //aktualisiren der daten
    robot = robot2;
  }
}
