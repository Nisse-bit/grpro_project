package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.World;
import java.awt.Color;

public class Wolf extends Animal {
    private int packID;
    private Wolf packAlpha;

    //Konstruktør
    public Wolf(int packID) {
        //Universelle felter instansieres
        this.di = new DisplayInformation(Color.RED,"wolf-small",true);
        this.energy = new int[]{100,100};

        //Unikke felter instansieres
        this.packID = packID;
        this.packAlpha = null;
    }

    //Metoder
    @Override public void act(World world){
        if(packAlpha == null){this.assertAlpha(world);}
        this.older(world);
        //Her skal vi lave en form for wolfBrain der

        if(dies){world.remove(this);}
    }

    @Override public void older(World world){
        age++;

        if(age == 40){
            di = new DisplayInformation(Color.RED,"wolf",true);
            if(packAlpha == this){di = new DisplayInformation(Color.RED,"wolf-alpha",true);}
        }
        if(age == 120){
            dies = true;
        }
    }

    /**
     * Ulven afgør om dens pack har en alfa -- hvis ikke bliver den selv til alfa.
     * @param world verdenen som ulven er i
     */
    public void assertAlpha(World world){
        //Kigger alle objekter i verden igennem
        for(Object o : world.getEntities().keySet()){
            //Hvis der en ulv...
            if(o instanceof Wolf w){
                //Tjek om w er samme pack og om den er alpha -- hvis w er alpha, gemmer vi den som denne ulvs alfa
                if(w.packID == this.packID && w.packAlpha == w){
                    this.packAlpha = w;
                }
            }
        }

        //Hvis denne ulvs packAlpha på dette tidspunkt stadig er null, er det fordi der ikke er en alfa
        //Derfor gør vi den nuværende ulv til alfa
        if(packAlpha == null){
            packAlpha = this;
            di = new DisplayInformation(Color.RED,"wolf-alpha",true);
            System.out.println(this + ": I'm the alpha of pack ["+ packID +"]!");
        }
        //Det vil sige, at for alle andre ulve finder de nu ud af, at pack'en har en alpha
    }
}