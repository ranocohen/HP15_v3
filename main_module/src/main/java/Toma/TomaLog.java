package Toma;

import gui.RunningView;
import main.Conformation;
import main.Log;
import main.OutputPrinter;
import main.Population;

import java.io.IOException;


public class TomaLog extends Log {
 	//Trajectory data

	/** The best mobility. */
	protected float[] bestMobility;

	/** The average fitness. */
	protected float[] averageMobility;

	/** The worst fitness. */
	protected float[] worstMobility;

	protected OutputPrinter outPrinter;

	public TomaLog(int trajectorySize, OutputPrinter outPrinter, RunningView gui) {
		super(trajectorySize, outPrinter, gui);
		bestMobility = new float[trajectorySize];
		averageMobility = new float[trajectorySize];
		worstMobility = new float[trajectorySize];
		this.outPrinter = outPrinter;

	}
	
	public void initialize(int runNumber) {
		super.initialize(runNumber);
		for (int i = 0; i < trajectorySize; i++) {
			bestMobility[i] = 0;
			averageMobility[i] = 0;
			worstMobility[i] = 0;
		}
		
	}
	
	public void printRun() {
		outPrinter.printRun(runNumber, this);
	}
	/**
	 * Gets the best fitness per generation array.
	 * 
	 * @return the best fitness
	 */
	public float[] getBestMobility() {
		return bestMobility;
	}
	
	/**
	 * Gets the worst fitness per generation array.
	 * 
	 * @return the worst fitness array
	 */
	public float[] getWorstMobility() {
		return worstMobility;
	}

	/**
	 * Gets the fittest Of Generation array.
	 * 
	 * @return the fittest array
	 */
	public Conformation[] getBestConformations() {
		return bestConformations;
	}
	
	/**
	 * Gets the average fitness per generation array.
	 * 
	 * @return the average fitness
	 */
	public float[] getAverageMobility() {
		return averageMobility;
	}

	public void collectStatistics(Population population, int currentGenerarionNum, int numberOfGenerations, Long runningTime) throws IOException {
		this.collectStatistics(population, currentGenerarionNum, numberOfGenerations, runningTime,-1);
	}
	public void collectStatistics(Population population, int currentGenerarionNum, int numberOfGenerations, Long runningTime, float temperature) throws IOException {
		super.collectStatistics(population.getFirst(), population.getBestEnergy(), population.getAverageEnergy(), population.getWorstEnergy(), currentGenerarionNum, numberOfGenerations, runningTime,temperature);
		averageMobility[step-1] = population.getAverageFitness();
		worstMobility[step-1]   = population.getLast().getFitness();
		bestMobility[step-1]    = population.getFirst().getFitness();
	}

}
