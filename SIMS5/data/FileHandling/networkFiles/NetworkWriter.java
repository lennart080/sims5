package SIMS5.data.FileHandling.networkFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.MyFileWriter;

public class NetworkWriter extends MyFileWriter implements NetworkData {

    public static boolean createNewNetwork(String name) {
        return createOrdner(networkPath, name);
    }

    public static boolean createNewRound(String name, int round) {
        return createFile(networkPath, name + File.separator + "round" + round, fileType);
    }

    public static boolean writeNeurons(String name, int round, int network, double[][][] neurons) {
        List<String> fileStrings = new ArrayList<>();
        fileStrings.add("n" + round);
        for (int i = 0; i < neurons.length; i++) {
            String temp = neurons[i][0][1]+"";
            for (int j = 2; j < neurons[i][0].length; j++) {
                temp+="$" + neurons[i][0][j];
            }
            for (int j = 1; j < neurons[i].length; j++) {
                temp+=":" + neurons[i][j][1];
                for (int j2 = 2; j2 < neurons[i][j].length; j2++) {
                    temp+="$" + neurons[i][j][j2];
                }
            }
            fileStrings.add(temp);
        }
        writeInFile(networkPath, name + File.separator + "round" + round, fileType, fileStrings);
        return true;
    }
}
