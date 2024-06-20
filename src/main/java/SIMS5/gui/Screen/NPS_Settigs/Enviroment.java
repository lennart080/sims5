package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.stage.Stage;

public class Enviroment extends Settings {
    public Enviroment(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("Enviroment");
    }

    @Override
    protected void createLabels(){}
}
