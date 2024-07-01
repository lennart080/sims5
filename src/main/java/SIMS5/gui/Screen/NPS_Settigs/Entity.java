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
    private Label entityEnergylossAjustmentPerDay = new Label("entityEnergylossAjustmentPerDay");
    private Label entityRustPlus = new Label("entityRustPlus");
    private Label entityRustLoss = new Label("entityRustLoss");
    private Label entityEnergyLoss = new Label("entityEnergyLoss");
    private Label entityHealthLoss = new Label("entityHealthLoss");
    private Label entityAttackEnergieLoss = new Label("entityAttackEnergieLoss");

    public Entity(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("Entity");
        labels = new Label[15];
        sliders = new Slider[15];
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
        sliders[0].setMinWidth(scene.getWidth()-150);
        vBox.getChildren().add(buttonBack);
    }

    @Override
    protected void createLabels(){
        labels[0] = entityStartEnergie;
        labels[0].setId("entityStartEnergie");
        labels[1] = entityStartSchrott;
        labels[1].setId("entityStartSchrott");
        labels[2] = entityStartAttack;
        labels[2].setId("entityStartAttack");
        labels[3] = entityStartEnergieCapacity;
        labels[3].setId("entityStartEnergieCapacity");
        labels[4] = entityStartSpeed;
        labels[4].setId("entityStartSpeed");
        labels[5] = entityStartDefense;
        labels[5].setId("entityStartDefense");
        labels[6] = entityStartHealth;
        labels[6].setId("entityStartHealth");
        labels[7] = entityStartRust;
        labels[7].setId("entityStartRust");
        labels[8] = entityStartSolar;
        labels[8].setId("entityStartSolar");
        labels[9] = entityEnergylossAjustmentPerDay;
        labels[9].setId("entityEnergylossAjustmentPerDay");
        labels[10] = entityRustPlus;
        labels[10].setId("entityRustPlus");
        labels[11] = entityRustLoss;
        labels[11].setId("entityRustLoss");
        labels[12] = entityEnergyLoss;
        labels[12].setId("entityEnergyLoss");
        labels[13] = entityHealthLoss;
        labels[13].setId("entityHealthLoss");
        labels[14] = entityAttackEnergieLoss;
        labels[14].setId("entityAttackEnergieLoss");
    }

    @Override
    protected void createSliders(){
        sliders[0] = new Slider(10,200,profile.getIntager(labels[0].getId()));
        sliders[0].setMajorTickUnit(10);
        sliders[1] = new Slider(0,10,profile.getIntager(labels[1].getId()));
        sliders[1].setMajorTickUnit(1);
        sliders[2] = new Slider(0,5,profile.getIntager(labels[2].getId()));
        sliders[2].setMajorTickUnit(1);
        sliders[3] = new Slider(10,200,profile.getIntager(labels[3].getId()));
        sliders[3].setMajorTickUnit(10);
        sliders[4] = new Slider(1,5,profile.getIntager(labels[4].getId()));
        sliders[4].setMajorTickUnit(1);
        sliders[5] = new Slider(0,5,profile.getIntager(labels[5].getId()));
        sliders[5].setMajorTickUnit(1);
        sliders[6] = new Slider(1,15,profile.getIntager(labels[6].getId()));
        sliders[6].setMajorTickUnit(1);
        sliders[7] = new Slider(0,5,profile.getIntager(labels[7].getId()));
        sliders[7].setMajorTickUnit(1);
        sliders[8] = new Slider(0,10,profile.getIntager(labels[8].getId()));
        sliders[8].setMajorTickUnit(1);
        //muss noch gemacht werden
        sliders[9] = new Slider(0.1,5,profile.getDouble(labels[9].getId()));
        sliders[9].setMajorTickUnit(0.5);
        sliders[10] = new Slider(0.1,5,profile.getDouble(labels[10].getId()));
        sliders[10].setMajorTickUnit(0.1);
        sliders[11] = new Slider(0.1,5,profile.getDouble(labels[1].getId()));
        sliders[11].setMajorTickUnit(0.5);
        sliders[12] = new Slider(0.1,5,profile.getDouble(labels[12].getId()));
        sliders[12].setMajorTickUnit(0.2);
        sliders[13] = new Slider(0.1,5,profile.getDouble(labels[13].getId()));
        sliders[13].setMajorTickUnit(0.1);
        sliders[14] = new Slider(0,10,profile.getIntager(labels[14].getId()));
        sliders[14].setMajorTickUnit(2);
    }
 
    @Override
    protected void saveInput(){
        for(int i = 0; i < sliders.length; i++){
            profile.set(labels[i].getId(),sliders[i].getValue());
        }
    }
}

//sliders[].adjustValue(1);