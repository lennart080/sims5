package SIMS5.data;

import java.io.*;
import java.util.List;

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

    public static boolean createFile(String profileName, boolean overrideProfileIfExists) {
      if (overrideProfileIfExists == false && checkIfFileExists(profileName)) {
        return false;
      } else if (checkIfFileExists(profileName)){
        deleteFile(profileName);
      }
      try {
        File datei = new File(pathOrdner, profileName+".p");
        BufferedWriter writer = new BufferedWriter(new FileWriter(datei));
        writer.write("");
        writer.close();
        return true;
      } catch(Exception e) {
        System.out.println(e);
      }
      return false;
    }

    private static boolean checkIfFileExists(String profileName) {
      File ordner = new File(pathOrdner);
      File[] dateien = ordner.listFiles();
      profileName+= ".p";
      for (int i = 0; i < dateien.length; i++) {
        if (dateien[i].getName().equals(profileName)) {
          return true;
        }
      }
      return false;
    }

    public static boolean createNewProfile(String profileName, boolean overrideProfileIfExists) {
      if (overrideProfileIfExists == false && checkIfFileExists(profileName)) {
        return false;
      }
      writeListInProfile(profileName, ProfileReader.getDefaultprofile());
      return true;
    }

    private static void deleteFile(String profileName) {
      File profile = new File(pathOrdner+profileName+".p");
      profile.delete();
    }

    public static void writeInProfile(String profileName, String atribute,int lenght, String[] value) {
      List<String[]> profile = ProfileReader.getprofile(profileName);
      int pos = atributPosFinder(profile, atribute);
      if (pos != -1) {
        String[] newline = new String[2+lenght];
        newline[0] = atribute;
        newline[1] = lenght+"";
        for (int i = 0; i < lenght; i++) {
          newline[2+i] = value[i];
        }
        profile.set(pos, newline);
        writeListInProfile(profileName, profile);
      }
    }

    private static void writeListInProfile(String profileName, List<String[]> settings) {
      try {
        if (profileName != null) {
          deleteFile(profileName);
          createFile(profileName, true);
          FileOutputStream outputStream = new FileOutputStream(pathOrdner + profileName + ".p", true);
          PrintWriter writer = new PrintWriter(outputStream);
          for (int i = 0; i < settings.size(); i++) {
            writer.print(settings.get(i)[0]);
            for (int j = 1; j < settings.get(i).length; j++) {
              writer.print(":"+settings.get(i)[j]);
            }
            writer.println();
          }
          writer.close();
        }
      } catch(Exception e) {
        System.out.println(e);
      }
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

    public static void writeInProfile(String profileName, String atribute, double value) {
      String[] x = {""+value};
      writeInProfile(profileName, atribute, 1, x);
    }

    public static void writeInProfile(String profileName, String atribute, boolean value) {
      String[] x = {""+value};
      writeInProfile(profileName, atribute, 1, x);
    }

    public static void writeInProfile(String profileName, String atribute, double[] value) {
      String[] x = new String[value.length];
      for (int i = 0; i < value.length; i++) {
        x[i] = Double.toString(value[i]);
      }
      writeInProfile(profileName, atribute,value.length, x);
    }
}
