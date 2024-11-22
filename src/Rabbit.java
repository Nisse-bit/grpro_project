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
    Burrow myburrow;
    Location location;

    //Konstruktør
    public Rabbit() {
        age = 0;
        myburrow = null;
        di = new DisplayInformation(Color.gray, "rabbit-small", false);
    }

    //Metoder
    @Override
    public DisplayInformation getInformation() {
        return di;
    }

    @Override
    public void act(World world) {
        if (world.getCurrentTime() % 10 == 0 || world.getCurrentTime() % 11 == 0 || world.getCurrentTime() % 12 == 0) {

            // hvis burrow ikke er null bevæg sig mod sit eget burrow.
            if (hasBurrow()) {
                this.moveto(world,myburrow.getLocation(world));
            } else {
                // hvis der er et tæt på burrow gør den det til sit eget.
                this.myburrow = Nearestburrow(world);
                // bevæger sig til sit nye myburrows lokation
                if(this.myburrow != null) {moveto(world, myburrow.getLocation(world));}


            }
        } else {
            // når det ikke er "after" bevæger den sig tilfældigt.
            this.move(world);
        }

        this.tryToEat(world);
        this.older(world);
    }

    //Rabbit bliver ældre & skifter udseende.
    public void older(World world) {
        if (world.getCurrentTime() % 10 == 0) {
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

    /**
     * bevæger sig tilfældigt i den iskolde verden.
     * @param world
     */
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
    private void tryToEat(World world) {
        Location l = world.getLocation(this);
        if (world.containsNonBlocking(l)) {
            Object o = world.getNonBlocking(l);
            if (o instanceof Grass) {
                int r = new Random().nextInt(2); //Genererer en int, enten 0 eller 1
                if (r == 0) {
                    world.delete(o);
                } //50% sandsynlighed for at spise
            }
        }
    }


    public boolean hasBurrow() {
        if (myburrow == null) {
            return false;
        }   else {
            return true;
        }
    }

    public double getAge() {
        return age;
    }


    //Grav et hul
    public void Digburrow(World world) {
        Location l = world.getLocation(this);
        if (!world.containsNonBlocking(l)) {
            world.setTile(l, new Burrow());
        }
    }

    /**
     * bevæger sig til specefikt givet lokation.
     * @param world
     * @param loc
     */
    public void moveto(World world, Location loc) {


        // place to move to
        int x = loc.getX();
        int y = loc.getY();
        // rabbit locations

        int Ry = world.getLocation(this).getY();
        int Rx = world.getLocation(this).getX();
            if (x == Rx && y ==  Ry) {
                System.out.println("you are already at the location doofus");
                // use hide function hvis der er et burrow og det er nat
                return;
            }

        if (x > Rx && y > Ry) {  // hvis Locationens xy er højere end kaninens X. og Y
            Rx = Rx + 1;
            Ry = Ry + 1; // bevæger sig en til diagonalt højre.

            Location locationXY = new Location(Rx, Ry);
            if (world.isTileEmpty(locationXY)) {  // checker om der står noget på feltet.
                world.move(this, locationXY); //  mover kanin til nyt sted
            } else {
                System.out.println("der er noget i vejen jeg kan ikke komme til mit hul");
            }

        } else if (x < Rx && y < Ry) {      // hvis Locationens xy er mindre end kaninens XY.
            Rx = Rx - 1;
            Ry = Ry - 1; // bevæger sig diagonalt ned af
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            } else {
                System.out.println("der er noget i vejen jeg kan ikke komme til mit hul");
            }
        } else if (x > Rx && y < Ry) {      // hvis Locationens x er større og y er mindre end kaninens XY.
            Rx = Rx + 1;
            Ry = Ry - 1;
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            } else {
                System.out.println("der er noget i vejen jeg kan ikke komme til mit hul");
            }
        } else if (x < Rx && y > Ry) {      // hvis Locationens x er større og y er mindre end kaninens XY.
            Rx = Rx - 1;
            Ry = Ry + 1;
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            } else {
                System.out.println("der er noget i vejen jeg kan ikke komme til mit hul");
            }
        } else if (x > Rx) {      // hvis Locationens x er større
            Rx = Rx + 1;  // en til højre

            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            } else {
                System.out.println("der er noget i vejen jeg kan ikke komme til mit hul");
            }
        } else if (x < Rx) {      // hvis Locationens x er mindre
            Rx = Rx - 1;  // en til venstre

            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            } else {
                System.out.println("der er noget i vejen jeg kan ikke komme til mit hul");
            }
        } else if (y > Ry) {      // hvis Locationens y er større
            Ry = Ry + 1;  // en op

            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            } else {
                System.out.println("der er noget i vejen jeg kan ikke komme til mit hul");
            }
        } else if (y < Ry) {       // hvis Locationens y er mindre
                Ry = Ry - 1;  // en ned

            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            } else {
                System.out.println("der er noget i vejen jeg kan ikke komme til mit hul");
            }
        }

    }

    /**
     *
     * @param world
     * @return
     */
    public Burrow Nearestburrow(World world) {  // ULTRA MEGA HUKOMMElSES TUNGT, find en bedre løsning på et tidspunkt
        // den retnere ret ofte et null
        // den her skal kigges mere på.
        // man kunne istedet for at retunere
        // sætte this.myburrow == burrow
        Set<Location> FirstRingofTiles = world.getSurroundingTiles();

        for (Location location : FirstRingofTiles) {
            if(world.containsNonBlocking(location)) {

                Object object = world.getNonBlocking(location);
                if (object instanceof Burrow) {
                    return (Burrow) object;
                }
            }
        }
        Set<Location> SecondRingofTiles = world.getSurroundingTiles(2);
        for (Location location : FirstRingofTiles) {
            if(world.containsNonBlocking(location)) {

                Object object = world.getNonBlocking(location);
                if (object instanceof Burrow) {
                    return (Burrow) object;
                }
            }
        }
        Set<Location> ThirdRingofTiles = world.getSurroundingTiles(3);
        for (Location location : FirstRingofTiles) {
            if(world.containsNonBlocking(location)) {

                Object object = world.getNonBlocking(location);
                if (object instanceof Burrow) {
                    return (Burrow) object;
                }
            }
        }

        return null;
    }
}




