package SIMS5.sim.network;

import java.util.Arrays;

import SIMS5.sim.enviroment.Field;

public abstract class Mind {
    protected Field field;
    protected double[] input;
    protected double[] output;

    public Mind(int outputLenght) {
        output = new double[outputLenght];
    }

    public void setInput(double[] input) {
        this.input = Arrays.copyOf(input, input.length);
    }

    public double[] getOutput() {
        return output;
    }
}
