package Plants;

import itumulator.world.NonBlocking;
import itumulator.world.World;
import itumulator.executable.DisplayInformation;

import java.awt.Color;

public class Grass extends Plant implements NonBlocking {

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
