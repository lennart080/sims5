package SIMS5.sim.util;

import java.util.ArrayList;
import java.util.List;

public abstract class MathUtil {

    private static long extendetSeed;

    public static void setSeed(int seed) {
        extendetSeed = seed;
    }

    public static double roundToDecPlaces(double value, int decPlaces) {
        return Math.round(value * (Math.pow(10, decPlaces))) / (Math.pow(10, decPlaces));
    }
    
    public static int normaliseValue(double value, int oldMax, int newMax) {     
        int originalMax = oldMax;
        double originalRange = (double) (originalMax);
        double newRange = (double) (newMax);
        double scaledValue = (value * newRange) / originalRange;
        return (int) Math.min(Math.max(scaledValue, 0), newMax);
    }

    public static double newRandom() {   
        long a = 1103515245; // multiplier
        long c = 12345; // increment
        long m = (long) Math.pow(2, 31); // modulus
        do {
            extendetSeed = (a * extendetSeed + c) % m;     
        } while (((double) extendetSeed / m) < 0 && ((double) extendetSeed / m) > 1);     
        return (double) extendetSeed / m;  
    }

    public static double[][][] covertInToArray(List<List<double[]>> pNeurons) {
        double[][][] arrayNeurons = new double[pNeurons.size()][][];
        for (int i = 0; i < arrayNeurons.length; i++) {
            arrayNeurons[i] = new double[pNeurons.get(i).size()][];
            for (int j = 0; j < arrayNeurons[i].length; j++) {
                arrayNeurons[i][j] = pNeurons.get(i).get(j);
            }
        }
        return arrayNeurons;
    }

    public static List<List<double[]>> convertInToList(double[][][] pNeurons) {
        List<List<double[]>> listNeurons = new ArrayList<>(pNeurons.length);
        for (int i = 0; i < pNeurons.length; i++) {
            listNeurons.add(new ArrayList<>(pNeurons[i].length));
            for (int j = 0; j < pNeurons[i].length; j++) {
                listNeurons.get(i).add(pNeurons[i][j]);
            }
        }
        return listNeurons;
    }

    public static long getExtendetSeed() {
        return extendetSeed;
    }
}