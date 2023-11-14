import javax.swing.*;
import java.lang.Math;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Manager {                        //Manager zuständig für timings und updates
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private Timer timer;
  private SimulationData simulationData;
  
  private int updates = 0;        //anzahl der updates seit start des programms  
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
    
    timer = new Timer(10, new ActionListener() {                    //timer welcher jede ... milisecunden daten und screen aufruft
      @Override
      public void actionPerformed(ActionEvent e) {
        updates++;
        fpsUpdate();
        //simulateData();                        zu test zwenken aus 
        updateGraphicData();
        updateScreen();
      }
    });
    timer.start();
  }  
  
  public void myTimerStop() {
    timer.stop();
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
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 100; j++) {
        if (((double)j/100.0) < (7.0/24.0) || ((double)j/100.0) > (20.0/24.0)) {
          System.out.println("flat");
          graphPanel.myUpdate(0, j+(i*100));
        } else {
          double w = simulationData.getNoiseStrength();
          double fx = Math.cos(((double)j/100.0)*Math.PI*(24.0/13.0));
          double gx = simulationData.getLightIntensityAtTime((((double)j/100.0)+i)*10);
          System.out.println("sin: " + fx + " j: " + j/100.0);
          graphPanel.myUpdate((int)((gx*w)+(fx*(1-w))*10), j+(i*100));
        }
      }
    }
    //----------------------------
  }
  
  private void updateGraphicData() {               //methode für die daten updates die graphic panele
    simulationPanel.myUpdate(updates);  
    dataPanel.myUpdate(fps);
  }
  
  private void updateScreen() {           //methode für neuzeichnen des bildschirms
    simulationPanel.repaint();
    dataPanel.repaint();
    graphPanel.repaint();
  }
}