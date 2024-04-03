package SIMS5.data.FileHandling;

import java.io.File;

public abstract class FileClass {

    protected static String dataVerzeichnis = System.getProperty("user.dir") + File.separator + "SIMS5" + File.separator + "data" + File.separator;

    public static boolean checkIfFileExistsInOrdner(String path, String name, String fileType) {
        String[] files = getAllFiles(path);
        for (int i = 0; i < files.length; i++) {
            if (files[i].equals(name+fileType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfFileExists(String path, String name, String fileType) {
        File file = new File(dataVerzeichnis+path+name+fileType);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean createFile(String path, String name, String fileType) {
        if (checkIfFileExists(path, name, fileType)) {
            return true;
        } 
        try {
            File file = new File(dataVerzeichnis+path+name+fileType);
            file.createNewFile();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean createOrdner(String path, String name) {
        if (checkIfFileExists(path, name, "")) {
            return true;
        }
        try {
            File ordner = new File(dataVerzeichnis+path+name);
            ordner.mkdir();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteFile(String path, String name, String fileType) {
        if (!checkIfFileExists(path, name, fileType)) {
            return true;
        }
        try {
            File file = new File(dataVerzeichnis+path+name+fileType);
            file.delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteOrdner(String path, String name) {
        return deleteFile(path, name, "");
    }

    public static String[] getAllFiles(String path) {
        File ordner = new File(dataVerzeichnis+path);
        File[] dateien = ordner.listFiles();
        String[] files = new String[dateien.length];
        for (int i = 0; i < dateien.length; i++) {
            files[i] = dateien[i].getName();
        }
        return files;
    }
}
