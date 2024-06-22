package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class KI extends Settings{

    //Labels
    private Label doSpawnedNeuronshaveABias = new Label("doSpawnedNeuronshaveABias");
    private Label mutationProbabilityWeightDying = new Label("mutationProbabilityWeightDying");
    private Label mutationProbabilityNewWeight = new Label("mutationProbabilityNewWeight");
    private Label mutationProbabilityWeightAjustment = new Label("mutationProbabilityWeightAjustment");
    private Label mutationProbabilityWeightAjustmentValue = new Label("mutationProbabilityWeightAjustmentValue");
    private Label mutationProbabilityBiasAjustment = new Label("mutationProbabilityBiasAjustment");
    private Label mutationProbabilityBiasAjustmentValue = new Label("mutationProbabilityBiasAjustmentValue");
    private Label mutationProbabilityNewNeuronRow = new Label("mutationProbabilityNewNeuronRow");
    private Label mutationProbabilityNewNeuron = new Label("mutationProbabilityNewNeuron");
    private Label mutationProbabilityNeuronDying = new Label("mutationProbabilityNeuronDying");
    private Label networkStartHiddenLayers = new Label("networkStartHiddenLayers");
    private Label entityWalkActivation = new Label("entityWalkActivation");
    private Label entitySelectionValueRandom = new Label("entitySelectionValueRandom");
    private Label entitySelectionValueMostDifferent = new Label("entitySelectionValueMostDifferent");
    private Label entitySelectionValueNew = new Label("entitySelectionValueNew");

    public KI(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("KI");
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
        vBox.getChildren().add(buttonBack);
    }

    @Override
    protected void createLabels(){
        labels[0] = doSpawnedNeuronshaveABias;
        labels[0].setId("doSpawnedNeuronshaveABias");
        labels[1] = mutationProbabilityWeightDying;
        labels[1].setId("mutationProbabilityWeightDying");
        labels[2] = mutationProbabilityNewWeight;
        labels[2].setId("mutationProbabilityNewWeight");
        labels[3] = mutationProbabilityWeightAjustment;
        labels[3].setId("mutationProbabilityWeightAjustment");
        labels[4] = mutationProbabilityWeightAjustmentValue;
        labels[4].setId("mutationProbabilityWeightAjustmentValue");
        labels[5] = mutationProbabilityBiasAjustment;
        labels[5].setId("mutationProbabilityBiasAjustment");
        labels[6] = mutationProbabilityBiasAjustmentValue;
        labels[6].setId("mutationProbabilityBiasAjustmentValue");
        labels[7] = mutationProbabilityNewNeuronRow;
        labels[7].setId("mutationProbabilityNewNeuronRow");
        labels[8] = mutationProbabilityNewNeuron;
        labels[8].setId("mutationProbabilityNewNeuron");
        labels[9] = mutationProbabilityNeuronDying;
        labels[9].setId("mutationProbabilityNeuronDying");
        labels[10] = networkStartHiddenLayers;
        labels[10].setId("networkStartHiddenLayers");
        labels[11] = entityWalkActivation;
        labels[11].setId("entityWalkActivation");
        labels[12] = entitySelectionValueRandom;
        labels[12].setId("entitySelectionValueRandom");
        labels[13] = entitySelectionValueMostDifferent;
        labels[13].setId("entitySelectionValueMostDifferent");
        labels[14] = entitySelectionValueNew;
        labels[14].setId("entitySelectionValueNew");
    }

    @Override
    protected void createSliders(){
        //muss noch gemacht werden
        sliders[0] = new Slider(0,0,profile.getIntager(labels[0].getId()));
        sliders[0].setMajorTickUnit(1);
        sliders[1] = new Slider(0,0,profile.getIntager(labels[1].getId()));
        sliders[1].setMajorTickUnit(1);
        sliders[2] = new Slider(0,0,profile.getIntager(labels[2].getId()));
        sliders[2].setMajorTickUnit(1);
        sliders[3] = new Slider(0,0,profile.getIntager(labels[3].getId()));
        sliders[3].setMajorTickUnit(1);
        sliders[4] = new Slider(0,0,profile.getIntager(labels[4].getId()));
        sliders[4].setMajorTickUnit(1);
        sliders[5] = new Slider(0,0,profile.getIntager(labels[5].getId()));
        sliders[5].setMajorTickUnit(1);
        sliders[6] = new Slider(0,0,profile.getIntager(labels[6].getId()));
        sliders[6].setMajorTickUnit(1);
        sliders[7] = new Slider(0,0,profile.getIntager(labels[7].getId()));
        sliders[7].setMajorTickUnit(1);
        sliders[8] = new Slider(0,0,profile.getIntager(labels[8].getId()));
        sliders[8].setMajorTickUnit(1);
        sliders[9] = new Slider(0,0,profile.getIntager(labels[9].getId()));
        sliders[9].setMajorTickUnit(1);
        sliders[10] = new Slider(0,0,profile.getIntager(labels[10].getId()));
        sliders[10].setMajorTickUnit(1);
        sliders[11] = new Slider(0,0,profile.getIntager(labels[11].getId()));
        sliders[11].setMajorTickUnit(1);
        sliders[12] = new Slider(0,0,profile.getIntager(labels[12].getId()));
        sliders[12].setMajorTickUnit(1);
        sliders[13] = new Slider(0,0,profile.getIntager(labels[13].getId()));
        sliders[13].setMajorTickUnit(1);
        sliders[14] = new Slider(0,0,profile.getIntager(labels[14].getId()));
        sliders[14].setMajorTickUnit(1);
    }

    @Override
    protected void saveInput(){
        for(int i = 0; i < sliders.length; i++){
            profile.set(labels[i].getId(),sliders[i].getValue());
        }
    }
}
