import Animals.Rabbit;

import java.io.FileNotFoundException;
import java.util.*;

import Plants.BerryBush;
import Plants.Grass;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Her tester vi spread funktioner & og berrybush funktioner på græs og berrybushes
public class PlantTest {
    World world;
    Location location;
    BerryBush berrybush;
    Rabbit rabbit;
    Grass grass;

    @BeforeEach //Før hver test...
    public void setUp() {
        world = new World(10);
        location = new Location(2,3);
        berrybush = new BerryBush();
        rabbit = new Rabbit();
        grass = new Grass();
    }

    @AfterEach //Efter hver test...
    public void tearDown(){
        world = null;
        location = null;
        berrybush = null;
        rabbit = null;
        grass = null;
    }

    @Test //K1-1a. Græs kan blive plantet når input filerne beskriver dette. Græs skal blot tilfældigt placeres.
    public void placeGrassFromFile() throws FileNotFoundException {
        FileReader fr = new FileReader("C:\\Users\\niels\\OneDrive\\Skrivebord\\GRPRO Eksamens projekt\\grpro_project\\src\\InputFiles\\week-1\\t1-1a.txt");
        int size = fr.getWorldSize();
        Program program = new Program(size, 12, 12);
        World world = program.getWorld();

        //Placerer alle nonblocking-entities (Vi tester kun om Grass placeres, så blocking-entities er ligegyldige)
        for (Object o : fr.getNonBlockingList()) {
            Random r = new Random();
            int x = r.nextInt(size);
            int y = r.nextInt(size);

            Location l = new Location(x, y);
            while (world.getNonBlocking(l) != null) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }
            world.setTile(l, o);
        }

        //Tæller antal Grass i verdenen
        int count = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Grass) {
                count++;
            }
        }

        //Har tælleren talt 3, har FileReader oversat Grass rigtig fra fil til verden; der er 3 Grass i filen
        Assertions.assertTrue(count == 3);
    }

    @Test //K1-1b. Græs kan sprede sig.
    public void grassSpreads() {
        world.setTile(location,grass);
        for (int i = 0; i < 150; i++) {
            grass.act(world);
            world.step();
        }

        int counter = 0;
        for (Object object : world.getEntities().keySet()) {
            if(object instanceof Grass) {counter++;}
        }
        Assert.assertTrue(counter > 1);
    }

    @Test //K1-1c. Dyr kan stå på græs uden der sker noget med græsset.
    public void animalStandOnGrass(){
        world = new World(3);
        location = new Location(1,1);

        double count = 0.0;
        for(int i=0; i < 1_000_000; i++){
            world.setTile(location, grass);
            world.setTile(location, rabbit);
            rabbit.tryToEat(world);
            if(!world.contains(grass)) { count++; }
            else{ world.delete(grass); }
            world.delete(rabbit);
        }

        Assertions.assertTrue(count/100_000.0 != 1.0);
    }

    @Test
    public void BerryBushReplenishTest(){

        world.setTile(location,berrybush);
        Assertions.assertTrue(world.contains(berrybush));


        berrybush.loseBerries();
        Assertions.assertFalse(berrybush.getFruit(world));


        for (int i = 0; i < 50; i++) {
            world.step();
            berrybush.act(world);
        }
        Assertions.assertTrue(berrybush.getFruit(world));
    }
}
