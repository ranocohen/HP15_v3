package Toma;

import main.Configuration;
import temperature.TemperatureManager;

public class TomaCoolingAlgorithm implements TemperatureManager {

    private final float mInitialTemperature;
    private float mCurrentTemperature;

    private final float DELTA = (float)0.998;

    public TomaCoolingAlgorithm(Configuration config) {
        mInitialTemperature = config.initialTemperature;
        mCurrentTemperature = mInitialTemperature;
    }
    public void reset(){
        mCurrentTemperature = mInitialTemperature;
    }
    
    public float getNextTemprature() {
        float out = mCurrentTemperature;
        mCurrentTemperature = mCurrentTemperature * DELTA;
        return out;
    }

    public float getCurrentTemperature(){
        return mCurrentTemperature;
    }
}
