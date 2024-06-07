package SIMS5.gui.Screen;

import SIMS5.gui.GuiManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// Ideen
/*
    passwordField.setPromptText("Your password"); //erstzen


 */


public class NewProfileScreen {

    // Objekte:
    private GuiManager manager;
    private Stage stage;
    private Scene scene;
    private VBox pane = new VBox();
    private VBox boxName = new VBox();
    private VBox boxPw1 = new VBox();
    private VBox boxPw2 = new VBox();

    //Componente:
    private Label labelCreateProfile = new Label("Create Profile");
    private Label labelName = new Label("Name*");
    private Label labelpw1 = new Label("Password");
    private Label labelpw2 = new Label("Repeat Password");
    private TextField inputName = new TextField("");
    private PasswordField passwordInput1 = new PasswordField();
    private PasswordField passwordInput2 = new PasswordField();
    private Button buttonCreate = new Button("Create new Profile");

    public NewProfileScreen(Stage stage,GuiManager manager){
        this.manager = manager;
        this.stage = stage;

        // VBox Configuration

        // pane
        pane.getChildren().addAll(labelCreateProfile,boxName,boxPw1,boxPw2,buttonCreate);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(30,30,30,30));
        pane.setSpacing(40);

        //boxName

        boxName.getChildren().addAll(labelName,inputName);

        //boxPw1

        boxPw1.getChildren().addAll(labelpw1,passwordInput1);

        //boxPw2

        boxPw2.getChildren().addAll(labelpw2,passwordInput2);

        //Componente:

        //Label: labelCreateProfile
        labelCreateProfile.setFont(Font.font("Stencil", FontWeight.MEDIUM,38));

        //Label: labelName
        labelName.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));

        //Label: labelpw1
        labelpw1.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));

        //Label: labelpw2
        labelpw2.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));

        //Button: buttonCreate
        buttonCreate.setFont(Font.font("Stencil", FontWeight.MEDIUM,21));
        buttonCreate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String pw1 = passwordInput1.getText();
                String pw2 = passwordInput2.getText();
                if (inputName.getText().isEmpty()) {
                    labelName.setTextFill(Color.RED);
                    labelName.setText("Name* - Please enter Name");
                }
                else {
                    if(passwordInput1.getText().isEmpty() & passwordInput2.getText().isEmpty()){
                        manager.createProfile(inputName.getText());
                        manager.startSimulation(inputName.getText());
                        new SimulationScreen(stage,manager);
                    }
                    if(pw1.equals(pw2)){
                        manager.createProfile(inputName.getText(),pw1);
                        manager.startSimulation(inputName.getText());
                        new SimulationScreen(stage,manager);
                    }
                    else{
                        labelpw2.setTextFill(Color.RED);
                        labelpw2.setText("Repeat Password - Password does not match");
                    }
                }
            }
        });

        // Scene Configuration

        scene = new Scene(pane,750,500);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ESCAPE){
                    new StartScreen(stage,manager);
                }
            }
        });


        // Stage Configuration

        stage.setTitle("NewProfileScreen");
        stage.setScene(scene);
    }

}