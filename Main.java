import SIMS5.gui.GuiManager;
import SIMS5.simulation.SimManager;
public class Main {
  public static void main(String[] args) {
    SimManager simM = new SimManager();
    GuiManager guiM = new GuiManager();
    guiM.setSimManager(simM);
    simM.setGuiManager(guiM);
  }
}