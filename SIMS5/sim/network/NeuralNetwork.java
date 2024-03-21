package SIMS5.sim.network;

import java.util.List;

public class NeuralNetwork extends Mind {

    protected double[][][] neurons; 
    protected List<double[]> weights;

    public NeuralNetwork(List<double[]> weights, double[][][] neurons) {
        this.weights = weights;
        this.neurons = neurons;
    }

    public double[][][] getNeurons() {
        return neurons;
    }

    public List<double[]> getWeights() {
        return weights;
    }
    
}
