package main;


import java.io.IOException;

import EDA.EDAlog;



public class PopulationOutputPrinter extends OutputPrinter {
	public PopulationOutputPrinter(Configuration config) throws IOException {
		super(config);
	}
	
	public void printRun(int runNumber,Log log1){
		boolean isEDA = false;
		writer.println("<run "+
	                   "             number=\""+runNumber+"\" "+
	                   "numberOfGenerations=\""+config.numberOfGenerations+"\" "+
				       "     populationSize=\""+config.populationSize+"\">");
		if (log1 instanceof EDAlog) {
			isEDA = true;
		}
		PopulationLog log = (PopulationLog) log1;
		for (int i = 0; i<log.getAverageEnergy().length; i++) {
			writer.println("\t<trajectory sample=\""+i+"\" "+
		                      "generation=\""+log.getGneration(i)+"\" ");
			if (isEDA) writer.println("temperature=\""+((EDAlog) log).getTemperature(i)+"\" ");
			writer.println("bestEnergy=\""+log.getBestEnergy()[i]+"\" "+
					          "averageEnergy=\""+log.getAverageEnergy()[i]+"\" "+
		                      "worstEnergy=\""+log.getWorstEnergy()[i]+"\" "+
					          "bestConformation=\""+log.getBestConformations()[i]+"\"/>");
		}
		writer.println("</run>");
	}
	
}
