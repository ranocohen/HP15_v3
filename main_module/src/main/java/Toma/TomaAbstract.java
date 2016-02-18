package Toma;

import gui.RunningView;
import main.*;
import mutation.MutationManager;
import temperature.TemperatureManager;

import java.io.IOException;
import java.util.Random;

public abstract class TomaAbstract implements Optimizer {

	public static String DEBUG_LOG = TomaAbstract.class.getCanonicalName();
	public static final boolean DEBUG = true;
	protected final boolean SHOW_BEST_IN_GUI = true;

	// private GUIGrid guiGrid;
	// private GUIGrid bestguiGrid;
	protected RunningView gui;

	protected boolean isFirstInit;

	// Data for All the Run
	/** The protein population. */
	protected TomaProtein protein;

	protected TomaProtein mutatedProtein;

	/** The mutation manager. */
	protected TomaMutationManager mutationManager;

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
	public TomaAbstract(OutputPrinter fileWriter, Configuration config,
						TomaMutationManager mutationManager, TemperatureManager temperatureManager) {

		this.temperatureManager = (TomaCoolingAlgorithm) temperatureManager;
		this.config = config;
		this.random = config.random;
		this.fileWriter = fileWriter;
		this.mutationManager = mutationManager;
		isFirstInit = true;
		this.numberOfGenerations = config.numberOfGenerations;
		this.reportEvery = config.reportEvery;
		int size = (config.sequence.length() * 2) - 1;
		//TODO: check if we need to change this
		this.bestProtein = new TomaProtein(config.dimensions, new Sequence(
				config.sequence), random, Grid.getInstance(size,
				config.dimensions), "best protein");
		gui = new RunningView("results", size, size);

	}

	/**
	 * create new random population
	 * 
	 * @param runNumber run number
	 */
	public void initiate(int runNumber) {

		Grid grid = Grid.getInstance(config.sequence.length(), config.dimensions);

		System.out.println("Initializing run # " + runNumber);
		this.protein = new TomaProtein(config.dimensions, new Sequence(
				config.sequence), random, grid, "basic protein");
		this.protein.updateMobilityFactor();
		currentTimeStep = 0;
		grid.reset(this.protein);
		this.mutatedProtein = new TomaProtein(config.dimensions, new Sequence(
				config.sequence), random, grid, "mutated");

		/*if (isFirstInit) {

			isFirstInit = false;
		}*/
			
	}

	public abstract void execute() throws IOException;
}
