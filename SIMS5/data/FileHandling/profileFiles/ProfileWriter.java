package SIMS5.data.FileHandling.profileFiles;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.MyFileReader;
import SIMS5.data.FileHandling.MyFileWriter;

public final class ProfileWriter extends MyFileWriter implements ProfileData {
    
    public static boolean createNewProfile(String name) {
        createFile(userProfilePath, name, fileType);
        writeInFile(userProfilePath, name, fileType, MyFileReader.readFile(defaultProfilePath, "defaultProfile", fileType));
        return true;
    }

    public static boolean writeInProfile(String name, String atribute, String[] value) {
        List<String[]> profile = ProfileReader.getprofile(name);
        int pos = atributPosFinder(profile, atribute);
        if (pos == -1) {
            return false;
        }
        String[] newline = new String[2+value.length];
        newline[0] = atribute;
        newline[1] = value.length+"";
        for (int i = 0; i < value.length; i++) {
            newline[2+i] = value[i];
        }
        profile.set(pos, newline);
        List<String> fileStrings = new ArrayList<>();
        for (int i = 0; i < profile.size(); i++) {
            String temp = profile.get(i)[0];
            for (int j = 1; j < profile.get(i).length; j++) {
                temp+=":"+profile.get(i)[j];
            }
            fileStrings.add(temp);
        }
        return writeInFile(userProfilePath, name, fileType, fileStrings);
    }

    private static int atributPosFinder(List<String[]> profile, String atribute) {
        int pos = -1;
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals(atribute)) {
                pos = i;
            }
        }
        return pos;
    }
}
