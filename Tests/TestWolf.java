import Animals.Animal;
import Animals.Rabbit;
import Animals.Wolf;
import Holes.WolfDen;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import itumulator.world.Location;
import itumulator.world.World;
import itumulator.executable.Program;

import java.io.FileNotFoundException;
import java.util.Random;

public class TestWolf {
    World world;
    Wolf wolf;

    WolfDen wolfden;
    Location location;

    @BeforeEach
    public void setUp() {
        world = new World(10);
        wolf = new Wolf(123);
        wolfden = new WolfDen(wolf.getPackID());
    }

    @Test //K2-1a tester om ulven kan sættes i verden
    public void placeInWorldTest() throws FileNotFoundException {
        FileReader fr = new FileReader("C:\\Users\\niels\\OneDrive\\Skrivebord\\GRPRO Eksamens projekt\\grpro_project\\src\\InputFiles\\week-2\\t2-1ab.txt");
        int size = fr.getWorldSize();
        Program p = new Program(size, 42, 42);
        World w = p.getWorld();

        Wolf wolf1 = (Wolf) fr.getEntityList().getFirst();

        //Tildel tilfældig lokation
        int x = new Random().nextInt(size);
        int y = new Random().nextInt(size);

        Location l = new Location(x, y);

        //Sæt ulven ind i verden
        w.setCurrentLocation(l);
        w.setTile(l, wolf1);

        assertTrue(w.contains(wolf1));
    }

    @Test  //k2-1b tester ulvens dø funktion
    public void Wolfsdying() {
        Wolf wolf = new Wolf(123);

        Location location = new Location(5,6);
        world.setTile(location, wolf);

        Assert.assertNotNull(wolf);

        wolf.die(world);
        // ulven findes ikke i listen af entities, eller på verdens kortet men er stadig et objekt.

        Assert.assertFalse(world.contains(wolf));
        Assert.assertFalse(world.getEntities().containsValue(location));
    }

    @Test //K2-1c tester at ulve jager og spiser kaniner
    public void huntTest() {
        Program program = new Program(4,42,42);
        World world = program.getWorld();

        Wolf wolf = new Wolf(123);
        Animal prey = new Rabbit();

        Location location1 = new Location(0,0);
        Location location2 = new Location(3,3);

        world.setTile(location1, wolf);
        world.setTile(location2, prey);

        for(int i=10; i-- > 0;){
            program.simulate();
        }
        assertFalse(world.contains(prey));
    }

    //K2-2a, tester ulve er flokdyr. Betaerne søger konstant mod flokkens alfa
    @Test
    public void WolfPackzTest() {
        Program program = new Program(7,42,42);
        World world = program.getWorld();

        Wolf wolf1 = new Wolf(123);
        Location location1 = new Location(1,0);
        world.setTile(location1, wolf1);

        Wolf wolf2 = new Wolf(123);
        Location location2 = new Location(0,0);
        world.setTile(location2, wolf2);

        for(int i=74; i-- > 0;){
            world.setDay();
            program.simulate();

            Location locationWolf1 = world.getLocation(wolf1);
            Location locationWolf2 = world.getLocation(wolf2);

            assertTrue(world.getSurroundingTiles(locationWolf1).contains(locationWolf2));
        }

    }

    //k2-3a Ulve og deres flok tilhørere en ulvehule, her formerere de sig - de graver selv deres hul
    // hvis bare de også gravede deres egne tests.

    @Test   // Alpha ulven spawner med dens Wolfden nå den bliver sat ind på verdenen
    public void WolfMakesDenTest() throws FileNotFoundException {
        FileReader fr = new FileReader("C:\\Users\\niels\\OneDrive\\Skrivebord\\GRPRO Eksamens projekt\\grpro_project\\src\\InputFiles\\week-2\\t2-1ab.txt");
        int size = fr.getWorldSize();
        Program p = new Program(size, 42, 42);
        World w = p.getWorld();

        Wolf wolf1 = (Wolf) fr.getEntityList().getFirst();
        WolfDen den = (WolfDen) fr.getNonBlockingList().getFirst();

        Location l = new Location(2, 2);
        Location l2 = new Location(2,3);

        //Sæt ulven ind i verden
        w.setCurrentLocation(l);
        w.setTile(l, wolf1);
        w.setTile(l2, den);
        //Object den = w.getNonBlocking(l);
        Assert.assertTrue(den instanceof WolfDen);
        assertTrue(w.contains(den) && w.contains(wolf1));
    }


}