package SIMS5.gui;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyFrame extends JFrame {                 //graphic manager (zuständig für wo welche fenster/menüs angezeigt werden)
  private GuiManager guiManager;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private MyPanelEntityData entityDataPanel;
  private MyPanelInput inputPanel;
  private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private int screenWidth = (int)screenSize.getWidth();
  private int screenHeight = (int)screenSize.getHeight();
  private int guiMode;
  
  public MyFrame(GuiManager gm, MyPanelSimulation sp, MyPanelData dp, MyPanelGraphs gp, MyPanelEntityData edp, MyPanelInput ip) {
    simulationPanel = sp;
    dataPanel = dp;
    graphPanel = gp;
    guiManager = gm;
    entityDataPanel = edp;
    inputPanel = ip;
    inputPanel.setFrame(this);
    this.setTitle("SIMS5");
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setUndecorated(true);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    guiModes(5);
    this.setLayout(null);
    this.addKeyListener(new MyKeyListener());
    this.setVisible(true);
  }
  
  private void myRemove(JPanel panel) {                                  //sichere methode zum entfernen eines panels vom bildschirm
    if (this.getContentPane().isAncestorOf(panel)) {
      this.remove(panel);
    }
  }
  
  private void myAdd(JPanel panel) {                                       //sichere methode zum hinzufügen eines panels vom bildschirm
    if (!this.getContentPane().isAncestorOf(panel)) {
      this.add(panel);
    }
  }

  public int getScreenWidth() {
    return screenWidth;
  }

  public int getScreenHeight() {
    return screenHeight;
  }

  public int getSimulationSize() {
    if (screenHeight < 1000 || screenWidth < 1000) {
      if (screenHeight < screenWidth) {
        return screenHeight;
      } else {
        return screenWidth;
      }
    } else {
      return 1000;    
    }
  }
  
  public void guiModes(int mode) {                           //methode für verschidene gui zustände(auf und zu klappen der menüs und fenster)
    guiMode = mode;
    myRemove(simulationPanel);
    myRemove(dataPanel);
    myRemove(graphPanel);
    myRemove(entityDataPanel);
    myRemove(inputPanel);
    switch (mode) {
      case 1 :                      
        myAdd(dataPanel);  
        myAdd(entityDataPanel);      
        myAdd(simulationPanel);
        
        dataPanel.setBounds(0,0,600,screenHeight/2);
        entityDataPanel.setBounds(0, screenHeight/2, 600, screenHeight);
        simulationPanel.setBounds(600,0, screenWidth-600, screenHeight); 
        break;
      case 2 : 
        myAdd(simulationPanel);
        
        simulationPanel.setBounds(0,0,screenWidth, screenHeight);   
        break;
      case 3 : 
        myAdd(simulationPanel);
        myAdd(graphPanel);
        
        simulationPanel.setBounds(0, 200, screenWidth, screenHeight-200);
        graphPanel.setBounds(0, 0, screenWidth, 200);  
        break;
      case 4 :               
        myAdd(simulationPanel);
        myAdd(graphPanel);
        myAdd(dataPanel);
        myAdd(entityDataPanel);
                
        dataPanel.setBounds(0,200,300,(screenHeight/2)-200);
        entityDataPanel.setBounds(0, screenHeight/2, 300, screenHeight);
        graphPanel.setBounds(0, 0, screenWidth, 200);
        simulationPanel.setBounds(300, 200, screenWidth-300, screenHeight-200);  
        break;
      case 5 :
        myAdd(inputPanel);
        inputPanel.setBounds(0, 0, screenWidth, screenHeight);
        break;
      default:   
    }
    revalidate();
  }

  public void repaintScreen() {
    if (this.getContentPane().isAncestorOf(simulationPanel)) {
      simulationPanel.repaint();
    }
    if (this.getContentPane().isAncestorOf(dataPanel)) {
      dataPanel.repaint();
    } 
    if (this.getContentPane().isAncestorOf(graphPanel)) {
      graphPanel.repaint();
    }
    if (this.getContentPane().isAncestorOf(entityDataPanel)) {
      entityDataPanel.repaint();
    }
    if (this.getContentPane().isAncestorOf(inputPanel)) {
      inputPanel.repaint();
    }
  }
  
  private class MyKeyListener extends KeyAdapter {               //keyListener zur regestrirung der benutzer inputs per tastatur
    @Override
    public void keyPressed(KeyEvent e) {
      if (guiMode == 5) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
          guiModes(4);
          
        }
      } else {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_A  :                         //test taste
          guiModes(1);
          break;
        case KeyEvent.VK_S  :              //test taste
          guiModes(2);
          break;
        case KeyEvent.VK_W  :                   //test taste
          guiModes(3);
          break;
        case KeyEvent.VK_D  :                     //test taste
          guiModes(4);
          break;
        default: 
        }
      }
      /* 
      if(47 < e.getKeyCode() && e.getKeyCode() < 58) {
        int zahl = (int)(e.getKeyCode()-38);    
      }
      */
    }
  }
}