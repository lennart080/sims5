package SIMS5.gui.Screen;

import java.util.ArrayList;

import SIMS5.gui.GuiManager;
import SIMS5.gui.Screen.NPS_Settigs.Entity;
import SIMS5.gui.Screen.NPS_Settigs.Enviroment;
import SIMS5.gui.Screen.NPS_Settigs.KI;
import SIMS5.gui.Screen.NPS_Settigs.Rest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class NewProfileScreen {

    // Objekte:
    private GuiManager manager;
    private Stage stage;
    private Scene scene;
    private VBox pane = new VBox();
    private HBox hBoxName = new HBox();
    private HBox hBoxSettings = new HBox();
    private ArrayList<String> modes = new ArrayList<>();
    

    //Componente:

    private Label labelCreateProfile = new Label("Create Profile");
    private Label labelSettings = new Label("Settings");
    private Label labelName = new Label("Name");
    private Label labelMode = new Label("Modes:");
    private TextField inputName = new TextField();

    private Button buttonCreate = new Button("Create Profile");
    private Button buttonSettingsEntity = new Button("Entity");
    private Button buttonSettingsEnviroment = new Button("Enviroment");
    private Button buttonSettingsKI = new Button("KI");
    private Button buttonSettingsRest = new Button("Rest");
 
    ChoiceBox modePick = new ChoiceBox<>();

    public NewProfileScreen(Stage stage,GuiManager manager){
        this.manager = manager;
        this.stage = stage;
        modes.add("PurAI");


        //pane 
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(50);
        pane.getChildren().addAll(labelCreateProfile,hBoxName,buttonCreate,labelSettings,hBoxSettings);
        pane.setPadding(new Insets(30,30,30,30));

        //hBoxName
        hBoxName.setAlignment(Pos.CENTER);
        hBoxName.setSpacing(30);
        hBoxName.getChildren().addAll(labelName,inputName,labelMode,modePick);

        //hBoxSettings
        hBoxSettings.setAlignment(Pos.CENTER);
        hBoxSettings.setSpacing(30);
        hBoxSettings.getChildren().addAll(buttonSettingsEntity,buttonSettingsEnviroment,buttonSettingsKI,buttonSettingsRest);

        //Componente:

        //modePick
        modePick.setValue(modes.get(0));
        modePick.setMaxSize(300, 30);
        modePick.setMinSize(200, 30);
        for(int i = 0; i < modes.size(); i++){
            modePick.getItems().add(modes.get(i));
        }
        

        //labels
        labelCreateProfile.setFont(Font.font("Stencil", FontWeight.MEDIUM,25));
        labelSettings.setFont(Font.font("Stencil", FontWeight.MEDIUM,23));
        labelName.setFont(Font.font("Stencil", FontWeight.MEDIUM,19));
        labelMode.setFont(Font.font("Stencil", FontWeight.MEDIUM,19));

        //buttons
        buttonCreate.setMinHeight(30);
        buttonCreate.setMaxWidth(300);
        buttonCreate.setMinWidth(300);
        buttonCreate.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));
        buttonCreate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (inputName.getText().isEmpty()) {
                    labelName.setTextFill(Color.RED);
                    labelName.setText("Name* - Please enter Name");
                }
                else {
                    manager.createProfile(inputName.getText());
                    manager.startSimulation(inputName.getText());
                }
            }
        });

        buttonSettingsEntity.setMinHeight(30);
        buttonSettingsEntity.setMaxWidth(150);
        buttonSettingsEntity.setMinWidth(150);
        buttonSettingsEntity.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonSettingsEntity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage tempStage = new Stage();
                new Entity(stage,tempStage,manager.getProfile());
                stage.close();
            }
        });

        buttonSettingsEnviroment.setMinHeight(30);
        buttonSettingsEnviroment.setMaxWidth(150);
        buttonSettingsEnviroment.setMinWidth(150);
        buttonSettingsEnviroment.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonSettingsEnviroment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage tempStage = new Stage();
                new Enviroment(stage,tempStage,manager.getProfile());
                stage.close();
            }
        });

        buttonSettingsKI.setMinHeight(30);
        buttonSettingsKI.setMaxWidth(150);
        buttonSettingsKI.setMinWidth(150);
        buttonSettingsKI.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonSettingsKI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage tempStage = new Stage();
                new KI(stage,tempStage,manager.getProfile());
                stage.close();
            }
        });

        buttonSettingsRest.setMinHeight(30);
        buttonSettingsRest.setMaxWidth(150);
        buttonSettingsRest.setMinWidth(150);
        buttonSettingsRest.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonSettingsRest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage tempStage = new Stage();
                new Rest(stage,tempStage,manager.getProfile());
                stage.close();
            }
        });

        // Scene Configuration

        scene = new Scene(pane,750,500);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    new StartScreen(stage,manager);
                }
            }
        });

        // Stage Configuration
        stage.setScene(scene);
    }
}