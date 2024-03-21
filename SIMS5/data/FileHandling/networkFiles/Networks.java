package SIMS5.data.FileHandling.networkFiles;

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
}
