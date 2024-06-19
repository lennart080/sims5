package SIMS5.gui;

import SIMS5.data.FileHandling.profileFiles.Profile;
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

    public static void main(String[] args) {launch();}
}


