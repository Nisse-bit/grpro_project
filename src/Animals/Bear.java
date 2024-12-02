package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.*;
import Plants.*;

import java.awt.Color;
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

            hunt(world);



        if (territory == null) {
            //find et territory som den kan knytte sig til.
            // siden bjørne ikke er flok dyr som ulve, har de et individuelt territorie defor "finder den et territoerie der hvor den står" hvis den ikke har et.
            createTerritory(world);
        } else {
            this.bearBrain(world);// bevæger sig i sit og mod det hvis den er ude af det territory.
        }
        if (dies) {
            world.delete(this);
        }

        adjustEnergy(world, -2); // det tager meget energi at være bjørn
    }
    /* Bjørnen er meget territoriel, og har som udgangspunkt ikke et bestemt sted den
    ’bor’. Den knytter sig derimod til et bestemt område og bevæger sig sjældent ud herfra.
    Dette territories centrum bestemmes ud fra bjørnens startplacering på kortet
     */

    /**
     * Returnerer Set med alle de felter hvor bjørnen bevæger sig i sit territorium.
     * @param world verdenen som bjørnen er i
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
     *
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
                System.out.println(this + ": bush inna da way");
                // destroyberrybush()  // eat()
            }
        }
    }

    @Override
    public void tryToEat(World world) {

        Set<Location> surroudingtiles = world.getSurroundingTiles(world.getLocation(this));
        //tilføjer sit eget felt, fordi den kan godt spise græs under den.
        Location e = world.getLocation(this);
        // bjørnen spiser kun græs den står på, alt andet ville være mærkeligt!
        if (world.containsNonBlocking(e)) {
            if (world.getNonBlocking(e) instanceof Grass g) {
                if (new Random().nextInt(10) < 2) { //20% sandsynlighed for at spise
                    world.delete(g);
                    this.adjustEnergy(world, 4); //Bjørnen får energi
                }
            }
        }
        for (Location l : surroudingtiles) {
            if (world.getTile(l) instanceof BerryBush bush) {
                // der er en chance for at bjørnen spiser Hele busken, hvis den er sulten nok,
                if (energy[1] <= 15) {
                    world.delete(bush);
                    adjustEnergy(world, 15);
                }

                if (bush.getFruit(world)) {
                    if (new Random().nextInt(10) <= 2) { //20% sandsynlighed for at spise
                        this.adjustEnergy(world, 10);
                        bush.loseBerries();
                    }
                }
            }
        }
        // bjørnen spiser kaniner som kommer tæt på.
        for (Location l : surroudingtiles) {
            if (world.getTile(l) instanceof Rabbit rabbit) {
                this.adjustEnergy(world, 20);
                rabbit.dies = true;
            }
        }
    }

    /**
     * Bjørnen jager dyr, når den er sulten,
     * @param world
     */

    public void hunt(World world) {
        Location preylocation;

        if (energy[1] < 150) {  // måske den her if rykkes til act
            System.out.println("Im hungry as shit lets hunt");
            Map<Object, Location> entities = world.getEntities();

            if(world.isNight()){return;} // den kan ikke kører om natte fordi så gemmer kaninerne sig


            if (!entities.containsKey(target) || !world.contains(target) || world.getLocation(target) == null) {
                for (Object object : entities.keySet()) {
                    if (object instanceof Rabbit) {
                        preylocation = entities.get(object);


                        //kunne godt tænke mig at den gemmer location, så den jager den samme kanin, og ikke en ny hver gang den her blir kørt
                        target = object;

                    }
                }
            } else {


            }
            this.moveTowards(world, world.getLocation(target));

        }
    }

    public void createTerritory (World world){
        this.territory = world.getLocation(this);
    }
}