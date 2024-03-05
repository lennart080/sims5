package SIMS5.data;

import java.io.*;

public class ProfileWriter {
    private static String runnVerzeichnis = System.getProperty("user.dir");
    private static String pathOrdner = runnVerzeichnis + File.separator + "SIMS5" + File.separator + "data" + File.separator + "profiles" + File.separator;

    public static void checkOrdner() {
      File ordner = new File(pathOrdner);
        if (!ordner.exists()) {
          if (ordner.mkdir()) {
            return;
          } else {
          System.out.println("error in path building");
        }
      }
    }

    public static boolean createNewProfile(String profileName, boolean overrideProfileIfExists) {
      File ordner = new File(pathOrdner);
      File[] dateien = ordner.listFiles();
      if (profileName == null) {
        profileName = "p1.p";
      } else {
        profileName+= ".p";
      }
      for (int i = 0; i < dateien.length; i++) {
        if (dateien[i].getName().equals(profileName)) {
          if (overrideProfileIfExists = false) {
            return false;
          }
        }
      }
      try {
        File datei = new File(pathOrdner, profileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(datei));
        writer.write("");
        writer.close();
        return true;
      } catch(Exception e) {
        System.out.println(e);
      }
      return false;
    }

    public static void writeInProfile(String profileName, String variableName, String value) {
      try {
        if (profileName != null) {
          FileOutputStream outputStream = new FileOutputStream(pathOrdner + profileName + ".p", true);
          PrintWriter writer = new PrintWriter(outputStream);
          if (variableName != null) {
            writer.print(variableName + ":");
          } else {
            writer.print("Null:");
          }
          if (value != null) {
            writer.print(value);
          } else {
            writer.print("Null");
          }
          writer.println();
          writer.close();
        }
      } catch(Exception e) {
        System.out.println(e);
      }
    }

    public static void writeInProfile(String profileName, String variableName, double value) {
      writeInProfile(profileName, variableName, ""+value);
    }

    public static void writeInProfile(String profileName, String variableName, boolean value) {
      writeInProfile(profileName, variableName, ""+value);
    }

    public static void writeInProfile(String profileName, String variableName, double[] value) {
      writeInProfile(profileName, "#" + variableName, "");
      for (int i = 0; i < value.length; i++) {
        writeInProfile(profileName, ""+i, value[i]);
      }
      writeInProfile(profileName, "#", "");
    }
}
