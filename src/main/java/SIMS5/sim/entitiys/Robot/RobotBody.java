package SIMS5.sim.entitiys.Robot;

import SIMS5.sim.entitiys.Body;

public class RobotBody extends Body {

    private Robot robot;

    public RobotBody(int[] pos, int size, int lastPosSize, Robot robot) {
        super(pos, size, lastPosSize);
        this.robot = robot;
    }

    public int getId() {
        return robot.getSerialNumber();
    }

    public double[] getStatistics() {
        return robot.getStatistics();
    }

    @Override
    public boolean looseLives(double lives) {
        lives-= robot.getStatistics()[5];
        if (lives > 0) {
            robot.alterStats(6, -lives);
            if (robot.getStatistics()[6] <= 0.0) {
                robot.delete();
                return true;
            }
        } 
        return false;
    }
}
