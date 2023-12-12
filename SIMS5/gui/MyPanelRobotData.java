package SIMS5.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import SIMS5.simulation.MyRobot;
public class MyPanelRobotData extends JPanel {          //graphic classe der daten
  private MyRobot robot;
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
    if (robot != null) {
      g2D.drawString("" + robot.getSerialNumber(), 20, 60);
      g2D.drawString("" + robot.getPositions()[0][0] + "  " + robot.getPositions()[0][1], 20, 80);
      for (int i = 0; i < robot.getNeurons().length; i++) {
        for (int j = 0; j < robot.getNeurons()[i].length; j++) {
          g2D.drawString("" + robot.getNeurons()[i][j], (i*50)+20, (j*20)+120);
        }
      }
    }
  }
  
  public void myUpdate(MyRobot robot2) {                 //aktualisiren der daten
    robot = robot2;
  }
}
