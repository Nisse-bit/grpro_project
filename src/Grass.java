import itumulator.executable.DisplayInformation;
import itumulator.world.NonBlocking;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

import itumulator.executable.DynamicDisplayInformationProvider;
import java.awt.Color;

public class Grass implements NonBlocking, DynamicDisplayInformationProvider{
    DisplayInformation di;
    World world;
    Location grassLocation;
    // skal græs have en amount eller skal vi lade dem genere i world.

    public Grass() {
        di = new DisplayInformation(Color.green, "grass", true);
    }

    @Override public DisplayInformation getInformation(){
        return di;
    }

    public void spread2(World world){
        //Tjek om omkringliggende felter er non-blocking
        List<Location> surroundingTilesWithoutNBO;
        surroundingTilesWithoutNBO = new LinkedList<>();
        for(Location l : world.getSurroundingTiles(world.getLocation(this))){
            if(!world.containsNonBlocking(l)) {
                surroundingTilesWithoutNBO.add(l);
            }
        }
        if(surroundingTilesWithoutNBO.isEmpty()) {return;}
        //Hvis surroundingTilesWithoutNBO IKKE er tom, spred til et tilfældigt omkringliggende felt
            //Implementér sandsynlighed for spredning
            //...
    }

    public void spread(World world) {
        // random til hvis der ikke skal være så meget græs.
        Random random = new Random();
        // spredning af græs
        Set<Location> GrassTiles = world.getSurroundingTiles(this.grassLocation);
        //For each for græssets surrounding tiles
        for (Location location : GrassTiles) {
            //Hvis at locationen ikke har non blocking skal der kunne komme nyt græs.
            
            if (!world.containsNonBlocking(location)) {  // Checker kun om der er nonblocking, hvad hvis der er en rabbit. skal græs kunne spawne under en rabbit?
                //

                //Opdatere world location | er det nyttigt? Nok ikke
                world.setCurrentLocation(location);
                
                //Hvis der er for meget græs tilføj tilfældighed til genering af nyt græs. her under
                if (!GrassTiles.isEmpty()) {
                    int x = random.nextInt(100);


                    if (x < 50) {  // chancen for at den lavet nyt græs.
                        // siden det er et Set, og der ingen index positioner er, vil den tage den første tilfældige ledige plads og sætte græs.

                        Grass grass = new Grass();

                        world.setTile(location, grass);
                    }
                }
            }
        }
    }
    public void kill(World world){
        world.delete(this);
    }

}
