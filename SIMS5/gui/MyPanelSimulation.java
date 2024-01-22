package SIMS5.gui;
import SIMS5.calculator.Calculator;
import SIMS5.simulation.MyRobot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

public class MyPanelSimulation extends JPanel {         //graphic classe der simulation
  private long realTimeSinceStart;
  private long start;
  private int simulationSize;
  private int robotsPerRound;
  
  private int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
  private int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
  private List<MyRobot> robots = new ArrayList<>();

  private BufferedImage bufImgEntity;

  public MyPanelSimulation() {
    start = System.currentTimeMillis();
    this.setBackground(Color.RED);
    
    try {
      bufImgEntity = ImageIO.read(new File(System.getProperty("user.dir") + "/SIMS5/gui/Grafik/Entity2.jpg"));
    } catch (Exception e) {
      System.out.println(e);
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
        g2D.drawRect(x-20, y-20, 40, 40);
        g2D.drawImage(bufImgEntity, x-20, y-20, null);
        } catch (Exception e){
          System.out.println(e);
        }
      }
    }
    for (int i = 0; i < (int)Math.sqrt(robotsPerRound); i++) {
      g2D.drawLine(0, i*(int)((double)simulationSize/Math.sqrt(robotsPerRound)), this.getWidth(), i*(int)((double)simulationSize/Math.sqrt(robotsPerRound)));
    }
    g2D.dispose();
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