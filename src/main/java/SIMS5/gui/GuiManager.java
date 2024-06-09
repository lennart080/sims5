package SIMS5.gui;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Screen.SimulationScreen;
import SIMS5.gui.Screen.StartScreen;
import SIMS5.sim.Gui.MainGui;
import SIMS5.sim.Manager;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.enviroment.LightData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;


public class GuiManager extends Application implements MainGui {

    // Objekte:
    private Manager simManager = new Manager(this);
    Profile profile;
    SimulationScreen simScreen;

    @Override
    public void start(Stage stage) {

        //Start Gui

        Pane emtiyPane = new Pane();
        Scene scene = new Scene(emtiyPane,0,0);
        stage.setScene(scene);
        new StartScreen(stage,this);

        // Tests

        Profile profiletest1 = new Profile("TP1");
        Profile profiletest2 = new Profile("TP2");
        Profile profiletest3 = new Profile("TP3");
        Profile profiletest4 = new Profile("TP4");
        profiletest1.set("entityStartEnergie",1);
        profiletest2.set("entityStartEnergie",1);
        profiletest3.set("entityStartEnergie",1);
        profiletest4.set("entityStartEnergie",1);

        profile = profiletest4;
    }

    public void setSimScreen(SimulationScreen simScreen){
        this.simScreen = simScreen;
    }

    public void createProfile(String profilname){
        Profile profile = new Profile(profilname);
        this.profile = profile;
    }

    public void createProfile(String profilname,String password){
        Profile profile = new Profile(profilname);
        this.profile = profile;

        //Password nicht fertig, das Profil wird ohne ein PW erstellt
    }

    public Profile getProfile() {
        return profile;
    }

    //Start Simulation

    public void startSimulation(){
        String profilname = "default";
        profile = new Profile(profilname);
        simManager.startSimulation(profilname);
    }

    public void startSimulation(String profileName){
        simManager.startSimulation(profileName);
    }

    //-------------------------------------

    @Override
    public void setSimulationSpeed(int speed) {
        simManager.setSpeed(speed);
    }

    @Override
    public void updateLightData(LightData lightData) {

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
        System.out.println("Manager, day: " + day); 
        simScreen.updateDay(day);
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

    public static void main(String[] args) {launch();}
}

