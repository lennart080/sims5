package SIMS5.gui.Screen;

import SIMS5.gui.GuiManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        
        //vBox.getChildren().addAll(boxName,boxEntityStart);
        pane.setPadding(new Insets(30,30,30,30));
        
        //boxName
        boxName.getChildren().addAll(labelName,inputName);

        //boxEntityStart
        boxEntityStart.getChildren().addAll(entityStartEnergie, entityStartSchrott, entityStartAttack, entityStartEnergieCapacity, entityStartSpeed, entityStartDefense, entityStartHealth, entityStartRust, entityStartSolar);

        //Componente:

        //Label: labelCreateProfile
        labelCreateProfile.setFont(Font.font("Stencil", FontWeight.MEDIUM,38));

        //Label: labelName
        labelName.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));

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
}