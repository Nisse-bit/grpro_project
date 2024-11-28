package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.*;
import Plants.*;
import java.awt.*;
import java.util.*;

public class Bear extends Animal {
    public Location territory;
    protected Set<Location> territorytiles;
    protected Object target;
    protected Location plocation; // target location p for prey
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
        if(world.isDay()) {
            //hunt(world);
            // der skal en boolean på her orker ikke mere
        }
        System.out.println(world.getLocation(this));
        System.out.print(" "+energy[1]);
        if (territory == null) {
            //find et territory som den kan knytte sig til.
            // siden bjørne ikke er flok dyr som ulve, har de et individuelt territorie defor "finder den et territoerie der hvor den står" hvis den ikke har et.
            createTerritory(world);
        } else {
            this.bearBrain(world);// bevæger sig i sit og mod det hvis den er ude af det territory.
        }
        if(dies){world.remove(this);}

        adjustEnergy(world,-5); // det tager meget energi at være bjørn
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

        tryToEat(world);
        //tryToBreed(world);


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

    @Override //Overidder selvom animals spise funktioner er ret ens.
    public void tryToEat(World world) {

        Set<Location> surroudingtiles = world.getSurroundingTiles(world.getLocation(this));
        //tilføjer sit eget felt, fordi den kan godt spise græs under den.
        Location Blocation = world.getLocation(this);

        for (Location location: surroudingtiles) {
            Object object = world.getTile(location);

            if (object instanceof Grass && Blocation == world.getLocation(object)) {
                // hvis bjørnen har meget energi, vil den have spist meget mad, eller være fuld af næring.
                // derfor skal den spise græs til at få fibre og fordøje dens mad.
                // ligesom kaninen kan bjørnen kun spise græs hvis den står over den.

                if(energy[1] < 130) {
                    // hvis energi er over 130, vil den spise græs {
                    this.adjustEnergy(world,2);
                    world.delete(object);
                }
            }
            if (object instanceof BerryBush) {
                if(((BerryBush) object).hasFruits) {
                    ((BerryBush) object).loseBerries();
                    adjustEnergy(world, 10);

                    // der er en chance for at bjørnen spiser Hele busken, hvis den er sulten nok,
                    // men så er den sulten på lang sigt.

                    if(energy[1] > 30) {
                        world.delete(object);
                        adjustEnergy(world, 9);
                    }
                }


            }
            // bjørnen spiser kaniner som kommer tæt på.
            if (object instanceof Rabbit) {
                adjustEnergy(world, 20); // meget energi fra kaniner
                world.delete(object);
            }
        }

    }

    /**jager dyr, nå den er sulten,
     *
     * @param world
     */
    public void hunt(World world){



        Location plocation;

        if(energy[1] < 60) {  // måske den her if rykkes til act
            System.out.println("Im hungry as shit lets hunt");
            Map<Object, Location> entities = world.getEntities();

            if (!entities.containsKey(target)||target == null) {
                for (Object object : entities.keySet()) {
                    if(object instanceof Rabbit) {
                        plocation = entities.get(object);
                        this.moveTowards(world, plocation);

                        //kunne godt tænke mig at den gemmer location, så den jager den samme kanin, og ikke en ny hver gang den her blir kørt
                        Object target = object;

                    }
                }
            } else {
                moveTowards(world, world.getLocation(target));
            }


        }
    }
    public void createTerritory(World world) {
        this.territory = world.getLocation(this);

    }

}


