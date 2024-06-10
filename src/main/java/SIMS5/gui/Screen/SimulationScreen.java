package SIMS5.gui.Screen;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Grafik.ImageDirecory;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.entitiys.Robot.RobotBody;
import SIMS5.sim.enviroment.LightData;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.List;

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
    private Rectangle[][] rectangles;
    private Rectangle2D bounds;
    private int rectSize;
    private LightData lightData;
    private List<Body> bodys;

    //Componente:

    private Label dataName1 = new Label("Round :");
    private Label dataName2 = new Label("Day :");
    private Label dataName3 = new Label("Time :");
    private Label dataName4 = new Label("Updates :");
    private Label grafLabel = new Label("graf");

    private Label dataValue1 = new Label();
    private Label dataValue2 = new Label();
    private Label dataValue3 = new Label();
    private Label dataValue4 = new Label();

    public SimulationScreen(Stage stage, GuiManager manager){

        //Init

        this.manager = manager;
        this.stage = stage;
        manager.setSimScreen(this);
        profile = manager.getProfile();
        Screen primaryScreen = Screen.getPrimary();
        bounds = primaryScreen.getVisualBounds();
        rectSize = (((int)bounds.getWidth())/manager.getProfile().getIntager("simulationSize"))-(int)graphPane.getHeight();
        rectangles = new Rectangle[manager.getProfile().getIntager("simulationSize")][manager.getProfile().getIntager("simulationSize")];


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
        dataPane.add(dataName4, 0, 3);
        dataPane.add(dataValue1,1,0);
        dataPane.add(dataValue2, 1, 1);
        dataPane.add(dataValue3, 1, 2);
        dataPane.add(dataValue4, 1, 3);

        //graphPane
        graphPane.getChildren().add(grafLabel);

        // simPane Configuration

        createNewField(rectangles);

        for (int i = 0; i < manager.getProfile().getIntager("simulationSize"); i++) {
            for (int j = 0; j < manager.getProfile().getIntager("simulationSize"); j++) {
                simPane.getChildren().add(rectangles[i][j]);
            }
        }

        /*
        for (int i = 0; i < manager.getProfile().getIntager("simulationSize"); i++) {
            for (int j = 0; j < manager.getProfile().getIntager("simulationSize"); j++) {
                Rectangle rect = new Rectangle(rectSize, rectSize);
                rect.setX((j * rectSize)+((int)bounds.getWidth())/3.5);
                rect.setY(i * rectSize);
                rect.setFill(Color.GREEN);
                if (image != null) {
                   // rect.setFill(new ImagePattern(image));                                  
                }
                simPane.getChildren().add(rect);
            }
        }
        */
    
        

        

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

    private void createNewField(Rectangle[][] rectangles){
        for (int i = 0; i < manager.getProfile().getIntager("simulationSize"); i++) {
            for (int j = 0; j < manager.getProfile().getIntager("simulationSize"); j++) {
                Rectangle rect = new Rectangle(rectSize, rectSize);
                rectangles[i][j] = rect;
                rect.setX((j * rectSize)+((int)bounds.getWidth())/3.5);
                rect.setY(i * rectSize);
                rect.setFill(Color.GREEN);
            }
        }
    }
    
    private Image loadImage()  throws IOException {
        FileInputStream inputStream = new FileInputStream(ImageDirectory + "Entity2.jpg");
        Image image = new Image(inputStream);
        return image;
    }

    private void setBodyPos(int x, int y){
        Rectangle rect = new Rectangle(rectSize, rectSize);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                rectangles[i][j] = rect;
            }
        }
        try {
            Image image = loadImage();
            rect.setFill(new ImagePattern(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLightData(LightData lightData){
        this.lightData = lightData;
    }

    public void updateRound(int round){
        System.out.println(round);
        Platform.runLater(() -> {
            dataValue1.setText(String.valueOf(round));
        });
    }

    public void updateDay(int day){
        System.out.println(day);
        Platform.runLater(() -> {
            try {
            dataValue2.setText(String.valueOf(day));
            manager.setSimulationSpeed(1);
            System.out.println(dataValue2.getText());
        } catch (Exception e) {
            System.err.println("Error updating day: " + e.getMessage());
        }
        });
    }

    public void updateTime(int time){
        //System.out.println(time);
        Platform.runLater(() -> {
            dataValue3.setText(String.valueOf(time));
        });
    }

    public void updateUpdates(int updates){
        //System.out.println(updates);
        Platform.runLater(() -> {
            dataValue4.setText(String.valueOf(updates));
        });
    }

    public void updateBodys(List<Body> bodys){
        this.bodys = bodys;
        updateBody();
    }

    public void updateBody(){
        for(int i = 0; i < bodys.size(); i++){
            bodys.get(i);
            
        }
    }
}