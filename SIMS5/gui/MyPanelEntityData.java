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
      /*g2D.drawString("" + robot.getSerialNumber(), 20, 60);
      g2D.drawString("" + robot.getPositions()[0][0] + "  " + robot.getPositions()[0][1], 20, 80);
      g2D.drawString("Loss: " + robot.getEnergieLoss(), 20, 100);
      g2D.drawString("Plus: " + robot.getEnergiePlus(), 100, 100);
      for (int i = 0; i < robot.getCalcTime().length; i++) {
        g2D.drawString("CT: " + robot.getCalcTime()[i], 20, (i*18)+120);
      }*/
      for (int i = 0; i < robot.getNeurons().size(); i++) {
        for (int j = 0; j < robot.getNeurons().get(i).size(); j++) {
          g2D.drawString(""+robot.getNeurons().get(i).get(j), (i*100)+20, (j*40)+50);
        }
      }
      for (int i = 0; i < robot.getWeights().size(); i++) {
        for (int j = 0; j < robot.getWeights().get(i).size(); j++) {
          for (int j2 = 0; j2 < robot.getWeights().get(i).get(j).size(); j2++) {
            int x1 =(i*100)+20;
            int y1 =(j*40)+50;
            int x2 =(int)(robot.getWeights().get(i).get(j).get(j2)[0]*100)+20;
            int y2 =(int)(robot.getWeights().get(i).get(j).get(j2)[1]*40)+50;
            g2D.setPaint(Color.PINK);
            g2D.drawLine(x1, y1, x2, y2);
            g2D.setPaint(Color.GREEN);
            g2D.drawString(""+roundToDecPlaces(robot.getWeights().get(i).get(j).get(j2)[2], 3), x1+((x2-x1)/2), y1+((y2-y1)/2));
          }
        }
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
