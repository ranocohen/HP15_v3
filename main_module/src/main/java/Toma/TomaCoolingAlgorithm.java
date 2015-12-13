package Toma;

import main.Configuration;
import temperature.TemperatureManager;

/**
 * Created by ITAY on 13/12/2015.
 */
public class TomaCoolingAlgorithm implements TemperatureManager {
    private final float initialTemperature;
    private float currentTemperature;

    private final float DELTA = (float)0.998;

    public TomaCoolingAlgorithm(Configuration config) {
        initialTemperature = config.initialTemperature;
        currentTemperature = initialTemperature;
    }
    public void reset(){
        currentTemperature = initialTemperature;
    }
    public float getNextTemprature() {
        float out = currentTemperature;
        currentTemperature = currentTemperature * DELTA;
        return out;
    }

    public float getCurrentTemperature(){
        return currentTemperature;
    }
}
