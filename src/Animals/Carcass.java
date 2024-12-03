package Animals;
import itumulator.executable.DisplayInformation;
import java.awt.Color;
import itumulator.world.World;

public class Carcass extends Animal{
    private boolean infected;

    //Konstruktør
    public Carcass(boolean infected, String size){
        //Arvede felter instansieres
        if(size.equals("big")){
            this.di = new DisplayInformation(Color.magenta,"carcass",true);
        }
        else if(size.equals("small")){
            this.di = new DisplayInformation(Color.magenta,"carcass-small",true);
        }
        else{
            throw new IllegalArgumentException("size not recognized; must be either \"big\" or \"small\"");
        }

        //Unikke felter instansieres
        this.infected = infected;
    }

    //Metoder
    @Override public void act(World world){
        //...
    }
}