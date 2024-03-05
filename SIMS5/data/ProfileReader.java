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

    public static void loadProfile(String name) {
      profile.clear();
      profile = readFile(profilePath, name);
    }

    public static void loadDefaultProfile() {
        defaultProfile.clear();
        defaultProfile = readFile(defaultPath, "defaultProfile.p");
    }

    private static List<String[]> readFile(String path, String name) {
        List<String[]> fileStrings = new ArrayList<>();
        try {
            File txtDatei = new File(path, name + ".p");
            BufferedReader reader = new BufferedReader(new FileReader(txtDatei));
            String line;
            while ((line = reader.readLine()) != null) { 
              String[] values = line.replaceAll(":$", "").split(":");
              if (values.length == 2 || values.length == 1) {
                fileStrings.add(values);
              }
            }
            reader.close();
          } catch(Exception e) {
            System.out.println(e);
          }
          return fileStrings;
    }

    public static double getDoubleSettings(String setting) {
        double value;
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals(setting)) {
                value = Double.parseDouble(profile.get(i)[1]);
                return value;
            }
        }
        return -1.0;
    }

    public static double getDefaultDoubleSettings(String setting) {
        double value;
        for (int i = 0; i < defaultProfile.size(); i++) {
            if (defaultProfile.get(i)[0].equals(setting)) {
                value = Double.parseDouble(defaultProfile.get(i)[1]);
                return value;
            }
        }
        return -1.0;
    }

    @SuppressWarnings("null")
    public static boolean getBooleanSettings(String setting) {
        boolean value;
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals(setting)) {
                value = Boolean.parseBoolean(profile.get(i)[1]);
                return value;
            }
        }
        return (Boolean) null;
    }

    @SuppressWarnings("null")
    public static boolean getDefaultProfileBooleanSettings(String setting) {
        boolean value;
        for (int i = 0; i < defaultProfile.size(); i++) {
            if (defaultProfile.get(i)[0].equals(setting)) {
                value = Boolean.parseBoolean(defaultProfile.get(i)[1]);
                return value;
            }
        }
        return (Boolean) null;
    }

    public static double[] getArraySettings(String setting) {
        List<Double> value = new ArrayList<>();
        for (int i = 0; i < profile.size(); i++) {
            if (profile.get(i)[0].equals("#" + setting)) {
                int pos = 1;
                while (profile.get(i+pos)[0].equals(""+(pos-1))) {
                    value.add(Double.parseDouble(profile.get(i+pos)[1]));
                    pos++;
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
            if (defaultProfile.get(i)[0].equals("#" + setting)) {
                int pos = 1;
                while (defaultProfile.get(i+pos)[0].equals(""+(pos-1))) {
                    value.add(Double.parseDouble(defaultProfile.get(i+pos)[1]));
                    pos++;
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
