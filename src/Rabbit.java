import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;
import java.awt.Color;
import java.util.*;

public class Rabbit implements Actor, DynamicDisplayInformationProvider {
    DisplayInformation di;
    double age;
    Location hole;

    //Konstruktør
    public Rabbit() {
        age = 0;
        hole = null;
        if (age >= 0 && age <= 1.5);{
            di = new DisplayInformation(Color.white, "rabbit-small", false);
        }
        if (age >= 2 && age <= 3);{
            di = new DisplayInformation(Color.gray, "rabbit-large", false);
        }
        if (age <= 3.5 && age >= 5);{
            di = new DisplayInformation(Color.black,"rabbit-large-fungi",false);
        }
    }

    //Metoder
    @Override
    public DisplayInformation getInformation(){
        return di;
    }

    @Override
    public void act(World world) {
        //Move Rabbit
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        int randomLocation;
        Random r = new Random();
        Location l = null;

        if (!list.isEmpty()) {
            randomLocation = r.nextInt(list.size());
            l = list.get(randomLocation);
            world.move(this, l);

        } if (list.size() <= 0) {
            System.out.println("No moveable spots available, i'll stand still.");
        }

    }

    public void Older(World world){
        if (world.getCurrentTime() == 19){
            age = age + 0.5;
        }
    }
}
