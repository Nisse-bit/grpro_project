import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;  // til PutBurrow metode
import itumulator.world.World;

import java.awt.Color;
import java.util.List;
import java.util.LinkedList;

public class Burrow implements DynamicDisplayInformationProvider{
    private DisplayInformation di;
    private List<Rabbit> associatedRabbits;

    //Konstruktør
    public Burrow(){
        di = new DisplayInformation(Color.orange,"hole-small",true);
        associatedRabbits = new LinkedList<>();
    }

    //Metoder return DisplayInformation
    @Override public DisplayInformation getInformation(){
        return di;
    }

    /**
     * hvis et hul har en rabbit eller flere, returner hullets lokation.
     * @param world
     * @return this burrows location
     */
    public Location getLocation(World world){
        if(associatedRabbits.isEmpty()){
            return null;
        } else {
            return world.getLocation(this);
        }
    }

    //Metoder til associates-listen
    public void addRabbit(Rabbit rabbit){
        associatedRabbits.add(rabbit);
    }
    public List<Rabbit> getAssociates(){return associatedRabbits;}

    // Lavet så niels kan putte burrows i test
    public void PutBurrow(World world, Location location) {
        if (world.getNonBlocking(location) != null) {
            world.setTile(location, new Burrow());
        }

    }
}
