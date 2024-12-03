package Animals;

import itumulator.executable.DisplayInformation;
import itumulator.world.*; //Kaninen benytter alle world's metoder

import java.awt.Color;
import java.util.Random;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import Holes.Burrow;
import Plants.*;

public class Rabbit extends Animal {
    private Burrow burrow;

    //Konstruktør Rabbit
    public Rabbit() {
        super();
        di = new DisplayInformation(Color.gray, "rabbit-small", true);
        energy = new int[]{50, 50};
        burrow = null;
    }

    //Metoder
    @Override
    public void act(World world) {
        this.older(world);
        this.RabbitMove(world);
        this.tryToEat(world);
        this.tryToBreed(world);

        if (dies) {
            world.delete(this);
        }
    }

    @Override
    public void tryToBreed(World world) {
        if (world.getCurrentTime() % 20 == 0) {
            canBreed = true;
        } //Hvis det er en ny dag, nulstil hasBredToday

        //Hvis kaninen ikke er fuldvoksen, eller den har parret i dag, stop metoden
        if (age < 30 || !canBreed) {
            return;
        }

        List<Location> tilesForBaby = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        if (tilesForBaby.isEmpty()) {
            return;
        } //Hvis der ikke er plads til at få en unge, stop metoden

        List<Rabbit> surroundingPartners = new ArrayList<>(); //Liste af omkringliggende potentielle partnere
        for (Location l : world.getSurroundingTiles(world.getLocation(this))) {
            if (world.getTile(l) instanceof Rabbit p) {
                if (this.sex.equals("male") && p.sex.equals("female")) {
                    surroundingPartners.add(p);
                }
                if (this.sex.equals("female") && p.sex.equals("male")) {
                    surroundingPartners.add(p);
                }
            }
        }
        if (surroundingPartners.isEmpty()) {
            return;
        } //Hvis der ikke er mindst én potentiel partner, stop metoden

        Random random = new Random();
        int rand1 = random.nextInt(100); //Sandsynlighed for at parre, for nu bare 100% for testing purposes
        if (rand1 < 100) {
            int rand2 = random.nextInt(surroundingPartners.size());
            Rabbit mate = surroundingPartners.get(rand2); //Vælger en tilfældig partner

            int rand3 = random.nextInt(tilesForBaby.size());
            Location l = tilesForBaby.get(rand3); //Vælger tilfældig lokation til ungen
            world.setTile(l, new Rabbit()); //Sætter ungen i verden

            this.canBreed = false; //Kaninen stoppes fra at parre mere i dag
            mate.canBreed = false; //Partneren stoppes fra at parre mere i dag
            this.adjustEnergy(world, -1); //Kaninen mister energi
            mate.adjustEnergy(world, -1); //Partneren mister energi
        }
    }

    @Override
    public void tryToEat(World world) {
        if (world.isNight() || !onMap) {
            return;
        } //Kaninen spiser kun om dagen og når den ikke er i sin hule

        Location l = world.getLocation(this);
        //Kaninen spiser kun græs den står på. alt andet ville væremærkeligt!
        if (world.containsNonBlocking(l)) {
            if (world.getNonBlocking(l) instanceof Grass g) {
                if (new Random().nextInt(4) < 3) { //75% sandsynlighed for at spise
                    world.delete(g);
                    this.adjustEnergy(world, 4); //Kaninen får energi
                }
            }
        }
        for (Location e : world.getSurroundingTiles(l)) {
            if (world.getTile(e) instanceof BerryBush bush) {
                if (bush.getFruit(world)) {
                    if (new Random().nextInt(4) <= 1) { //50% sandsynlighed for at spise
                        this.adjustEnergy(world, 6);
                        bush.loseBerries();

                    }
                }
            }
        }
    }

        @Override
        public void older (World world){
            //Bliver ældre
            age++;
            //Opdaterer udseende og justerer max-energi
            if (age == 30) {
                di = new DisplayInformation(Color.darkGray, "rabbit-large", true);
                energy[0] = (new Random().nextInt(3) == 0) ? 40 : 30;
                //33% sandsynlighed for at kaninen får 30 max-energi i stedet for 40
            }
            if (age == 60) {
                di = new DisplayInformation(Color.black, "rabbit-old", true);
                energy[0] = (new Random().nextInt(2) == 0) ? 25 : 10;
                //50% sandsynlighed for at kaninen får 10 max-energi i stedet for 25
            }
            if (age == 90) {
                dies = true;
                System.out.println("[" + this + "] died from old age");
            }
        }

        /**
         * Kaninen bevæger sig tilfældigt, søger imod sin hule eller gemmer sig, alt efter tiden på døgnet.
         *
         * @param world verdenen som kaninen er i
         */
        public void RabbitMove (World world){
            int time = world.getCurrentTime();
            Random random = new Random();

            //Hvis det er morgen, hopper kaninen ud af hulen
            if (time == 19 && !onMap) {
                Location bLocation = world.getLocation(burrow);

                //Kaninen finder en tilfældig plads at hoppe ud på
                List<Location> reappearableTiles = new ArrayList<>(world.getEmptySurroundingTiles(bLocation));
                if (world.isTileEmpty(bLocation)) {
                    reappearableTiles.add(bLocation);
                }
                if (reappearableTiles.isEmpty()) {
                    dies = true;
                    System.out.println("[" + this + "] suffocated in its burrow");
                    return; //Hvis der er ingen ledige pladser, dør kaninen
                }
                Location reappearTile = reappearableTiles.get(random.nextInt(reappearableTiles.size()));
                world.setTile(reappearTile, this);
                onMap = true;
                this.adjustEnergy(world, -1);
                return; //Kaninen hopper ud og stopper metoden så den ikke hopper videre
            } else if (!onMap) {
                return;
            } //Hvis kaninen ellers er i sin hule, stoppes metoden

            //Hvis det er aften eller nat, pathfinder kaninen til sin hule
            if (9 <= time && time <= 18) {
                Location rLocation = world.getLocation(this);

                //Hvis kaninen ikke har en hule, finder- eller graver den sig en
                if (burrow == null) {
                    burrow = nearestBurrow(world, 3); //Hvis kaninen ikke har en hule, forsøger den at finde en
                    if (burrow == null) {
                        //Hvis kaninen ikke fandt en hule, forsøger den at grave en
                        // hvad hvis den graver et hul der hvor der er græs.

                        if (!(world.getNonBlocking(rLocation) instanceof NonBlocking)) {
                            Burrow b = new Burrow();
                            world.setTile(rLocation, b);
                            burrow = b; //Kaninen graver en hule og tilknytter sig den
                        }
                        //Hvis kaninen stadig ikke har en hule, finder den nu nærmeste hule i hele verdenen
                        if (burrow == null) {
                            burrow = nearestBurrow(world, world.getSize());
                        }
                    }
                    //Nu MÅ kaninen altså have en hule
                }

                //Nu hvor kaninen har en hule, hopper den ned i den hvis den står på den, ellers hopper den imod den
                if (world.getNonBlocking(world.getLocation(this)) == burrow) {
                    world.remove(this);
                    onMap = false;
                    canBreed = false;
                } else {
                    // Object does not exist in the world exception.


                       moveTowards(world, world.getLocation(burrow));

                }
            } else {
                this.moveRandomly(world); //Hvis det ellers er dag, hopper kaninen tilfældigt
            }
        }

        /**
         * Kigger kaninens omkringliggende felter gennem efter en hule. Metoden afvikles kun hvis kaninens burrow er null.
         *
         * @param world  verdenen som kaninen er i
         * @param radius radius hvormed metoden leder efter nærliggende huler
         * @return Kaninens nærmeste hule eller null hvis ingen er indenfor rækkevidde
         */
        public Burrow nearestBurrow (World world,int radius){
            if (world.getNonBlocking(world.getLocation(this)) instanceof Burrow b) {
                return b;
            }

            int searchRadius = 1; //Inderste ring
            while (searchRadius <= radius) { //Op til angive radius
                Set<Location> surroundingTiles = world.getSurroundingTiles(world.getLocation(this), searchRadius);
                for (Location location : surroundingTiles) {
                    if (world.containsNonBlocking(location)) {
                        Object object = world.getNonBlocking(location);
                        if (object instanceof Burrow b) {
                            return b;
                        }
                        //Hvis der er en hule, returneres den
                    }
                }
                searchRadius++; //Øger radius
            }
            return null; //Hvis der ikke er en hule i nærheden returneres null
        }
    }