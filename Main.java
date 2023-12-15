import SIMS5.gui.GuiManager;
import SIMS5.simulation.SimManager;
public class Main {
  public static void main(String[] args) {
    GuiManager guiM = new GuiManager();
    SimManager simM = new SimManager();
    guiM.setSimManager(simM);
    simM.setGuiManager(guiM);
  }
}