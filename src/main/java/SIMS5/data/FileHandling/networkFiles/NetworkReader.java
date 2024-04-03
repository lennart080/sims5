package SIMS5.data.FileHandling.networkFiles;

import java.io.File;

import SIMS5.data.FileHandling.MyFileReader;

public class NetworkReader extends MyFileReader implements NetworkData {

    public static boolean checkIfRoundExists(String name, int round) {
        return checkIfFileExists(networkPath, name + File.separator + "round" + round, "");
    }

    public static boolean checkIfNetworkExists(String name) {
        return checkIfFileExists(networkPath, name, "");
    }
}
