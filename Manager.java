import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

  private Timer simulationTimer;
  private Timer guiTimer;

  private int round = 0;
  private int updates = 0;        //anzahl der updates seit start des programms  
  private int time = 0;           //simulations zeit in sec
  private int programmSpeed = 10;   //um ... schneller als echtzeit (0-100)
  private int simulationUpdatesPerSec = 10;
  private int fps;
  private int sollFps = 20;
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
    
  
    ActionListener taskPerformerSimulation = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        updates++;
        simulateData(); 
        if (updates == (int)((double)updates/10.0)*10) {
          time++;       
        } 
      }
    };
  simulationTimer = new Timer(1000/(simulationUpdatesPerSec*programmSpeed), taskPerformerSimulation);
  simulationTimer.start(); 
  
  /* 
  ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // Hier wird die Aufgabe definiert, die alle 100 Mikrosekunden ausgeführt wird
    Runnable task = () -> {
            // Deine Aufgabenlogik hier einfügen
    System.out.println("Aufgabe ausgeführt!");
  };

        // Zeitintervall in Mikrosekunden (hier 100 Mikrosekunden = 0.1 Millisekunden)
  long intervalMicroseconds = 100;

        // Umrechnung des Intervalls in Nanosekunden für die Planung des Tasks
  long intervalNanos = TimeUnit.MICROSECONDS.toNanos(intervalMicroseconds);

        // Starte die Aufgabe mit dem angegebenen Intervall
  executor.scheduleAtFixedRate(task, 0, intervalNanos, TimeUnit.NANOSECONDS);
  */

    ActionListener taskPerformerGui = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        fpsUpdate();
        updateGraphicData();
        updateScreen();
      }
    };
  guiTimer = new Timer(1000/sollFps, taskPerformerGui);
  guiTimer.start();
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
    if (round == 0) {
      int maxInt = 0;
      for (int i = 0; i < simulationData.getPermut().length; i++) {
        if (maxInt < simulationData.getPermut()[i]) {
          maxInt = simulationData.getPermut()[i];
        } 
      }
      for (int i = 0; i < robots.length; i++) {
        int posX = normaliseValue(simulationData.getPermut()[i*2], maxInt, screen.getScreenWidth());
        int posY = normaliseValue(simulationData.getPermut()[(i*2)+1], maxInt, screen.getScreenHeight());
        int[] pos = {posX, posY};
        robots[i] = new Robot(null, pos);
      }
    } else {
      
    }
    round++;       
  }

  public static int normaliseValue(int value, int oldMax, int newMax) {       //linear mapping
    int originalMax = oldMax;
    double originalRange = (double) (originalMax);
    double newRange = (double) (newMax);
    double scaledValue = ((double) (value) * newRange) / originalRange;

    // Ensure the scaled value fits within the new range
    return (int) Math.min(Math.max(scaledValue, 0), newMax);
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
    if (round != 0) {
      int[][] robo = new int[robots.length][2];
      for (int i = 0; i < robo.length; i++) {
        robo[i] = robots[i].getPosition();
      }
      simulationPanel.robotest(robo); 
    }
  }
  
  private void updateScreen() {           //methode für neuzeichnen des bildschirms
    simulationPanel.repaint();
    dataPanel.repaint();
    graphPanel.repaint();
  }
}