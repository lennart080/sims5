package SIMS5.sim.network.handling;

import java.util.List;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.modes.RoundHandler;
import SIMS5.sim.util.MathUtil;
import SIMS5.sim.util.NeuronReturner;

public class Mutator extends MathUtil {

    private RoundHandler roundHandler;
    private NetworkFixer fixer;
    private boolean spawnedNeuronsHaveBias;
    private double[][] prbabilityValues = new double[9][3];

    public Mutator(Profile profile, RoundHandler roundHandler) {
        this.roundHandler = roundHandler;
        fixer = new NetworkFixer();
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

    private double currentAjustmentValue(int ajustment) { // get Current Declining Ajustment value
        double decay = Math.log(prbabilityValues[ajustment][1] / prbabilityValues[0][0]) / prbabilityValues[0][2];
        return prbabilityValues[0][0] * Math.pow(Math.E, (decay * roundHandler.getRound()));
    }

    public NeuronReturner mutate(List<double[]> weights, double[][][] neurons) {
        weights = ajustWeights(weights);
        weights = deleteWeights(weights, neurons);
        weights = newWeights(weights, neurons);
        neurons = ajustBias(neurons);
        NeuronReturner nr = deleteNeurons(weights, neurons);
        neurons = nr.getNeurons();
        weights = nr.getWeights();
        nr = newNeurons(weights, neurons);
        neurons = nr.getNeurons();
        weights = nr.getWeights();
        return new NeuronReturner(neurons, weights);
    }

    private NeuronReturner newNeurons(List<double[]> weights, double[][][] neurons) {
        if (newRandom() < currentAjustmentValue(7)) {
            if (newRandom() < currentAjustmentValue(6)) {
                // new row not done
            } else {
                int row;
                if (neurons.length == 3) {
                    row = 1;
                } else {
                    row = 1 + normaliseValue(newRandom(), 1, neurons.length - 3);
                }
                List<List<double[]>> listNeurons = convertInToList(neurons);
                double[] neuron = { 0.0, 0.0, getFreeNId(neurons) };
                if (spawnedNeuronsHaveBias) {
                    neuron[1] = (newRandom() - 0.5) * 2;
                }
                listNeurons.get(row).add(neuron);
                neurons = covertInToArray(listNeurons);
                weights = fixer.weightFixer(neurons, weights);
            }
        }
        return new NeuronReturner(neurons, weights);
    }

    private List<double[]> ajustWeights(List<double[]> weights) {
        for (int i = 0; i < weights.size(); i++) {
            if (newRandom() < currentAjustmentValue(2)) {
                double[] ajustedWeight = {weights.get(i)[0], weights.get(i)[1], weights.get(i)[2] + (((newRandom() - 0.5) * 2) * currentAjustmentValue(3))};
                weights.set(i, ajustedWeight);
            }
        }
        return weights;
    }

    private List<double[]> deleteWeights(List<double[]> weights, double[][][] neurons) {
        for (int i = 0; i < weights.size(); i++) {
            if (newRandom() < currentAjustmentValue(0)) {
                weights.remove(i);
                weights = fixer.weightFixer(neurons, weights);
            }
        }
        return weights;
    }

    private List<double[]> newWeights(List<double[]> weights, double[][][] neurons) {
        for (int i = 0; i < neurons.length - 1; i++) {
            for (int j = 0; j < neurons[i].length; j++) {
                if (newRandom() < currentAjustmentValue(1)) {
                    int x = i + 1 + normaliseValue(newRandom(), 1, neurons.length - 2 - i);
                    int y = normaliseValue(newRandom(), 1, neurons[x].length);
                    double[] w = {neurons[i][j][2], neurons[x][y][2], (newRandom() - 0.5) * 2};
                    weights.add(w);
                }
            }
        }
        return weights;
    }

    private double[][][] ajustBias(double[][][] neurons) {
        for (int i = 1; i < neurons.length - 1; i++) {
            for (int j = 0; j < neurons[i].length; j++) {
                if (newRandom() < currentAjustmentValue(4)) {
                    neurons[i][j][1] = roundToDecPlaces(neurons[i][j][1] + ((newRandom() - 0.5) * 2 * currentAjustmentValue(5)), 4);
                }
            }
        }
        return neurons;
    }

    private NeuronReturner deleteNeurons(List<double[]> weights, double[][][] neurons) {
        if (newRandom() < currentAjustmentValue(8)) {
            int row;
            if (neurons.length == 3) {
                row = 1;
            } else {
                row = 1 + normaliseValue(newRandom(), 1, neurons.length - 3);
            }
            int y = normaliseValue(newRandom(), 1, neurons[row].length - 1);
            List<List<double[]>> listNeurons = convertInToList(neurons);
            listNeurons.get(row).remove(y);
            neurons = fixer.neuronFixer(covertInToArray(listNeurons));
            weights = fixer.weightFixer(neurons, weights);
        }
        return new NeuronReturner(neurons, weights);
    }

    private int getFreeNId(double[][][] pNeurons) {
        int freeNId = 0;
        for (int i = 0; i < pNeurons.length; i++) {
            for (int j = 0; j < pNeurons[i].length; j++) {
                if (freeNId < pNeurons[i][j][2]) {
                    freeNId = (int) pNeurons[i][j][2];
                }
            }
        }
        return freeNId + 1;
    }
}
