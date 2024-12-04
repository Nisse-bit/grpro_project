package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.*;

import java.awt.*;
import java.util.*;

public class Fungus extends Animal {
    private String size;

    //Konstruktør
    public Fungus(String size) {
        this.size = size;
        if (size.equals("small")) {
            this.di = new DisplayInformation(Color.yellow, "fungi-small", true);

        }
        else if (size.equals("big")) {
            this.di = new DisplayInformation(Color.yellow, "fungi-large", true);
        }
        else throw new IllegalArgumentException("*** Size must be either \"big\", or \"small\"");
        }


    //Metoder
    @Override
    public void act(World world) {
        older(world);
        spreadInfection(world);

        if (dies) {
            world.delete(this);
        }
    }


    /**
     * Spreder infection til alle Carcasses i nearestCarcasses.
     *
     * @param world verdenen som svampen er i
     */
    public void spreadInfection(World world) {
        ArrayList<Carcass> nearestCarcasses = NearestCarcasses(world);

        int carcassListSize = nearestCarcasses.size();
        int infectedcounter = 0;
        for (Carcass carcass : nearestCarcasses) {
            infectedcounter++;
            if (carcass.getInfected()) {


            } else {
                carcass.Infect();

            }
        }
    }

    /**
     * Metoden finder Carcasses i en radius på 5, sætter dem på en liste og returnerer listen.
     * @param world verdenen som svampen er i
     * @return De tætteste carcasses i en liste
     */
    public ArrayList<Carcass> NearestCarcasses(World world) {
        //nearest carcasses
        ArrayList<Carcass> nearestCarcasses = new ArrayList<>();
        Map<Object, Location> entities = world.getEntities();

        Set<Location> nearbyTiles = world.getSurroundingTiles(world.getLocation(this), 5);

        for (Object object : entities.keySet()) {
            if (object instanceof Carcass carcass && nearbyTiles.contains(entities.get(object))) {
                nearestCarcasses.add(carcass);
            }
        }

        return nearestCarcasses;
    }
    @Override
    public void older(World world){
        age++;
        if (age == 30 && size.equals("small")) {
            dies = true;
        }
        if (age == 60) {
            dies = true;
        }
    }
}
