package SIMS5.gui;
import javax.swing.*;

import SIMS5.calculator.Calculator;
import SIMS5.simulation.MyRobot;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
public class MyPanelSimulation extends JPanel {         //graphic classe der simulation
  private int updates;
  private int time;
  private int day;
  private long realTimeSinceStart;
  private long start;

  private int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
  private int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
  private List<MyRobot> robots = new ArrayList<>();
  public MyPanelSimulation() {
    start = System.currentTimeMillis();
    this.setBackground(Color.RED);
    repaint(); 
  }
  
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der simulation)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.BLUE);
    g2D.drawString("Simulation-Panel", 20, 40);
    g2D.drawString("Updates: " + updates, 20, 60);
    g2D.drawString("Time: " + time, 20, 80);
    g2D.drawString("Day: " + day, 20, 100);
    g2D.drawString("Real_Time_Since_Start: " + realTimeSinceStart, 20, 120);
    //-------test--------
    if (robots != null && robots.size() != 0) {
      for (int i = 0; i < robots.size(); i++) {
        try {
        int x = Calculator.normaliseValue(robots.get(i).getPositions()[robots.get(i).getPositions().length-1][0], screenWidth, this.getWidth());
        int y = Calculator.normaliseValue(robots.get(i).getPositions()[robots.get(i).getPositions().length-1][1], screenHeight, this.getHeight());
        g2D.drawString("ROBO_" + robots.get(i).getSerialNumber(), x, y);
        g2D.drawString("ENERGIE_" + robots.get(i).getStatistics()[0], x, y+20);
        g2D.drawString("SCHROTT_" + robots.get(i).getStatistics()[1], x, y+40);
        g2D.drawString("ATTAK_" + robots.get(i).getStatistics()[2], x, y+60);
        g2D.drawString("ENERGIE-SPEICHER_" + robots.get(i).getStatistics()[3], x, y+80);
        g2D.drawString("SPEED_" + robots.get(i).getStatistics()[4], x, y+100);
        g2D.drawString("DEFENCE_" + robots.get(i).getStatistics()[5], x, y+120);
        g2D.drawString("HEALTH_" + robots.get(i).getStatistics()[6], x, y+140);
        g2D.drawString("RUST_" + robots.get(i).getStatistics()[7], x, y+160);
        g2D.drawString("SOLAR_" + robots.get(i).getStatistics()[8], x, y+180);         
        } catch (Exception e) {
        }
      }
    }
    //-------------------
  }
  
  public void myUpdate(int pUpdates, int pTime, int pDay) {                 //aktualisiren der daten
    updates = pUpdates;
    time = pTime;
    day = pDay;
    realTimeSinceStart = (int)((System.currentTimeMillis()-start)/1000);
  }

  public void roboUpdate(List<MyRobot> pRobots) {               //test
    robots = pRobots;
  }
}