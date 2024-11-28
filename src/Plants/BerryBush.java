package Plants;

import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.*;
import java.util.Random;

public class BerryBush extends Plant {
    public boolean hasFruits;

    //Konstruktør
    public BerryBush() {
        this.hasFruits = false;
        di = new DisplayInformation(Color.green,"bush",true);
    }

    //Metoder
    @Override
    public void act(World world) {
        this.grow(world);
    }

    /**
     * Hvis busken har bær bliver displayInformationen opdateret med dette.
     * Hvis busken ikke har bær er der en 2% chance for at busken gror bær, og displayInformation opdateres.
     * @param world verdenen som busken er i
     */
    public void grow(World world) {
        if (this.hasFruits) {
            di = new DisplayInformation(Color.blue,"bush-berries",true);
        }
        else {
            di = new DisplayInformation(Color.green,"bush",true);
            Random random = new Random();
            if (random.nextInt(50) <= 10) { //20% chance for at gro bær
                this.hasFruits = true;
            }
        }
    }

    public void loseBerries(){
        if (this.hasFruits){
            this.hasFruits = false;
        }
    }

    public boolean getFruit(World world){
        return hasFruits;
    }
}



