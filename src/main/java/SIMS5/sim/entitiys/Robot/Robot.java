package SIMS5.sim.entitiys.Robot;

import java.util.ArrayList;
import java.util.List;

import SIMS5.sim.entitiys.Body;
import SIMS5.sim.entitiys.MyEntity;
import SIMS5.sim.enviroment.Field;
import SIMS5.sim.modes.PurAi;
import SIMS5.sim.modes.RoundHandler;
import SIMS5.sim.network.NeuralNetwork;
import SIMS5.sim.util.MathUtil;

public class Robot extends MyEntity {

    private double walkActivasion;
    private double attakActivision;
    private int lastMovment = 0;
    private List<Boolean> wasMoving = new ArrayList<>();
    private int score = 0;
    private int killScoreBonus;

    public Robot(RoundHandler handler, double[] statistics, double[] updateList, double energieLossAjustment, double walkActivasion, Field field, int lastPosSize, double attakActivision, int killScoreBonus) {
        super(handler, statistics, updateList, energieLossAjustment, field);
        this.walkActivasion = walkActivasion;
        this.attakActivision = attakActivision;
        this.killScoreBonus = killScoreBonus;
        setMoved(lastPosSize);
    }

    private void setMoved(int lastPosSize) {
        for (int i = 0; i < lastPosSize; i++) {
            wasMoving.add(true);
        }
    }

    public void simulate(double currentLight) {
            updateStatistics(currentLight);
            setInputs();
            mind.setInput(input);
            ((NeuralNetwork) mind).simulate();
            double[] output = mind.getOutput();
            prossesingOutput(output);
    }

    private void setInputs() {
        input[0] = MathUtil.roundToDecPlaces(statistics[0]/statistics[3], 4);
        input[1] = MathUtil.roundToDecPlaces(statistics[6]/10.0, 4);
        input[2] = statistics[7];
        input[3] = MathUtil.roundToDecPlaces((double)body.getPosX()/(double)field.getSize(), 4);
        input[4] = MathUtil.roundToDecPlaces((double)body.getPosY()/(double)field.getSize(), 4);
        input[5] = lastMovment;
        input[6] = MathUtil.roundToDecPlaces(field.getDistanceToNearestBody(body)/(Math.sqrt(Math.pow(field.getSize(), 2)+Math.pow(field.getSize(), 2))), 4);
        input[7] = field.getDirectionToNearestBody(body);
    }

    private void updateStatistics(double currentLight) {
        if (!wasMoving.contains(true)) {
            statistics[7] = MathUtil.roundToDecPlaces(statistics[7]+(updateList[0]/60), 3);
        } else {
            if (statistics[7] >= 0.01) {
                statistics[7] = MathUtil.roundToDecPlaces(statistics[7]-(updateList[1]/60), 3);
            }
        }
        if (statistics[0] < statistics[3]) { // wenn weniger energie als speicherplatz
            statistics[0] = MathUtil.roundToDecPlaces(statistics[0]+((statistics[8]*currentLight)/60), 3); // bekommt enerige
            if (statistics[0] > statistics[3]) { // wenn zu viel energie
                statistics[0] = statistics[3]; // bekommt max enegrie
            }
        }
        statistics[0] = MathUtil.roundToDecPlaces(statistics[0]-(((updateList[2]/60)*((day+1)*energieLossAjustment))+statistics[7]), 3); // energie verlust
        if (statistics[0] <= 0.0) { // wenn keine energie
            statistics[0] = 0.0; // bekommt min energie
            statistics[6] = MathUtil.roundToDecPlaces(statistics[6]-(updateList[3]/60), 3); // leben verlust
            if (statistics[6] <= 0.0) { // wenn keine enerige
                delete(); // stirbt
            }
        } 
    }

    private void prossesingOutput(double[] output) {
        int highestPos = 0;
        for (int i = 1; i < 4; i++) {
            if (output[i] > output[highestPos]) {
                highestPos = i;  
            }
        }
        if (output[highestPos] > walkActivasion) {
            int[] pos = new int[2];
            switch (highestPos) {
                case 0:
                    pos[0] = 0;
                    pos[1] = 1;
                    lastMovment = 1;
                    break;
                case 1:
                    pos[0] = 1;
                    pos[1] = 0;
                    lastMovment = 2;
                    break;
                case 2:
                    pos[0] = 0;
                    pos[1] = -1;
                    lastMovment = 3;
                    break;
                case 3:
                    pos[0] = -1;
                    pos[1] = 0;
                    lastMovment = 4;
                    break;
            }
            if (field.moveEntity(body, pos[0], pos[1])) {
                wasMovingNewIndex(true);
            } else wasMovingNewIndex(false);
            lastMovment = 0;
        } else {
            wasMovingNewIndex(false);
            lastMovment = 0;
        }
        statistics[2] = defaultStats[2] + Math.round(statistics[1] * output[4]);
        statistics[3] = defaultStats[3] + (10 * Math.round(statistics[1] * output[5]));
        statistics[4] = defaultStats[4] + Math.round(statistics[1] * output[6]);
        statistics[5] = defaultStats[5] + Math.round(statistics[1] * output[7]);
        statistics[8] = defaultStats[8] + Math.round(statistics[1] * output[8]);
        if ((output[9] > attakActivision) && (output[9] > output[10])) {
            Body totchingBody = field.getBodyFromTotchingBody(body);
            if (totchingBody != null) {
                double[] stats = totchingBody.getStatistics();
                boolean killed = totchingBody.looseLives(statistics[2]);
                statistics[0] = MathUtil.roundToDecPlaces(statistics[0] - updateList[4], 3);
                if (killed) { 
                    if (totchingBody instanceof RobotBody) {
                        statistics[1]+= stats[1];
                        score+=killScoreBonus;
                    }
                }
            }
        }
    }

    private void wasMovingNewIndex(boolean moved) {
        wasMoving.remove(0);
        wasMoving.add(moved);
    }

    public int getScore() {
        return score;
    }

    public void alterScore(int score) {
        this.score += score;
    }

    public void delete() {
        roundHandler.deleteEntity(this);
    }
}