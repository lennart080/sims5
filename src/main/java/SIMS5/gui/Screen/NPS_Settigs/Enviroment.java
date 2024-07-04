package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Enviroment extends Settings {

    //Labels
    private Label lightTime = new Label("lightTime");
    private Label lightIntensity = new Label("lightIntensity");
    private Label noiseStrength = new Label("noiseStrength");
    private Label noiseSize = new Label("noiseSize");
    private Label dayLengthVariation = new Label("dayLengthVariation");
    private Label simulationSize = new Label("simulationSize");

    public Enviroment(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("Enviroment");
        labels = new Label[6];
        sliders = new Slider[6];
        createLabels();
        createInput();
        for(int i = 0; i < labels.length; i++){
            labels[i].setFont(Font.font("Stencil", FontWeight.MEDIUM,16));
            sliders[i].setShowTickMarks(true);
            sliders[i].setShowTickLabels(true);
            sliders[i].setMinorTickCount(0);
            sliders[i].setBlockIncrement(0);
            sliders[i].setSnapToTicks(true);
            vBox.getChildren().add(labels[i]);
            vBox.getChildren().add(sliders[i]);
        }
        sliders[0].setMinWidth(scene.getWidth()-150);
        vBox.getChildren().add(buttonBack);
    }

    @Override
    protected void createLabels(){
        labels[0] = lightTime;
        labels[0].setId("lightTime");
        labels[1] = lightIntensity;
        labels[1].setId("lightIntensity");
        labels[2] = noiseStrength;
        labels[2].setId("noiseStrength");
        labels[3] = noiseSize;
        labels[3].setId("noiseSize");
        labels[4] = dayLengthVariation;
        labels[4].setId("dayLengthVariation");
        labels[5] = simulationSize;
        labels[5].setId("simulationSize");
    }

    @Override
    protected void createInput(){
        //muss noch gemacht werden
        sliders[0] = new Slider(1,59,profile.getIntager(labels[0].getId()));
        sliders[0].setMajorTickUnit(1);
        sliders[1] = new Slider(0,1,profile.getIntager(labels[1].getId()));
        sliders[1].setMajorTickUnit(0.01);
        sliders[2] = new Slider(0.001,0.029,profile.getIntager(labels[2].getId()));
        sliders[2].setMajorTickUnit(0.001);
        sliders[3] = new Slider(0,1,profile.getIntager(labels[3].getId()));
        sliders[3].setMajorTickUnit(0.01);
        sliders[4] = new Slider(0,1,profile.getIntager(labels[4].getId()));
        sliders[4].setMajorTickUnit(0.01);
        double simulationSize = Math.sqrt(profile.getIntager("entitysPerRound"))*profile.getIntager("entitySize")+600;
        sliders[5] = new Slider(simulationSize,simulationSize+600,profile.getIntager(labels[5].getId()));
        sliders[5].setMajorTickUnit(1);
    }

    @Override
    protected void saveInput(){
        for(int i = 0; i < sliders.length; i++){
            profile.set(labels[i].getId(),sliders[i].getValue());
        }
    }
}
