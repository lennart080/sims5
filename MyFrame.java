import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
public class MyFrame extends JFrame {                 //graphic manager (zuständig für wo welche fenster/menüs angezeigt werden)
  private Manager manager;
  private MyPanelSimulation simulationPanel;
  private MyPanelData dataPanel;
  private MyPanelGraphs graphPanel;
  private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private int screenWidth = (int)screenSize.getWidth();
  private int screenHeight = (int)screenSize.getHeight();
  
  public MyFrame(Manager mp, MyPanelSimulation sp, MyPanelData dp, MyPanelGraphs gp) {            //zuweißung der graphic componenten(fenster/menüs usw.)
    simulationPanel = sp;
    dataPanel = dp;
    graphPanel = gp;
    manager = mp;
    
    this.setTitle("Info-Projekt");
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setUndecorated(true);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GuiModes(1);
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
  
  private void GuiModes(int mode) {                           //methode für verschidene gui zustände(auf und zu klappen der menüs und fenster)
    myRemove(simulationPanel);
    myRemove(dataPanel);
    myRemove(graphPanel);
    switch (mode) {
      case 1 :                      
        myAdd(dataPanel);        
        myAdd(simulationPanel);
        
        dataPanel.setBounds(0,0,300,screenHeight);
        simulationPanel.setBounds(300,0, screenWidth-300, screenHeight);                
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
                
        dataPanel.setBounds(0,0,300,screenHeight);
        graphPanel.setBounds(300, 0, screenWidth-300, 200);
        simulationPanel.setBounds(300, 200, screenWidth-300, screenHeight-200);       
        break;
      default:   
    }
    revalidate();
  }
  
  private class MyKeyListener extends KeyAdapter {               //keyListener zur regestrirung der benutzer inputs per tastatur
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_A  : 
          GuiModes(1);
          break;
        case KeyEvent.VK_S  : 
          GuiModes(2);
          break;
        case KeyEvent.VK_W  : 
          GuiModes(3);
          break;
        case KeyEvent.VK_D  : 
          GuiModes(4);
          break;
        case KeyEvent.VK_H  : 
          manager.myTimerStop();
          break;
        default: 
      }
      if(47 < e.getKeyCode() && e.getKeyCode() < 58) {
        int zahl = (int)(e.getKeyCode()-38);    
      }
    }
  }
}