package SIMS5.gui.Screen;

import java.util.ArrayList;

import SIMS5.gui.GuiManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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

    private Label labelreateProfile = new Label("Create Profile");
    private Label labelSettings = new Label("Settings");
    private Label labelName = new Label("Name");
    private Label labelMode = new Label("Modes:");
    private TextField inputName = new TextField();

    private Button buttonCreate = new Button("Create Profile");
    private Button buttonSettingsEntity = new Button("Entity");
    private Button buttonSettingsEnviroment = new Button("Enviroment");
    private Button buttonSettingsKI = new Button("KI");
    private Button buttonSettingsRest = new Button("Rest");
 
    ListView<String> modePick = new ListView<String>();

    public NewProfileScreen(Stage stage,GuiManager manager){
        this.manager = manager;
        this.stage = stage;
        modes.add("PurAI");

        //pane 
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(labelreateProfile,hBoxName,buttonCreate,labelSettings,hBoxSettings);
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
        for(int i = 0; i < modes.size(); i++){
            modePick.getItems().add(modes.get(i));
            
        }
        

        //labels
        labelreateProfile.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));
        labelSettings.setFont(Font.font("Stencil", FontWeight.MEDIUM,19));
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
                    new SimulationScreen(stage,manager); 
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
                
            }
        });

        buttonSettingsEnviroment.setMinHeight(30);
        buttonSettingsEnviroment.setMaxWidth(150);
        buttonSettingsEnviroment.setMinWidth(150);
        buttonSettingsEnviroment.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonSettingsEnviroment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });

        buttonSettingsKI.setMinHeight(30);
        buttonSettingsKI.setMaxWidth(150);
        buttonSettingsKI.setMinWidth(150);
        buttonSettingsKI.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonSettingsKI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });

        buttonSettingsRest.setMinHeight(30);
        buttonSettingsRest.setMaxWidth(150);
        buttonSettingsRest.setMinWidth(150);
        buttonSettingsRest.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonSettingsRest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
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
        stage.setHeight(800);
        stage.setMinHeight(780);
        stage.setWidth(900);
        stage.setMinWidth(900);
        stage.setTitle("NewProfileScreen");
    }
}

/* 
public class NewProfileScreen {

    // Objekte:
    private GuiManager manager;
    private Stage stage;
    private Scene scene;
    private ScrollPane pane = new ScrollPane();
    private VBox vBox = new VBox();
    private VBox boxName = new VBox();
    private VBox boxEntityStart = new VBox();

    //Componente:

    //Labels
    private Label[] labels = new Label[100];
    private TextField[] textFields = new TextField[100];
    

    private Label labelCreateProfile = new Label("Create Profile");
    private Label labelName = new Label("Name*");
    private Label entityStartEnergie = new Label("EntityStartEnergie - The initial energy that a robot has");
    private Label entityStartSchrott = new Label("EntityStartSchrott - The initial scrap that the robot has");
    private Label entityStartAttack = new Label("EntityStartAttack - The initial attack the robot has");
    private Label entityStartEnergieCapacity = new Label("EntityStartEnergieCapacity - The initial energy capacity the robot has");
    private Label entityStartSpeed = new Label("EntityStartSpeed - The initial speed the robot has");
    private Label entityStartDefense = new Label("EntityStartDefense - The initial defence the robot has");
    private Label entityStartHealth = new Label("EntityStartHealth - The initial health the robot has");
    private Label entityStartRust = new Label("EntityStartRust - The initial rust the robot has");
    private Label entityStartSolar = new Label("EntityStartSolar - The initial solar the robot has");

    //TextFields
    private TextField inputName = new TextField("");


    //Buttons
    private Button buttonCreate = new Button("Create new Profile");

    public NewProfileScreen(Stage stage,GuiManager manager){
        this.manager = manager;
        this.stage = stage;

        // VBox Configuration

        // pane
        pane.setContent(vBox);
        //vBox.setSpacing(50);
        createNewLabels();
        createTextFields();
        setComponent();
        
        pane.setPadding(new Insets(30,30,30,30));
        

        //Button: buttonCreate
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
                    new SimulationScreen(stage,manager); 
                }
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
        stage.setHeight(800);
        stage.setMinHeight(780);
        stage.setWidth(900);
        stage.setMinWidth(900);
        stage.setTitle("NewProfileScreen");
        
    }


    // Ist für die Zukunft um Code zu sparen
    // Die Labels sollen im Array sein und von der settingRestrictions ausgelesen werden
    private void createNewLabels(){
        labels[0] = labelName;
        labels[1] = entityStartEnergie;
        labels[2] = entityStartSchrott;
        labels[3] = entityStartAttack;
        labels[4] = entityStartEnergieCapacity;
        labels[5] = entityStartSpeed;
        labels[6] = entityStartDefense;
        labels[7] = entityStartHealth;
        labels[8] = entityStartRust;
        labels[9] = entityStartSolar;
    }

    // Die TextFields sollen im Array sein und von der settingRestrictions ausgelesen werden
    private void createTextFields(){
        for(int i = 0; i < labels.length; i++){
            textFields[i] = new TextField();
        }
    }

    private void setComponent(){
        Platform.runLater(() -> {
            for(int i = 0; i < labels.length; i++){
                labels[i].setFont(Font.font("Stencil", FontWeight.MEDIUM,21));
                vBox.getChildren().addAll(labels[i],textFields[i]);
            }
            vBox.getChildren().add(buttonCreate);
        });
    }
}*/