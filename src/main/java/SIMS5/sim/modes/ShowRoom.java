package SIMS5.sim.modes;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.Manager;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.entitiys.Robot.Robot;
import SIMS5.sim.enviroment.Field;
import SIMS5.sim.enviroment.LightData;
import SIMS5.sim.network.Mind;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class ShowRoom extends RoundHandler {

    private int simSize;
    private int robotSize;
    private int lastPosSize;

    public ShowRoom(Profile profile, LightData lightdata, Field field, Manager manager) {
        super(profile, lightdata, field, manager);
        simSize = profile.getIntager("simulationSize");
        robotSize = profile.getIntager("entitySize");
        lastPosSize = profile.getIntager("entityPosSave");
    }

    public void startShowRoom(List<Robot> entity, int round, int entityId) {
        network.getNeurons(round, entityId);
        network.getWeights(round, entityId);

    }

    @Override
    public List<Body> getBodys() {
        return List.of();
    }

    @Override
    public List<Mind> getMinds() {
        return List.of();
    }
}
