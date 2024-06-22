package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public abstract class Settings {

    //Objekte:
    protected Stage stage;
    protected Stage mainStage;
    protected Scene scene;
    protected Profile profile;
    protected ScrollPane pane;
    protected VBox vBox = new VBox();
    protected Label[] labels;
    protected Slider[] sliders;

    //Componente:
    protected Label labelTitle = new Label();
    protected Button buttonBack = new Button("Back");

    public Settings(Stage mainStage,Stage tempStage,Profile profile){

        //Init
        this.profile = profile;
        //Profile tempProfile = new Profile("temp");
        this.stage = tempStage;
        this.mainStage = mainStage;

        //vBox
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(50,50,50,50));
        vBox.setSpacing(30);
        vBox.getChildren().add(labelTitle);
    
        //labelTitle
        labelTitle.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));

        //buttonBack
        buttonBack.setMinHeight(30);
        buttonBack.setMaxWidth(150);
        buttonBack.setMinWidth(150);
        buttonBack.setFont(Font.font("Stencil", FontWeight.MEDIUM,18));
        buttonBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveInput();
                stage.close();
                mainStage.show();
            }
        });

        //pane
        pane = new ScrollPane();
        pane.setContent(vBox);
        pane.setMinSize(660, 440);
        pane.setMaxSize(900, 600);
        stage.setWidth(780);
        stage.setHeight(520);

        //Scene
        scene = new Scene(pane);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    saveInput();
                    stage.close();
                    mainStage.show();
                }
            }
        });

        //Stage
        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.show();
    }

    protected abstract void createLabels();

    protected abstract void createSliders();

    protected abstract void saveInput();
}
