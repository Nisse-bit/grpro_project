import itumulator.world.World;
import itumulator.world.Location;
import itumulator.simulator.Actor;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

abstract class Animal implements Actor, DynamicDisplayInformationProvider {
    protected DisplayInformation di;
    protected int age;
    protected int[] energy;
    protected String sex;
    protected boolean dies;
    protected boolean canBreed;
    protected boolean onMap;

    //Konstruktør
    public Animal() {
        age = 0;
        sex = (new Random().nextInt(2) == 0) ? "male" : "female";
        energy = new int[2]; //Energi som int-array. Første tal angiver max-energi, andet tal faktisk energi
        dies = false;
        canBreed = false;
        onMap = true;
    }

    //Metoder
    @Override
    public DisplayInformation getInformation() {
        return di;
    }

    @Override
    public void act(World world) {
    }

    /**
     * Justerer dyrets nuværende energi-niveau. (dyrets energi er delt i to; max-energi og faktisk/nuværende-energi)
     *
     * @param world verdenen som dyret er i
     * @param value værdien energi-niveauet justeres med
     */
    public void adjustEnergy(World world, int value) {
        //Gemmer max- og faktisk energi i variable så koden er nemmere at læse
        int maximumEnergy = energy[0];
        int currentEnergy = energy[1];

        if (currentEnergy + value > maximumEnergy) {
            //Hvis energien som en metode ønsker at tilføje ville overskrive max-energi...
            energy[1] = energy[0]; //... sættes faktisk energi lig max-energi, så max aldrig overskrides
        } else if (currentEnergy + value <= 0) {
            //Hvis energien som en metode ønsker at fjerne resulterer i 0 eller negativ energi...
            dies = true; //... så dør kaninen
            System.out.println("[" + this + "] died from hunger");
        } else {
            energy[1] += value; //Ellers rettes energien bare
        }
    }

    /**
     * Returnerer dyrets alder.
     *
     * @return dyrets alder
     */
    public int getAge() {
        return age;
    }

    /**
     * Dyret forsøger at parre sig med et tilfældigt, nært, modsat-kønnet dyr, op til én gang om dagen.
     *
     * @param world verdenen som dyret er i
     */
    public void tryToBreed(World world) {
    }

    /**
     * Dyret forsøger at spise.
     * @param world verdenen som dyret er i
     */
    public void tryToEat(World world) {}

    /**
     * Dyret bliver ældre, skifter udseende og justerer max-energi alt efter hvor længe det har været i verdenen.
     * @param world verdenen som dyret er i
     */
    public void older(World world){}

    /**
     * Dyret bevæger sig imod en given lokation
     * @param world    verdenen som dyret er i
     * @param location lokationen som dyret hopper imod
     */
    public void moveTowards(World world, Location location) {
        //Ønskede koordinater
        int x = location.getX();
        int y = location.getY();

        //Kaninens koordinater
        int Ry = world.getLocation(this).getY();
        int Rx = world.getLocation(this).getX();

        if (x > Rx && y > Ry) { //Hvis lokationens xy er højere end dyrets xy...
            Rx = Rx + 1;
            Ry = Ry + 1; //Bevæger sig til diagonalt op til højre
            Location locationXY = new Location(Rx, Ry);
            if (world.isTileEmpty(locationXY)) {  //Checker om der står noget på feltet
                world.move(this, locationXY); //Bevæger dyret til det nye sted
            }
            /*else{System.out.println("["+ this +"] Der er noget i vejen, jeg kan ikke komme til mit hul!");}*/
        } else if (x < Rx && y < Ry) { //Hvis lokationens xy er mindre end dyrets xy...
            Rx = Rx - 1;
            Ry = Ry - 1; //Bevæger sig diagonalt ned til venstre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (x > Rx && y < Ry) { //Hvis lokationens x er større og y er mindre end dyrets xy...
            Rx = Rx + 1;
            Ry = Ry - 1; //Bevæger sig diagonalt ned til højre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (x < Rx && y > Ry) { //Hvis lokationens x er større og y er mindre end dyrets xy...
            Rx = Rx - 1;
            Ry = Ry + 1; //Bevæger sig diagonalt op til venstre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (x > Rx) { //Hvis lokationens x er større end dyrets x...
            Rx = Rx + 1;  //Bevæger sig direkte til højre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (x < Rx) { //Hvis lokationens x er mindre ned dyrets x...
            Rx = Rx - 1;  //Bevæger sig direkte til venstre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (y > Ry) { //Hvis lokationens y er større end dyrets y...
            Ry = Ry + 1;  //Bevæger sig direkte op
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (y < Ry) { //Hvis lokationens y er mindre end dyrets y...
            Ry = Ry - 1;  //Bevæger sig direkte ned
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        }

        this.adjustEnergy(world, -1);
    }

    /**
     * Dyret bevæger sig tilfældigt i den iskolde verden.
     * @param world verdenen som dyret er i
     */
    public void moveRandomly(World world){
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if (!list.isEmpty()) {
            int r = new Random().nextInt(list.size());
            Location l = list.get(r);
            world.move(this, l);
            this.adjustEnergy(world, -1); //Kaninen mister energi
        }
        /*if(list.size() == 0){System.out.println("["+ this +"]: No moveable spots available, I'll stand still.");}*/
    }
}
