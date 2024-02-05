package SIMS5.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class MyPanelInput extends JPanel {
  private MyFrame screen;
  private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  private int screenWidth = (int)screenSize.getWidth();
  private int screenHeight = (int)screenSize.getHeight();
  private GuiManager guiManager;
  private int[] lastClick = new int[2];

  public MyPanelInput(GuiManager pGuiManager) {
    guiManager = pGuiManager;
    this.setBackground(Color.WHITE);
    setUpCompponents();
    repaint(); 
  }

  private void setUpCompponents() {
    
  }

  public class MyMouseListener extends MouseAdapter{
    @Override
    public void mouseClicked(MouseEvent e) {
        lastClick[0] = e.getX();
        lastClick[1] = e.getY();
        repaint();
    }
  }

  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der daten)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.BLACK);
    g2D.drawString("Input-Panel", 20, 40);
    if (lastClick[0] != 0 && lastClick[1] != 0) {
      g2D.drawOval(lastClick[0]-3, lastClick[1]-3, 6, 6);
    }
  }

  public void sendData() {  

  }

  public void setFrame(MyFrame pScreen) {
    screen = pScreen;
  }
}