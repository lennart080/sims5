package SIMS5.data.FileHandling.networkFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.MyFileReader;
import SIMS5.sim.util.MathUtil;

public abstract class NetworkReader extends MyFileReader implements NetworkData {

    public static boolean checkIfRoundExists(String name, int round) {
        if (checkIfFileExists(networkPath, name + File.separator + "round" + round, fileTypeWeights)) {
            return checkIfFileExists(networkPath, name + File.separator + "round" + round, fileTypeNeurons);    
        }
        return false;
    }

    public static boolean checkIfNetworkExists(String name) {
        return checkIfFileExists(networkPath, name, "");
    }

    private static List<String> readNetworkFromRound(String name, int round, int network, String fileType, String nameType) {
        List<String> roundStrings = readFile(networkPath, name + File.separator + "round" + round, fileType);
        List<String> networkStrings = new ArrayList<>();
        int start = 0 ,end = roundStrings.size();
        for (int i = 0; i < roundStrings.size(); i++) {
            if (roundStrings.get(i).equals(nameType + network)) {
                start = i;
            } else if (roundStrings.get(i).equals(nameType + (network+1))) {
                end = i;
                break;
            }
        }
        for (int i = (start+1); i < end; i++) {
            networkStrings.add(roundStrings.get(i));
        }
        return networkStrings;
    }

    public static double[][][] getNeurons(String name, int round, int network) {
        List<List<double[]>> neurons = new ArrayList<>();
        List<String> networkStrings = readNetworkFromRound(name, round, network, fileTypeNeurons, nameTypeNeurons);
        for (int i = 0; i < networkStrings.size(); i++) {
            List<double[]> temp = new ArrayList<>();
            neurons.add(temp);
            String[] values = networkStrings.get(i).split(":");
            for (int j = 0; j < values.length; j++) {
                String[] neuron = values[j].split("\\$");
                double[] doubleneuron = new double[neuron.length + 1];
                doubleneuron[0] = 0.0;
                for (int k = 0; k < neuron.length; k++) {
                    try {
                    doubleneuron[k+1] = Double.parseDouble(neuron[k]);
                    } catch (Exception e) {
                        System.out.println(neuron[k]);
                    }
                }
                neurons.get(i).add(doubleneuron);
            } 
        }
        return MathUtil.covertInToArray(neurons);
    }

    public static List<double[]> getWeights(String name, int round, int network) {
        List<double[]> formatedWeights = new ArrayList<>();
        List<String> networkStrings = readNetworkFromRound(name, round, network, fileTypeWeights, nameTypeWeights);
        if (networkStrings.size() == 1) {
            String[] splitedList = networkStrings.get(0).split(":");
            for (int i = 0; i < splitedList.length; i++) {
                String[] tempString = splitedList[i].split("\\$");
                double[] oneWeight = new double[tempString.length];
                for (int j = 0; j < tempString.length; j++) {
                    oneWeight[j] = Double.parseDouble(tempString[j]);
                }
                formatedWeights.add(oneWeight);
            }
        } 
        return formatedWeights;
    }
}
