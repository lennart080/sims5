package SIMS5.sim.network.handling;


import SIMS5.data.FileHandling.profileFiles.Profile;

public class Mutator {

    private boolean spawnedNeuronsHaveBias;
    private double[][] prbabilityValues = new double[9][3];

    public Mutator(Profile profile) {
        spawnedNeuronsHaveBias = profile.getBoolean("doSpawnedNeuronshaveABias");
        prbabilityValues[0] = profile.getArray("mutationProbabilityWeightDying");
        prbabilityValues[1] = profile.getArray("mutationProbabilityNewWeight");
        prbabilityValues[2] = profile.getArray("mutationProbabilityWeightAjustment");
        prbabilityValues[3] = profile.getArray("mutationProbabilityWeightAjustmentValue");
        prbabilityValues[4] = profile.getArray("mutationProbabilityBiasAjustment");
        prbabilityValues[5] = profile.getArray("mutationProbabilityBiasAjustmentValue");
        prbabilityValues[6] = profile.getArray("mutationProbabilityNewNeuronRow");
        prbabilityValues[7] = profile.getArray("mutationProbabilityNewNeuron");
        prbabilityValues[8] = profile.getArray("mutationProbabilityNeuronDying");
    }

    private double getCDAV(int ajustment, int round) {  //get Current Declining Ajustment value
        double decay = Math.log(prbabilityValues[ajustment][1]/prbabilityValues[0][0])/prbabilityValues[0][2];
        return prbabilityValues[0][0]*Math.pow(Math.E, (decay*round));
    }
}
