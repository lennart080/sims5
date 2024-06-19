package SIMS5.gui.Screen;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Grafik.ImageDirecory;
import SIMS5.sim.Gui.Schnittstelle;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.entitiys.Robot.RobotBody;
import SIMS5.sim.enviroment.LightData;
import SIMS5.gui.GuiManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationScreen implements ImageDirecory{

    // Objekte:
    private GuiManager manager;
    private Stage stage;    
    private Scene scene;

    private BorderPane pane = new BorderPane();
    private StackPane simPane = new StackPane();
    private GridPane dataPane = new GridPane();
    private Pane graphPane = new Pane();

    private Profile profile;
    private Image bodyImage;
    private Image backRoundImage;
    private LightData lightData;
    private List<Body> bodies;
    private Rectangle2D bounds;
    private boolean simPaneIsReady;
    private boolean haveBodies;

    //Componente:
    private Label dataRoundName = new Label("Round :");
    private Label dataDayName = new Label("Day :");
    private Label dataTimeName = new Label("Time :");
    private Label dataUpdatesName = new Label("Updates :");
    private Label grafLabel = new Label("graf");

    private Label dataRoundValue = new Label();
    private Label dataDayValue = new Label();
    private Label dataTimeValue = new Label();
    private Label dataUpdatesValue = new Label();

    private Rectangle simBack;

    public SimulationScreen(Stage stage, GuiManager manager){
    
        //Init
        this.manager = manager;
        this.stage = stage;
        this.profile = manager.getProfile();
        this.bodies = new ArrayList<>(profile.getIntager("entitysPerRound"));

        //Bilder laden
        /* backRoundImage fehlt noch
        try {
            backRoundImage = loadImage("backRoundImage");
        } catch (IOException e) {
            System.err.println("Error loading backRoundImage: " + e.getMessage());
        }
            */

        try {
            bodyImage = loadImage("Entity2");
        } catch (IOException e) {
            System.err.println("Error loading backRoundImage: " + e.getMessage());
        }
        
        //Grössen anpassen an den Bildschirm
        Screen primaryScreen = Screen.getPrimary();
        this.bounds = primaryScreen.getVisualBounds();
        
        //BorderPane Configuration

        pane.setCenter(simPane);
        pane.setLeft(dataPane);
        pane.setTop(graphPane);
        
        //graphPane Configuration
        graphPane.getChildren().add(grafLabel);

        //tests
    
        

        Rectangle rect2 = new Rectangle(100,100);
        graphPane.getChildren().add(rect2);

        //simPaneConfiguration
        simPane.setAlignment(Pos.TOP_LEFT);
        
        simBack = new Rectangle(manager.getProfile().getIntager("simulationSize"),(manager.getProfile().getIntager("simulationSize")));    
        //simBack.setFill(new ImagePattern(backRoundImage));
        simBack.setFill(Color.WHITE);
        simBack.setStroke(Color.BLACK);
        simBack.setTranslateX((int)bounds.getWidth()/4);
        simPane.getChildren().add(simBack); 
        simPaneIsReady = true;

        //dataPane Configuration
        dataPane.add(dataRoundName,0,0);
        dataPane.add(dataDayName, 0, 1);
        dataPane.add(dataTimeName, 0, 2);
        dataPane.add(dataUpdatesName, 0, 3);
        dataPane.add(dataRoundValue,1,0);
        dataPane.add(dataDayValue, 1, 1);
        dataPane.add(dataTimeValue, 1, 2);
        dataPane.add(dataUpdatesValue, 1, 3);

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
        stage.setScene(scene);
        stage.setMaxWidth(4000);
        stage.setMaxHeight(2000);
        //stage.setResizable(false);
        stage.setMaximized(true);
        //stage.setFullScreen(true);
        stage.setFullScreenExitHint(" ");

        updateBodiesPosLoop();
    }

    private Image loadImage(String imageName)  throws IOException {
        FileInputStream inputStream = new FileInputStream(ImageDirectory + imageName + ".jpg");
        Image image = new Image(inputStream);
        return image;
    }

    private void setBodyPos(int x, int y){
        Platform.runLater(() -> {
            ImageView imageView = new ImageView(bodyImage);
            imageView.setTranslateX(x+((int)bounds.getWidth()/4));
            imageView.setTranslateY(y)  ;
            simPane.getChildren().add(imageView);
        });
    }

    private void updateBodiesPosLoop() {
            new Thread(() -> {
            while (true) {
                int xPos;
                int yPos;
                updateRound();
                updateDay();
                updateTime();
                updateUpdates();
                updateBodys();
                Platform.runLater(() -> {
                    simPane.getChildren().clear();
                    simPane.getChildren().add(simBack); 
                });
                if((simPaneIsReady==true)&&(haveBodies==true)){
                    simPaneIsReady = false;
                    for(int i = 0; i < bodies.size(); i++){
                        RobotBody rBody = (RobotBody)bodies.get(i);
                        //if(rBody){      //Überprüfen ob der Roboter noch lebt
                            xPos = rBody.getPosX();
                            yPos = rBody.getPosY();
                            setBodyPos(xPos, yPos);
                        //}
                    }
                    simPaneIsReady = true;
                }
                try {
                    Thread.sleep(1); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

  


    public void updateAllBodyPos(){
        for (Body body : bodies) {
            setBodyPos(body.getPosX(), body.getPosY());
            //System.out.println("PosX: " + body.getPosX() + "  " + "PosY: " + body.getPosY());
        }
    }

    public void updateRound(){
        Platform.runLater(() -> {
            dataRoundValue.setText(String.valueOf(manager.getRound()));
        });
    }

    public void updateDay(){
        Platform.runLater(() -> {
            dataDayValue.setText(String.valueOf(manager.getDay()));
        });
    }

    public void updateTime(){
        Platform.runLater(() -> {
            dataTimeValue.setText(String.valueOf(manager.getTime()));
        });
    }

    public void updateUpdates(){
        Platform.runLater(() -> {
            dataUpdatesValue.setText(String.valueOf(manager.getUpdates()));
        });
    }

    public void updateLightData(){
        lightData = manager.getLightData();
    }

    public void updateBodys(){
        this.bodies = manager.getBodys();
        System.out.println("Bodys: "+manager.getBodys()+"Simpane Bodys: "+bodies);
        haveBodies = true;
    }

}