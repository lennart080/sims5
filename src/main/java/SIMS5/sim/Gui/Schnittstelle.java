package SIMS5.sim.Gui;

import SIMS5.sim.entitiys.Body;
import SIMS5.sim.enviroment.LightData;

import java.util.List;

public abstract class Schnittstelle {
    private LightData lightData;
    private List<Body> bodys;
    private int SimSpeed;
    private int round;
    private int day;
    private int time;
    private int updates;
    private int mode = 0;
    private int SRround = 0;
    private int SRentity = 0;

    private boolean ready = false;

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

    protected void setMyDay(int day) {
        this.day = day;
    }

    protected void setMyRound(int round) {
        this.round = round;
    }

    protected void setMyTime(int time) {
        this.time = time;
    }

    protected void setMyUpdates(int updates) {
        this.updates = updates;
    }

    protected void setMyLightData(LightData lightData) {
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

    protected void setMyBodys(List<Body> bodys) {
        this.bodys = bodys;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    protected void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean getReady() {
        return ready;
    }

    public void setRoundOfShowroom(int round) {
        SRround = round;
    }

    public void setEntityOfShowroom(int entity) {
        SRentity = entity;
    }

    public int getSRround() {
        return SRround;
    }

    public int getSRentity() {
        return SRentity;
    }
}
