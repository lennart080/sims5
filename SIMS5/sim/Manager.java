package SIMS5.sim;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.entitiys.Robot.Robot;
import SIMS5.sim.enviroment.LightData;
import SIMS5.sim.modes.PurAi;
import SIMS5.sim.modes.RoundHandler;

public class Manager {
    private RoundHandler roundHandler;
    private Profile profile;
    private LightData light;
    private int round;

    public void startSimulation(String profileName) {
        Thread simulationThread = new Thread(() -> {
            profile = new Profile(profileName);
            light = new LightData(profile);
            selectMode();
        });
        simulationThread.start();
    }

    private void selectMode() {
        if (profile.getIntager("simulationMode") == 0) {
            roundHandler = new PurAi(profile, light);
            List<Robot> robots = new ArrayList<>();
            while (true) {
                for (int i = 0; i < profile.getIntager("entitysPerRound"); i++) {
                    robots.add(new Robot(roundHandler));
                }
                ((PurAi) roundHandler).startFirstRound(robots);
            }
        }
    }

    public int getRound() {
        return round;
    }
}
