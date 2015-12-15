package Toma;

import main.*;

import java.util.Random;

public class TomaProtein extends Protein {

    /**
     * each residue contains f(i) which describe it's mobility.
     * f(i) = 0 - movement is completely free
     * f(i) < 0 - residue involved in a loop defined by HH bond
     */
    protected int[] mMobilityFactor;

    TomaProtein(Dimensions dimensions, Sequence sequence, Random random,
                Grid grid, String name){
        super(dimensions, sequence, random, grid, name, false);
        mMobilityFactor = new int[sequence.size()];
    }

    public int getMobilityFactor(int index){
        return mMobilityFactor[index];
    }

    /**
     * set mobility factor for residue in place @index
     * @param index target residue
     * @param dif as determined by g(k)
     */
    public void setMobilityFactor(int index, int dif){
        mMobilityFactor[index] -= dif;
    }

}
