package SIMS5.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
public class MyPanelData extends JPanel {          //graphic classe der daten
  // start attributes
  private int fps;
  private int entitysPerRound;
  private int sollEntitysPerRound;
  private int updates;
  private int time;
  private int day;
  private int round;
  private int longestEntity;
  // end attributes
  public MyPanelData() {
    this.setBackground(Color.BLUE);
    repaint(); 
  }
  
  // start methods
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.RED);
    g2D.drawString("Data-Panel", 20, 40);
    g2D.drawString("FPS: " + fps, 20, 60);
    g2D.drawString("Robots-Alive: " + entitysPerRound, 20, 80);
    g2D.drawString("Robots-Per-Round: " + sollEntitysPerRound, 20, 100);
    g2D.drawString("Updates: " + updates, 20, 120);
    g2D.drawString("Time: " + time, 20, 140);
    g2D.drawString("Day: " + day, 20, 160);
    g2D.drawString("Round: " + round, 20, 180);
    g2D.drawString("Longest-Robot: " + longestEntity, 20, 200);
  }
  
  public void myUpdate(int pFps, int pEntityPerRound, int pSollEntitysPerRound, int pUpdates, int pTime, int pDay, int pRound, int pLongestEntity) {                 //aktualisiren der daten
    fps = pFps;
    entitysPerRound = pEntityPerRound;
    sollEntitysPerRound = pSollEntitysPerRound;
    updates = pUpdates;
    time = pTime;
    day = pDay;
    round = pRound;
    longestEntity = pLongestEntity;
  }
  // end methods
}