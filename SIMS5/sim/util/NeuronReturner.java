package SIMS5.sim.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NeuronReturner {

    private double[][][] neurons;
    private List<double[]> weights;

    public NeuronReturner(double[][][] pNeurons, List<double[]> pWeights) {
      this.neurons = Arrays.copyOf(pNeurons, pNeurons.length);
      this.weights = new ArrayList<>(pWeights);
    }

    public double[][][] getNeurons() {
        return neurons;
    }

    public List<double[]> getWeights() {
        return weights;
    }
}
