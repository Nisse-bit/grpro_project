import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.Color;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Rabbit implements Actor, DynamicDisplayInformationProvider {
    private DisplayInformation di;
    private int age;
    private Burrow myburrow;
    private Location location;
    private String sex;
    private boolean hasBredToday;

    //Konstruktør
    public Rabbit() {
        di = new DisplayInformation(Color.gray, "rabbit-small", false);
        age = 0;
        myburrow = null;

        int r = new Random().nextInt(2);
        if(r == 0) {sex = "male";}
        if(r == 1) {sex = "female";}
        hasBredToday = false;
    }

    //Metoder
    @Override
    public DisplayInformation getInformation() {
        return di;
    }

    @Override
    public void act(World world) {
        this.older(world);
        if(!world.contains(this)){return;} //Hvis kaninen døde i forrige metode, stop act

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
        this.tryToBreed(world);
    }

    /**
     * Kaninen ældes og skifter udseende
     * @param world
     */
    private void older(World world) {
        //Bliver ældre
        age++;
        //Opdaterer udseende
        if(age == 30){
            di = new DisplayInformation(Color.darkGray, "rabbit-large", true);
        }
        if(age == 60){
            di = new DisplayInformation(Color.black,"rabbit-large-fungi",true);
        }
        if(age == 90){
            world.delete(this);
        }
    }

    /**
     * Kaninen bevæger sig tilfældigt i den iskolde verden
     * @param world
     */
    public void move(World world) {
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

    /**
     * Kaninen prøver at spise græs, hvis den står på det
     * @param world
     */
    private void tryToEat(World world) {
        if(world.isNight()){return;} //Kaninen spiser kun om dagen

        Location l = world.getLocation(this);
        if (world.containsNonBlocking(l)) {
            if (world.getNonBlocking(l) instanceof Grass g) {
                int r = new Random().nextInt(2); //Genererer en int, enten 0 eller 1
                if (r == 0) { //50% sandsynlighed for at spise
                    world.delete(g);
                }
            }
        }
    }

    /**
     * Returnerer hvorvidt kaninen er tilknyttet en hule
     * @return true hvis kaninen er tilknyttet en hule, ellers false
     */
    public boolean hasBurrow() {
        if(myburrow == null) {return false;}
        else {return true;}
    }

    public double getAge() {
        return age;
    }

    public void Digburrow(World world) {
        Location l = world.getLocation(this);
        if (!world.containsNonBlocking(l)) {
            world.setTile(l, new Burrow());
        }
    }

    /**
     * bevæger sig til specifikt givet lokation.
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
     * @param world
     * @return Burrows i nærheden af kaninen eller null hvis ingen er tilstede
     */
    public Burrow Nearestburrow(World world) {
        // ULTRA MEGA HUKOMMElSES TUNGT, find en bedre løsning på et tidspunkt
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

    /**
     * Kaninen forsøger at parre sig med en tilfældig, nær, modsat-kønnet kanin
     * @param world
     */
    private void tryToBreed(World world){
        if(age < 30){return;} //Hvis kaninen ikke er fuldvoksen, stop metoden

        if(world.getCurrentTime()%20 == 0){hasBredToday = false;} //Hvis det er en ny dag, nulstil hasBredToday
        if(hasBredToday){return;} //Hvis kaninen har parret i dag, stop metoden

        List<Location> tilesForBaby = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        if(tilesForBaby.isEmpty()){return;} //Hvis der ikke er plads til at få en unge, stop metoden

        List<Rabbit> surroundingPartners = new ArrayList<>(); //Liste af omkringliggende potentielle partnere
        for(Location l : world.getSurroundingTiles(world.getLocation(this))){
            if(world.getTile(l) instanceof Rabbit p){
                if(this.sex.equals("male") && p.sex.equals("female")){surroundingPartners.add(p);}
                if(this.sex.equals("female") && p.sex.equals("male")){surroundingPartners.add(p);}
            }
        }
        if(surroundingPartners.isEmpty()){return;} //Hvis der ikke er mindst én potentiel partner, stop metoden

        Random random = new Random();
        int rand1 = random.nextInt(100); //Sandsynlighed for at parre, for nu bare 100% for testing purposes
        if(rand1 < 100){
            int rand2 = random.nextInt(surroundingPartners.size());
            Rabbit partner = surroundingPartners.get(rand2); //Vælger en tilfældig partner

            int rand3 = random.nextInt(tilesForBaby.size());
            Location l = tilesForBaby.get(rand3); //Vælger tilfældig lokation til ungen
            world.setTile(l, new Rabbit()); //Sætter ungen i verden

            this.hasBredToday = true; //Kaninen stoppes fra at parre mere i dag
            partner.hasBredToday = true; //Partneren stoppes fra at parre mere i dag
        }
    }
}