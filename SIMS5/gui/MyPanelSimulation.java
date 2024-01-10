package SIMS5.gui;
import javax.imageio.ImageIO;
import javax.swing.*;

import SIMS5.calculator.Calculator;
import SIMS5.simulation.MyRobot;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class MyPanelSimulation extends JPanel {         //graphic classe der simulation
  private long realTimeSinceStart;
  private long start;
  private int simulationSize;
  private int robotsPerRound;
  
  private int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
  private int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
  private BufferedImage bufImgRoboter = new BufferedImage(screenWidth,screenHeight,BufferedImage.TYPE_INT_RGB);
  private List<MyRobot> robots = new ArrayList<>();
  public MyPanelSimulation() {
    start = System.currentTimeMillis();
    this.setBackground(Color.RED);

    try {
    bufImgRoboter = ImageIO.read(new File("Grafik/Roboter_V1.png"));
    }
    catch (IOException ex) {
    ex.printStackTrace();
    }

    repaint();
    
    
  }
  
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der simulation)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setColor(Color.BLACK);
    g2D.drawString("Simulation-Panel", 20, 40);
    g2D.drawString("Real_Time_Since_Start: " + realTimeSinceStart, 20, 60);
    //-------test--------
    realTimeSinceStart = (int)((System.currentTimeMillis()-start)/1000);
    if (robots != null && robots.size() != 0) {
      for (int i = 0; i < robots.size(); i++) {
        try {
        int x = Calculator.normaliseValue(robots.get(i).getPositions()[robots.get(i).getPositions().length-1][0], screenWidth, this.getWidth());
        int y = Calculator.normaliseValue(robots.get(i).getPositions()[robots.get(i).getPositions().length-1][1], screenHeight, this.getHeight());
        g2D.setColor(Color.BLUE);
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
        g2D.setColor(Color.GREEN); 
        g2D.drawRect(x-20, y-20, 40, 40);
        //g2D.drawImage(bufImgRoboter, x-20, x-20, null);
        } catch (Exception e) {
        }
      }
    }
    g2D.setColor(Color.CYAN);
    if (simulationSize != 0) {
      g2D.drawRect(0, 0, Calculator.normaliseValue(simulationSize, screenWidth, this.getWidth()), Calculator.normaliseValue(simulationSize, screenHeight, this.getHeight()));
    }
    g2D.setColor(Color.BLACK);
    for (int i = 0; i < (int)Math.sqrt(robotsPerRound); i++) {
      g2D.drawLine(i*(int)((double)simulationSize/Math.sqrt(robotsPerRound)), 0, i*(int)((double)simulationSize/Math.sqrt(robotsPerRound)), this.getHeight());
    }
    for (int i = 0; i < (int)Math.sqrt(robotsPerRound); i++) {
      g2D.drawLine(0, i*(int)((double)simulationSize/Math.sqrt(robotsPerRound)), this.getWidth(), i*(int)((double)simulationSize/Math.sqrt(robotsPerRound)));
    }
    //-------------------
  }
  
  public void myUpdate(List<MyRobot> pRobots) {                 //aktualisiren der daten
    robots = pRobots;    
  }

  public void setSimulationSize(int simSize) {
    simulationSize = simSize;
  }

  public void setRobotsPerRound(int pRobotsPerRound) {
    robotsPerRound = pRobotsPerRound;
  }
}