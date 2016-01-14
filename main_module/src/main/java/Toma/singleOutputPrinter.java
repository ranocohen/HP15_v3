package Toma;

import main.Configuration;
import main.Log;
import main.OutputPrinter;
import java.io.IOException;
import Toma.TomaLog;

public class singleOutputPrinter extends OutputPrinter{
    public singleOutputPrinter(Configuration config) throws IOException {
        super(config);
    }

    public void printRun(int runNumber, Log log1){
        TomaLog log = (TomaLog) log1;
        writer.println("<run "+
                "             number=\""+runNumber+"\" "+
                "numberOfTimeSteps=\""+config.numberOfGenerations+"\" "+
                "     temperature=\""+ log.getTemperature()+"\">");

        /*for (int i = 0; i<log.getAverageEnergy().length; i++) {
            writer.println("\t<trajectory sample=\""+i+"\" "+
                    "generation=\""+log.getGneration(i)+"\" ");
            writer.println("bestEnergy=\""+log.getBestEnergy()[i]+"\" "+
                    "averageEnergy=\""+log.getAverageEnergy()[i]+"\" "+
                    "worstEnergy=\""+log.getWorstEnergy()[i]+"\" "+
                    "bestConformation=\""+log.getBestConformations()[i]+"\"/>");
        }*/
        writer.println("</run>");
    }
}
