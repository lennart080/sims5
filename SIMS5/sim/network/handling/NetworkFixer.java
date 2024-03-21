package SIMS5.sim.network.handling;

import java.util.List;

import SIMS5.sim.util.MathUtil;

public class NetworkFixer extends MathUtil {

    public double[][][] neuronFixer(double[][][] pNeurons) {
        for (int i = 0; i < pNeurons.length; i++) {
            if (pNeurons[i].length == 0) {
                List<List<double[]>> listNeurons = convertInToList(pNeurons);
                listNeurons.remove(i);
                pNeurons = covertInToArray(listNeurons);
            }
        }   
        return pNeurons;
    }

    public List<double[]> weightFixer(double[][][] pNeurons, List<double[]> pWeights) {
        for (int i = 0; i < pWeights.size(); i++) {
            boolean id1 = false;
            boolean id2 = false;
            for (int j = 0; j < pNeurons.length; j++) {
                for (int j2 = 0; j2 < pNeurons[j].length; j2++) {
                    if (pNeurons[j][j2][2] == pWeights.get(i)[0]) {
                        id1 = true;
                    }
                    if (pNeurons[j][j2][2] == pWeights.get(i)[1]) {
                        id2 = true;
                    }
                }
            }
            if (id1 != true || id2 != true) {
                pWeights.remove(i);
            }
        }
        for (int j = 0; j < pNeurons.length; j++) {
            for (int j2 = 0; j2 < pNeurons[j].length; j2++) {
                boolean hasOutWeight = false;
                boolean hasInWeight = false;
                for (int i = 0; i < pWeights.size(); i++) {
                    if (pNeurons[j][j2][2] == pWeights.get(i)[0]) {
                        hasInWeight = true;
                    }     
                    if (pNeurons[j][j2][2] == pWeights.get(i)[1]) {
                        hasOutWeight = true;
                    }
                }
                if (hasInWeight == false && j != pNeurons.length-1) {
                    int x = j + 1 + normaliseValue(newRandom(), 1, pNeurons.length-2-j);
                    int y = normaliseValue(newRandom(), 1, pNeurons[x].length);
                    double nId2 = pNeurons[x][y][2]; 
                    double[] weight = {pNeurons[j][j2][2], nId2, newRandom()*2};
                    pWeights.add(weight);
                }
                if (hasOutWeight == false && j != 0) {
                    int x = normaliseValue(newRandom(), 1, j-1);
                    int y = normaliseValue(newRandom(), 1, pNeurons[x].length);
                    double nId2 = pNeurons[x][y][2]; 
                    double[] weight = {nId2, pNeurons[j][j2][2], newRandom()*2};
                    pWeights.add(weight);
                }
            }
        }
        return pWeights;
    }
}
