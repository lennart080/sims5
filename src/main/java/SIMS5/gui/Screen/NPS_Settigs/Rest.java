package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Rest extends Settings{

    //Labels
    private Label seed = new Label("seed");
    private Label speed = new Label("speed");
    private Label robotsPerRound = new Label("robotsPerRound");
    private Label entityPosSave = new Label("entityPosSave");

    public Rest(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("Rest");

        //seed
        seed.setFont(Font.font("Stencil", FontWeight.MEDIUM,16));
        TextField inputField = new TextField();
        vBox.getChildren().add(seed);

        labels = new Label[4];
        sliders = new Slider[4];
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
        labels[0] = speed;
        labels[1] = robotsPerRound;
        labels[2] = entityPosSave;
    }

    @Override
    protected void createSliders(){
        //muss noch gemacht werden
        sliders[0] = new Slider(0,0,1);
        sliders[0].setMajorTickUnit(1);
        sliders[1] = new Slider(0,0,1);
        sliders[1].setMajorTickUnit(1);
        sliders[2] = new Slider(0,0,1);
        sliders[2].setMajorTickUnit(1);
    }
/*
    @Override
    protected void saveInput(){
        for(int i = 0; i < sliders.length; i++){
            profile.set(labels[i].getText(),sliders[i].getValue());
        }
    }*/
}
