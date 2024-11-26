package Plants;

import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.*;

public class BerryBush extends Plant {
    //kan ikke spises af kaniner

    //Konstruktør
    public BerryBush() {
        di = new DisplayInformation(Color.green, "bush-berries", true);
    }

    //Metoder
    @Override
    public void act(World world) {
    }

    ;
}


