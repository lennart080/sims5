package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public abstract class Settings {

    //Objekte:
    protected Stage stage;
    protected Stage mainStage;
    protected Profile profile;
    protected Pane pane;

    //Componente:
    protected Label labelTitle = new Label();
    protected Button buttonBack = new Button("Back");

    public Settings(Stage mainStage,Stage tempStage,Profile profile){

        //Init
        this.profile = profile;
        this.stage = tempStage;
        this.mainStage = mainStage;

        //pane
        pane = new Pane();
        pane.getChildren().add(buttonBack);
        pane.setPrefSize(400, 300);

        //buttonBack
        buttonBack.setMinHeight(30);
        buttonBack.setMaxWidth(150);
        buttonBack.setMinWidth(150);
        buttonBack.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                mainStage.show();
            }
        });

        //Stage
        stage.setScene(new Scene(pane));
        stage.show();
    }
}
