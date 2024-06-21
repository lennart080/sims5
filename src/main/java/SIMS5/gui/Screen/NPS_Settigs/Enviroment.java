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
        createSliders();
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
        vBox.getChildren().add(buttonBack);
    }

    @Override
    protected void createLabels(){
        labels[0] = lightTime;
        labels[1] = lightIntensity;
        labels[2] = noiseStrength;
        labels[3] = noiseSize;
        labels[4] = dayLengthVariation;
        labels[5] = simulationSize;
    }

    @Override
    protected void createSliders(){
        //muss noch gemacht werden
        sliders[0] = new Slider(0,0,0);
        sliders[0].setMajorTickUnit(1);
        sliders[1] = new Slider(0,0,0);
        sliders[1].setMajorTickUnit(1);
        sliders[2] = new Slider(0,0,0);
        sliders[2].setMajorTickUnit(1);
        sliders[3] = new Slider(0,0,0);
        sliders[3].setMajorTickUnit(1);
        sliders[4] = new Slider(0,0,0);
        sliders[4].setMajorTickUnit(1);
        sliders[5] = new Slider(0,0,0);
        sliders[5].setMajorTickUnit(1);
    }
/* 
    @Override
    protected void saveInput(){
        for(int i = 0; i < sliders.length; i++){
            profile.set(labels[i].getText(),sliders[i].getValue());
        }
    }*/
}
