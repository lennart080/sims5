package SIMS5.gui;
import javax.swing.*;

import SIMS5.calculator.Calculator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
public class MyPanelGraphs extends JPanel {         //graphic classe der simulation
  private List<Double> loadedLight = new ArrayList<>();
  private BufferedImage graphImage;
  private BufferedImage bufferdGraphImage;
  private BufferedImage coordinateSystem;
  private double maximumLight;
  private int randGröße = 25;
  private int daysOnSlide = 3;
  private double updatesPerPixel;
  private int time;
  private int biWidth = 1800;
  private int biHeight;
  private int startPosGraph;
  private int startPosBufferdGraph;
  private double[] startLight;
  private boolean start = true;
  public MyPanelGraphs() {
    this.setBackground(Color.BLACK);
    repaint(); 
  }
  
  @Override
  public void paint(Graphics g) {               //output methode(anzeigen der simulation)
    super.paint(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setPaint(Color.GRAY);
    g2D.drawString("Graph-Panel", 20, 40);
    if (bufferdGraphImage != null && graphImage != null) {
      int graphImageXpos = (int)(startPosGraph+((-1)*((time % (graphImage.getWidth()*updatesPerPixel))/updatesPerPixel)));
      int bufferdGraphImageXpos = (int)(startPosBufferdGraph+((-1)*((time % (bufferdGraphImage.getWidth()*updatesPerPixel))/updatesPerPixel)));
      g2D.drawImage(coordinateSystem, startPosGraph, randGröße, null);
      g2D.drawImage(graphImage, graphImageXpos, randGröße, null);
      g2D.drawImage(bufferdGraphImage, bufferdGraphImageXpos, randGröße, null);   
    }
    g2D.dispose();
  }

  public void myUpdate(double[] pLoadedlight) {
    loadedLight.clear();
    for (int i = 0; i < pLoadedlight.length; i++) {
      loadedLight.add(pLoadedlight[i]);
    }
    updateBufferedImage();
    start = false;
  }

  public void start(double[] pStartLight) {
    startPosBufferdGraph = this.getWidth()-((this.getWidth()-biWidth)/2);
    startPosGraph = (this.getWidth()-biWidth)/2;
    biHeight = this.getHeight()-randGröße*2;
    coordinateSystem = new BufferedImage(biWidth, biHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D = coordinateSystem.createGraphics();
    g2D.setColor(Color.GRAY);
    g2D.drawLine((int)(coordinateSystem.getWidth()/2), 0, (int)(coordinateSystem.getWidth()/2), coordinateSystem.getHeight());
    g2D.drawLine(0 ,coordinateSystem.getHeight()-1,coordinateSystem.getWidth() ,coordinateSystem.getHeight()-1);
    g2D.dispose();
    startLight = pStartLight;
  }

  public void updateBufferedImage() {
    graphImage = null;
    graphImage = new BufferedImage(biWidth, biHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D = graphImage.createGraphics();
    g2D.drawImage(bufferdGraphImage, 0, 0, null);
    g2D.dispose();
    bufferdGraphImage = null;
    bufferdGraphImage = new BufferedImage(graphImage.getWidth(), graphImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D2 = bufferdGraphImage.createGraphics();
    g2D2.setColor(Color.WHITE);
    for (int i = 0; i < bufferdGraphImage.getWidth(); i++) {
      double light = loadedLight.get((loadedLight.size()/bufferdGraphImage.getWidth())*i);
      int y = Calculator.normaliseValue(light, (int)maximumLight, biHeight-1);
      y = bufferdGraphImage.getHeight()-y-1;
      g2D2.drawLine(i, y, i+1, y);
    }
    g2D2.dispose();
    if (start == true) {
      graphImage = null;
      graphImage = new BufferedImage(biWidth, biHeight, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2D3 = graphImage.createGraphics();
      g2D3.setColor(Color.WHITE);
      for (int i = 0; i < graphImage.getWidth(); i++) {
        double oneLight = startLight[(startLight.length/graphImage.getWidth())*i];
        int y = Calculator.normaliseValue(oneLight, (int)maximumLight, biHeight-1);
        y = graphImage.getHeight()-y-1;
        g2D3.drawLine(i, y, i+1, y);
      }
      g2D3.dispose();
    }
  }

  public void setTime(int pTime) {
    time = pTime;
  }

  public void updateGraphSize(int maxLight) {
    maximumLight = maxLight;
  } 

  public void updateMySize() {
    updatesPerPixel = 1/(biWidth/(3600.0*(double)daysOnSlide));   
  }

  public int getDaysOnSlide() {
    return daysOnSlide;
  }
}