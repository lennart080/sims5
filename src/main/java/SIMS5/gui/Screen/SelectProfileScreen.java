package SIMS5.gui.Screen;

import SIMS5.gui.GuiManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class SelectProfileScreen {

    // Objekte + Arrays:
    private GuiManager manager;
    private Stage stage;
    private Scene scene;
    private ScrollPane pane = new ScrollPane();

    //Componente:
    private Label test = new Label("test");
    private Button confirm = new Button("confirm");

    public SelectProfileScreen(Stage stage, GuiManager manager) {
        this.manager = manager;
        this.stage = stage;
        String[] profiles = manager.getProfile().getAllProfiles();

        /*for (int i = 0; i<profiles.length; i++){
            System.out.println(i);

        }*/




        // Componente:

        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    new SimulationScreen(stage,manager);
            }
        });


        // Scene Configuration
        scene = new Scene(pane, 750, 500);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    new StartScreen(stage,manager);
                }
            }
        });

        // Stage Configuration

        stage.setTitle("SelectProfileScreen");
        stage.setScene(scene);
    }
}