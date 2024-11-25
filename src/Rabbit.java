import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.*; //Kaninen benytter alle world's metoder
import java.awt.Color;
import java.util.Random;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class Rabbit implements Actor, DynamicDisplayInformationProvider {
    private DisplayInformation di;
    private int age;
    private String sex;
    private boolean hasBredToday;
    private Burrow burrow;
    private boolean inBurrow;
    private int[] energy;
    private boolean dies;

    //Konstruktør
    public Rabbit() {
        di = new DisplayInformation(Color.gray,"rabbit-small",true);
        age = 0;
        sex = (new Random().nextInt(2) == 0)? "male" : "female";
        hasBredToday = false;
        burrow = null;
        inBurrow = false;
        energy = new int[]{50,50}; //Energi som int-array. Første tal angiver max-energi, andet tal faktisk energi
        dies = false;
    }

    //Metoder
    @Override
    public DisplayInformation getInformation(){
        return di;
    }

    @Override
    public void act(World world) {
        this.older(world);
        this.advancedMove(world);
        this.tryToEat(world);
        this.tryToBreed(world);

        if(dies){world.delete(this);}
    }

    /**
     * Justerer kaninens nuværende energi-niveau. (Kaninens energi er delt i to; max-energi og faktisk/nuværende-energi)
     * @param world verdenen som kaninen er i
     * @param value værdien energi-niveauet justeres med
     */
    public void adjustEnergy(World world, int value){
        //Gemmer max- og faktisk energi i variable så koden er nemmere at læse
        int maximumEnergy = energy[0];
        int currentEnergy = energy[1];

        if(currentEnergy+value > maximumEnergy){
            //Hvis energien som en metode ønsker at tilføje ville overskrive max-energi...
            energy[1] = energy[0]; //... sættes faktisk energi lig max-energi, så max aldrig overskrides
        }
        else if(currentEnergy+value <= 0){
            //Hvis energien som en metode ønsker at fjerne resulterer i 0 eller negativ energi...
            dies = true; //... så dør kaninen
            System.out.println("["+ this +"] died from hunger");
        }
        else{
            energy[1] += value; //Ellers rettes energien bare
        }
    }

    /**
     * Kaninen forsøger at parre sig med en tilfældig, nær, modsat-kønnet kanin, op til én gang om dagen.
     * @param world verdenen som kaninen er i
     */
    public void tryToBreed(World world){
        if(world.getCurrentTime()%20 == 0){hasBredToday = false;} //Hvis det er en ny dag, nulstil hasBredToday

        //Hvis kaninen ikke er fuldvoksen, eller den har parret i dag, eller den er i sin hule, stop metoden
        if(age < 30 || hasBredToday || inBurrow){return;}

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
            Rabbit mate = surroundingPartners.get(rand2); //Vælger en tilfældig partner

            int rand3 = random.nextInt(tilesForBaby.size());
            Location l = tilesForBaby.get(rand3); //Vælger tilfældig lokation til unge
            world.setTile(l, new Rabbit()); //Sætter ungen i verden

            this.hasBredToday = true; //Kaninen stoppes fra at parre mere i dag
            mate.hasBredToday = true; //Partneren stoppes fra at parre mere i dag
            this.adjustEnergy(world, -1); //Kaninen mister energi
            mate.adjustEnergy(world, -1); //Partneren mister energi
        }
    }

    /**
     * Kaninen har 75% sandsynlighed at spise græs, hvis den står på det.
     * @param world verdenen som kaninen er i
     */
    public void tryToEat(World world){
        if(world.isNight() || inBurrow){return;} //Kaninen spiser kun om dagen og når den ikke er i sin hule

        Location l = world.getLocation(this);
        if(world.containsNonBlocking(l)){
            if(world.getNonBlocking(l) instanceof Grass g){
                if(new Random().nextInt(4) < 3){ //75% sandsynlighed for at spise
                    world.delete(g);
                    this.adjustEnergy(world, 4); //Kaninen får energi
                }
            }
        }
    }

    /**
     * Kaninen bliver ældre, skifter udseende og justerer max-energi alt efter hvor længe den har været i verdenen.
     * @param world verdenen som kaninen er i
     */
    public void older(World world){
        //Bliver ældre
        age++;
        //Opdaterer udseende og justerer max-energi
        if(age == 30){
            di = new DisplayInformation(Color.darkGray, "rabbit-large", true);
            energy[0] = (new Random().nextInt(3) == 0)? 40 : 30;
            //33% sandsynlighed for at kaninen får 30 max-energi istedet for 40
        }
        if(age == 60){
            di = new DisplayInformation(Color.black,"rabbit-large-fungi",true);
            energy[0] = (new Random().nextInt(2) == 0)? 25 : 10;
            //50% sandsynlighed for at kaninen får 10 max-energi istedet for 25
        }
        if(age == 90){
            dies = true;
            System.out.println("["+ this +"] died from old age");
        }
    }

    /**
     * Kaninen bevæger sig tilfældigt i den iskolde verden.
     * @param world verdenen som kaninen er i
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

    /**
     * Kaninen bevæger sig tilfældigt, søger imod sin hule eller gemmer sig, alt efter tiden på døgnet.
     * @param world verdenen som kaninen er i
     */
    public void advancedMove(World world){
        int time = world.getCurrentTime();
        Random random = new Random();

        //Hvis det er morgen, hopper kaninen ud af hulen
        if(time == 19 && inBurrow){
            Location bLocation = world.getLocation(burrow);

            //Kaninen finder en tilfældig plads at hoppe ud på
            List<Location> reappearableTiles = new ArrayList<>(world.getEmptySurroundingTiles(bLocation));
            if(world.isTileEmpty(bLocation)){reappearableTiles.add(bLocation);}
            if(reappearableTiles.isEmpty()){
                dies = true;
                System.out.println("["+ this +"] suffocated in its burrow");
                return; //Hvis der er ingen ledige pladser, dør kaninen
            }
            Location reappearTile = reappearableTiles.get(random.nextInt(reappearableTiles.size()));
            world.setTile(reappearTile, this);
            inBurrow = false;
            this.adjustEnergy(world, -1);
            return; //Kaninen hopper ud og stopper metoden så den ikke hopper videre
        }
        else if(inBurrow){return;} //Hvis kaninen ellers er i sin hule, stoppes metoden

        //Hvis det er aften eller nat, pathfinder kaninen til sin hule
        if(9 <= time && time <= 18){
            Location rLocation = world.getLocation(this);

            //Kaninen finder- eller graver sig et hul:
            if(burrow == null){
                burrow = nearestBurrow(world,3); //Hvis kaninen ikke har en hule, forsøger den at finde en
                if(burrow == null){
                    //Hvis kaninen ikke fandt en hule, forsøger den at grave en:
                    if(!(world.getNonBlocking(rLocation) instanceof NonBlocking)){
                        Burrow b = new Burrow();
                        world.setTile(rLocation, b);
                        burrow = b; //Kaninen graver en hule og tilknytter sig den
                    }
                    //Hvis kaninen stadig ikke har en hule, finder den nu nærmeste hule i hele verdenen:
                    if(burrow == null){
                        burrow = nearestBurrow(world, world.getSize());
                    }
                }
                //Nu MÅ kaninen altså have en hule
            }

            //Nu hvor kaninen har en hule, hopper den ned i den hvis den står på den, ellers hopper den imod den
            if(world.getNonBlocking(world.getLocation(this)) == burrow){
                world.remove(this);
                inBurrow = true;
            }
            else{
                this.moveTowards(world, world.getLocation(burrow));
            }
        }
        else{
            this.moveRandomly(world); //Hvis det ellers er dag, hopper kaninen tilfældigt
        }
    }

    /**
     * Kaninen hopper imod en given lokation.
     * @param world verdenen som kaninen er i
     * @param location lokationen som kaninen hopper imod
     */
    public void moveTowards(World world, Location location) {
        //Ønskede koordinater
        int x = location.getX();
        int y = location.getY();

        //Kaninens koordinater
        int Ry = world.getLocation(this).getY();
        int Rx = world.getLocation(this).getX();

        if (x > Rx && y > Ry) { //Hvis lokationens xy er højere end kaninens xy...
            Rx = Rx + 1;
            Ry = Ry + 1; //Bevæger sig til diagonalt op til højre
            Location locationXY = new Location(Rx, Ry);
            if (world.isTileEmpty(locationXY)) {  //Checker om der står noget på feltet
                world.move(this, locationXY); //Bevæger kaninen til det nye sted
            }
            /*else{System.out.println("["+ this +"] Der er noget i vejen, jeg kan ikke komme til mit hul!");}*/
        } else if (x < Rx && y < Ry) { //Hvis lokationens xy er mindre end kaninens xy...
            Rx = Rx - 1;
            Ry = Ry - 1; //Bevæger sig diagonalt ned til venstre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (x > Rx && y < Ry) { //Hvis lokationens x er større og y er mindre end kaninens xy...
            Rx = Rx + 1;
            Ry = Ry - 1; //Bevæger sig diagonalt ned til højre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (x < Rx && y > Ry) { //Hvis lokationens x er større og y er mindre end kaninens xy...
            Rx = Rx - 1;
            Ry = Ry + 1; //Bevæger sig diagonalt op til venstre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (x > Rx) { //Hvis lokationens x er større end kaninens x...
            Rx = Rx + 1;  //Bevæger sig direkte til højre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (x < Rx) { //Hvis lokationens x er mindre ned kaninens x...
            Rx = Rx - 1;  //Bevæger sig direkte til venstre
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (y > Ry) { //Hvis lokationens y er større end kaninens y...
            Ry = Ry + 1;  //Bevæger sig direkte op
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        } else if (y < Ry) { //Hvis lokationens y er mindre end kaninens y...
            Ry = Ry - 1;  //Bevæger sig direkte ned
            Location locationX = new Location(Rx, Ry);
            if (world.isTileEmpty(locationX)) {
                world.move(this, locationX);
            }
        }

        this.adjustEnergy(world, -1);
    }

    /**
     * Kigger kaninens omkringliggende felter gennem efter en hule. Metoden afvikles kun hvis kaninens burrow er null.
     * @param world verdenen som kaninen er i
     * @param radius radius hvormed metoden leder efter nærliggende huler
     * @return Kaninens nærmeste hule eller null hvis ingen er indenfor rækkevidde
     */
    public Burrow nearestBurrow(World world, int radius) {
        if(world.getNonBlocking(world.getLocation(this)) instanceof Burrow b){return b;}

        int searchRadius = 1; //Inderste ring
        while (searchRadius <= radius) { //Op til angive radius
            Set<Location> surroundingTiles = world.getSurroundingTiles(world.getLocation(this), searchRadius);
            for (Location location : surroundingTiles) {
                if (world.containsNonBlocking(location)) {
                    Object object = world.getNonBlocking(location);
                    if (object instanceof Burrow b) {return b;}
                    //Hvis der er en hule, returneres den
                }
            }
            searchRadius++; //Øger radius
        }
        return null; //Hvis der ikke er en hule i nærheden returneres null
    }
}