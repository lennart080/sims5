package SIMS5.sim.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import SIMS5.sim.util.MathUtil;

public class NeuralNetwork extends Mind {

    private double[][][] neurons; 
    private List<double[]> weights;
    protected int simSize;

    public NeuralNetwork(List<double[]> weights, double[][][] neurons, int simSize) {
        super(neurons[neurons.length-1].length);
        this.weights = new ArrayList<>(weights);
        Collections.sort(this.weights, Comparator.comparingDouble(arr -> arr[1]));
        this.neurons = Arrays.copyOf(neurons, neurons.length);
        this.simSize = simSize;
    }

    public double[][][] getNeurons() {
        return neurons;
    }

    public List<double[]> getWeights() {
        return weights;
    }
    
    public void simulate() {
        setInputs();
        calculate();
        formatOutputNeurons();
        setOutputs();
    }

    private void setInputs() {
        for (int i = 0; i < input.length; i++) {
            neurons[0][i][0] = input[i];
        }
    }
   
    private void calculate() {
        int neuronNumber = -1;
        for (int i = 0; i < weights.size(); i++) {
            boolean deleteValue = false;
            if (weights.get(i)[1] > neuronNumber) {
                deleteValue = true;
                neuronNumber = (int)weights.get(i)[1];
            }
            double neuronValue = 0;
            double weightValue = weights.get(i)[2];
            int nId1 = (int)weights.get(i)[0];
            int nId2 = (int)weights.get(i)[1];
            int nOutPosX = 0;
            int nOutPosY = 0;
            for (int j = 0; j < neurons.length; j++) {
                for (int j2 = 0; j2 < neurons[j].length; j2++) {
                    if (neurons[j][j2][2] == nId1) {
                        neuronValue = neurons[j][j2][0];
                    } else if (neurons[j][j2][2] == nId2) {
                        nOutPosX = j;
                        nOutPosY = j2;
                    }
                }
            }
            if (deleteValue == true) {
                neurons[nOutPosX][nOutPosY][0] = neurons[nOutPosX][nOutPosY][1];               
            }
            neurons[nOutPosX][nOutPosY][0] = MathUtil.roundToDecPlaces(neurons[nOutPosX][nOutPosY][0] + (weightValue*neuronValue), 4);
        }
    }  

    private void formatOutputNeurons() {
        double sumExp = 0.0; // softmax funktion for the first 4 outputs (walking)
        for (int i = 0; i < 4; i++) {
            sumExp += Math.exp(neurons[neurons.length-1][i][0]);
        }
        for (int i = 0; i < 4; i++) {
            neurons[neurons.length-1][i][0] = MathUtil.roundToDecPlaces(Math.exp(neurons[neurons.length-1][i][0]) / sumExp, 4);
        }
        double sumExp2 = 0.0; // softmax funktion for the outputs 5 to 9 (upgrades)
        for (int i = 4; i < 9; i++) {
            sumExp2 += Math.exp(neurons[neurons.length-1][i][0]);
        }
        for (int i = 4; i < 9; i++) {
            neurons[neurons.length-1][i][0] = MathUtil.roundToDecPlaces(Math.exp(neurons[neurons.length-1][i][0]) / sumExp2, 4);
        }
        double sumExp3 = 0.0; // softmax funktion for the outputs 10 to 11 (attack)
        for (int i = 9; i < 11; i++) {
            sumExp3 += Math.exp(neurons[neurons.length-1][i][0]);
        }
        for (int i = 9; i < 11; i++) {
            neurons[neurons.length-1][i][0] = MathUtil.roundToDecPlaces(Math.exp(neurons[neurons.length-1][i][0]) / sumExp3, 4);
        }
    }

    private void setOutputs() {
        for (int i = 0; i < output.length; i++) {
            output[i] = neurons[neurons.length-1][i][0];
        }
    }
}
