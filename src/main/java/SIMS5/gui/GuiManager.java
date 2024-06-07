package SIMS5.gui;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Screen.StartScreen;
import SIMS5.sim.Manager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class GuiManager extends Application {

    // Objekte:
    private Manager simManager = new Manager();
    Profile profile;

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

    public static void main(String[] args) {launch();}
}

