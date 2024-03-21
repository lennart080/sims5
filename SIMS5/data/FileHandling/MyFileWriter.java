package SIMS5.data.FileHandling;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

public abstract class MyFileWriter extends FileClass {

    public static boolean writeInFile(String path, String name, String fileType, List<String> fileStrings) {
        if (checkIfFileExists(path, name, fileType)) {
            deleteFile(path, name, fileType);
        }
        createFile(path, name, fileType);
        try {
            FileOutputStream outputStream = new FileOutputStream(dataVerzeichnis + path + name + fileType, true);
            PrintWriter writer = new PrintWriter(outputStream);
            for (int i = 0; i < fileStrings.size(); i++) {
                writer.println(fileStrings.get(i));
            }
            writer.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
