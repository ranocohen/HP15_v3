package Toma;

import main.*;

import java.util.Random;

public class TomaProtein extends Protein {

    private final String USER_DATA = "MobilityFactor";
    private final double INIT_MOBILITY_FACTOR = 0.0;

    /**
     * each residue contains f(i) which describes it's mobility.
     * f(i) = 0 - movement is completely free
     * f(i) < 0 - residue involved in a loop defined by HH bond
     */

    TomaProtein(Dimensions dimensions, Sequence sequence, Random random,
                Grid grid, String name) {
        super(dimensions, sequence, random, grid, name, false);
        for (Monomer m : this) {
            m.addUserData(USER_DATA, INIT_MOBILITY_FACTOR);
        }
    }


    /**
     * get mobility factor for residue in place @index
     *
     * @param index target residue
     */
    public double getMobilityFactor(int index) {
        return (double) this.get(index).getUserData(USER_DATA);
    }


    public void updateMobilityFactor() {
        Double loopSum = 0.0;
        for (Monomer monomer : this) {
            Pair<Double, Double> ans = new Pair<>(0.0, 0.0);
            countLoops(monomer, ans);
            //assuming a 'new' loop if we find a loop vs a later (index wise) monomer
            if ( ans.getFirst() > 0.0){
                loopSum -= ans.getFirst();
            }
            monomer.updateUserData(USER_DATA, loopSum);
            //assuming closer of a loop if we find a loop vs a former (index wise) monomer
            if ( ans.getSecond() > 0.0){
                loopSum += ans.getSecond();
            }
        }

    }

    /**@param monomer monomer against check loop existence
     * @param ans 'first' value is the 'new' loops and
     * 'second' value as the closed loops (by this monomer)
     * */
    public void countLoops(Monomer monomer, Pair<Double,Double> ans) {
        //checking vs all monomers (4 directions) in bound (on the grid)
        Monomer monomer1;
        if (monomer.type != MonomerType.H)
            return;
        int x = monomer.getX();
        int y = monomer.getY();
        int z = monomer.getZ();
        if (!grid.xEdge(x)) {
            monomer1 = grid.getCell(x + 1, y, z);
            updateLoopCount(ans, monomer, monomer1);
        }
        if (x > 0) {
            monomer1 = grid.getCell(x - 1, y, z);
            updateLoopCount(ans, monomer, monomer1);
        }
        if (!grid.yEdge(y)) {
            monomer1 = grid.getCell(x, y + 1, z);
            updateLoopCount(ans, monomer, monomer1);
        }
        if (y > 0){
            monomer1 = grid.getCell(x, y - 1, z);
            updateLoopCount(ans, monomer, monomer1);
        }
    }


    private static void updateLoopCount(Pair<Double,Double> ans, Monomer monomer, Monomer monomer1){
        if ((TomaOptimizer.DEBUG) && (monomer1 != null)
                && (monomer.protein != monomer1.protein)) {
            throw new RuntimeException("Two proteins on the grid\n"
                    + monomer.protein + "\n" + monomer1.protein);
        }
        // checking if not last monomer, not connected directly and is H type
        if ((monomer1 != null) && (monomer1 != monomer.getPrev())
                && (monomer1 != monomer.getNext())
                && (monomer1.type == MonomerType.H)) {
            if(monomer1.getNumber()>monomer.getNumber())
            {
                ans.setFirst(ans.getFirst() + calcMFactor(monomer1.getNumber() - monomer.getNumber() + 1));
            }

            else
            {
                ans.setSecond(ans.getSecond() + calcMFactor(monomer.getNumber() - monomer1.getNumber() + 1));
            }
        }
    }
    private static Double calcMFactor(double k){
        return 20 / ( 17 + k);
    }

}
