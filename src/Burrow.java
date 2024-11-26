import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;  // til PutBurrow metode
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.awt.Color;

public class Burrow implements DynamicDisplayInformationProvider, NonBlocking {
    private DisplayInformation di;

    //Konstruktør
    public Burrow(){
        di = new DisplayInformation(Color.orange,"hole-small",true);
        //associatedRabbits = new LinkedList<>();
    }

    //Metoder
    @Override public DisplayInformation getInformation(){
        return di;
    }

    // Lavet så Niels kan putte burrows i test
    public void PutBurrow(World world, Location location) {
        if (world.getNonBlocking(location) == null) {
            world.setTile(location, new Burrow());
        }
    }
}
