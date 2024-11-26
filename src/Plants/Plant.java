package Plants;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class Plant implements Actor, DynamicDisplayInformationProvider {
    protected DisplayInformation di;

    //Konstruktør
    protected Plant() {
    }

    @Override
    public void act(World world) {
    }

    public DisplayInformation getInformation() {
        return di;
    }

    /**
     * Planter har 2% sandsynlighed for at sprede sig.
     * @param world verdenen som planten er i
     */
    public void tryToSpread(World world) {
        List<Location> surroundingAvailableNonBlockingTiles = new ArrayList<>(); //Opretter en liste til tilgængelige felter
        for (Location l : world.getSurroundingTiles(world.getLocation(this))) { //Fylder listen...
            if (!world.containsNonBlocking(l)) { //... med de tiles som ikke indeholder NBO'er
                surroundingAvailableNonBlockingTiles.add(l);
            }
        }
        if (surroundingAvailableNonBlockingTiles.isEmpty()) {
            return;
        } //Hvis der er ingen ledige pladser, stop

        if (new Random().nextInt(0, 50) == 42) { //2% chance for at sprede sig
            int r = new Random().nextInt(0, surroundingAvailableNonBlockingTiles.size()); //Vælger tilfældigt index
            Location l = surroundingAvailableNonBlockingTiles.get(r); //Gemmer lokationen med indexnummer r
            if (this.getClass() == Grass.class) {
                world.setTile(l, new Grass()); //Sætter nyt græs
            }
            if (this.getClass() == BerryBush.class) {
                world.setTile(l, new BerryBush()); //Sætter ny berrybush
            }

        }


    }
}