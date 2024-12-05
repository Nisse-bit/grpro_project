package Animals;

import itumulator.executable.DisplayInformation;

import java.awt.Color;
import java.util.Random;

import itumulator.world.Location;
import itumulator.world.World;

public class Carcass extends Animal {
    private boolean infected;
    private String size;

    //Konstruktør
    public Carcass(boolean infected, String size) {
        //Arvede felter instansieres
        this.dies = false;
        if (size.equals("big")) {
            this.di = new DisplayInformation(Color.magenta, "carcass", true);
        } else if (size.equals("small")) {
            this.di = new DisplayInformation(Color.magenta, "carcass-small", true);
        } else {
            throw new IllegalArgumentException("size not recognized; must be either \"big\" or \"small\"");
        }

        //Unikke felter instansieres
        this.infected = infected;
        this.size = size;
    }

    //Metoder
    @Override
    public void act(World world) {
        this.older(world);
        if (dies) {
            this.die(world);
        }
    }
    @Override
    public void older(World world) {
        //Bliver ældre
        age++;
        //Opdaterer udseende og justerer max-energi
        if (age == 30){
            dies = true;
        }
    }

    public boolean getInfected(){
        return infected;
    }


    /**
     * Hvis ådslet bliver ramt af en spore fra svamp, bliver den infected.
     */
    public void Infect(){
        infected = true;
    }
    
    public String getSize(){
        return size;
    }

    @Override
    public void die(World world){
        Location l = world.getLocation(this);
        if(infected){
            world.delete(this);

            Fungus fungus = new Fungus(size);
            world.setTile(l, fungus);
        }
        else{
            world.delete(this);
        }
    }
}
