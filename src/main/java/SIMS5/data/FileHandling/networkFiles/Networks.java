package SIMS5.data.FileHandling.networkFiles;

import java.util.List;

import SIMS5.data.FileHandling.profileFiles.Profile;

public class Networks {
    private String name; 

    public Networks(Profile profile) {
        name = profile.getName();
        if (!NetworkReader.checkIfNetworkExists(name)) {
            NetworkWriter.createNewNetwork(name);
        }
        NetworkWriter.createNewRound(name, 0);
    }

    public void writeNetworkNeurons(int round, int network, double[][][] neurons) {
        if (!NetworkReader.checkIfRoundExists(name, round)) {
            NetworkWriter.createNewRound(name, round);
        }
        NetworkWriter.writeNeurons(name, round, network, neurons);
    }

    public double[][][] getNeurons(int round, int network) {
        return NetworkReader.getNeurons(name, round, network);
    }

    public List<double[]> getWeights(int round, int network) {
        return NetworkReader.getWeights(name, round, network);
    }

    public void writeNetworkWeights(int round, int network, List<double[]> weights) {
        if (!NetworkReader.checkIfRoundExists(name, round)) {
            NetworkWriter.createNewRound(name, round);
        }
        NetworkWriter.writeWeights(name, round, network, weights);
    }

    public static int getLastRound(String name) {
        boolean exists = true;
        int count = 0;
        while (exists) {
            exists = NetworkReader.checkIfRoundExists(name, count);
            count++;
        }
        return count-1;
    }
}
