package SIMS5.sim.entitiys;

import SIMS5.sim.modes.RoundHandler;
import SIMS5.sim.network.Mind;

public abstract class MyEntity {

    private static int lastSerialNumber = 0;
    protected int serialNumber;
    protected Body body;
    protected Mind mind;
    protected RoundHandler roundHandler;

    public MyEntity(RoundHandler handler) {
        serialNumber = lastSerialNumber;
        lastSerialNumber++;
        roundHandler = handler;
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

    public abstract void simulate();
}
