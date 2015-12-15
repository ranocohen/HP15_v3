package Toma;

import main.*;

import java.util.Random;

public class TomaProtein extends Protein {

    private final String USER_DATA = "MobilityFactor";
    private final double INIT_MOBILITY_FACTOR = 0;

    /**
     * each residue contains f(i) which describes it's mobility.
     * f(i) = 0 - movement is completely free
     * f(i) < 0 - residue involved in a loop defined by HH bond
     */

    TomaProtein(Dimensions dimensions, Sequence sequence, Random random,
                Grid grid, String name){
        super(dimensions, sequence, random, grid, name, false);
        for (Monomer m : this){
            m.addUserData(USER_DATA, INIT_MOBILITY_FACTOR);
        }
    }


    /**
     * get mobility factor for residue in place @index
     * @param index target residue
     */
    public double getMobilityFactor(int index){
        return (double)this.get(index).getUserData(USER_DATA);
    }

    /**
     * set mobility factor for residue in place @index
     * @param index target residue
     * @param dif as determined by g(k)
     */
    public void updateMobilityFactor(int index, int dif){
        double newValue = getMobilityFactor(index) - dif;
        this.get(index).updateUserData(USER_DATA, newValue);
    }

}
