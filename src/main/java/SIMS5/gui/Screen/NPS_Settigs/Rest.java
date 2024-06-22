package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Rest extends Settings{

    //Labels
    private Label seed = new Label("seed");
    private Label speed = new Label("speed");
    private Label robotsPerRound = new Label("robotsPerRound");
    private Label entityPosSave = new Label("entityPosSave");
    private TextField inputField;

    public Rest(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("Rest");

        //seed
        seed.setFont(Font.font("Stencil", FontWeight.MEDIUM,16));
        seed.setId("seed");
        inputField = new TextField();
        inputField.setText(String.valueOf(profile.getIntager("seed")));
        vBox.getChildren().addAll(seed,inputField);

        labels = new Label[3];
        sliders = new Slider[3];
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
        buttonBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(checkInputSeed()){
                    saveInput();
                    stage.close();
                    mainStage.show();
                    }
            }
        });
        vBox.getChildren().add(buttonBack);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    if(checkInputSeed()){
                        saveInput();
                        stage.close();
                        mainStage.show();
                    }
                    else{
                        seed.setTextFill(Color.RED);
                        seed.setText("seed* - Please enter numbers!");
                    }
                }
            }
        });
    }

    @Override
    protected void createLabels(){
        labels[0] = speed;
        labels[0].setId("speed");
        labels[1] = robotsPerRound;
        labels[1].setId("robotsPerRound");
        labels[2] = entityPosSave;
        labels[2].setId("entityPosSave");
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
    }

    @Override
    protected void saveInput(){
        profile.set(seed.getId(),Double.parseDouble(inputField.getText()));
        for(int i = 0; i < sliders.length; i++){
            profile.set(labels[i].getId(),sliders[i].getValue());
        }
    }

    private boolean checkInputSeed(){
        String text = inputField.getText();
        if(text.matches("[0-9]+")){return true;}
        else{return false;}
    }
}
