package SIMS5.gui;

import java.util.List;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Screen.SimulationScreen;
import SIMS5.gui.Screen.StartScreen;
import SIMS5.sim.Gui.Schnittstelle;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.enviroment.LightData;
import SIMS5.sim.Manager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GuiManager extends Application {

    // Objekte:
    private Manager simManager = new Manager(this);
    private Profile profile;
    private Stage stage;

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
    
    public void deleteAllProfiles(String[] profiles){
        for (String profile : profiles){
            Profile temp = new Profile(profile);
            temp.deleteProfile();
        }
    }

    public Profile getProfile() {
        return profile;
    }

    //Start Simulation

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Stage getStage(){
        return stage;
    }

    public void startSimulation(){
        profile = new Profile("default");
        simManager.startSimulation(profile.getName());
        while (!getReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        new SimulationScreen(getStage(),this);
    }

    public void startSimulation(String profileName){
        profile = new Profile(profileName);
        simManager.startSimulation(profileName);
        while (!getReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        new SimulationScreen(getStage(),this);
    }

    public int getRound(){
        return ((Schnittstelle) simManager).getRound();
    }

    public int getDay(){
        return ((Schnittstelle) simManager).getDay();
    }

    public int getTime(){
        return ((Schnittstelle) simManager).getTime();
    }

    public int getUpdates(){
        return ((Schnittstelle) simManager).getUpdates();
    }

    public boolean getReady() {
        return ((Schnittstelle) simManager).getReady();
    }

    public LightData getLightData() {
        return ((Schnittstelle) simManager).getLightData();
    }

    public int getSimSpeed(){
        return ((Schnittstelle) simManager).getSimSpeed();
    }

    public List<Body> getBodys(){
        return ((Schnittstelle) simManager).getBodys();
    }

    public int getMode() {
        return ((Schnittstelle) simManager).getMode();
    }

    public static void main(String[] args) {launch();}

    public void closeCall() {
        simManager.closeCall();
    }
}


