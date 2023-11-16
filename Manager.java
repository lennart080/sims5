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

  private Robot[] robots;

  private Timer simualtionTimer;
  private int updates = 0;        //anzahl der updates seit start des programms  
  private int time = 0;           //simulations zeit in sec
  private int programmSpeed = 100;   //um ... schneller als echtzeit (0-100)
  private int fps;
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
    
    simualtionTimer = new Timer((100/programmSpeed), new ActionListener() {                    //timer welcher jede ... milisecunden daten und screen aufruft
      @Override
      public void actionPerformed(ActionEvent e) {
        long ts = System.currentTimeMillis();
        updates++;
        if (updates == (((int)((double)updates/10.0))*10)) {
          time++;
        }
        fpsUpdate();
        simulateData();                    
        updateGraphicData();
        updateScreen();
        if (System.currentTimeMillis()-ts > 0) {
          simualtionTimer.setDelay((int)(100.0/(double)programmSpeed)-(int)(System.currentTimeMillis()-ts));     
        } 
        if (simualtionTimer.getDelay() == 0) {
          simualtionTimer.setDelay(1);
        }
      }
    });
    simualtionTimer.start();
  }  
  
  public void myTimerStop() {
    simualtionTimer.stop();
  }
  
  private void fpsUpdate() {                                              //calkuliren der angezegten bilder pro secunde
    if ((timeSave+1) <= (System.currentTimeMillis()/1000)) {
      timeSave = System.currentTimeMillis()/1000;
      fps = fpsCounter;
      fpsCounter = 0;
    } else {
      fpsCounter++;
    }
  }

  public void startSimulation() {        //not working 
    int[] help = new int[5];
    for (int i = 0; i < 30; i++) {
      robots[i] = new Robot(help, help);
    }
    int[] robo = new int[robots.length];
    for (int i = 0; i < robo.length; i++) {
      robo[i] = robots[i].getSerialNumber();
      System.out.println("pso");
    }
    simulationPanel.robotest(robo);
  }
  
  public void simulateData() {         //methode für die simulations berechnungen
    // ------testzwecke-----------
    for (int i = 1; i < (86400*8); i+=864) {
       graphPanel.myUpdate((int)simulationData.getLightIntensityAtTime(i), (int)((double)i*0.00115740740740740740740740740741));
    }
    //----------------------------
  }
  
  private void updateGraphicData() {               //methode für die daten updates die graphic panele
    simulationPanel.myUpdate(updates, time);  
    dataPanel.myUpdate(fps);
  }
  
  private void updateScreen() {           //methode für neuzeichnen des bildschirms
    simulationPanel.repaint();
    dataPanel.repaint();
    graphPanel.repaint();
  }
}