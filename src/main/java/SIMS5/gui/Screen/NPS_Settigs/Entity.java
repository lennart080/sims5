package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Entity extends Settings{

    //Labels
    private Label entityStartEnergie = new Label("EntityStartEnergie - The initial energy that a robot has");
    private Label entityStartSchrott = new Label("EntityStartSchrott - The initial scrap that the robot has");
    private Label entityStartAttack = new Label("EntityStartAttack - The initial attack the robot has");
    private Label entityStartEnergieCapacity = new Label("EntityStartEnergieCapacity - The initial energy capacity the robot has");
    private Label entityStartSpeed = new Label("EntityStartSpeed - The initial speed the robot has");
    private Label entityStartDefense = new Label("EntityStartDefense - The initial defence the robot has");
    private Label entityStartHealth = new Label("EntityStartHealth - The initial health the robot has");
    private Label entityStartRust = new Label("EntityStartRust - The initial rust the robot has");
    private Label entityStartSolar = new Label("EntityStartSolar - The initial solar the robot has");

    public Entity(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("Entity");
        labels = new Label[9];
        sliders = new Slider[9];
        createLabels();
        createSliders();
        for(int i = 0; i < labels.length; i++){
            labels[i].setFont(Font.font("Stencil", FontWeight.MEDIUM,16));
            sliders[i].setShowTickMarks(true);
            sliders[i].setShowTickLabels(true);
            sliders[i].setMajorTickUnit(1);
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
        labels[0] = entityStartEnergie;
        labels[1] = entityStartSchrott;
        labels[2] = entityStartAttack;
        labels[3] = entityStartEnergieCapacity;
        labels[4] = entityStartSpeed;
        labels[5] = entityStartDefense;
        labels[6] = entityStartHealth;
        labels[7] = entityStartRust;
        labels[8] = entityStartSolar;
    }

    private void createSliders(){
        sliders[0] = new Slider(1,10,0);
        sliders[1] = new Slider(1,10,0);
        sliders[2] = new Slider(1,10,0);
        sliders[3] = new Slider(1,10,0);
        sliders[4] = new Slider(1,10,0);
        sliders[5] = new Slider(1,10,0);
        sliders[6] = new Slider(1,10,0);
        sliders[7] = new Slider(1,10,0);
        sliders[8] = new Slider(1,10,0);
    }
}

//sliders[].adjustValue(1);