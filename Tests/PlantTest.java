
import Animals.Rabbit;

import java.util.*;

import Plants.BerryBush;
import Plants.Grass;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import itumulator.world.Location;
import itumulator.world.World;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PlantTest {
    World world;
    Location location;
    BerryBush berrybush;
    Rabbit rabbit;
Grass grass;
    // Her tester vi spread funktioner & og berrybush funktioner på græs og berrybushes
    @BeforeEach
    public void Setup() {
        world = new World(10);
        location = new Location(2,3);
        berrybush = new BerryBush();
        rabbit = new Rabbit();
        grass = new Grass();
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
    @Test

public void GrassSpreadTest () {
        world.setTile(location,grass);
        for (int i = 0; i < 150; i++) {
            grass.act(world);
            world.step();
        }

        Map<Object, Location> entities =  world.getEntities();
        int counter = 0;
        for (Object key : entities.keySet()) {
            if(key instanceof Grass) {counter ++;}

        }

    Assert.assertTrue(counter > 1);
}


}
