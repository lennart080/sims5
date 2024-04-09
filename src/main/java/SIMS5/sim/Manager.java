package SIMS5.sim;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.entitiys.Robot.Robot;
import SIMS5.sim.enviroment.Field;
import SIMS5.sim.enviroment.LightData;
import SIMS5.sim.modes.PurAi;
import SIMS5.sim.modes.RoundHandler;
import SIMS5.sim.network.Mind;

public class Manager {
    private RoundHandler roundHandler;
    private Profile profile;
    private LightData light;
    private Field field; 
    private int entitysPerRound;
    private double[] robotStartStatistics = new double[9];
    private double[] updateList = new double[5];
    private double energieLossAjustment;
    private boolean simulationReady = false;

    public void startSimulation(String profileName) {
        Thread simulationThread = new Thread(() -> {
            profile = new Profile(profileName);
            light = new LightData(profile);
            loadProfileSettings();
            field = new Field(profile);
            selectMode();
        });
        simulationThread.start();
    }

    private void loadProfileSettings() {
        robotStartStatistics[0] = profile.getDouble("entityStartEnergie");
        robotStartStatistics[1] = profile.getDouble("entityStartSchrott");
        robotStartStatistics[2] = profile.getDouble("entityStartAttack");
        robotStartStatistics[3] = profile.getDouble("entityStartEnergieCapacity");
        robotStartStatistics[4] = profile.getDouble("entityStartSpeed");
        robotStartStatistics[5] = profile.getDouble("entityStartDefense");
        robotStartStatistics[6] = profile.getDouble("entityStartHealth");
        robotStartStatistics[7] = profile.getDouble("entityStartRust");
        robotStartStatistics[8] = profile.getDouble("entityStartSolar");
        updateList[0] = profile.getDouble("entityRustPlus");
        updateList[1] = profile.getDouble("entityRustLoss");
        updateList[2] = profile.getDouble("entityEnergyLoss");
        updateList[3] = profile.getDouble("entityHealthLoss");
        updateList[4] = profile.getDouble("entityAttackEnergieLoss");
        energieLossAjustment = profile.getDouble("entityEnergylossAjustmentPerDay");
    }

    private void selectMode() {
        if (profile.getIntager("simulationMode") == 0) {
            purAiMode();
        }
    }

    private void purAiMode() {
        double walkActivasion = profile.getDouble("entityWalkActivation");
        double attakActivision = profile.getDouble("attakActivision");
        roundHandler = new PurAi(profile, light, field);
        entitysPerRound = profile.getIntager("entitysPerRound");
        int lastPosSize = profile.getIntager("entityPosSave");
        List<Robot> robots = new ArrayList<>(entitysPerRound);
        for (int i = 0; i < entitysPerRound; i++) {
            robots.add(new Robot(roundHandler, robotStartStatistics, updateList, energieLossAjustment, walkActivasion, field, lastPosSize, attakActivision));
        }
        simulationReady = true;
        ((PurAi) roundHandler).startFirstRound(robots);
        robots.clear();
        while (true) {
            for (int i = 0; i < entitysPerRound; i++) {
                robots.add(new Robot(roundHandler, robotStartStatistics, updateList, energieLossAjustment, walkActivasion, field, lastPosSize, attakActivision));
            }
            ((PurAi) roundHandler).startRound(robots);
            robots.clear();
        }
    }

    public LightData getLightData() {
        return roundHandler.getLightData();
    }

    public List<Body> getEntitys() {
        return roundHandler.getBodys();
    }

    public List<Mind> getNetworks() {
        return roundHandler.getMinds();
    } 

    public int getRound() {
        return roundHandler.getRound();
    }

    public int getDay() {
        return roundHandler.getDay();
    }

    public int getTime() {
        return roundHandler.getTime();
    }

    public int getUpdates() {
        return roundHandler.getUpdates();
    }

    public boolean getSimulationReady() {
        return simulationReady;
    }
}
