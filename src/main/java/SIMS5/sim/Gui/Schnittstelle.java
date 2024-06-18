package SIMS5.sim.Gui;

import SIMS5.sim.entitiys.Body;
import SIMS5.sim.enviroment.LightData;

import java.util.List;

public class Schnittstelle {
    private LightData lightData;
    private List<Body> bodys;
    private int SimSpeed;
    private int round;
    private int day;
    private int time;
    private int updates;

    public int getDay() {
        return day;
    }

    public int getRound() {
        return round;
    }

    public int getTime() {
        return time;
    }

    public int getUpdates() {
        return updates;
    }

    protected void setDay(int day) {
        this.day = day;
    }

    protected void setRound(int round) {
        this.round = round;
    }

    protected void setTime(int time) {
        this.time = time;
    }

    protected void setUpdates(int updates) {
        this.updates = updates;
    }

    protected void setLightData(LightData lightData) {
        this.lightData = lightData;
    }

    public LightData getLightData() {
        return lightData;
    }

    public void setSimSpeed(int simSpeed) {
        if (simSpeed <= 0) {
            SimSpeed = 1;
            return;
        }
        SimSpeed = simSpeed;
    }

    public int getSimSpeed() {
        return SimSpeed;
    }

    public List<Body> getBodys() {
        return bodys;
    }

    protected void setBodys(List<Body> bodys) {
        this.bodys = bodys;
    }
}
