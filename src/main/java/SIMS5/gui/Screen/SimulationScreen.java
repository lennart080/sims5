package SIMS5.gui.Screen;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.GuiManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;

import java.io.IOException;

public class SimulationScreen {

    // Objekte:
    private GuiManager manager;
    private Stage stage;
    private Scene scene;
    private Profile profile;
    private BorderPane pane = new BorderPane();
    private Pane simPane = new Pane();
    private VBox dataPane = new VBox();
    private HBox graphPane = new HBox();

    //Componente:

    private Rectangle test4Eck = new Rectangle();
    private Label data1 = new Label("1111");
    private Label data2 = new Label("2222");
    private Label data3 = new Label("3333");
    private Label data4 = new Label("4444");

    //Feld
    int[] field;
    


    public SimulationScreen(Stage stage, GuiManager manager){
        this.manager = manager;
        this.stage = stage;
        manager.setSimScreen(this);
        profile = manager.getProfile();
        field = new int[profile.getIntager("simulationSize")];
        Screen primaryScreen = Screen.getPrimary();
        Rectangle2D bounds = primaryScreen.getVisualBounds();
        int rectSize = (int)bounds.getWidth();

        // BorderPane Configuration

        pane.setCenter(simPane);
        pane.setLeft(dataPane);
        pane.setTop(graphPane);


        //dataPane Configuration
        dataPane.getChildren().addAll(data1, data2, data3);

        //graphPane
        graphPane.getChildren().add(data4);

        // simPane Configuration

        for (int i = 0; i < manager.getProfile().getIntager("simulationSize"); i++) {
            for (int j = 0; j < manager.getProfile().getIntager("simulationSize"); j++) {
                Rectangle rect = new Rectangle(rectSize, rectSize);
                rect.setX(j * rectSize);
                rect.setY(i * rectSize);
                rect.setFill(Color.LIGHTGRAY); // Setze die Farbe nach Bedarf
                rect.setStroke(Color.BLACK); // Setze die Randfarbe nach Bedarf
                rect.setStrokeWidth(0.5);
                simPane.getChildren().add(rect);
            }
        }
    
        

        // Componente:


        // test
        test4Eck.setWidth(400);
        test4Eck.setHeight(400);
        test4Eck.setX(200);
        test4Eck.setY(250);
        try {
            //test4Eck.setFill(new ImagePattern(loadImage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Scene Configuration

        scene = new Scene(pane,0,0);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                   // new MenueScreen(stage,manager,_____)
                }
            }
        });

        // Stage Configuration

        stage.setMaxWidth(4000);
        stage.setMaxHeight(2000);
        stage.setMaximized(true);
        //stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setTitle("SimulationScreen");
        stage.setScene(scene);
    }

    private Image loadImage()  throws IOException {
        FileInputStream inputStream = new FileInputStream("/gui/Grafik/Entity2.jpg");
        Image image = new Image(inputStream);
        return image;
    }

    public void updateRound(int round){
        System.out.println(round);
        data1.setText(String.valueOf(round));
    }

    public void updateDay(int day){
        System.out.println(day);
        Platform.runLater(() -> data2.setText(String.valueOf(day)));
        manager.setSimulationSpeed(1);
    }

}