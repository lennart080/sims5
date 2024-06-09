package SIMS5.gui.Screen;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Grafik.ImageDirecory;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;

import java.io.IOException;

public class SimulationScreen implements ImageDirecory {

    // Objekte:
    private GuiManager manager;
    private Stage stage;
    private Scene scene;
    private Profile profile;
    private BorderPane pane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox graphPane = new HBox();
    private GridPane dataPane = new GridPane();

    //Componente:

    private Rectangle test4Eck = new Rectangle();
    private Label dataName1 = new Label("1111");
    private Label dataName2 = new Label("Day :");
    private Label dataName3 = new Label("3333");
    private Label grafLabel = new Label("4444");

    private Label dataValue1 = new Label("AAAA");
    private Label dataValue2 = new Label();
    private Label dataValue3 = new Label("CCCC");

    public SimulationScreen(Stage stage, GuiManager manager){
        this.manager = manager;
        this.stage = stage;
        manager.setSimScreen(this);
        profile = manager.getProfile();
        Screen primaryScreen = Screen.getPrimary();
        Rectangle2D bounds = primaryScreen.getVisualBounds();
        int rectSize = ((int)bounds.getWidth())/manager.getProfile().getIntager("simulationSize");

        // Componente:

        Image image = null;
        try {
            image = loadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // BorderPane Configuration

        pane.setCenter(simPane);
        pane.setLeft(dataPane);
        pane.setTop(graphPane);


        //dataPane Configuration
        dataPane.add(dataName1,0,0);
        dataPane.add(dataName2, 0, 1);
        dataPane.add(dataName3, 0, 2);
        dataPane.add(dataValue1,1,0);
        dataPane.add(dataValue2, 1, 1);
        dataPane.add(dataValue3, 1, 2);

        //graphPane
        graphPane.getChildren().add(grafLabel);

        // simPane Configuration
        for (int i = 0; i < manager.getProfile().getIntager("simulationSize"); i++) {
            for (int j = 0; j < manager.getProfile().getIntager("simulationSize"); j++) {
                Rectangle rect = new Rectangle(rectSize, rectSize);
                rect.setX(j * rectSize);
                rect.setY(i * rectSize);
                if (image != null) {
                    rect.setFill(new ImagePattern(image));
                }
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(0.5);
                simPane.getChildren().add(rect);
            }
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
        FileInputStream inputStream = new FileInputStream(ImageDirectory + "Entity2.jpg");
        Image image = new Image(inputStream);
        return image;
    }

    public void updateRound(int round){
        System.out.println(round);
        dataName1.setText(String.valueOf(round));
    }

    public void updateDay(int day){
        System.out.println(day);
        Platform.runLater(() -> {
            dataValue2.setText(String.valueOf(day));
            manager.setSimulationSpeed(1);
        });
    }
}