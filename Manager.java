import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import panels.MyPanelData;
import panels.MyPanelGraphs;
import panels.MyPanelSimulation;
public class Manager {                        //Manager zuständig für timings und updates
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private SimulationData simulationData;

  private Robot[] robots = new Robot[100];

  private Timer simualtionTimer;
  private Timer realTimer;
  private int updates = 0;        //anzahl der updates seit start des programms  
  private int time = 0;           //simulations zeit in sec
  private int programmSpeed = 100;   //um ... schneller als echtzeit (0-100)
  private int fps;
  private int sollFps = 24;
  private int fpsCounter = 0;
  private long timeSave = System.currentTimeMillis()/1000;

  public Manager() {
    SwingUtilities.invokeLater(() -> {;                     //erstellen der graphic elemente
      simulationPanel = new MyPanelSimulation();
      dataPanel = new MyPanelData();
      graphPanel = new MyPanelGraphs();
      screen = new MyFrame(this, simulationPanel, dataPanel, graphPanel);
    });
    
    simulationData = new SimulationData();
    startSimulation();
    
    simualtionTimer = new Timer(100/programmSpeed, new ActionListener() {                    //timer welcher jede ... milisecunden daten und screen aufruft
      @Override
      public void actionPerformed(ActionEvent e) {
        updates++;
        if (updates == (((int)((double)updates/10.0))*10)) {
          time++;
        }
        simulateData();                    
      }
    });
    //simualtionTimer.start();

    realTimer = new Timer(50, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fpsUpdate();
        updateGraphicData();
        updateScreen();
      }
    });
    realTimer.start();
  }  
  
  public void myTimerStop() {
    simualtionTimer.stop();
    realTimer.stop();
  }
  
  private void fpsUpdate() {                                              //calkuliren der angezegten bilder pro secunde
    if ((timeSave+1) <= (System.currentTimeMillis()/1000)) {
      timeSave = System.currentTimeMillis()/1000;
      fps = fpsCounter;
      fpsCounter = 0;
    }
    fpsCounter++;
  }

  public void startSimulation() {       
    for (int i = 0; i < robots.length; i++) {
      int[] help = {(int)(Math.random()*100.0)*15, (int)(Math.random()*100.0)*10};   //no end produkt 
      robots[i] = new Robot(null, help);
    }
  }
  
  public void simulateData() {         //methode für die simulations berechnungen

  }

  public void loadLight() {                        //test methode
    for (int i = 1; i < 86400; i+=80) {
       graphPanel.myUpdate((int)simulationData.getLightIntensityAtTime(i), i/80);
    }
  }
  
  private void updateGraphicData() {               //methode für die daten updates die graphic panele
    simulationPanel.myUpdate(updates, time);  
    dataPanel.myUpdate(fps);

    //test
    int[][] robo = new int[robots.length][2];
    for (int i = 0; i < robo.length; i++) {
      robo[i] = robots[i].getPosition();
    }
    simulationPanel.robotest(robo); 
       
  }
  
  private void updateScreen() {           //methode für neuzeichnen des bildschirms
    simulationPanel.repaint();
    dataPanel.repaint();
    graphPanel.repaint();
  }
}