import Animals.Bear;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;

import Plants.BerryBush;
import Animals.Rabbit;
import Animals.Wolf;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import org.junit.jupiter.api.*;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class BearTest {
    World world;
    Bear bear;
    Location tlocation;

    Location location;

    Set<Location> territory;

    // dette bliver kørt før hver test.
    @BeforeEach
    public void setUp() {
        world = new World(10);
        world.setDay();
        bear = new Bear();
        Location tlocation = new Location(3,4);
        world.setTile(tlocation, bear);
        territory = bear.getTerritoryTiles(world,tlocation);
    }

    @Test //K2-4a; tester om bjørne sættes i verden
    public void placeInWorldTest() throws FileNotFoundException {
        FileReader fr = new FileReader("C:\\Users\\niels\\OneDrive\\Skrivebord\\GRPRO Eksamens projekt\\grpro_project\\src\\InputFiles\\week-2\\t2-4a.txt");
        int size = fr.getWorldSize();
        Program p = new Program(size, 42, 42);
        World w = p.getWorld();

        Bear bear1 = (Bear) fr.getEntityList().getFirst();

        //Tildel tilfældig lokation
        int x = new Random().nextInt(size);
        int y = new Random().nextInt(size);

        Location l = new Location(x, y);

        //Sæt ulven ind i verden
        w.setCurrentLocation(l);
        w.setTile(l, bear1);

        assertTrue(w.contains(bear1));
    }

    // første test: virker bjørnen som kaninen gør
    @Test
    public void BearMoveTest(){
       tlocation = new Location(3,4);
        System.out.println(tlocation);
        System.out.println(bear.getTerritoryTiles(world,tlocation));

        Location Startlocation = world.getLocation(bear);
        // bevæger sig forhåbenligt
        bear.createTerritory(world);
        bear.bearBrain(world);


        assertNotEquals(Startlocation, world.getLocation(bear));
        assertTrue(territory.contains(world.getLocation(bear)));
    }

    @Test
    public void BearCreateTerritoryTest(){
        Bear bear1 = new Bear();
        Location location = new Location(5,5);
        world.setTile(location, bear1);
        bear.createTerritory(world);

        assertNotNull(bear.territory);
    }

    @Test
    public void TerritoryTest(){
        Bear bear1 = new Bear();
        Location location = new Location(5,5);
        world.setTile(location, bear1);
        bear.createTerritory(world);
        System.out.println(bear.getTerritoryTiles(world,bear.territory));
        assertNotNull(bear.getTerritoryTiles(world,bear.territory));
    }

    @Test
    public void Bearhunt(){

        System.out.println(world.getLocation(bear));

        Rabbit rabbit = new Rabbit();
        Location location = new Location (9,9);

        world.setTile(location,rabbit);

        Location StartLocation = world.getLocation(bear);



        for (int i = 0; i < 50; i++) {
            //Location location
            bear.hunt(world);
            world.step();

        }
        Location StopLocation = world.getLocation(bear);

        System.out.println(StartLocation + " " + StopLocation);
        Assertions.assertNotEquals(StopLocation,StartLocation);
        Assertions.assertFalse(world.contains(rabbit));

    }

    @Test // tester hvis der er flere kaniner
    public void Bearhunt2(){

        System.out.println(world.getLocation(bear));

        Rabbit rabbit = new Rabbit();
        Rabbit rabbit1 = new Rabbit();
        Rabbit rabbit2 = new Rabbit();

        Location location = new Location (0,9);
        Location location1 = new Location (9,0);
        Location location2 = new Location (9,9);
        world.setTile(location,rabbit);
        world.setTile(location1,rabbit1);
        world.setTile(location2,rabbit2);



        Location StartLocation = world.getLocation(bear);


        bear.adjustEnergy(world,-100);

        for (int i = 0; i < 100; i++) {
            //Location location

            bear.hunt(world);
            world.step();
            bear.tryToEat(world);

        }
        Location StopLocation = world.getLocation(bear);

        System.out.println(StartLocation + " " + StopLocation);



        Assertions.assertFalse(world.contains(rabbit));
        Assertions.assertFalse(world.contains(rabbit1));
        Assertions.assertFalse(world.contains(rabbit2));
    }


    @Test //K2-6a; tester at bjørnen spiser bær fra buske en gang i mellem
    public void eatsBerriesTest() {
        BerryBush bush = new BerryBush();
        Location bushLocation = new Location(3, 3);
        world.setTile(bushLocation, bush);
        // Grim
        for (int i = 10; i-- > 0;) {
            bear.tryToEat(world);
        }

        assertFalse(bush.hasFruits);
    }
}