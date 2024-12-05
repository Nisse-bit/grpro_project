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
            this.die(world);
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
            } if (world.getTile(l) instanceof Carcass carcass) {
                // måske kun spise lidt af den?
                world.delete(carcass);
                adjustEnergy(world,30);
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
     * @param
     */
    public Object getTarget(){
        return target;
    }

    public void hunt(World world) {
        Location preylocation;

        if (energy[1] < 81) {  // måske den her if rykkes til act

           // Map<Object, Location> entities = world.getEntities();


            List<Rabbit> rabbits = new ArrayList<>();
            for (Location l : world.getSurroundingTiles(world.getLocation(this), 3)) {
                Object o = world.getTile(l);
                if (o instanceof Rabbit rabbit) {
                    rabbits.add(rabbit);
                }
            }
            if (rabbits.isEmpty()) {
                this.moveRandomly(world);
                return;
            }
//Finder den nærmeste kanin, og bevæger sig mod den
            Rabbit nearestRabbit = rabbits.getFirst();
            int previous_dX = Integer.MAX_VALUE;
            int previous_dY = Integer.MAX_VALUE;

            for (Rabbit rabbit : rabbits) {
                int thisX = world.getLocation(this).getX();
                int thisY = world.getLocation(this).getY();

                int thatX = world.getLocation(rabbit).getX();
                int thatY = world.getLocation(rabbit).getY();

                int dX = Math.abs(thisX - thatX);
                int dY = Math.abs(thisY - thatY);

                boolean betterX = dX <= previous_dX;
                boolean betterY = dY <= previous_dY;

                if (betterX && betterY) {
                    nearestRabbit = rabbit;
                }

                previous_dX = dX;
                previous_dY = dY;
            }
            this.moveTowards(world, world.getLocation(nearestRabbit));
        }
    }

    public void createTerritory (World world){
        this.territory = world.getLocation(this);
    }
}