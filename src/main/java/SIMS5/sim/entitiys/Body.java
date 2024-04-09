package SIMS5.sim.entitiys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Body {

    protected int posX;
    protected int posY;
    protected List<int[]> lastPos = new ArrayList<>();
    protected int size;
    private int lastPosSize;

    public Body(int[] pos, int size, int lastPosSize) {
        this.size = size;
        this.lastPosSize = lastPosSize;
        posX = pos[0];
        posY = pos[1];
        setLastPos(pos);
    }

    private void setLastPos(int[] pos) {
        for (int i = 0; i < lastPosSize; i++) {
            lastPos.add(Arrays.copyOf(pos, pos.length)); 
        }
    }

    public List<int[]> getLastPos() {
        return lastPos;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getSize() {
        return size;
    }

    public void alterPos(int posX, int posY) {
        this.posY += posY;
        this.posX += posX;
        lastPos.remove(0);
        int[] temp = {this.posX, this.posY};
        lastPos.add(temp);
    }

    public abstract double[] getStatistics();
    
    public abstract boolean looseLives(double lives);
}
