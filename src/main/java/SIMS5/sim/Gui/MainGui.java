package SIMS5.sim.Gui;

import SIMS5.sim.Manager;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.enviroment.LightData;

import java.util.List;

public interface MainGui {

    default void startSimulation(String profileName) {
        //simManager.startSimulation(profileName);
    }

    default void setSimulationSpeed(int speed) {
        //simManager.setSpeed(speed);
    }


    void updateLightData(LightData lightData);
    void updateBodys(List<Body> bodys);
    void updateRound(int round);
    void updateDay(int day);
    void updateTime(int time);
    void updateUpdates(int updates);
}
