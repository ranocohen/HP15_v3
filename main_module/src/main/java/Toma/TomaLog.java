package Toma;

import gui.RunningView;
import main.Conformation;
import main.Log;
import main.OutputPrinter;
import main.Population;

import java.io.IOException;

//TODO: Fix TomaLog, not based on population log
// since we only have 2 proteins and not a population
public class TomaLog extends Log {

	protected OutputPrinter outPrinter;

	public TomaLog(int trajectorySize, OutputPrinter outPrinter, RunningView gui) {
		super(trajectorySize, outPrinter, gui);
		this.outPrinter = outPrinter;

	}
	
	public void initialize(int runNumber) {
		super.initialize(runNumber);
	}
	
	public void printRun() {
		outPrinter.printRun(runNumber, this);
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

	public void collectStatistics(Population population, int currentGenerarionNum, int numberOfGenerations, Long runningTime) throws IOException {
		this.collectStatistics(population, currentGenerarionNum, numberOfGenerations, runningTime,-1);
	}
	public void collectStatistics(Population population, int currentGenerarionNum, int numberOfGenerations, Long runningTime, float temperature) throws IOException {
		super.collectStatistics(population.getFirst(), population.getBestEnergy(), population.getAverageEnergy(), population.getWorstEnergy(), currentGenerarionNum, numberOfGenerations, runningTime,temperature);

	}

}
