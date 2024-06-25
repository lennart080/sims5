package SIMS5.gui.Screen;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Grafik.ImageDirecory;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.enviroment.LightData;
import SIMS5.gui.GuiManager;
import SIMS5.sim.util.MathUtil;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class SimulationScreen implements ImageDirecory{

    // Objekte:
    private GuiManager manager;   
    private Scene scene;

    private AnimationTimer timer;

    private HBox pane = new HBox();
    private StackPane simPane = new StackPane();
    private GridPane dataPane = new GridPane();

    private Profile profile;
    private Image bodyImage;
    private Image backRoundImage;
    private LightData lightData;
    private List<Body> bodies;
    private Rectangle2D bounds;
    private boolean endMode = false;

    private double simFiledX;
    private double simFiledY;
    private int simSize;
    private double guiSimSize;

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

    private long timeforFrame = 0;

    public SimulationScreen(Stage stage, GuiManager manager){
        this.manager = manager;
        this.profile = manager.getProfile();
        this.bodies = new ArrayList<>(profile.getIntager("entitysPerRound"));
        simSize = profile.getIntager("simulationSize");
        bodyImage = loadImage("Entity2");
        Screen primaryScreen = Screen.getPrimary();
        this.bounds = primaryScreen.getVisualBounds();
        guiSimSize = this.bounds.getHeight()-50;
        lightData = manager.getLightData();

        //pane Configuration
        pane.getChildren().addAll(dataPane,simPane);
        //pane.setTop(graphPane);
        dataPane.setMinWidth(100);
        
        //graphPane Configuration
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("day");
        yAxis.setLabel("LightIntensity");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Light Intensity Over the Day");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i = 0; i < lightData.getLightOfDay(manager.getDay()).length; i++) {
            double lightIntensity = lightData.getLightIntensityAtTime(i);
            series.getData().add(new XYChart.Data<>(i, lightIntensity));
        }

        lineChart.getData().add(series);

        //simPaneConfiguration
        simPane.setAlignment(Pos.TOP_LEFT);
        
        simBack = new Rectangle(manager.getProfile().getIntager("simulationSize"),(manager.getProfile().getIntager("simulationSize")));    
        //simBack.setFill(new ImagePattern(backRoundImage));
        simBack.setFill(Color.WHITE);
        simBack.setStroke(Color.BLACK);
        simBack.setTranslateX((int)bounds.getWidth()/4);
        simPane.getChildren().add(simBack); 

        //dataPane Configuration
        dataPane.add(lineChart, 0, 0);
        dataPane.add(dataRoundName,0,1);
        dataPane.add(dataDayName, 0, 2);
        dataPane.add(dataTimeName, 0, 3);
        dataPane.add(dataUpdatesName, 0, 4);
        dataPane.add(dataRoundValue,1,1);
        dataPane.add(dataDayValue, 1, 2);
        dataPane.add(dataTimeValue, 1, 3);
        dataPane.add(dataUpdatesValue, 1, 4);

        // Scene Configuration
        scene = new Scene(pane,0,0);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    Stage tempStage = new Stage();
                    new MenueScreen(manager,tempStage,stage);
                }
            }
        });

        // Stage Configuration
        stage.setScene(scene);
        stage.setMaxWidth(4000);
        stage.setMaxHeight(2000);
        stage.setMaximized(true);
        stage.setFullScreenExitHint(" ");
        stage.setOnCloseRequest(event -> handelWindowOnClose(event));

        runRounds();
    }

    private Image loadImage(String imageName) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(ImageDirectory + imageName + ".jpg");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Image(inputStream);
    }

    private void runRounds() {
        while (!manager.getReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        startRound();
    }

    private void startRound(){
        updateRound();
        updateLightData();
        updateBodys();
        updateSimPane();
    }

    private void updateSimPane(){
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!endMode){
                    updateDay();
                    updateTime();
                    updateUpdates();

                    if(bodies!=null){
                        simPane.getChildren().clear();
                        for (Body body : bodies) {
                            ImageView imageView = new ImageView(bodyImage);
                            simPane.getChildren().add(setPos(imageView, body.getPosX(), body.getPosY()));
                        }
                    } else {
                        stopTimer();
                    }
                }
            }
        };
        timer.start();
    }

    private void stopTimer() {
        timer.stop();
        runRounds();
    }

    private ImageView setPos(ImageView imageView, int x, int y) {
        imageView.setTranslateX(MathUtil.normaliseValue(x, simSize, (int)guiSimSize));
        imageView.setTranslateY(MathUtil.normaliseValue(y, simSize, (int)guiSimSize));
        return imageView;
    }

    private void updateRound(){
        Platform.runLater(() -> {
            dataRoundValue.setText(String.valueOf(manager.getRound()));
        });
    }

    private void updateDay(){
        Platform.runLater(() -> {
            dataDayValue.setText(String.valueOf(manager.getDay()));
        });
    }

    private void updateTime(){
        Platform.runLater(() -> {
            dataTimeValue.setText(String.valueOf(manager.getTime()));
        });
    }

    private void updateUpdates(){
        Platform.runLater(() -> {
            dataUpdatesValue.setText(String.valueOf(manager.getUpdates()));
        });
    }

    private void updateLightData(){
        lightData = manager.getLightData();
    }

    private void updateBodys(){
        if (bodies != null) {
            this.bodies.clear();
        }
        this.bodies = manager.getBodys();
    }

    private void handelWindowOnClose(WindowEvent event) {
        manager.closeCall();
    }
}