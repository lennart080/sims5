package SIMS5.sim.modes;

import java.util.List;

import SIMS5.data.FileHandling.networkFiles.Networks;
import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.Manager;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.enviroment.Field;
import SIMS5.sim.enviroment.LightData;
import SIMS5.sim.network.Mind;

public abstract class RoundHandler {

    private Manager manager;
    protected LightData light;
    protected Networks network;
    protected Field field; 
    protected int round = 0;
    protected int day = 0;
    protected int time = 0;
    protected int updates = 0;
    private int simSpeed;


    public RoundHandler(Profile profile, LightData lightdata, Field field, Manager manager) {
        this.manager = manager;
        network = new Networks(profile);
        simSpeed = profile.getIntager("oneDayInSeconds");
        this.field = field;
        light = lightdata;
    }

    public abstract List<Body> getBodys();

    public int getRound() {
        return round;
    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }

    public int getUpdates() {
        return updates;
    }

    public LightData getLightData() {
        return light;
    }

    protected void updatesAndSleepHandling() {
        updates++;
        if (updates % 60 == 0) {
            time++;
            if (time >= 60) {
              time = 0;
            }
            manager.updateTime(time);
            if (time % 60 == 0) {
                day++;
                manager.updateDay(day);
            }
        }
        manager.updateUpdates(updates);
        manager.updateLightData(light);
        try {
            Thread.sleep((long)(((double)simSpeed/3600.0)*1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract List<Mind> getMinds();

    public void setSpeed(int speed) {
        this.simSpeed = speed;
    }

    protected void updateBodys(List<Body> bodies) {
        manager.updateEntitys(bodies);
    }
}
