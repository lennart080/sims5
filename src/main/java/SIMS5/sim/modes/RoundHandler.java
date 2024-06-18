package SIMS5.sim.modes;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.networkFiles.Networks;
import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.sim.Gui.Schnittstelle;
import SIMS5.sim.Manager;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.entitiys.MyEntity;
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
    private boolean endMode = false;


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

    protected void simulate(List<MyEntity> entity) {
        for (int i = 0; i < entity.size(); i++) {
            entity.get(i).simulate(light.getLightIntensityAtTime(updates));
        }
    }

    protected void runRound(List<MyEntity> entity) {
        List<Body> bodies = new ArrayList<>(entity.size());
        for (MyEntity my : entity) {
            bodies.add(my.getBody());
        }
        manager.setBodys(bodies);
        updates = 0;
        time = 0;
        day = 0;
        while (!entity.isEmpty() || !endMode) {
            simulate(entity);
            int temp = day;
            updatesAndSleepHandling();
            if (day > temp) {
                for (MyEntity myEntity : entity) {
                    myEntity.setDay(day);
                }
            }
        }
        endMode = false;
        round++;
        manager.updateRound(round);
    }

    public abstract List<Mind> getMinds();

    public void setSpeed(int speed) {
        this.simSpeed = speed;
    }

    public void stop() {
        endMode = true;
    }
}
