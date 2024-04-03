package SIMS5.data.FileHandling.profileFiles;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.FileClass;
import SIMS5.data.FileHandling.MyFileReader;

public final class ProfileReader extends MyFileReader implements ProfileData {

    public static boolean checkIfProfileExists(String name) {
        return checkIfFileExists(userProfilePath, name, fileType);
    }

    public static List<String[]> getprofile(String name) {
        List<String[]> sortedFileStrings = new ArrayList<>();
        List<String> fileStrings = MyFileReader.readFile(userProfilePath, name, fileType);
        for (int i = 0; i < fileStrings.size(); i++) {
            String[] values = fileStrings.get(i).replaceAll(":$", "").split(":");
            sortedFileStrings.add(values);
        }
        return sortedFileStrings;
    }

    public static String[] getAllProfiles() {
        return FileClass.getAllFiles(userProfilePath);
    }
}
