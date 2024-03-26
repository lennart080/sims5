package SIMS5.data.FileHandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public abstract class MyFileReader extends FileClass {

    public static List<String> readFile(String path, String name, String fileType) {
        List<String> fileStrings = new ArrayList<>();
        if (checkIfFileExists(path, name, fileType)) {
            try {
                File txtDatei = new File(dataVerzeichnis+path+name+fileType);
                BufferedReader reader = new BufferedReader(new FileReader(txtDatei));
                String line;
                while ((line = reader.readLine()) != null) { 
                    fileStrings.add(line);
                }
                reader.close();
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        return fileStrings;
    }
}
