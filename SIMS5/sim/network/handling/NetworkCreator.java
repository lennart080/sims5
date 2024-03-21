package SIMS5.sim.network.handling;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.util.MathUtil;
import SIMS5.sim.util.NeuronReturner;

public class NetworkCreator extends MathUtil {

    private Mutator mutator;
    private NetworkFixer fixer;

    private int[] neuronLayers;
    private int inputNeurons = 12;
    private int outputNeurons = 10;

    public NetworkCreator(Profile profile) {
        double[] temp = profile.getArray("networkStartHiddenLayers");
        neuronLayers = new int[temp.length+2];  
        neuronLayers[0] = inputNeurons;
        for (int i = 0; i < temp.length; i++) {
            neuronLayers[i+1] = (int) temp[i]; 
        }
        neuronLayers[neuronLayers.length-1] = outputNeurons;
        mutator = new Mutator(profile);
        fixer = new NetworkFixer();
    }

    public NeuronReturner newNetwork() {
        double[][][] neurons = new double[neuronLayers.length][][]; 
        List<double[]> weights = new ArrayList<>();
        int nId = 0;
        for (int j = 0; j < neurons.length; j++) {
            neurons[j] = new double[neuronLayers[j]][3];
            for (int j2 = 0; j2 < neurons[j].length; j2++) {
                neurons[j][j2][2] = nId;
                if (j != neurons.length-1 && j != 0) {
                    neurons[j][j2][1] = roundToDecPlaces((newRandom()-0.5)*2, 4);
                } else {
                    neurons[j][j2][1] = 0;
                }
                nId++;
            }
        }
        weights = fixer.weightFixer(neurons, weights);
        return new NeuronReturner(neurons, weights);
    } 

    

}
