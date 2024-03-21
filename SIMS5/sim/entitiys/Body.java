package SIMS5.sim.entitiys;

public abstract class Body {

    protected int posX;
    protected int posY;

    public Body(int[] pos) {
        posX = pos[0];
        posY = pos[1];
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
