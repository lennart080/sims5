package SIMS5.gui.Screen;

import SIMS5.gui.GuiManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StartScreen{

    // Objekte:
    private Scene scene;
    private VBox pane = new VBox();
    private HBox hBoxProfile = new HBox();

    //Componente:
    private Label projectname = new Label("Sims");
    private Button newProfile = new Button("Create new Profile");
    private Button selectProfile = new Button("Select a Profile");
    private Button defaultsettings = new Button("Start with default Settings");

    public StartScreen(Stage stage,GuiManager manager) {
        scene = new Scene(pane,750,500);

        // VBox Configuration

        pane.getChildren().addAll(projectname,hBoxProfile,defaultsettings);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(30,30,30,30));
        pane.setSpacing(40);

        // HBox Configuration

        hBoxProfile.getChildren().addAll(newProfile,selectProfile);
        hBoxProfile.setAlignment(Pos.CENTER);
        hBoxProfile.setSpacing(40);

        // Componente:

        // Label:projectname
        projectname.setFont(Font.font("Stencil", FontWeight.MEDIUM,38));

        //Button: newProfile:
        newProfile.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));
        newProfile.setMinHeight(30);
        newProfile.setMaxWidth(300);
        newProfile.setMinWidth(300);
        newProfile.setOnAction(new EventHandler<ActionEvent>() {    //Funktion beim Drücken
            @Override
            public void handle(ActionEvent event) {
                new NewProfileScreen(stage,manager);
            }
         });

        //Button: selectProfile:
        selectProfile.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));
        selectProfile.setMinHeight(30);
        selectProfile.setMaxWidth(300);
        selectProfile.setMinWidth(300);
        selectProfile.setOnAction(new EventHandler<ActionEvent>() {    //Funktion beim Drücken
            @Override
            public void handle(ActionEvent event) {
                new SelectProfileScreen(stage,manager);
            }
        });

        selectProfile.setOnAction(e -> {
            new SelectProfileScreen(stage,manager);
        });

        //Button: defaultsettings:
        defaultsettings.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));
        defaultsettings.setMinHeight(30);
        defaultsettings.setMaxWidth(400);
        defaultsettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                manager.startSimulation();
                new SimulationScreen(stage,manager);
            }
        });

        // Stage Configuration

        stage.setMaxHeight(600);
        stage.setMaxWidth(900);
        stage.setMinHeight(440);
        stage.setMinWidth(660);
        stage.setWidth(780);
        stage.setHeight(520);
        stage.setTitle("SIMS5");
        stage.setScene(scene);
        stage.show();
    }
}