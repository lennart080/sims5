package SIMS5.data.FileHandling.profileFiles;

import SIMS5.data.FileHandling.FileClass;

import java.util.ArrayList;
import java.util.List;

public class Profile implements ProfileData {

    private String name;
    private List<String[]> profile = new ArrayList<>();

    public Profile(String name) {
        this.name = name;
        if (!ProfileReader.checkIfProfileExists(name)) {
            ProfileWriter.createNewProfile(name);
        }
        reloadProfile();
    }

    public String[] getAllProfiles() {
        return ProfileReader.getAllProfiles();
    }

    public void reloadProfile() {
        profile.clear();
        profile = ProfileReader.getprofile(name);
      }

    public void set(String atribute, double value) {
        String[] x = {""+value};
        ProfileWriter.writeInProfile(name, atribute, x);
        reloadProfile();
    }
  
    public void set(String atribute, boolean value) {
        String[] x = {""+value};
        ProfileWriter.writeInProfile(name, atribute, x);
        reloadProfile();
    }
  
    public void set(String atribute, double[] value) {
        String[] x = new String[value.length];
        for (int i = 0; i < value.length; i++) {
            x[i] = Double.toString(value[i]);
        }
        ProfileWriter.writeInProfile(name, atribute, x);
        reloadProfile();
    }

    public double getDouble(String atribute) {
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals(atribute) && profile.get(i)[1].equals("1")) {
                return Double.parseDouble(profile.get(i)[2]);
            }
        }
        return -1.0;
    }

    public int getIntager(String atribute) {
        return (int) getDouble(atribute);
    }

    public boolean getBoolean(String setting) {
        boolean value;
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals(setting) && profile.get(i)[1].equals("1")) {
                value = Boolean.parseBoolean(profile.get(i)[2]);
                return value;
            }
        }
        return false;
    }

    public double[] getArray(String setting) {
        List<Double> value = new ArrayList<>();
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals(setting)) {
                for (int j = 2; j < Integer.parseInt(profile.get(i)[1])+2; j++) {
                    value.add(Double.parseDouble(profile.get(i)[j]));
                }
                double[] x = new double[value.size()];
                for (int j = 0; j < value.size(); j++) {
                    x[j] = value.get(j);
                }
                return x;
            }
        }
        double[] v = {};
        return v;
    }

    public List<String[]> getAllAtributs() {
        return ProfileReader.getprofile(name);
    }

    public void deleteProfile() {
        FileClass.deleteFile(userProfilePath, name, fileType);
    }

    public String getName() {
        return name;
    }
}
