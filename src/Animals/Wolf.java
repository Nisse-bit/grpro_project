package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

import Holes.WolfDen;

public class Wolf extends Animal {
    private int packID;
    private Wolf packAlpha;
    private Location denLocation;

    //Konstruktør
    public Wolf(int packID) {
        //Universelle felter instansieres
        this.di = new DisplayInformation(Color.RED, "wolf-small", true);
        this.energy = new int[]{100, 100};

        //Unikke felter instansieres
        this.packID = packID;
        this.packAlpha = null;
        this.denLocation = null;
    }

    //Metoder
    @Override
    public void act(World world) {
        if (packAlpha == null) {
            this.assertAlpha(world);
        }
        this.older(world);
        this.wolfBrain(world);

        if (dies) {
            world.remove(this);
        }
    }

    @Override
    public void older(World world) {
        age++;

        if (age == 40) {
            di = new DisplayInformation(Color.RED, "wolf", true);
            if (packAlpha == this) {
                di = new DisplayInformation(Color.RED, "wolf-alpha", true);
            }
        }
        if (age == 120) {
            dies = true;
        }
    }

    /**
     * Ulven afgør om dens pack har en alfa -- hvis ikke bliver den selv til alfa.
     * @param world verdenen som ulven er i
     */
    public void assertAlpha(World world) {
        //Kigger alle objekter i verden igennem
        for (Object o : world.getEntities().keySet()) {
            //Hvis der en ulv...
            if (o instanceof Wolf w) {
                //Tjek om w er samme af flok og om den er alfa -- hvis w er alfa, gemmer vi den som denne ulvs alfa
                if (w.packID == this.packID && w.packAlpha == w) {
                    this.packAlpha = w;
                }
            }
        }

        //Hvis denne ulvs packAlpha på dette tidspunkt stadig er null, er det fordi der ikke er en alfa i flokken
        //Derfor gør vi den nuværende ulv til alfa
        if (packAlpha == null) {
            packAlpha = this;
            di = new DisplayInformation(Color.RED, "wolf-alpha", true);
            System.out.println(this + ": I'm alpha of pack [" + packID + "]!");
        }
        //Det vil sige, at for alle andre ulve i flokken, finder de nu ud af, at flokken har en alpha
    }

    public void wolfBrain(World world) {
        //Gemmer ulvens hules lokation, hvis den ikke allerede er gemt
        if (denLocation == null) {
            for (Object o : world.getEntities().keySet()) {
                if ((o instanceof WolfDen wd) && (wd.getPackID() == this.packID)) {
                    denLocation = world.getLocation(wd);
                }
            }
        }

        if (!onMap && world.getCurrentTime() == 1) {
            //Ulven finder en tilfældig plads at hoppe ud på
            List<Location> reappearableTiles = new ArrayList<>(world.getEmptySurroundingTiles(denLocation));
            if (world.isTileEmpty(denLocation)) {
                reappearableTiles.add(denLocation);
            }
            if (reappearableTiles.isEmpty()) {
                dies = true;
                System.out.println("[" + this + "] suffocated in its den");
                return; //Hvis der er ingen ledige pladser, dør ulven
            }
            Location reappearTile = reappearableTiles.get(new Random().nextInt(reappearableTiles.size()));
            world.setTile(reappearTile, this);
            onMap = true;
            canBreed = false;
            this.adjustEnergy(world, -1);
            return; //Ulven hopper ud og stopper metoden så den ikke hopper videre
        }

        if (packAlpha == this) {
            //Hvis ulven er alfa og det er dag
            if (world.isDay()) {
                //Laver en liste af nære kaniner
                List<Rabbit> surroundingRabbits = new ArrayList<>();
                for (Location l : world.getSurroundingTiles(world.getLocation(this))) {
                    Object o = world.getTile(l);
                    if (o instanceof Rabbit r) {
                        surroundingRabbits.add(r);
                    }
                }
                //Hvis der er kaniner i omkringliggende felter, spiser ulven en af dem
                if (!surroundingRabbits.isEmpty()) {
                    //Vælger en tilfældig kanin
                    Rabbit r = surroundingRabbits.get(new Random().nextInt(surroundingRabbits.size()));
                    Location rLocation = world.getLocation(r); //Gemmer kaninens lokation
                    world.delete(r); //Spiser kaninen
                    world.move(this, rLocation); //Rykker over på kaninens lokation
                    this.adjustEnergy(world, 15);
                    return;
                } else {
                    //Ellers finder den kaniner i en range på 3
                    for (Location l : world.getSurroundingTiles(world.getLocation(this), 3)) {
                        if (world.getTile(l) instanceof Rabbit r) {
                            surroundingRabbits.add(r);
                        }
                    }
                    if (surroundingRabbits.isEmpty()) {
                        //Hvis der stadig ikke er nogen kaniner, bevæger ulven sig tilfældigt
                        this.moveRandomly(world);
                    } else {
                        //Ellers, jager ulven en tilfældig kanin
                        Rabbit r = surroundingRabbits.get(new Random().nextInt(surroundingRabbits.size()));
                        Location rLocation = world.getLocation(r); //Gemmer kaninens lokation
                        this.moveTowards(world, rLocation);
                    }
                }
            } else {
                //Ellers, hvis ulven er alfa og det er nat
                if (denLocation == world.getLocation(this)) {
                    world.remove(this);
                    onMap = false;
                    canBreed = true;
                } else {
                    this.moveTowards(world, denLocation);
                }
            }
        } else {
            //Ellers, hvis ulven er beta og det er dag
            if (world.isDay()) {
                //Laver en liste af nære kaniner
                List<Rabbit> surroundingRabbits = new ArrayList<>();
                for (Location l : world.getSurroundingTiles(world.getLocation(this))) {
                    Object o = world.getTile(l);
                    if (o instanceof Rabbit r) {
                        surroundingRabbits.add(r);
                    }
                }
                //Hvis der er kaniner i omkringliggende felter, spiser ulven en af dem
                if (!surroundingRabbits.isEmpty()) {
                    //Vælger en tilfældig kanin
                    Rabbit r = surroundingRabbits.get(new Random().nextInt(surroundingRabbits.size()));
                    Location rLocation = world.getLocation(r); //Gemmer kaninens lokation
                    world.delete(r); //Spiser kaninen
                    world.move(this,rLocation); //Rykker over på kaninens lokation
                    this.adjustEnergy(world, 15);
                    return;
                } else {
                    //Ellers, bevæger kaninen sig mod flokkens alfa
                    this.moveTowards(world, world.getLocation(packAlpha));
                }
            } else {
                //Ellers, hvis ulven er beta og det er nat
                if (denLocation == world.getLocation(this)) {
                    world.remove(this);
                    onMap = false;
                    canBreed = true;
                } else {
                    this.moveTowards(world, denLocation);
                }
            }
        }
    }
}