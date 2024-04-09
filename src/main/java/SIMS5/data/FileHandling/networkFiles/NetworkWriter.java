package SIMS5.data.FileHandling.networkFiles;

import java.io.File;
import java.util.List;

import SIMS5.data.FileHandling.MyFileReader;
import SIMS5.data.FileHandling.MyFileWriter;

public abstract class NetworkWriter extends MyFileWriter implements NetworkData {

    public static boolean createNewNetwork(String name) {
        return createOrdner(networkPath, name);
    }

    public static boolean createNewRound(String name, int round) {
        deleteFile(networkPath, name + File.separator + "round" + round, fileTypeNeurons);
        deleteFile(networkPath, name + File.separator + "round" + round, fileTypeWeights);
        if (createFile(networkPath, name + File.separator + "round" + round, fileTypeWeights)) {
            return createFile(networkPath, name + File.separator + "round" + round, fileTypeNeurons);
        }
        return false;
    }

    public static boolean writeNeurons(String name, int round, int network, double[][][] newNeurons) {
        List<String> neurons = MyFileReader.readFile(networkPath, name + File.separator + "round"+round, fileTypeNeurons);
        neurons.add("n"+network);
        for (int i = 0; i < newNeurons.length; i++) {
            String temp = newNeurons[i][0][1]+"";
            for (int j = 2; j < newNeurons[i][0].length; j++) {
                temp+="$" + newNeurons[i][0][j];
            }
             for (int j = 1; j < newNeurons[i].length; j++) {
                temp+=":" + newNeurons[i][j][1];
                for (int j2 = 2; j2 < newNeurons[i][j].length; j2++) {
                    temp+="$" + newNeurons[i][j][j2];
                }
            }
            neurons.add(temp);
        }
        return MyFileWriter.writeInFile(networkPath, name + File.separator + "round"+round, fileTypeNeurons, neurons);
    }

    public static boolean writeWeights(String name, int round, int network, List<double[]> newWeights) {
        List<String> weights = MyFileReader.readFile(networkPath, name + File.separator + "round" + round, fileTypeWeights);
        weights.add("w"+network);
        String weight = "";
        weight+= newWeights.get(0)[0];
        for (int i = 1; i < newWeights.get(0).length; i++) {
            weight+= "$" + newWeights.get(0)[i];
        }
        for (int i = 1; i < newWeights.size(); i++) {
            weight+= ":" + newWeights.get(i)[0];
            for (int j = 1; j < newWeights.get(i).length; j++) {
                weight+= "$" + newWeights.get(i)[j];
            }
        }
        weights.add(weight);
        return writeInFile(networkPath, name + File.separator + "round" + round, fileTypeWeights, weights);
    }
}
