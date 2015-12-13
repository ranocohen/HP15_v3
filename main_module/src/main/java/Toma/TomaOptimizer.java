package Toma;


import main.*;
import mutation.MutationManager;
import temperature.TemperatureManager;

import java.io.IOException;
import java.util.Random;

/**
 * A single run of the Toma algorithm.
 */
public class TomaOptimizer extends TomaAbstruct {

	/**
	 * the graph that represent's the system
	 *
	 */
	//changed PopulationLog to TomaLog
	private TomaLog log;
	private int runNumber;
	private Random randomMonomerIndex;
	private Random randomVal;
	private Random randomDirectionMovement;

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
		log = new TomaLog(config.numberOfRepeats/config.reportEvery+1, fileWriter, gui);
		runNumber = 0;
	}

	

	/**
	 * Execute the run.
	 * @throws IOException 
	 * 
	 * @see Optimizer
	 */
	@Override
	public void execute() throws IOException {
		TomaProtein in = protein, out;
		long startTime = System.currentTimeMillis();
		long runningTime;
		log.initialize(runNumber);
		currentTimeStep = 0;
		while(currentTimeStep < config.numberOfRepeats) {

			//TODO: choose random monomer in protein
			monomerIndex = getRandomMonomer();
			int mobilityFactor = in.getMobilityFactor(monomerIndex);
			float temperature = temperatureManager.getCurrentTemperature();

			//TODO: check if (rnd < exp(f(i)/ck))  and act accordingly
			double exponent = Math.exp(mobilityFactor / temperature);
			double rnd = randomDirectionMovement.nextDouble();

				if (rnd < exponent){
					// TODO: pick a random direction to mutate
					// TODO: if not valid direction, "continue;"
					// TODO: if here then direction to mutate is valid thus "apply said mutation"
					// TODO: replace current protein with mutated one
					// TODO: evaluate energy
					// TODO: increment timestep
					currentTimeStep++;
					// TODO: lower temperature
					temperatureManager.getNextTemprature();
					// TODO: recalculate mobility
					// TODO: if new conformation is best so far(lowest energy) replace current best

			}

			/*if (random.nextFloat() < config.crossoverRate) {
				in = protein;
				out = new Protein(protein.dimensions, protein.sequence, protein.random, protein.getGrid() ,
						"Protein " + runNumber, false);
				Protein.crossover(in1, in2, out1, out2, random);
				population.updateLastTwo();

			} else {
				in1 = population.chooseProtein();
				if (in1.getConformation().sequenceSize() == 0)
					throw new RuntimeException("in1.conformation.sequenceSize() == 0\n"+"energy = "+in1.getEnergy());
				out1 = population.getLast();
				mutationManager.mutate(in1, out1, 10);
				population.updateLastTwo();
			}

			if (SHOW_BEST_IN_GUI
					&& (this.bestProtein == null || best.getEnergy() < this.bestProtein
							.getEnergy())) {
				this.bestProtein.reset();
				this.bestProtein.setConformation(best.getConformation());
				// this.bestProtein=new Protein(best);
			
			}

			runningTime = (System.currentTimeMillis() - startTime);
			if (currentTimeStep % config.reportEvery == 0) {
				log.collectStatistics(population, currentTimeStep, numberOfGenerations, runningTime);
			}*/
		}
		log.printRun();
		runNumber++;
	}

	public int getRandomMonomer() {
		return randomMonomerIndex.nextInt(sequenceSize);
	}
		
	/*public void testPopulation() {
		for (int i = 0; i < population.sequenceSize()-2; i++) {
			Protein protein = population.getByRef(i);
			if (protein.getEnergy() == Float.MAX_VALUE)
				throw new RuntimeException("This is weird "+protein);
		}
	}*/

		
}
