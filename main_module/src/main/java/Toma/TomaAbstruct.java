package Toma;

import gui.RunningView;
import main.*;
import mutation.MutationManager;
import temperature.TemperatureManager;

import java.io.IOException;
import java.util.Random;

public abstract class TomaAbstruct implements Optimizer {
	public static final boolean debug = true;
	protected final boolean SHOW_BEST_IN_GUI = true;
	// private GUIGrid guiGrid;
	// private GUIGrid bestguiGrid;
	protected RunningView gui;

	protected boolean isFirstInit;






	// Data for All the Run
	/** The protein population. */
	protected TomaProtein protein;

	/** The mutation manager. */
	protected MutationManager mutationManager;

	/** The current time step num. */
	protected int currentTimeStep;

	/** The random number for usage in the run. */
	protected Random random;

	/** the output file writer. */
	protected OutputPrinter fileWriter;

	/** The configuration class to get User given arguments. */
	protected Configuration config;

	protected int numberOfGenerations, reportEvery;
	protected TomaProtein bestProtein;


	protected TomaCoolingAlgorithm temperatureManager;
	/**
	 *
	 *
	 * Instantiates a new Genetic Algorithm (GA) run.
	 *
	 * @param fileWriter
	 *            the file writer to write the stored data in the run
	 * @param config
	 *            the configuration object to load user given data
	 *
	 */

	public TomaAbstruct(OutputPrinter fileWriter, Configuration config,
						MutationManager mutationManager, TemperatureManager temperatureManager) {

		this.temperatureManager = (TomaCoolingAlgorithm) temperatureManager;
		this.config = config;
		this.random = config.random;
		this.fileWriter = fileWriter;
		this.mutationManager = mutationManager;
		isFirstInit = true;
		this.numberOfGenerations = config.numberOfGenerations;
		this.reportEvery = config.reportEvery;
		int size = (config.sequence.length() * 2) - 1;
		this.bestProtein = new TomaProtein(config.dimensions, new Sequence(
				config.sequence), random, Grid.getInstance(size,
				config.dimensions), "best protein");
		gui = new RunningView("results", size, size);

	}

	/**
	 * create new random population
	 * 
	 * @param runNumber
	 */
	public void initiate(int runNumber) {
		Dimensions dimensions = config.dimensions;
		Sequence sequence = new Sequence(config.sequence);
		Grid grid = Grid.getInstance(config.sequence.length(), dimensions);

		System.out.println("Initializing run # " + runNumber);
		protein = new TomaProtein(dimensions, sequence, random, grid, "First");
		currentTimeStep = 0;
		//not sure if this is needed, or how
		if (isFirstInit) { // if this is the first init then create the needed
							isFirstInit = false;// arrays else. clear the existing one's
			//population.sort();
			// Collections.sort(population, Collections.reverseOrder());
		}
			
	}

	/**
	 * Gets the fitest.
	 * 
	 * @return the fitest
	 * @throws IOException 
	 */
	

	
	public abstract void execute() throws IOException;
}
