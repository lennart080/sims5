package SIMS5.gui;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Screen.SimulationScreen;
import SIMS5.gui.Screen.StartScreen;
import SIMS5.sim.Gui.Schnittstelle;
import SIMS5.sim.Manager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GuiManager extends Application {

    // Objekte:
    private Manager simManager = new Manager(this);
    private Profile profile;
    private SimulationScreen simScreen;

    public void setSimScreen(SimulationScreen simScreen){
        this.simScreen = simScreen;
    }

    @Override
    public void start(Stage stage) {

        //Start Gui

        Pane emtiyPane = new Pane();
        Scene scene = new Scene(emtiyPane,0,0);
        stage.setScene(scene);
        new StartScreen(stage,this);
    }

    

    public void createProfile(String profilname){
        Profile profile = new Profile(profilname);
        this.profile = profile;
    }                             

    public Profile getProfile() {
        return profile;
    }

    //Start Simulation

    public void startSimulation(){
        profile = new Profile("default");
        startSimulation("default");
    }

    public void startSimulation(String profileName){
        simManager.startSimulation(profileName);
        ((Schnittstelle) simManager).setSimSpeed(60);
    }

    //-------------------------------------



   /* @Override
    public void setSimulationSpeed(int speed) {
        simManager.setSpeed(speed);
    }

    @Override
    public void updateLightData(LightData lightData) {
        simScreen.updateLightData(lightData);
    }

    @Override
    public void updateBodys(List<Body> bodys) {
        System.out.println("Manager, bodys was send");
        simScreen.updateBodys(bodys);
    }

    @Override
    public void updateRound(int round) {
        System.out.println("Manager, round: " + round);
        simScreen.updateRound(round);
    }

    @Override
    public void updateDay(int day) {
        //System.out.println("Manager, day: " + day); 
        if (simScreen != null) {
            simScreen.updateDay(day);
            
        } else {System.out.println("simScreen gibts nicht");}
    }

    @Override
    public void updateTime(int time) {
        //System.out.println("Manager, time: " + time);
        simScreen.updateTime(time);
    }

    @Override
    public void updateUpdates(int updates) {
       // System.out.println("Manager, updates: " + updates);
        simScreen.updateUpdates(updates);
    }

    @Override
    public void endCurrentMode() {
        simManager.endCurrentMode();
    }

    public static void main(String[] args) {launch();}
*/
}


