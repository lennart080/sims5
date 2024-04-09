package SIMS5.sim.enviroment;

import java.util.ArrayList;
import java.util.List;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.gui.Calculator;
import SIMS5.sim.entitiys.Body;
import SIMS5.sim.util.MathUtil;

public class Field extends MathUtil {

    private int size;
    private int entitySize;
    private int gridCount;
    private int gridSize;
    private List<Body> bodys = new ArrayList<>(); 

    public Field(Profile profile) {
        size = profile.getIntager("simulationSize");
        gridCount = (int)Math.ceil(Math.sqrt(profile.getIntager("entitysPerRound"))); 
        entitySize = profile.getIntager("entitySize");
        gridSize = (int)((double)size/(double)gridCount);
    }
    
    public int[] newPosition(int x) {  
        int xGridPos = x % gridCount;
        int yGridpos = x / gridCount;
        int[] position = {xGridPos*gridSize, yGridpos*gridSize};
        position[0]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, gridSize-entitySize);
        position[1]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, gridSize-entitySize);
        return position;
    } 

    public boolean moveEntity(Body body, int alterX, int alterY) {
        int nextX = body.getPosX() + alterX;
        int nextY = body.getPosY() + alterY;
        int halfBodySize = body.getSize()/2;
        Body nearestBody = getNearestBody(body);
        if (nearestBody != null) {
            int[] differences = {nearestBody.getPosX() - nextX, nearestBody.getPosY() - nextY};
            for (int i = 0; i < differences.length; i++) {
                if (differences[i] < 0) {
                    differences[i]*=-1;
                }  
            }
            int mindestAbstand = (nearestBody.getSize()/2) + (body.getSize()/2);
            if ((differences[0] <= mindestAbstand) && (differences[1] <= mindestAbstand)) {
                return false;
            } 
        }
        if (((nextX + halfBodySize) > size) || ((nextY + halfBodySize) > size) || ((nextX - halfBodySize) < 0) || ((nextY - halfBodySize) < 0)) {
            return false;
        }
        body.alterPos(alterX, alterY);
        return true;
    }

    public void addToField(Body body) {
        if (bodys.contains(body)) {
            return;
        }
        bodys.add(body);
    }

    public void removeFromField(Body body) {
        for (int i = 0; i < bodys.size(); i++) {
            if (bodys.get(i).equals(body)) {
                bodys.remove(i);
                return;
            }    
        }
    }

    private int getPosInt(int x) {
        if (x < 0) {
            x*=(-1);
        }
        return x;
    }

    public int getSize() {
        return size;
    }

    private Body getNearestBody(Body body) {
        Body nearestBody = null;
        double distance = size+1;
        for (int i = 0; i < bodys.size(); i++) {
            if (!bodys.get(i).equals(body)) {
                int[] differences = getDifference(bodys.get(i), body);
                double temp = Math.sqrt(Math.pow((double)differences[0], 2) + Math.pow((double)differences[1], 2));
                if (temp < distance) {
                    distance = temp;
                    nearestBody = bodys.get(i);
                }
            }
        }
        return nearestBody;
    }

    private int[] getDifference(Body body, Body body2) {
        int[] differences = {body2.getPosX() - body.getPosX(), body2.getPosY() - body.getPosY()};
        for (int i = 0; i < differences.length; i++) {
            if (differences[i] < 0) {
                differences[i]*=-1;
            }  
        }
        return differences;
    }

    public int getDirectionToNearestBody(Body body) {
        Body nearestBody = getNearestBody(body);
        if (nearestBody != null) {
            int direction;
            int diffX = body.getPosX() - nearestBody.getPosX();
            int diffY = body.getPosY() - nearestBody.getPosY();
            if ((diffY > 0) && (getPosInt(diffY) > getPosInt(diffX))) {
                direction = 1;
            } else if ((diffX < 0) && (getPosInt(diffX) > getPosInt(diffY))) {
                direction = 2;
            } else if ((diffY < 0) && (getPosInt(diffY) > getPosInt(diffX))) {
                direction = 3;
            } else {
                direction = 4;
            }
            return direction; 
        } 
        return 0;
    }

    public double getDistanceToNearestBody(Body body) {
        Body nearestBody = getNearestBody(body);
        if (nearestBody != null) {
            int[] differences = getDifference(body, nearestBody);
            return Math.sqrt(Math.pow((double)differences[0], 2) + Math.pow((double)differences[1], 2));
        } 
        return size;
    }

    public Body getBodyFromTotchingBody(Body body) {
        Body nearestBody = getNearestBody(body);
        if ((nearestBody != null) ) {
            int[] differences = getDifference(nearestBody, body);
            int mindestAbstand = (nearestBody.getSize()/2) + (body.getSize()/2);
            if ((differences[0] <= mindestAbstand) && (differences[1] <= mindestAbstand)) {
                return nearestBody;
            } 
        }
        return null;
    }
}
