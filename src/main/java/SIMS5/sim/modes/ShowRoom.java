package SIMS5.sim.modes;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.Manager;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.entitiys.MyEntity;
import SIMS5.sim.entitiys.Robot.Robot;
import SIMS5.sim.entitiys.Robot.RobotBody;
import SIMS5.sim.enviroment.Field;
import SIMS5.sim.enviroment.LightData;
import SIMS5.sim.network.Mind;
import SIMS5.sim.network.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class ShowRoom extends RoundHandler {

    private List<MyEntity> robot;

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
        robot = new ArrayList<>();
        robot.add(entity.get(0));
        int[] pos = field.newPosition(0);
        robot.get(0).setBody(new RobotBody(pos, robotSize, lastPosSize, (Robot) robot.get(0)));
        field.addToField(robot.get(0).getBody());
        robot.get(0).setMind(new NeuralNetwork(network.getWeights(round, entityId), network.getNeurons(round, entityId), simSize));
        runRound(robot);
    }

    public void deleteEntity(MyEntity entity) {
        field.removeFromField(robot.get(0).getBody());
        robot.remove(0);
    }

    @Override
    public List<Body> getBodys() {
        List<Body> body = new ArrayList<>(1);
        body.add(robot.get(0).getBody());
        return body;
    }

    @Override
    public List<Mind> getMinds() {
        List<Mind> mind = new ArrayList<>(1);
        mind.add(robot.get(0).getMind());
        return mind;
    }
}
