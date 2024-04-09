package SIMS5.sim.modes;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.networkFiles.Networks;
import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.entitiys.Body;
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
    private List<Robot> robots = new ArrayList<>();
    private int simSize;
    private int robotSize;
    private int lastPosSize;

    public PurAi(Profile profile, LightData light, Field field) {
        super(profile, light, field);
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
            robots.get(i).setBody(new RobotBody(pos, robotSize, lastPosSize, robots.get(i)));
            field.addToField(robots.get(i).getBody());
            NeuronReturner nr = networkCreator.newNetwork();
            network.writeNetworkNeurons(0, i, nr.getNeurons());
            network.writeNetworkWeights(0, i, nr.getWeights());
            robots.get(i).setMind(new NeuralNetwork(nr.getWeights(), nr.getNeurons(), simSize));
        }
        runRound();
    }

    public void startRound(List<Robot> entitys) {
        robots.clear();
        for (int i = 0; i < entitys.size(); i++) {
            robots.add(entitys.get(i));
            int[] pos = field.newPosition(i);
            robots.get(i).setBody(new RobotBody(pos, robotSize, lastPosSize, robots.get(i)));
            field.addToField(robots.get(i).getBody());
            NeuronReturner nr =  mutator.mutate(network.getWeights(round-1, i), network.getNeurons(round-1, i));
            network.writeNetworkNeurons(round, i, nr.getNeurons());
            network.writeNetworkWeights(round, i, nr.getWeights());
            robots.get(i).setMind(new NeuralNetwork(nr.getWeights(), nr.getNeurons(), simSize));
        }
        runRound();
    }

    private void runRound() {
        updates = 0;
        time = 0;
        day = 0;
        while (robots.size() != 0) {
            simulate();
            int temp = day;
            updatesAndSleepHandling();
            if (day > temp) {
                for (int i = 0; i < robots.size(); i++) {
                    robots.get(i).setDay(day);
                }
            }
        }
        round++;
    }

    private void simulate() {
        for (int i = 0; i < robots.size(); i++) {
            robots.get(i).simulate(light.getLightIntensityAtTime(updates));
        }
    }

    public void deleteRobo(Robot robot) {
        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getSerialNumber() == robot.getSerialNumber()) {
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
}
