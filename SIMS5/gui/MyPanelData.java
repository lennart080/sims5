package SIMS5.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
public class MyPanelData extends JPanel {          //graphic classe der daten
  private int fps;
  private int robotsPerRound;
  private int sollRobotsPerRound;
  private int updates;
  private int time;
  private int day;
  private int round;
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
    g2D.drawString("FPS: " + fps, 20, 60);
    g2D.drawString("Robots-Alive: " + robotsPerRound, 20, 80);
    g2D.drawString("Robots-Per-Round: " + sollRobotsPerRound, 20, 100);
    g2D.drawString("Updates: " + updates, 20, 120);
    g2D.drawString("Time: " + time, 20, 140);
    g2D.drawString("Day: " + day, 20, 160);
    g2D.drawString("Round: " + round, 20, 180);
  }
  
  public void myUpdate(int pFps, int pRobotsPerRound, int pSollRobotsPerRound, int pUpdates, int pTime, int pDay, int pRound) {                 //aktualisiren der daten
    fps = pFps;
    robotsPerRound = pRobotsPerRound;
    sollRobotsPerRound = pSollRobotsPerRound;
    updates = pUpdates;
    time = pTime;
    day = pDay;
    round = pRound;
  }
}