package SIMS5.gui.Screen;

import SIMS5.gui.GuiManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenueScreen {

    // Objekte:
    private GuiManager manager;
    private Stage mainStage;
    private Stage stage;
    private Scene scene;

    //Componente:
    private VBox vBox = new VBox();
    private Button buttonQuit = new Button("Quit");
    private Button buttonDeleteProfile = new Button("Delete Profile");
    private Button buttonBack = new Button("Back to the Simulation");

    public MenueScreen(GuiManager manager, Stage tempStage, Stage mainStage){
        this.manager = manager;
        this.stage = tempStage;
        this.mainStage = mainStage;

        //Button Configuration
        buttonQuit.setMinHeight(30);
        buttonQuit.setMaxWidth(300);
        buttonQuit.setMinWidth(300);
        buttonQuit.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                manager.closeCall();
                mainStage.setMaximized(false);
                new StartScreen(mainStage,manager);
                stage.close();
            }
        });

        buttonDeleteProfile.setMinHeight(30);
        buttonDeleteProfile.setMaxWidth(300);
        buttonDeleteProfile.setMinWidth(300);
        buttonDeleteProfile.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonDeleteProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                manager.closeCall();
                manager.getProfile().deleteProfile();
                mainStage.setMaximized(false);
                new StartScreen(mainStage,manager);
                stage.close();
            }
        });

        buttonBack.setMinHeight(30);
        buttonBack.setMaxWidth(300);
        buttonBack.setMinWidth(300);
        buttonBack.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        //hBox Configuration
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(buttonQuit,buttonDeleteProfile,buttonBack);
        vBox.setSpacing(50);
        vBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");

        //Scene Configuration
        scene = new Scene(vBox);
        scene.setFill(Color.TRANSPARENT);

        //Stage Configuration
        stage.setWidth(400);
        stage.setHeight(300);
        stage.setTitle("Menue");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setOpacity(0.8);
        stage.setScene(scene);
        stage.show();
    }
}
