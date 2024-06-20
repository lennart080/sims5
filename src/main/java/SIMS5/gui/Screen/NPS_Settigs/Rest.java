package SIMS5.gui.Screen.NPS_Settigs;

import SIMS5.data.FileHandling.profileFiles.Profile;
import javafx.stage.Stage;

public class Rest extends Settings{
    public Rest(Stage mainStage,Stage tempStage, Profile profile){
        super(mainStage,tempStage,profile);
        labelTitle.setText("Rest");
    }

    @Override
    protected void createLabels(){}
}
