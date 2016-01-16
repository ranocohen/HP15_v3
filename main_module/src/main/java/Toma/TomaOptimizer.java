package Toma;


import main.*;
import mutation.MutationManager;
import temperature.TemperatureManager;

import java.io.IOException;
import java.util.Random;

/**
 * A single run of the Toma algorithm.
 */
public class TomaOptimizer extends TomaAbstract {

	/**
	 * the graph that represent's the system
	 *
	 */
	//changed PopulationLog to TomaLog
	private TomaLog log;
	private int runNumber;
	private Random randomMonomerIndex;
	private Random randomVal;

	private final int DIRECTIONS = 3;
	private int sequenceSize;
	private int monomerIndex;
	/**
	 * constructor : creates a new GARun
	 *
	 * @param fileWriter
	 *            the fileWriter of the system to write the result of the run
	 *            when finished
	 * @param config
	 *            the configuration file of the system
	 * @param mutationManager
	 *            the mutation manager of the system
	 */
	public TomaOptimizer(OutputPrinter fileWriter, Configuration config,
						 MutationManager mutationManager, TemperatureManager temperatureManager) {
		super(fileWriter, config, mutationManager, temperatureManager);
		//changed PopulationLog to TomaLog,
		//changed numberOfGeneration to numberOfRepeats
		sequenceSize = new Sequence(config.sequence).size();
		log = new TomaLog(config.numberOfGenerations/config.reportEvery+1, fileWriter, temperatureManager, gui);
		runNumber = 0;
		randomVal = new Random(System.currentTimeMillis());
		randomMonomerIndex = new Random(System.currentTimeMillis());
	}

	

	/**
	 * Execute the run.
	 * @throws IOException 
	 * 
	 * @see Optimizer
	 */
	@Override
	public void execute() throws IOException {

		long startTime = System.currentTimeMillis();
		long runningTime;

		log.initialize(runNumber);
		currentTimeStep = 0;
		while(currentTimeStep < config.numberOfGenerations) {

			//choose random monomer in protein
			monomerIndex = getRandomMonomer();
			double mobilityFactor = protein.getMobilityFactor(monomerIndex);
			float temperature = temperatureManager.getCurrentTemperature();

			//check if (rnd < exp(f(i)/ck))  and act accordingly
			double exponent = Math.exp(mobilityFactor / temperature);
			double rnd = randomVal.nextDouble();
			//System.out.println("Mobility of monomer number " + monomerIndex + " is: " + mobilityFactor);
			//System.out.println("Exp val is: " + exponent);
				if (rnd < exponent){


					// mutate current protein,
					// the mutate function as defined in MutationPreDefined evaluates the energy
					mutationManager.mutate(protein, mutatedProtein, 10);
					this.protein.reset();
					this.protein.setConformation(mutatedProtein.getConformation());
					//System.out.println("grid's proteing == null??? " + protein.getGrid().getCurrentProteinOnGrid() == null);
					// increment timestep
					currentTimeStep++;

					// lower temperature
					temperatureManager.getNextTemprature();

					// recalculate mobility - g(k) should be calculated together with energy calculations
					protein.updateMobilityFactor();
					this.protein.getGrid().reset(protein);

					// if new conformation is best so far(lowest energy) replace current best
					if (SHOW_BEST_IN_GUI
							&& (this.bestProtein == null || bestProtein.getEnergy() >= protein.getEnergy())) {
						this.bestProtein.reset();
						this.bestProtein.setConformation(protein.getConformation());
					}
					runningTime = (System.currentTimeMillis() - startTime);
					if (currentTimeStep % config.reportEvery == 0) {
						System.out.println(currentTimeStep);
						log.collectStatistics(protein,protein.getEnergy(), currentTimeStep, numberOfGenerations,
								runningTime, temperature);
					}
				}
		}
		log.printRun();
		runNumber++;
	}

	public int getRandomMonomer() {
		return randomMonomerIndex.nextInt(sequenceSize);
	}

}
