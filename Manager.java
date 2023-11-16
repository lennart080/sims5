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
  private Timer timer;
  private Timer timeTimer;

  
  private int updates = 0;        //anzahl der updates seit start des programms  
  private int time = 0;
  private int programmSpeed = 1;
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
    
    timer = new Timer((100/programmSpeed), new ActionListener() {                    //timer welcher jede ... milisecunden daten und screen aufruft
      @Override
      public void actionPerformed(ActionEvent e) {
        long ts = System.currentTimeMillis();
        updates++;
        fpsUpdate();
        simulateData();                       // zu test zwenken aus 
        updateGraphicData();
        updateScreen();
        if (System.currentTimeMillis()-ts != 0) {
          timer.setDelay((100/programmSpeed)-(int)(System.currentTimeMillis()-ts)+6);     
        } 
      }
    });
    timer.start();

    timeTimer = new Timer(1000/programmSpeed, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        long ts = System.currentTimeMillis();
        time++;
        if (System.currentTimeMillis()-ts != 0) {
          timer.setDelay((1000/programmSpeed)-(int)(System.currentTimeMillis()-ts));     
        } 
      }
    });
    timeTimer.start();
  }  
  
  public void myTimerStop() {
    timer.stop();
    timeTimer.stop();
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
  
  public void simulateData() {         //methode für die simulations berechnungen
    // ------testzwecke-----------
    for (int i = 1; i < (86400*8); i++) {
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