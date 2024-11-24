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

public class Grass implements Actor, NonBlocking, DynamicDisplayInformationProvider {
    private DisplayInformation di;

    //Konstruktør
    public Grass() {
        di = new DisplayInformation(Color.green, "grass", true);
    }

    //Metoder
    @Override
    public DisplayInformation getInformation() {
        return di;
    }

    @Override
    public void act(World world) {
        tryToSpread(world);
    }

    //Spread
    /**
     * Metoden kaldes på et græs-objekt, og får objekterne til at prøve at sprede sig.
     * @param world
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
            world.setTile(l, new Grass()); //Sætter nyt græs
        }
    }
}