package Toma;

import main.*;

import java.util.Random;

/**
 * Created by ITAY on 13/12/2015.
 */


public class TomaProtein extends Protein {
    protected int[] mobilityFactor;

    TomaProtein(Dimensions dimensions, Sequence sequence, Random random,
                Grid grid, String name){
        super(dimensions, sequence, random, grid, name, false);
        mobilityFactor = new int[sequence.size()];
    }

    public int getMobilityFactor(int index){
        return mobilityFactor[index];
    }

    public void updateMobilityFactor(int index, int dif){
        mobilityFactor[index] -= dif;
    }

}
