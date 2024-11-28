package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.*;

import java.awt.*;
import java.util.*;

public class Bear extends Animal {
    public Location territory;
    protected Set<Location> territorytiles;

    //Konstruktører
    //uden territorie
    public Bear() {
        di = new DisplayInformation(Color.BLACK, "bear", true);
        territory = null;
        energy = new int[]{150, 150};
    }

    //med territorie
    public Bear(Location territory) {
        di = new DisplayInformation(Color.BLACK, "bear", true);
        this.territory = territory;
        energy = new int[]{150, 150};
    }

    //Metoder
    @Override
    public void act(World world) {
        System.out.println(world.getLocation(this));
        if (territory == null) {
            //find et territory som den kan knytte sig til.
            // siden bjørne ikke er flok dyr som ulve, har de et individuelt territorie defor "finder den et territoerie der hvor den står" hvis den ikke har et.
            createTerritory(world);
        } else {
            this.bearBrain(world);// bevæger sig i sit og mod det hvis den er ude af det territory.
        }
        if(dies){world.remove(this);}
    }
/* Bjørnen er meget territoriel, og har som udgangspunkt ikke et bestemt sted den
’bor’. Den knytter sig derimod til et bestemt område og bevæger sig sjældent ud herfra.
Dette territories centrum bestemmes ud fra bjørnens startplacering på kortet
 */

    /**
     * Returnerer Set med alle de felter hvor bjørnen bevæger sig i sit territeorie.
     *
     * @param world
     * @param territory bjønens start territory.
     * @return Set<Location>
     */
    public Set<Location> getTerritoryTiles(World world, Location territory) {
        // bjørnen bevæger sig kun inden for 2 radius af sit startfelt.
        Set<Location> territoryTiles = world.getSurroundingTiles(territory, 1);
        territoryTiles.add(territory);
        return territoryTiles;
    }

    /**
     * Bevægelses logik for bjørnen, der gør, at den går rundt inde i sit territorie tilfældigt.
     * Hvis den kommer til at bevæge sig ud fra sit territorie, så prøver den at bevæge sig tilbage.
     * @param world
     */
    public void bearBrain(World world) {
        territorytiles = getTerritoryTiles(world, territory);


        if (territorytiles.contains(world.getLocation(this))) {
            this.moveRandomly(world);

        } else {
            Location olocation = world.getLocation(this);
            this.moveTowards(world, territory);
            // bjørn kan sidde fast, hvis bjørnen har en busk i vejen vil den stå stille, derfor skal vi gemme dens gamle location
            if (olocation.equals(world.getLocation(this))) {
                moveRandomly(world);
                System.out.println("bush inna da way");
                // destroyberrybush()  // eat()
            }
        }


    }

    public void createTerritory(World world) {
        this.territory = world.getLocation(this);
    }

}


