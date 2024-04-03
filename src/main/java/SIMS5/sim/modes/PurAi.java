package SIMS5.sim.modes;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.entitiys.Robot.Robot;
import SIMS5.sim.entitiys.Robot.RobotBody;
import SIMS5.sim.enviroment.LightData;
import SIMS5.sim.network.NeuralNetwork;
import SIMS5.sim.util.NeuronReturner;

public class PurAi extends RoundHandler {

    private List<Robot> robots = new ArrayList<>();

    public PurAi(Profile profile, LightData light) {
        super(profile, light);
    }
    
    public void startFirstRound(List<Robot> entitys) {
        robots.clear();
        for (int i = 0; i < entitys.size(); i++) {
            robots.add(entitys.get(i));
            int[] pos = field.newPosition(i);
            robots.get(i).setBody(new RobotBody(pos));
            NeuronReturner nr = networkCreator.newNetwork();
            network.writeNetworkNeurons(0, i, nr.getNeurons());
            test(nr.getNeurons());
            robots.get(i).setMind(new NeuralNetwork(nr.getWeights(), nr.getNeurons()));
        }
        while (robots.size() != 0) {
            simulate();
        }
    }

    private void test(double[][][] neurons) {
        for (int i = 0; i < neurons.length; i++) {
            for (int j = 0; j < neurons[i].length; j++) {
                for (int j2 = 0; j2 < neurons[i][j].length; j2++) {
                    System.out.print(neurons[i][j][j2] + " : ");
                }
                System.out.println("");
            }
        }
    }

    public void startRound(List<Robot> entitys) {
        robots.clear();
        for (int i = 0; i < entitys.size(); i++) {
            robots.add(entitys.get(i));
            int[] pos = field.newPosition(i);
            robots.get(i).setBody(new RobotBody(pos));
            robots.get(i).setMind(new NeuralNetwork(null, null));
        }
        while (robots.size() != 0) {
            simulate();
        }
    }

    private void simulate() {
        for (int i = 0; i < robots.size(); i++) {
            robots.get(i).simulate();
        }
    }
}
