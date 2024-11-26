package Plants;

import itumulator.world.NonBlocking;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Grass extends Plant {


    //Konstruktør
    public Grass() {
        di = new DisplayInformation(Color.green, "grass", true);
    }

    //Metoder
    @Override
    public void act(World world) {
        tryToSpread(world);
    }

}
