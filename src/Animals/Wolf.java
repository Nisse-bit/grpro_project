package Animals;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import Holes.WolfDen;
import itumulator.executable.DisplayInformation;
import java.awt.Color;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Random;
import itumulator.world.NonBlocking;
import java.util.ConcurrentModificationException;

public class Wolf extends Animal {
    //Unikke felter
    private int packID;
    private List<Wolf> pack;
    private Wolf alpha;
    private WolfDen den;
    private boolean finishedCreation;

    //Konstruktør
    public Wolf(int packID){
        //Arvede felter instansieres
        this.di = new DisplayInformation(Color.cyan,"wolf-small",true);
        this.energy = new int[]{75,75};

        //Unikke felter instansieres ('pack', 'alpha' og 'den' løses i act)
        this.packID = packID;
        this.pack = null;
        this.alpha = null;
        this.den = null;
        this.finishedCreation = false;
    }

    //Metoder
    public void act(World world){
        if(!finishedCreation){finishCreation(world);}

        this.older(world);
        if(alpha == this){
            moveAlpha(world);
            tryToBreed(world);
        }
        else{
            moveBeta(world);
        }

        if(dies){
            try{
                this.die(world);
            }
            catch(ConcurrentModificationException e){}
        }
    }

    /**
     * Færdiggør instansiering af ulvens private felter, efter ulven er sat i verdenen.
     * @param world verdenen som ulven er i
     */
    public void finishCreation(World world){
        //Flokken, hulen og alfaen identificeres
        this.pack = new LinkedList<>();
        for(Object o : world.getEntities().keySet()){
            if((o instanceof Wolf w) && (w.packID == this.packID)){
                this.pack.add(w);
            }
            else if((o instanceof WolfDen wd) && (wd.getPackID() == this.packID)){
                this.den = wd;
            }
        }

        //Alfaen identificeres
        this.alpha = null;
        for(Wolf w : pack){
            if(w.alpha == w){this.alpha = w;}
        }

        //Hvis alfa stadig er null, er det fordi der ikke er en alfa i flokken, derfor gøres denne ulv til alfa
        if(alpha == null){
            alpha = this;
            di = new DisplayInformation(Color.RED,"wolf-alpha",true);
            System.out.println(this + ": I'm alpha of pack ["+ packID +"]!");
        }

        //finishedCreation slås til så metoden aldrig køres igen for denne ulv
        finishedCreation = true;
    }

    @Override public void older(World world){
        age++;

        if(age == 40 && alpha != this){
            di = new DisplayInformation(Color.RED,"wolf",true);
        }
        if(age == 125){
            dies = true;
            System.out.println("["+ this +"] died from old age");
        }
    }

    /**
     * Ulven spiser et dyr i nærheden.
     * @param world verdenen som ulven er i
     * @return true hvis ulven spiser, ellers false
     */
    public boolean eats(World world){
        Location here = world.getLocation(this);

        //Laver en liste af omkringliggende spiselige dyr, hvis listen er tom stopper metoden og returnerer false
        List<Animal> prey = new LinkedList<>();
        for(Location l : world.getSurroundingTiles(here)){
            Object o = world.getTile(l);

            if(o instanceof Rabbit t){
                prey.add(t);
            }
            else if((o instanceof Wolf t) && (t.packID != this.packID)){
                prey.add(t);
            }
        }
        if(prey.isEmpty()){return false;}

        //Vælger et tilfældigt bytte, spiser det, og rykker over på dets plads
        int r = new Random().nextInt(prey.size());
        Animal a = prey.get(r);
        Location there = world.getLocation(a);

        world.delete(a);
        world.move(this, there);
        this.adjustEnergy(world, 15);

        //Hvis ulven har spist, returneres true
        return true;
    }

    /**
     * Alfa-ulvens move-metode.
     * @param world verdenen som ulven er i
     */
    public void moveAlpha(World world) {
        //Hvis ulven er i sin hule og det er morgen, går den ud
        if (!onMap && world.getCurrentTime() == 0) {
            this.toggleHide(world);
        } else if (!onMap) {return;}

        //Uanset om det er dag eller nat, prøver alfaen at spise et dyr i nærheden
        if (this.eats(world)) {return;}

        /*
         * Hvis intet dyr spises, agerer alfaen afhængigt af tiden på dagen:
         * Om dagen jager alfaen den nærmeste kanin eller bevæger sig tilfældigt, hvis der ikke findes en kanin
         * Om natten søger alfaen mod sin hule, og går ind i den hvis den er på den
         */
        if (world.getCurrentTime() >= 1 && world.getCurrentTime() < 10) {
            //Laver en liste af kaniner. Hvis listen er tom, bevæger alfaen sig tilfældigt
            List<Rabbit> rabbits = new ArrayList<>();
            for (Location l : world.getSurroundingTiles(world.getLocation(this), 3)) {
                Object o = world.getTile(l);
                if (o instanceof Rabbit r) {
                    rabbits.add(r);
                }
            }
            if (rabbits.isEmpty()) {
                this.moveRandomly(world);
                return;
            }

            //Finder den nærmeste kanin, og bevæger sig mod den
            Rabbit nearestRabbit = rabbits.get(0);
            int previous_dX = Integer.MAX_VALUE;
            int previous_dY = Integer.MAX_VALUE;

            for (Rabbit r : rabbits) {
                int thisX = world.getLocation(this).getX();
                int thisY = world.getLocation(this).getY();

                int thatX = world.getLocation(r).getX();
                int thatY = world.getLocation(r).getY();

                int dX = Math.abs(thisX - thatX);
                int dY = Math.abs(thisY - thatY);

                boolean betterX = dX <= previous_dX;
                boolean betterY = dY <= previous_dY;

                if (betterX && betterY) {
                    nearestRabbit = r;
                }

                previous_dX = dX;
                previous_dY = dY;
            }
            this.moveTowards(world, world.getLocation(nearestRabbit));
        } else if (world.isNight()) {
            if (world.getNonBlocking(world.getLocation(this)) == den) {
                world.remove(this);
                this.onMap = false;
                this.canBreed = true;
            } else {
                //this.moveTowards(world, world.getLocation(den));
                Location olocation = world.getLocation(this);
                this.moveTowards(world, world.getLocation(den));
                //Ulven kan sidde fast, hvis der er busk i vejen vil den stå stille, derfor skal vi gemme dens gamle location
                if (olocation.equals(world.getLocation(this))) {
                    moveRandomly(world);
                    System.out.println(this + " (alfa): bush inna da way");
                }
            }
        }
    }

        /**
         * Beta-ulvenes move-metode.
         * @param world verdenen som ulven er i
         */
        public void moveBeta (World world){
            //Hvis ulven er i sin hule og det er morgen, går den ud
            if (!onMap && world.getCurrentTime() == 0) {
                this.toggleHide(world);
            } else if (!onMap) {
                return;
            }

            //Uanset om det er dag eller nat, prøver betaen at spise et dyr i nærheden
            if (this.eats(world)) {
                return;
            }

            /*
             * Hvis betaen ikke spiste, agerer den afhængigt af tiden på dagen:
             * Om dagen følger betaen efter alfaen, hvis alfaen er i verdenen, ellers bevæger den sig tilfældigt
             * Om natten søger betaen mod sin hule, og går ind i den hvis den er på den
             */
            if (world.getCurrentTime() >= 1 && world.getCurrentTime() < 10) {
                if (alpha.onMap && alpha != null) {
                    try {
                        this.moveTowards(world, world.getLocation(alpha));
                    } catch (IllegalArgumentException e) {
                        this.moveRandomly(world);
                    }
                } else {
                    this.moveRandomly(world);
                }
            } else if (world.isNight()) {
                if (world.getNonBlocking(world.getLocation(this)) == den) {
                    toggleHide(world);
                } else {
                    //this.moveTowards(world, world.getLocation(den));
                    Location olocation = world.getLocation(this);
                    this.moveTowards(world, world.getLocation(den));
                    //Ulven kan sidde fast, hvis der er busk i vejen vil den stå stille, derfor skal vi gemme dens gamle location
                    if (olocation.equals(world.getLocation(this))) {
                        moveRandomly(world);
                        System.out.println(this + " (beta): bush inna da way");
                    }
                }
            }
        }

        /**
         * Ulven går ind i- eller ud af hulen.
         * @param world verdenen som ulven er i
         */
        public void toggleHide (World world){
            //Hvis ulven er i hulen, går den ud, ellers går den i
            if (!onMap) {
                //Laver en liste af felter, ulven kan komme ud på,
                List<Location> tiles = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(den)));
                if (world.getTile(world.getLocation(den)) instanceof NonBlocking) {
                    tiles.add(world.getLocation(den));
                }
                if (tiles.isEmpty()) {
                    System.out.println("[" + this + "] suffocated in its den");
                    this.die(world);
                    return;
                }
                int r = new Random().nextInt(tiles.size());
                Location l = tiles.get(r);

                world.setTile(l, this);
                this.onMap = true;
                this.canBreed = false;
            } else {
                world.remove(this);
                this.onMap = false;
                this.canBreed = true;
            }
        }

        @Override public void tryToBreed (World world){
            //Hvis ulven ikke er klar til at parre, stoppes metoden
            if (age < 40 || onMap || !canBreed) {
                return;
            }

            //Laver en liste af potentielle partnere. Hvis der ingen er, stoppes metoden
            List<Wolf> potentialPartners = new ArrayList<>();
            for (Object o : world.getEntities().keySet()) {
                if ((o instanceof Wolf p) && p.packID == this.packID) {
                    if (this.sex.equals("male") && p.sex.equals("female")) {
                        potentialPartners.add(p);
                    }
                    if (this.sex.equals("female") && p.sex.equals("male")) {
                        potentialPartners.add(p);
                    }
                }
            }
            if (potentialPartners.isEmpty()) {
                return;
            }

            //Vælger en tilfældig partner, justerer energier og slår parring fra
            int r = new Random().nextInt(potentialPartners.size());
            Wolf mate = potentialPartners.get(r);

            this.canBreed = false; //Ulven stoppes fra at parre mere i dag
            mate.canBreed = false; //Partneren stoppes fra at parre mere i dag
            this.adjustEnergy(world, -1); //Ulven mister energi
            mate.adjustEnergy(world, -1); //Partneren mister energi

            //Tilføjer en unge til hulen (hulen finder selv ud af at komme ungen ud i verden)
            den.addToDen(new Wolf(this.packID));
        }

        /**
         * Ulven dør, og retter dermed hele flokkens flok-liste og evt. alfa-status.
         * @param world verdenen som ulven er i
         */
        public void die (World world) throws ConcurrentModificationException {


            if (pack != null) {  // hvis der ingen pack er skal den bare fortsætte

                //Fjerner ulven fra hele flokkens flok-liste
                for (Wolf w : pack) {
                    w.pack.remove(this);
                }

                //Hvis ulven er alfa, nustilles alfa-status for hele flokken
                boolean wasAlpha = false;
                if (this == alpha) {
                    for (Wolf w : pack) {
                        w.alpha = null;
                    }
                    wasAlpha = true;
                }
                // fjerner ulven fra flokken
                pack.remove(this);
                //Hvis ulven var alfa, finder flokken en ny alfa
                if (wasAlpha) {
                    for (Wolf w : pack) {
                        w.finishCreation(world);
                    }
                }

            }


            //Ulven dør
            world.delete(this);


        }

        /**
         * Returnerer ulvens flok-ID.
         * @return ulvens flok-ID.
         */
        public int getPackID () {
            return this.packID;
        }
    }