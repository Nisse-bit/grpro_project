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
    Burrow myBurrow;
    Location location;
    //Konstruktør
    public Rabbit() {
        age = 0;
        burrow = null;
        di = new DisplayInformation(Color.gray, "rabbit-small", false);
    }

    //Metoder
    @Override
    public DisplayInformation getInformation() {
        return di;
    }

    @Override
    public void act(World world) {
        if (world.getCurrentTime()%10 == 0|| world.getCurrentTime()%11 == 0 || world.getCurrentTime()%12 == 0 ) {

        } else {
            // når det ikke er "after" bevæger den sig tilfældigt.
            this.move(world);
        }


        this.older(world);
    }
    //Rabbit bliver ældre & skifter udseende.
    public void older(World world) {
        if (world.getCurrentTime()%10 == 0) {
            age = age + 0.5;
        }
        if (age >= 1.5 && age <= 2.5) {
            di = new DisplayInformation(Color.DARK_GRAY, "rabbit-large", false);
        }
        if (age >= 3 && age <= 4) {
            di = new DisplayInformation(Color.black, "rabbit-large-fungi", false);
        }
        if (age > 4) {
            world.delete(this);
        }
    }

    // Bevægelses logik
    public void move(World world) {
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
            this.location = l;

        }
        if (list.size() <= 0) {
            System.out.println("No moveable spots available, i'll stand still.");
        }
    }

    //Græs-spisning
    public void eat(World world) {
       // if(world.);
        //if (world.getLocation(this) == world.getLocation(wo)){

        }
    }

    //Grav et hul
    public void Digburrow(World world){
        Location l = world.getLocation(this);
        if(!world.containsNonBlocking(l)){
            world.setTile(l, new Burrow());
        }
    }

    public void MovenearestBurrow(World world) {
             Set<Location> tileSet = world.getSurroundingTiles(this.location,2); // radius af tiles rundt om her 25 felter i alt.

             for(Location loc : tileSet ){
                 // Skal finde tættest Burrow.

                 // placeholder location til at huske den tættest location.

                 Location Closestloc = new Location(0,0);

                 if(world.getNonBlocking(loc) instanceof Burrow){
                     // her skal vi sammenligne rabbit og burrows location.
                     // location fra tileset some indeholders burrows Location.
                   int x = loc.getX();
                   int y = loc.getY();

                   // Rabbit locations
                   int Ry = this.location.getX();
                   int Rx = this.location.getX();

                   if (x == Rx || y == Ry) { // middle Rabbit already in a hole standing on a hole;

                       if (x > Rx || y > Ry ) {} // top right
                       if (x < Rx || y > Ry ) {} // top left
                       if (x == Rx || y > Ry ) {} // top middle
                       if (x > Rx || y == Ry ) {} //  center right,
                       if (x < Rx || y == Ry ) {} // center left,
                       if (x > Rx || y < Ry ) {} // bottom right
                       if (x < Rx || y < Ry ) {} // bottom left
                       if (x == Rx || y < Ry ) {}  // bottom middle
                   }
             }
         }

    }

    public boolean hasBurrow() {
        if (burrow != null)
        {return true;}
        else
        {return false;}
    }

    public double getAge() {
        return age;
    }
}


