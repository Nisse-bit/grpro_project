package Holes;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.NonBlocking;

abstract class Hole implements NonBlocking, DynamicDisplayInformationProvider {
    protected DisplayInformation di;

    //Konstruktør
    public Hole(){
    }

    @Override public DisplayInformation getInformation() {
        return di;
    }
}
