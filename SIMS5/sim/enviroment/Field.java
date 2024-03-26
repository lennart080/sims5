package SIMS5.sim.enviroment;

import SIMS5.data.FileHandling.profileFiles.Profile;
import SIMS5.calculator.Calculator;
import SIMS5.sim.util.MathUtil;

public class Field extends MathUtil {

    private int size;
    private int entitySize;
    private int gridCount;
    private int gridSize;

    public Field(Profile profile) {
        size = profile.getIntager("simulationSize");
        gridCount = (int)Math.ceil(Math.sqrt(profile.getIntager("entitysPerRound"))); 
        entitySize = profile.getIntager("entitySize");
        gridSize = (int)((double)size/(double)gridCount);
    }
    
    public int[] newPosition(int x) {  
        int xGridPos = x % gridCount;
        int yGridpos = x / gridCount;
        int[] position = {xGridPos*gridCount, yGridpos*gridCount};
        position[0]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, gridSize-entitySize);
        position[1]+= (entitySize/2) + Calculator.normaliseValue(newRandom(), 1, gridSize-entitySize);
        return position;
    } 
}
