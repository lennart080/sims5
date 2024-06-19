package SIMS5.gui.Screen;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.GuiManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SelectProfileScreen {

    // Objekte + Arrays:
    private Scene scene;
    private ScrollPane pane = new ScrollPane();

    //Componente:
    private VBox vbox = new VBox();

    public SelectProfileScreen(Stage stage, GuiManager manager) {
        String[] profiles = new Profile("Default").getAllProfiles();

        for (String profile : profiles){

            //Button: profileButton
            Button profileButton = new Button();
            profileButton.setText(profile);
            profileButton.setMinSize(600, 100);
            profileButton.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));
            profileButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    manager.startSimulation(profile);
                    new SimulationScreen(stage,manager);
                }
            });
            
            vbox.getChildren().add(profileButton);
        }

        // VBox Configuration
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(50,50,50,50));
        vbox.setAlignment(Pos.CENTER);

        // pane
        pane.setContent(vbox);

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

        stage.setMaxWidth(750);
        stage.setMinWidth(750);
        stage.setScene(scene);
    }
}