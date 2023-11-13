import javax.swing.*;
import javax.swing.SwingUtilities;
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
    for (int i = 0; i < 500; i++) {
      System.out.println("i: " + i);                                                   //nicht die vertige funtion(testinhalt)
      System.out.println("sim: " + simulationData.getLightIntensityAtTime(i));
      graphPanel.myUpdate((int)simulationData.getLightIntensityAtTime(i), i);
    }
    //simulationData.
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