package SIMS5.sim.modes;

import java.util.ArrayList;
import java.util.List;

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
import SIMS5.sim.network.handling.Mutator;
import SIMS5.sim.network.handling.NetworkHandler;
import SIMS5.sim.util.NeuronReturner;

public class PurAi extends RoundHandler {

    private NetworkHandler networkCreator;
    private Mutator mutator;
    private List<MyEntity> robots = new ArrayList<>();
    private int simSize;
    private int robotSize;
    private int lastPosSize;

    public PurAi(Profile profile, LightData light, Field field, Manager manager) {
        super(profile, light, field, manager);
        networkCreator = new NetworkHandler(profile, this);
        mutator = new Mutator(profile, this);
        simSize = profile.getIntager("simulationSize");
        robotSize = profile.getIntager("entitySize");
        lastPosSize = profile.getIntager("entityPosSave");
    }
    
    public void startFirstRound(List<Robot> entitys) {
        robots.clear();
        for (int i = 0; i < entitys.size(); i++) {
            robots.add(entitys.get(i));
            int[] pos = field.newPosition(i);
            robots.get(i).setBody(new RobotBody(pos, robotSize, lastPosSize, (Robot) robots.get(i)));
            field.addToField(robots.get(i).getBody());
            NeuronReturner nr = networkCreator.newNetwork();
            network.writeNetworkNeurons(0, i, nr.getNeurons());
            network.writeNetworkWeights(0, i, nr.getWeights());
            robots.get(i).setMind(new NeuralNetwork(nr.getWeights(), nr.getNeurons(), simSize));
        }
        runRound(robots);
    }

    public void startRound(List<Robot> entitys) {
        robots.clear();
        for (int i = 0; i < entitys.size(); i++) {
            robots.add(entitys.get(i));
            int[] pos = field.newPosition(i);
            robots.get(i).setBody(new RobotBody(pos, robotSize, lastPosSize, (Robot) robots.get(i)));
            field.addToField(robots.get(i).getBody());
            NeuronReturner nr =  mutator.mutate(network.getWeights(round-1, i), network.getNeurons(round-1, i));
            network.writeNetworkNeurons(round, i, nr.getNeurons());
            network.writeNetworkWeights(round, i, nr.getWeights());
            robots.get(i).setMind(new NeuralNetwork(nr.getWeights(), nr.getNeurons(), simSize));
        }
        runRound(robots);
    }

    public void deleteEntity(MyEntity entity) {
        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getSerialNumber() == entity.getSerialNumber()) {
                field.removeFromField(robots.get(i).getBody());
                robots.remove(i);
                return;
            }
        }
    }

    @Override
    public List<Body> getBodys() {
        List<Body> bodys = new ArrayList<>();
        for (int i = 0; i < robots.size(); i++) {
            bodys.add(robots.get(i).getBody());
        }
        return bodys;
    }

    @Override
    public List<Mind> getMinds() {
        List<Mind> minds = new ArrayList<>();
        for (int i = 0; i < robots.size(); i++) {
            minds.add(robots.get(i).getMind());
        }
        return minds;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
