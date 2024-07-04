package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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

    CheckBox checkBox = new CheckBox();

    public KI(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("KI");
        labels = new Label[15];

        //Inputs
        sliders = new Slider[0];

        createLabels();
        createInput();

        //Zentrieren
        Label transperendLabel = new Label();
        transperendLabel.setMaxHeight(0);
        transperendLabel.setMinWidth(scene.getWidth()-150);
        vBox.getChildren().add(transperendLabel);

        for(int i = 0; i < labels.length; i++){
            labels[i].setFont(Font.font("Stencil", FontWeight.MEDIUM,16));
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
    protected void createInput(){
        vBox.getChildren().add(labels[0]);
        vBox.getChildren().add(checkBox);
        for (int i = 1; i < 10; i++) {
            HBox hBox = new HBox();
            HBox hBoxLabel = new HBox(labels[i]);
            hBox.setAlignment(Pos.CENTER);
            hBoxLabel.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(new TextField(),new TextField(),new TextField());
            vBox.getChildren().addAll(hBoxLabel,hBox);
        }
    }

    @Override
    protected void saveInput(){
        profile.set(labels[0].getId(),checkBox.isPressed());
        for (int i = 1; i < 10; i++) {
            
        }
    }
}
