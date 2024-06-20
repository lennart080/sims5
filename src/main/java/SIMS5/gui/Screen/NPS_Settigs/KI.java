package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.stage.Stage;

public class KI extends Settings{
    public KI(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("KI");
    }

    @Override
    protected void createLabels(){}
}
