package SIMS5.data.FileHandling.profileFiles;

import java.io.File;

public interface ProfileData {
    public static final String userProfilePath = "profiles" + File.separator + "userProfiles" + File.separator;
    public static final String defaultProfilePath = "profiles" + File.separator + "default" + File.separator;
    public static final String fileType = ".p";
}
