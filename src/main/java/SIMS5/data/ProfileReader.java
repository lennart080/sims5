package SIMS5.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileReader {
    private static String runnVerzeichnis = System.getProperty("user.dir");
    private static String profilePath = runnVerzeichnis + File.separator + "SIMS5" + File.separator + "data" + File.separator + "profiles" + File.separator;
    private static String defaultPath = runnVerzeichnis + File.separator + "SIMS5" + File.separator + "data" + File.separator + "default" + File.separator;
    private static List<String[]> profile = new ArrayList<>();
    private static List<String[]> defaultProfile = new ArrayList<>();

    public static void loadProfile(String profileName) {
      profile.clear();
      profile = readFile(profilePath, profileName);
    }

    public static void loadDefaultProfile() {
        defaultProfile.clear();
        defaultProfile = readFile(defaultPath, "defaultProfile");
    }

    public static List<String[]> getprofile(String profileName) {
      return readFile(profilePath, profileName);
    }

    public static List<String[]> getDefaultprofile() {
        return readFile(defaultPath, "defaultProfile");
      }

    private static List<String[]> readFile(String path, String name) {
        List<String[]> fileStrings = new ArrayList<>();
        try {
            File txtDatei = new File(path, name + ".p");
            BufferedReader reader = new BufferedReader(new FileReader(txtDatei));
            String line;
            while ((line = reader.readLine()) != null) { 
              String[] values = line.replaceAll(":$", "").split(":");
              fileStrings.add(values);
            }
            reader.close();
          } catch(Exception e) {
            System.out.println(e);
          }
          return fileStrings;
    }

    public static double getDoubleSettings(String setting) {
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals(setting) && profile.get(i)[1].equals("1")) {
                return Double.parseDouble(profile.get(i)[2]);
            }
        }
        return -1.0;
    }

    public static double getDefaultDoubleSettings(String setting) {
        for (int i = 0; i < defaultProfile.size(); i++) {
            if (defaultProfile.get(i)[0].equals(setting) && profile.get(i)[1].equals("1")) {
                return Double.parseDouble(defaultProfile.get(i)[2]);
            }
        }
        return -1.0;
    }

    @SuppressWarnings("null")
    public static boolean getBooleanSettings(String setting) {
        boolean value;
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals(setting) && profile.get(i)[1].equals("1")) {
                value = Boolean.parseBoolean(profile.get(i)[2]);
                return value;
            }
        }
        return (Boolean) null;
    }

    @SuppressWarnings("null")
    public static boolean getDefaultProfileBooleanSettings(String setting) {
        boolean value;
        for (int i = 0; i < defaultProfile.size(); i++) {
            if (defaultProfile.get(i)[0].equals(setting) && profile.get(i)[1].equals("1")) {
                value = Boolean.parseBoolean(defaultProfile.get(i)[2]);
                return value;
            }
        }
        return (Boolean) null;
    }

    public static double[] getArraySettings(String setting) {
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

    public static double[] getDefaultProfileArraySettings(String setting) {
        List<Double> value = new ArrayList<>();
        for (int i = 0; i < defaultProfile.size(); i++) {
            if (defaultProfile.get(i)[0].equals(setting)) {
                for (int j = 2; j < Integer.parseInt(defaultProfile.get(i)[1])+2; j++) {
                    value.add(Double.parseDouble(defaultProfile.get(i)[j]));
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
}
