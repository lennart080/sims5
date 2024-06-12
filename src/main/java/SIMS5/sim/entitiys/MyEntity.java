package SIMS5.sim.entitiys;

import java.util.Arrays;

import SIMS5.sim.enviroment.Field;
import SIMS5.sim.modes.RoundHandler;
import SIMS5.sim.network.Mind;

public abstract class MyEntity {

    private static int lastSerialNumber = 0;
    protected int serialNumber;
    protected Body body;
    protected Mind mind;
    protected Field field;
    protected RoundHandler roundHandler;
    protected double[] statistics;
    protected double[] defaultStats;
    protected double[] updateList;
    protected double energieLossAjustment;
    protected double[] input;
    protected int day;

    public MyEntity(RoundHandler handler, double[] statistics, double[] updateList, double energieLossAjustment, Field field) {
        this.field = field;
        serialNumber = lastSerialNumber;
        lastSerialNumber++;
        roundHandler = handler;
        this.statistics = Arrays.copyOf(statistics, statistics.length);
        defaultStats = Arrays.copyOf(statistics, statistics.length);
        this.updateList = Arrays.copyOf(updateList, updateList.length);
        this.energieLossAjustment = energieLossAjustment;
        input = new double[8];
    }

    public void setMind(Mind mind) {
        this.mind = mind;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Mind getMind() {
        return mind;
    }

    public Body getBody() {
        return body;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public double[] getStatistics() {
        return statistics;
    }

    public void alterStats(int stat, double alterValue) {
        statistics[stat]+= alterValue;
    }

    public abstract void simulate(double currentLight);

    public abstract void delete();

    public void setDay(int day) {
        this.day = day;
    }
}
