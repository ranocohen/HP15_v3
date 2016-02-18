package Toma;

import main.*;
import java.lang.Exception;
import java.util.Random;

public class TomaProtein extends Protein {
    private final String USER_DATA = "MobilityFactor";
    private RuntimeException up = new RuntimeException("g(k) should never be positive!");
    private final double INIT_MOBILITY_FACTOR = 0.0;
    private Pair<Double, Double> pair = new Pair<>(0.0, 0.0);

    /**
     * each residue contains f(i) which describes it's mobility.
     * f(i) = 0 - movement is completely free
     * f(i) < 0 - residue involved in a loop defined by HH bond
     */

    TomaProtein(Dimensions dimensions, Sequence sequence, Random random,
                Grid grid, String name) {
        super(dimensions, sequence, random, grid, name, true);
        for (Monomer m : this) {
            m.addUserData(USER_DATA, INIT_MOBILITY_FACTOR);
        }

    }

    TomaProtein(TomaProtein other, String name) {
        super(other.dimensions, other.sequence, other.random, other.grid, name, false);
        grid.reset(other);
        this.setConformation(other.getConformation());
        //setInitConformation(sequence.size());

    }
    /**
     * get mobility factor for residue in place @index
     *
     * @param index target residue
     */
    public double getMobilityFactor(int index) {
       // System.out.println(get(index).getUserData(USER_DATA));
        return (double) this.get(index).getUserData(USER_DATA);
    }

    public void setInitConformation(int size){
        conformation = new Conformation(size);
        reset();
        conformation.clear();
        boolean first = true;
        for (Monomer monomer : this) {
            if (first) {
                conformation.add(MonomerDirection.FIRST);
                if (!monomer.setRelativeDirection(MonomerDirection.FIRST))
                    throw new RuntimeException(
                            "Why not accept first direction?");
                first = false;
            } else {
                MonomerDirection newDirection = MonomerDirection.FORWARD;
                monomer.setRelativeDirection(newDirection);
                conformation.add(monomer.getRelativeDirection());
            }
        }
    }

    public void updateMobilityFactor(){
        if (grid.getCurrentProteinOnGrid() == null){
            for (Monomer m : this){
                grid.update(m);
            }
        }
        Double loopSum = 0.0;
        for (Monomer monomer : this) {
            pair.setFirst(0.0);
            pair.setSecond(0.0);
            countLoops(monomer, pair);
            //assuming a 'new' loop if we find a loop vs a later (index wise) monomer
            if (pair.getFirst() > 0){
                loopSum -= pair.getFirst();
            }
                if (loopSum > 0.0) {
                    System.out.println("loopSum val is: " + loopSum + " for monomer: " + monomer.toString());
                    throw up;
                }
            monomer.updateUserData(USER_DATA, loopSum);
           // System.out.println("monomer number " + monomer.getNumber() + " has mobility factor of " + monomer.getUserData(USER_DATA));
            //assuming closer of a loop if we find a loop vs a former (index wise) monomer
            if (pair.getFirst() > 0){
                loopSum += pair.getSecond();
            }
        }


       // System.out.println("protein is: " + name);

    }

    /**
     *
     * @param monomer monomer against check loop existence
     * @param ans 'first' value is the 'new' loops and
     * 'second' value as the closed loops (by this monomer)
     * */
    public void countLoops(Monomer monomer, Pair<Double,Double> ans) {
        //checking vs all monomers (4 directions) in bound (on the grid)
        Monomer monomer1;
        if (grid.getCurrentProteinOnGrid() == null) {
           try{
                grid.update(monomer);
           }
           catch (Exception e) {
               throw new RuntimeException("grid does not have a protein (null value)");
           }
        }
        if (monomer.type != MonomerType.H)
            return;
        int x = monomer.getX();
        int y = monomer.getY();
        int z = monomer.getZ();
        if (!grid.xEdge(x)) {
            monomer1 = grid.getCell(x + 1, y, z);
            updateLoopCount(ans, monomer, monomer1);
        }
        if (!grid.minXEdge(x)) {
            monomer1 = grid.getCell(x - 1, y, z);
            updateLoopCount(ans, monomer, monomer1);
        }
        if (!grid.yEdge(y)) {
            monomer1 = grid.getCell(x, y + 1, z);
            updateLoopCount(ans, monomer, monomer1);
        }
        if (!grid.minYEdge(y)){
            monomer1 = grid.getCell(x, y - 1, z);
            updateLoopCount(ans, monomer, monomer1);
        }
    }


    /**
     * check {@code monomer1} relative to {@code monomer}
     * @param ans {@code monomer} pair
     * @param monomer
     * @param monomer1
     */
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
        return 20 / (17 + k);
    }

}
