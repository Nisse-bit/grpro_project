package Animals;
import itumulator.executable.DisplayInformation;
import java.awt.Color;
import java.util.Random;
import itumulator.world.World;

public class Cordyceps extends Animal{

    //Konstruktør
    public Cordyceps(String animal){
        if(animal.equals("rabbit")){
            int r = new Random().nextInt(2);
            if(r == 0){this.di = new DisplayInformation(Color.pink,"rabbit-fungi-small");}
            if(r == 1){this.di = new DisplayInformation(Color.pink,"rabbit-large-fungi");}
        }
        if(animal.equals("wolf")){
            int r = new Random().nextInt(2);
            if(r == 0){this.di = new DisplayInformation(Color.pink,"wolf-fungi-small");}
            if(r == 1){this.di = new DisplayInformation(Color.pink,"wolf-fungi");}
        }
        if(animal.equals("bear")){
            int r = new Random().nextInt(2);
            if(r == 0){this.di = new DisplayInformation(Color.pink,"bear-fungi-small");}
            if(r == 1){this.di = new DisplayInformation(Color.pink,"bear-fungi");}
        }

        this.energy = new int[]{500, 500};
    }

    //Metoder
    @Override public void act(World world){
        if(new Random().nextInt(9) == 0){
            this.moveRandomly(world);
        }
    }
}