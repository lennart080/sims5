package SIMS5.sim.modes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import SIMS5.data.FileHandling.networkFiles.Networks;
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
    private List<MyEntity> bestRobots = new ArrayList<>();
    private int simSize;
    private int robotSize;
    private int lastPosSize;
    private int bestEntitySize;
    private double lastManStanding;
    private int entityPerRound;

    public PurAi(Profile profile, LightData light, Field field, Manager manager) {
        super(profile, light, field, manager);
        networkCreator = new NetworkHandler(profile, this);
        mutator = new Mutator(profile, this);
        simSize = profile.getIntager("simulationSize");
        robotSize = profile.getIntager("entitySize");
        lastPosSize = profile.getIntager("entityPosSave");
        bestEntitySize = profile.getIntager("bestEntitySize");
        entityPerRound = profile.getIntager("entitysPerRound");
        lastManStanding = profile.getDouble("lastManStanding");
    }

    public void startRound(List<Robot> entitys, boolean firstRound) {
        robots.clear();
        bestRobots.clear();
        int divider = entitys.size() / bestEntitySize;
        for (int i = 0; i < entitys.size(); i++) {
            robots.add(entitys.get(i));
            int[] pos = field.newPosition(i);
            robots.get(i).setBody(new RobotBody(pos, robotSize, lastPosSize, (Robot) robots.get(i)));
            field.addToField(robots.get(i).getBody());
            NeuronReturner nr;
            if (firstRound) {
                nr = networkCreator.newNetwork();
            } else {
                nr =  mutator.mutate(network.getWeights(round-1, i/divider), network.getNeurons(round-1, i/divider));
            }
            robots.get(i).setMind(new NeuralNetwork(nr.getWeights(), nr.getNeurons(), simSize));
        }
        runRound(robots);
    }

    public void deleteEntity(MyEntity entity) {
        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getSerialNumber() == entity.getSerialNumber()) {
                ((Robot)robots.get(i)).alterScore((int)((entityPerRound-robots.size())*lastManStanding));
                System.out.println(((Robot)robots.get(i)).getScore()); //hrejerjtrhejeje4rjrkterhwe3hjj
                if (bestRobots.size() < bestEntitySize) {
                    bestRobots.add(robots.get(i));
                } else {
                   if (((Robot)bestRobots.get(0)).getScore() < ((Robot)robots.get(i)).getScore()) {
                       bestRobots.remove(0);
                       bestRobots.add(robots.get(i));
                   }
                }
                bestRobots.sort(Comparator.comparingInt(o -> ((Robot) o).getScore()));
                field.removeFromField(robots.get(i).getBody());
                deadBody(robots.get(i).getBody());
                robots.remove(i);
                if (robots.isEmpty()) {
                    for (int j = 0; j < bestRobots.size(); j++) {
                        network.writeNetworkNeurons(round, j, ((NeuralNetwork) bestRobots.get(j).getMind()).getNeurons());
                        network.writeNetworkWeights(round, j, ((NeuralNetwork) bestRobots.get(j).getMind()).getWeights());
                    }
                }
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
