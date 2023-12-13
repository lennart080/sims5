package SIMS5;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import SIMS5.calculator.Calculator;
import SIMS5.gui.GuiManager;
import SIMS5.gui.MyFrame;
import SIMS5.gui.MyPanelData;
import SIMS5.gui.MyPanelGraphs;
import SIMS5.gui.MyPanelRobotData;
import SIMS5.gui.MyPanelSimulation;
import SIMS5.simulation.SimManager;
import SIMS5.simulation.SimulationData;
public class Manager {                        //Manager zuständig für timings und updates
  private GuiManager guiManager;
  private SimManager simManager;
  private MyFrame screen;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private MyPanelRobotData robotDataPanel;
  private SimulationData simulationData;

  private Timer timer;

  //mit 20fps ist 1tag ~ 1:30min
  private int updates = 0;        //anzahl der updates seit start des programms //um programmSpeed pro sec 
  private int timeInMin = 0;           //simulations zeit in min (ingame) //10 updates
  private int programmSpeed = 1;   // berechnungen pro frame

  public Manager() {
    guiManager = new GuiManager();
    SwingUtilities.invokeLater(() -> {;                     //erstellen der graphic elemente
      simulationPanel = new MyPanelSimulation();
      dataPanel = new MyPanelData();
      graphPanel = new MyPanelGraphs();
      robotDataPanel = new MyPanelRobotData();
      screen = new MyFrame(this, guiManager, simulationPanel, dataPanel, graphPanel, robotDataPanel);
    });
    do {
      System.out.println(""+guiReady());
    } while (!guiReady());
    guiManager.setGraphicObjekts(screen, simulationPanel, dataPanel, graphPanel, robotDataPanel);

    //wait until sim starts for user imput
    simulationData = new SimulationData(guiManager.getSeed());
    Calculator.setSeed(guiManager.getSeed());
    simManager = new SimManager(this, guiManager, simulationData);
    guiManager.setSimManager(simManager);

    /*ActionListener taskPerformerGui = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        for (int i = 0; i < programmSpeed; i++) {
          updates++;
          System.out.println(""+ready());
          simManager.simulateData(timeInMin); 
          if (updates == (int)((double)updates/10.0)*10) {
            timeInMin++;       
          }
        }
        guiManager.updateGui(updates, timeInMin);
      }
    };
    timer = new Timer(1000/guiManager.getSollFps(), taskPerformerGui);
    timer.start();
    */

    while (true) {
      long startTime, endTime, elapsedTime;
      startTime = System.nanoTime();

      for (int i = 0; i < programmSpeed; i++) {
        updates++;
        simManager.simulateData(timeInMin); 
        if (updates == (int)((double)updates/10.0)*10) {
          timeInMin++;       
        }
      }
      guiManager.updateGui(updates, timeInMin);

      endTime = System.nanoTime();
      elapsedTime = (endTime - startTime) / 1000;
      long remainingTime = (1000/guiManager.getSollFps()) - elapsedTime;
      if (remainingTime > 0) {
        try {
          Thread.sleep(remainingTime /* / 1000, (int) (remainingTime % 1000) */);       
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        guiManager.setSollFps((int)(guiManager.getSollFps() - 1));
      }
    }
  }  

  public boolean simReady() {
    if (simulationData != null && simManager != null) {
      return true;
    }
    return false;
  }

  public boolean guiReady() {
    if (screen != null && simulationPanel != null && dataPanel != null && graphPanel != null && robotDataPanel != null && guiManager != null) {
      return true;
    }
    return false;
  }

  public int getTimeInMin() {
    return timeInMin;
  }

  public int getUpdates() {
    return updates;
  }

  public int getProgrammSpeed() {
    return programmSpeed;
  }
}